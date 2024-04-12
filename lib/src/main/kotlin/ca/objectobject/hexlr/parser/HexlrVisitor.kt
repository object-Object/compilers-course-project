package ca.objectobject.hexlr.parser

import ca.objectobject.hexlr.eval.actions.Action
import ca.objectobject.hexlr.eval.actions.PatternRegistry
import ca.objectobject.hexlr.eval.actions.patterns.OpEscape
import ca.objectobject.hexlr.eval.actions.patterns.OpNumber
import ca.objectobject.hexlr.parser.HexlrParser.*

typealias Actions = List<Action>

class HexlrVisitor : HexlrParserBaseVisitor<Actions>() {
    override fun aggregateResult(aggregate: Actions?, nextResult: Actions?) =
        listOfNotNull(aggregate, nextResult).flatten()

    override fun visitNamedPattern(ctx: NamedPatternContext) = listOf(PatternRegistry.get(ctx.name.text))

    override fun visitNamedPatternWithArg(ctx: NamedPatternWithArgContext) =
        listOf(PatternRegistry.get(ctx.name.text, ctx.arg.text))

    override fun visitEscapedPattern(ctx: EscapedPatternContext) = listOf(OpEscape) + visitChildren(ctx)

    override fun visitNumberPattern(ctx: NumberPatternContext) = listOf(OpNumber(ctx.NUMBER().text))
}
