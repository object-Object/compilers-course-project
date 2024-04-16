package ca.objectobject.hexlr.parser

import ca.objectobject.hexlr.eval.Pattern
import ca.objectobject.hexlr.eval.iotas.Iota
import org.antlr.v4.runtime.ParserRuleContext

data class ParsedIota(
    val iota: Iota,
    val ctx: ParserRuleContext,
    val definitionCtx: ParserRuleContext? = null,
)

fun <T: Pattern> T.toParsedIota(ctx: ParserRuleContext) = ParsedIota(this.toIota(), ctx)

fun <T: Iota> T.toParsedIota(ctx: ParserRuleContext) = ParsedIota(this, ctx)
