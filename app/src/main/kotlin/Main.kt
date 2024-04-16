
import ca.objectobject.hexlr.eval.Pattern
import ca.objectobject.hexlr.eval.Runtime
import ca.objectobject.hexlr.eval.iotas.NullIota
import ca.objectobject.hexlr.parseIotas
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.help
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.boolean
import com.github.ajalt.clikt.parameters.types.file
import org.antlr.v4.runtime.CharStreams
import java.io.File

class Hello : CliktCommand() {
    private val file by argument()
        .file(mustExist=true, mustBeReadable=true, canBeDir=false)
        .optional()
        .help("hexpattern source file to execute")

    private val debug by option()
        .boolean()
        .default(false)

    override fun run() {
        val input = file?.run { reader() }?.let(CharStreams::fromReader)
        if (input != null) {
            // run file
            val iotas = parseIotas(input).map { it.iota }
            val runtime = if (debug) {
                val writer = File("debug.log").writer()
                object : Runtime() {
                    override fun beforeExecute(pattern: Pattern) {
                        writer.write("""
                            -------------------
                            Next: ${pattern.name}
                        """.trimIndent() + "\n" + printStack(this))
                        writer.flush()
                    }
                }.apply { execute(iotas) }
            } else {
                Runtime().apply { execute(iotas) }
            }
            print(printStack(runtime))
        } else {
            // repl
            var runtime = Runtime()
            println("Hexlr REPL")
            while (true) {
                if (runtime.isEscaping) {
                    print("  ")
                } else {
                    print("> ")
                }

                val line = readln()
                if (line == "/clear") {
                    runtime = Runtime()
                    continue
                }

                try {
                    val iotas = parseIotas(line).map { it.iota }
                    runtime.execute(iotas)
                    if (!runtime.isEscaping) print(printStack(runtime))
                } catch (e: Throwable) {
                    println("$e")
                    runtime.cancelEscape()
                }
            }
        }
    }

    private fun printStack(runtime: Runtime): String {
        val result = StringBuilder()
        result.appendLine("Stack:")
        for (iota in runtime.stack.asReversed()) {
            result.appendLine("| ${iota.toRevealString()}")
        }

        with (runtime.ravenmind) {
            if (this != NullIota) result.appendLine("Ravenmind:\n| ${toRevealString()}")
        }
        return result.toString()
    }
}

fun main(args: Array<String>) = Hello().main(args)