package ca.objectobject.hexlr

import ca.objectobject.hexlr.eval.Runtime
import ca.objectobject.hexlr.parser.*
import org.antlr.v4.runtime.CharStream
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTree

fun parseTree(input: CharStream): ParseTree {
    val errorListener = HexlrErrorListener()
    val lexer = HexlrLexer(input).setErrorListener(errorListener)
    val tokens = CommonTokenStream(lexer)
    val parser = HexlrParser(tokens).setErrorListener(errorListener)
    return parser.start()
}

fun parseIotas(source: String) = parseIotas(CharStreams.fromString(source))

fun parseIotas(input: CharStream): List<ParsedIota> {
    val tree = parseTree(input)
    return HexlrVisitor().visit(tree)
}

fun execute(source: String) = execute(CharStreams.fromString(source))

fun execute(input: CharStream): Runtime {
    val iotas = parseIotas(input).map { it.iota }
    return Runtime().apply { execute(iotas) }
}
