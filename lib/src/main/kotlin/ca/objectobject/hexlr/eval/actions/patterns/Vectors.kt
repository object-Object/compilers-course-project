package ca.objectobject.hexlr.eval.actions.patterns

import ca.objectobject.hexlr.eval.Runtime
import ca.objectobject.hexlr.eval.actions.Pattern3
import ca.objectobject.hexlr.eval.iotas.NumberIota
import ca.objectobject.hexlr.eval.iotas.VectorIota

data object OpCreateVec :
    Pattern3<NumberIota, NumberIota, NumberIota>(NumberIota::class, NumberIota::class, NumberIota::class) {
    override val outputTypes = listOf(VectorIota::class)

    override fun eval(runtime: Runtime, input0: NumberIota, input1: NumberIota, input2: NumberIota) =
        listOf(VectorIota(input0.number, input1.number, input2.number))
}
