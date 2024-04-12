package ca.objectobject.hexlr.parser

import ca.objectobject.hexlr.eval.PatternRegistry
import ca.objectobject.hexlr.eval.patterns.OpEscape
import ca.objectobject.hexlr.eval.patterns.OpNumber
import ca.objectobject.hexlr.eval.iotas.Iota
import ca.objectobject.hexlr.eval.patterns.OpMask
import ca.objectobject.hexlr.parser.HexlrParser.*

class HexlrVisitor : HexlrParserBaseVisitor<List<Iota>>() {
    private val macros = mutableMapOf<String, List<Iota>>()

    override fun aggregateResult(aggregate: List<Iota>?, nextResult: List<Iota>?) =
        listOfNotNull(aggregate, nextResult).flatten()

    override fun visitNamedPattern(ctx: NamedPatternContext) = ctx.name.text.let {name ->
        macros[name] ?: listOf(PatternRegistry.get(name).toIota())
    }

    override fun visitNamedPatternWithArg(ctx: NamedPatternWithArgContext) =
        listOf(PatternRegistry.get(ctx.name.text, ctx.arg.text).toIota())

    override fun visitEscapedPattern(ctx: EscapedPatternContext) = listOf(OpEscape.toIota()) + visitChildren(ctx)

    override fun visitNumberPattern(ctx: NumberPatternContext) = listOf(OpNumber(ctx.NUMBER().text).toIota())

    override fun visitMaskPattern(ctx: MaskPatternContext) = listOf(OpMask(ctx.MASK().text).toIota())

    override fun visitDefineDirective(ctx: DefineDirectiveContext): List<Iota> {
        macros[ctx.name.text] = visitChildren(ctx.block().statements())
        return listOf()
    }
}
