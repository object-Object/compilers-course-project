package ca.objectobject.hexlr

import ca.objectobject.hexlr.eval.Runtime
import ca.objectobject.hexlr.eval.iotas.Iota
import ca.objectobject.hexlr.parser.*
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTree

fun parseTree(source: String): ParseTree {
    val errorListener = HexlrErrorListener()
    val input = CharStreams.fromString(source)
    val lexer = HexlrLexer(input).setErrorListener(errorListener)
    val tokens = CommonTokenStream(lexer)
    val parser = HexlrParser(tokens).setErrorListener(errorListener)
    return parser.start()
}

fun parseIotas(source: String): List<Iota> {
    val tree = parseTree(source)
    return HexlrVisitor().visit(tree)
}

fun execute(source: String): Runtime {
    val iotas = parseIotas(source)
    return Runtime().apply { execute(iotas) }
}

fun main() {
    val source = """
        Numerical Reflection: 1
        Numerical Reflection: 2
        Numerical Reflection: 3
        Vector Exaltation
        True Reflection
        Division Distillation
    """.trimIndent()

    val iotas = parseIotas(source)
    println(iotas)

    val runtime = Runtime()
    for (iota in iotas) {
        runtime.execute(iota)
        println(runtime)
    }
}
