package ca.objectobject.hexlr.parser

import ca.objectobject.hexlr.eval.actions.Action
import ca.objectobject.hexlr.eval.actions.ActionRegistry
import ca.objectobject.hexlr.eval.actions.patterns.OpEscape
import ca.objectobject.hexlr.parser.HexlrParser.*

typealias Actions = List<Action>

class HexlrVisitor : HexlrParserBaseVisitor<Actions>() {
    override fun aggregateResult(aggregate: Actions?, nextResult: Actions?) =
        listOfNotNull(aggregate, nextResult).flatten()

    override fun visitNamedPattern(ctx: NamedPatternContext) = listOf(ActionRegistry.get(ctx.name))

    override fun visitNamedPatternWithArg(ctx: NamedPatternWithArgContext) =
        listOf(ActionRegistry.get(ctx.name, ctx.arg))

    override fun visitEscapedPattern(ctx: EscapedPatternContext) = listOf(OpEscape) + visitChildren(ctx)
}
