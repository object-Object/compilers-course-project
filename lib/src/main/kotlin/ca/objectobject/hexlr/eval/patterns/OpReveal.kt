package ca.objectobject.hexlr.eval.patterns

import ca.objectobject.hexlr.eval.EvalSingle
import ca.objectobject.hexlr.eval.Runtime
import ca.objectobject.hexlr.eval.TypedPatternSingle
import ca.objectobject.hexlr.eval.iotas.Iota

data object OpReveal : TypedPatternSingle() {
    override val eval: EvalSingle = ::eval
    fun eval(runtime: Runtime, value: Iota) = value.apply { runtime.println(toRevealString()) }
}
