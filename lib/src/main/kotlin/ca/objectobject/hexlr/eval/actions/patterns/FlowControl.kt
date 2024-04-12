package ca.objectobject.hexlr.eval.actions.patterns

import ca.objectobject.hexlr.eval.Runtime
import ca.objectobject.hexlr.eval.actions.EvalFn
import ca.objectobject.hexlr.eval.actions.TypedPattern
import ca.objectobject.hexlr.eval.iotas.BooleanIota
import ca.objectobject.hexlr.eval.iotas.EvaluableIota
import ca.objectobject.hexlr.eval.iotas.Iota
import ca.objectobject.hexlr.eval.iotas.PatternIota

data object OpIf : TypedPattern() {
    override val eval: EvalFn = ::eval

    fun eval(condition: BooleanIota, valueIf: Iota, valueElse: Iota) = listOf(
        if (condition.value) {
            valueIf
        } else {
            valueElse
        }
    )
}

data object OpEval : TypedPattern() {
    override val eval: EvalFn = ::eval

    fun eval(runtime: Runtime, iota: EvaluableIota): List<Iota> {
        when (iota) {
            is PatternIota -> runtime.execute(iota.value)
        }
        return listOf()
    }
}
