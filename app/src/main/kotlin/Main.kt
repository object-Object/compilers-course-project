
import ca.objectobject.hexlr.eval.Runtime
import ca.objectobject.hexlr.eval.iotas.NullIota
import ca.objectobject.hexlr.execute
import ca.objectobject.hexlr.parseIotas
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.help
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.types.file
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
        println("Stack:")
        for (iota in runtime.stack.asReversed()) {
            println("| ${iota.toRevealString()}")
        }

        with (runtime.ravenmind) {
            if (this != NullIota) println("Ravenmind:\n| ${toRevealString()}")
        }
    }
}

fun main(args: Array<String>) = Hello().main(args)