package ca.objectobject.hexlr.eval.patterns

import ca.objectobject.hexlr.eval.EvalSingle
import ca.objectobject.hexlr.eval.TypedPatternSingle
import ca.objectobject.hexlr.eval.iotas.Iota

data object OpReveal : TypedPatternSingle() {
    override val eval: EvalSingle = ::eval
    fun eval(value: Iota) = value.apply { println(toRevealString()) }
}
