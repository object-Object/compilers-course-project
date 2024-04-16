package ca.objectobject.hexlr.debug

import ca.objectobject.hexlr.eval.iotas.Iota
import ca.objectobject.hexlr.eval.iotas.ListIota
import ca.objectobject.hexlr.parseIotas
import ca.objectobject.hexlr.parser.ParsedIota
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.ParserRuleContext
import org.eclipse.lsp4j.debug.*
import kotlin.io.path.Path
import kotlin.io.path.name

private const val MIN_ALLOCATED_VARIABLES_REF = 128

class HexlrDebugger(
    private val initArgs: InitializeRequestArguments,
    private val launchArgs: LaunchArgs,
    private val runtime: DebugRuntime,
) {
    private val programSource: Source = Source().apply {
        name = Path(launchArgs.program).name
        path = launchArgs.program
    }

    private val allocatedVariables = mutableListOf<Sequence<Variable>>()

    private val breakpointLocations = mutableSetOf<Pair<Int, Int?>>()

    private val parsedIotas: MutableList<ParsedIota>

    private val nextIota get() = parsedIotas.lastOrNull()

    init {
        val input = CharStreams.fromFileName(launchArgs.program)
        parsedIotas = parseIotas(input).reversed().toMutableList()
    }

    // TODO: support multiple frames
    fun getScopes(frameId: Int): List<Scope> {
        val scopes = mutableListOf(
            Scope().apply {
                name = "Stack"
                variablesReference = 1
            },
            Scope().apply {
                name = "Ravenmind"
                variablesReference = 2
            },
        )
        if (runtime.escapeLevel > 0) {
            scopes += Scope().apply {
                name = "Intro/Retro"
                variablesReference = 3
            }
        }
        return scopes
    }

    fun getVariables(variablesReference: Int): Sequence<Variable> = when (variablesReference) {
        // stack
        1 -> toVariables(runtime.stack.asReversed())
        // ravenmind
        2 -> sequenceOf(toVariable("Ravenmind", runtime.ravenmind))
        // intro/retro
        3 -> toVariables(runtime.newListContentsView)
        else -> getAllocatedVariables(variablesReference)
    }

    private fun toVariables(iotas: Iterable<Iota>) = toVariables(iotas.asSequence())

    private fun toVariables(iotas: Sequence<Iota>) = iotas.mapIndexed(::toVariable)

    private fun toVariable(index: Number, iota: Iota) = toVariable("$index", iota)

    private fun toVariable(name: String, iota: Iota): Variable = Variable().apply {
        this.name = name
        type = iota::class.simpleName
        value = when (iota) {
            is ListIota -> {
                variablesReference = allocateVariables(toVariables(iota.values))
                indexedVariables = iota.values.count()
                "(${iota.values.count()}) [${iota.values.joinToString { it.toRevealString() }}]"
            }

            else -> iota.toRevealString()
        }
    }

    private fun allocateVariables(values: Sequence<Variable>): Int {
        allocatedVariables.add(values)
        return allocatedVariables.lastIndex + MIN_ALLOCATED_VARIABLES_REF
    }

    private fun getAllocatedVariables(variablesReference: Int): Sequence<Variable> {
        return allocatedVariables.getOrElse(variablesReference - MIN_ALLOCATED_VARIABLES_REF) { sequenceOf() }
    }

    fun getStackFrames(): Sequence<StackFrame> = sequenceOf(StackFrame().apply {
        id = 1
        name = "main"
        source = programSource
        val (l, c) = getLineAndColumn(nextIota?.ctx)
        line = l
        column = c
    })

    fun setBreakpoints(sourceBreakpoints: Array<SourceBreakpoint>) = sourceBreakpoints.map {
        Breakpoint().apply {
            isVerified = true
            source = programSource
            line = it.line
            column = it.column
        }
    }.apply {
        breakpointLocations.clear()
        breakpointLocations.addAll(map { Pair(it.line, it.column) })
    }

    fun next(): Boolean {
        allocatedVariables.clear()
        val parsedIota = parsedIotas.removeLastOrNull() ?: return false
        runtime.execute(parsedIota.iota)
        return parsedIotas.isNotEmpty()
    }

    fun `continue`(): Boolean {
        while (parsedIotas.isNotEmpty()) {
            val nextPos = getLineAndColumn(parsedIotas.last().ctx)
            if (
                breakpointLocations.contains(nextPos)
                || breakpointLocations.contains(nextPos.first to null)
            ) return true
            next()
        }
        return false
    }

    private fun getLineAndColumn(ctx: ParserRuleContext?): Pair<Int, Int> {
        if (ctx == null) return Pair(0, 0)

        var line = ctx.getStart().line
        if (!initArgs.linesStartAt1) line -= 1

        var column = ctx.getStart().charPositionInLine
        if (initArgs.columnsStartAt1) column += 1

        return Pair(line, column)
    }
}
