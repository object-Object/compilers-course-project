package ca.objectobject.hexlr

import ca.objectobject.hexlr.actions.Action
import ca.objectobject.hexlr.eval.Runtime
import ca.objectobject.hexlr.parser.HexlrLexer
import ca.objectobject.hexlr.parser.HexlrParser
import ca.objectobject.hexlr.parser.HexlrVisitor
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream

fun parse(source: String): List<Action> {
    val input = CharStreams.fromString(source)
    val lexer = HexlrLexer(input)
    val tokens = CommonTokenStream(lexer)
    val parser = HexlrParser(tokens)
    val tree = parser.start()
    return HexlrVisitor().visit(tree)
}

fun execute(source: String) {
    val actions = parse(source)
    val runtime = Runtime()
    runtime.execute(actions)
}
