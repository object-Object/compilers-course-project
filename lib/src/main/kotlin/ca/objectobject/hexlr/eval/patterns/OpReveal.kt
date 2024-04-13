package ca.objectobject.hexlr.eval.patterns

import ca.objectobject.hexlr.eval.EvalFn
import ca.objectobject.hexlr.eval.TypedPattern
import ca.objectobject.hexlr.eval.iotas.Iota

data object OpReveal : TypedPattern() {
    override val eval: EvalFn = ::eval

    fun eval(value: Iota): List<Iota>? {
        println(value.toRevealString())
        return null
    }
}
