import ca.objectobject.hexlr.eval.Runtime
import ca.objectobject.hexlr.execute
import ca.objectobject.hexlr.parseIotas
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.*
import com.github.ajalt.clikt.parameters.types.*
import org.antlr.v4.runtime.CharStreams

class Hello : CliktCommand() {
    private val file by argument()
        .file(mustExist=true, mustBeReadable=true, canBeDir=false)
        .optional()
        .help("hexpattern source file to execute")

    override fun run() {
        val input = file?.run { reader() }?.let(CharStreams::fromReader)
        if (input != null) {
            // run file
            val runtime = execute(input)
            printStack(runtime)
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
                    val iotas = parseIotas(line)
                    runtime.execute(iotas)
                    if (!runtime.isEscaping) printStack(runtime)
                } catch (e: Throwable) {
                    println("$e")
                    runtime.cancelEscape()
                }
            }
        }
    }

    private fun printStack(runtime: Runtime) {
        for (iota in runtime.stack.asReversed()) {
            println("| $iota")
        }
    }
}

fun main(args: Array<String>) = Hello().main(args)