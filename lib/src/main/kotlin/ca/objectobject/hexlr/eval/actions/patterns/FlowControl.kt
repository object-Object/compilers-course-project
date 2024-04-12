package ca.objectobject.hexlr.eval.actions.patterns

import ca.objectobject.hexlr.eval.Runtime
import ca.objectobject.hexlr.eval.actions.Pattern1
import ca.objectobject.hexlr.eval.actions.Pattern3
import ca.objectobject.hexlr.eval.actions.TypedPattern
import ca.objectobject.hexlr.eval.iotas.BooleanIota
import ca.objectobject.hexlr.eval.iotas.EvaluableIota
import ca.objectobject.hexlr.eval.iotas.Iota
import ca.objectobject.hexlr.eval.iotas.PatternIota

data object OpTrue : TypedPattern() {
    override val outputTypes = listOf(BooleanIota::class)

    override fun eval(runtime: Runtime, inputs: List<Iota>) = listOf(BooleanIota(true))
}

data object OpFalse : TypedPattern() {
    override val outputTypes = listOf(BooleanIota::class)

    override fun eval(runtime: Runtime, inputs: List<Iota>) = listOf(BooleanIota(false))
}

data object OpIf : Pattern3<BooleanIota, Iota, Iota>(BooleanIota::class, Iota::class, Iota::class) {
    override val outputTypes = listOf(Iota::class)

    override fun eval(runtime: Runtime, input0: BooleanIota, input1: Iota, input2: Iota) =
        listOf(if (input0.value) { input1 } else { input2 })
}

data object OpEval : Pattern1<EvaluableIota>(EvaluableIota::class) {
    override fun eval(runtime: Runtime, input0: EvaluableIota): List<Iota> {
        when (input0) {
            is PatternIota -> runtime.execute(input0.value)
        }
        return listOf()
    }
}
