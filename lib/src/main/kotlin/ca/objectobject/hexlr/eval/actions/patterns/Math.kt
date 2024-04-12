package ca.objectobject.hexlr.eval.actions.patterns

import ca.objectobject.hexlr.eval.Runtime
import ca.objectobject.hexlr.eval.actions.Pattern2
import ca.objectobject.hexlr.eval.actions.TypedPattern
import ca.objectobject.hexlr.eval.iotas.Iota
import ca.objectobject.hexlr.eval.iotas.NumberIota

data class OpNumber(val number: Double) : TypedPattern() {
    override val outputTypes = listOf(NumberIota::class)

    constructor(number: String) : this(number.toDouble())

    override fun eval(runtime: Runtime, inputs: List<Iota>) = listOf(NumberIota(number))
}

data object OpAdd : Pattern2<NumberIota, NumberIota>(NumberIota::class, NumberIota::class) {
    override val outputTypes = listOf(NumberIota::class)

    override fun eval(runtime: Runtime, input0: NumberIota, input1: NumberIota) =
        listOf(NumberIota(input0.number + input1.number))
}
