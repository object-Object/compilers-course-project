package ca.objectobject.hexlr.eval.patterns

import ca.objectobject.hexlr.eval.Runtime
import ca.objectobject.hexlr.eval.Pattern
import ca.objectobject.hexlr.eval.iotas.BooleanIota
import ca.objectobject.hexlr.eval.iotas.Iota
import ca.objectobject.hexlr.eval.iotas.NumberIota

abstract class ConstPattern(private val output: Iota) : Pattern {
    override fun eval(runtime: Runtime) {
        runtime.stack.push(output)
    }
}

data object OpTrue : ConstPattern(BooleanIota(true))

data object OpFalse : ConstPattern(BooleanIota(false))

data class OpNumber(val number: Number) : ConstPattern(NumberIota(number)) {
    constructor(number: String) : this(number.toDouble())
}
