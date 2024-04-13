package ca.objectobject.hexlr.eval.patterns

import ca.objectobject.hexlr.eval.*
import ca.objectobject.hexlr.eval.iotas.Iota

data object OpReadRavenmind : TypedPatternSingle() {
    override val eval: EvalSingle = ::evalSingle

    fun evalSingle(runtime: Runtime) = runtime.ravenmind
}

data object OpWriteRavenmind : TypedPatternUnit() {
    override val eval: EvalUnit = ::evalUnit

    fun evalUnit(runtime: Runtime, iota: Iota) {
        runtime.ravenmind = iota
    }
}
