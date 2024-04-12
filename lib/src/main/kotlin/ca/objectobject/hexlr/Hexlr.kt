package ca.objectobject.hexlr

import ca.objectobject.hexlr.actions.Action
import ca.objectobject.hexlr.eval.Runtime
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

fun parseActions(source: String): List<Action> {
    val tree = parseTree(source)
    return HexlrVisitor().visit(tree)
}

fun execute(source: String) {
    val actions = parseActions(source)
    val runtime = Runtime()
    runtime.execute(actions)
}

fun main() {
    val tree = parseTree("""
        #define Foo = Introspection ->
        {
        }
    """.trimIndent())
    println(tree)
}
