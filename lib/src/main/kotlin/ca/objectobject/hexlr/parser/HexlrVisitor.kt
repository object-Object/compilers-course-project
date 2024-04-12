package ca.objectobject.hexlr.parser

import ca.objectobject.hexlr.actions.Action

class HexlrVisitor : HexlrParserBaseVisitor<List<Action>>() {
    override fun visitNormalPattern(ctx: HexlrParser.NormalPatternContext): List<Action> {
        return super.visitNormalPattern(ctx)
    }

    override fun aggregateResult(aggregate: List<Action>, nextResult: List<Action>) = aggregate + nextResult
}
