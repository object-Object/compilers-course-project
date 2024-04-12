package ca.objectobject.hexlr.parser

import ca.objectobject.hexlr.eval.PatternRegistry
import ca.objectobject.hexlr.eval.patterns.OpEscape
import ca.objectobject.hexlr.eval.patterns.OpNumber
import ca.objectobject.hexlr.eval.iotas.Iota
import ca.objectobject.hexlr.parser.HexlrParser.*

class HexlrVisitor : HexlrParserBaseVisitor<List<Iota>>() {
    override fun aggregateResult(aggregate: List<Iota>?, nextResult: List<Iota>?) =
        listOfNotNull(aggregate, nextResult).flatten()

    override fun visitNamedPattern(ctx: NamedPatternContext) = listOf(PatternRegistry.get(ctx.name.text).toIota())

    override fun visitNamedPatternWithArg(ctx: NamedPatternWithArgContext) =
        listOf(PatternRegistry.get(ctx.name.text, ctx.arg.text).toIota())

    override fun visitEscapedPattern(ctx: EscapedPatternContext) = listOf(OpEscape.toIota()) + visitChildren(ctx)

    override fun visitNumberPattern(ctx: NumberPatternContext) = listOf(OpNumber(ctx.NUMBER().text).toIota())
}
