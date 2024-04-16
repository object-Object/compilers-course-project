package ca.objectobject.hexlr.parser

import ca.objectobject.hexlr.eval.PatternRegistry
import ca.objectobject.hexlr.eval.iotas.BooleanIota
import ca.objectobject.hexlr.eval.iotas.ListIota
import ca.objectobject.hexlr.eval.iotas.NumberIota
import ca.objectobject.hexlr.eval.iotas.VectorIota
import ca.objectobject.hexlr.eval.patterns.OpEscape
import ca.objectobject.hexlr.eval.patterns.OpMask
import ca.objectobject.hexlr.eval.patterns.OpNumber
import ca.objectobject.hexlr.parser.HexlrParser.*

class HexlrVisitor : HexlrParserBaseVisitor<List<ParsedIota>>() {
    private val macros = mutableMapOf<String, List<ParsedIota>>()

    override fun aggregateResult(aggregate: List<ParsedIota>?, nextResult: List<ParsedIota>?): List<ParsedIota> {
        return listOfNotNull(aggregate, nextResult).flatten()
    }

    override fun visitNamedPattern(ctx: NamedPatternContext): List<ParsedIota> {
        return ctx.name.text.let {name ->
            macros[name]?.map { it.copy(ctx=ctx, definitionCtx=it.ctx) }?.toList()
            ?: listOf(PatternRegistry.get(name).toParsedIota(ctx))
        }
    }

    override fun visitNamedPatternWithArg(ctx: NamedPatternWithArgContext): List<ParsedIota> {
        return listOf(PatternRegistry.get(ctx.name.text, ctx.arg.text).toParsedIota(ctx))
    }

    override fun visitEscapedPattern(ctx: EscapedPatternContext): List<ParsedIota> {
        return listOf(OpEscape.toParsedIota(ctx)) + visitChildren(ctx)
    }

    override fun visitNumberPattern(ctx: NumberPatternContext): List<ParsedIota> {
        return listOf(OpNumber(ctx.NUMBER().text).toParsedIota(ctx))
    }

    override fun visitMaskPattern(ctx: MaskPatternContext): List<ParsedIota> {
        return listOf(OpMask(ctx.MASK().text).toParsedIota(ctx))
    }

    override fun visitDefineDirective(ctx: DefineDirectiveContext): List<ParsedIota> {
        macros[ctx.name.text] = visitChildren(ctx.block().statements())
        return listOf()
    }

    override fun visitBooleanIota(ctx: BooleanIotaContext): List<ParsedIota> {
        return listOf(BooleanIota(ctx.BOOLEAN().text == "true").toParsedIota(ctx))
    }

    override fun visitNumberIota(ctx: NumberIotaContext): List<ParsedIota> {
        return listOf(NumberIota(ctx.NUMBER().text).toParsedIota(ctx))
    }

    override fun visitVectorIota(ctx: VectorIotaContext): List<ParsedIota> {
        return listOf(VectorIota(ctx.x.text, ctx.y.text, ctx.z.text).toParsedIota(ctx))
    }

    override fun visitListIota(ctx: ListIotaContext): List<ParsedIota> {
        return listOf(ListIota(visitChildren(ctx).map { it.iota }).toParsedIota(ctx))
    }
}
