package ca.objectobject.hexlr.eval.patterns

import ca.objectobject.hexlr.eval.EvalUnit
import ca.objectobject.hexlr.eval.TypedPatternUnit
import ca.objectobject.hexlr.eval.iotas.Iota

data object OpReveal : TypedPatternUnit() {
    override val eval: EvalUnit = ::evalUnit

    fun evalUnit(value: Iota) {
        println(value.toRevealString())
    }
}
