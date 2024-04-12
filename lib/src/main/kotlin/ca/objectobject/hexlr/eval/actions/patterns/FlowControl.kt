package ca.objectobject.hexlr.eval.actions.patterns

import ca.objectobject.hexlr.eval.Runtime
import ca.objectobject.hexlr.eval.actions.EvalFn
import ca.objectobject.hexlr.eval.actions.TypedPattern
import ca.objectobject.hexlr.eval.iotas.*

data object OpIf : TypedPattern() {
    override val eval: EvalFn = ::eval

    fun eval(condition: BooleanIota, valueIf: Iota, valueElse: Iota) = if (condition.value) {
        valueIf
    } else {
        valueElse
    }.toSingle()
}

data object OpEval : TypedPattern() {
    override val eval: EvalFn = ::eval

    fun eval(runtime: Runtime, iota: EvaluableIota): Iterable<Iota>? {
        when (iota) {
            is PatternIota -> runtime.execute(iota)
            is ListIota -> runtime.execute(iota.values)
        }
        return null
    }
}
