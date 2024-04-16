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

class HexlrDebugger(
    private val initArgs: InitializeRequestArguments,
    private val launchArgs: LaunchArgs,
    private val writeStdout: (String) -> Unit,
) {
    private val runtime = DebugRuntime(writeStdout)

    private val programSource: Source = Source().apply {
        name = Path(launchArgs.program).name
        path = launchArgs.program
    }

    private val parsedIotas: MutableList<ParsedIota>

    private val nextIota get() = parsedIotas.lastOrNull()

    init {
        val input = CharStreams.fromFileName(launchArgs.program)
        parsedIotas = parseIotas(input).reversed().toMutableList()
    }

    // TODO: support multiple frames
    fun getScopes(frameId: Int): Array<Scope> = arrayOf(
        Scope().apply {
            name = "Stack"
            variablesReference = 1
        },
        Scope().apply {
            name = "Ravenmind"
            variablesReference = 2
        },
    )

    fun getVariables(variablesReference: Int): Sequence<Variable> = when (variablesReference) {
        // stack
        1 -> runtime.stack.asReversed().asSequence().mapIndexed { i, it -> it.toVariable("$i") }
        // ravenmind
        2 -> sequenceOf(runtime.ravenmind.toVariable("Ravenmind"))
        else -> sequenceOf()
    }

    fun getStackFrames(): Sequence<StackFrame> = sequenceOf(
        StackFrame().apply {
            id = 1
            name = "main"
            source = programSource
            val (l, c) = getLineAndColumn(nextIota?.ctx)
            line = l
            column = c
        }
    )

    fun next(): Boolean {
        val parsedIota = parsedIotas.removeLastOrNull() ?: return false
        runtime.execute(parsedIota.iota)
        return parsedIotas.isNotEmpty()
    }

    fun `continue`() {
        while (next()) {}
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

fun <T : Iota> T.toVariable(name: String): Variable = Variable().also {
    it.name = name
    it.type = this::class.simpleName
    when (this) {
        is ListIota -> {
            it.value = "[...]"
            // TODO: variable reference
        }

        else -> it.value = toRevealString()
    }
}
