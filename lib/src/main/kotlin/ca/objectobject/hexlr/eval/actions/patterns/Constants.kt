package ca.objectobject.hexlr.eval.actions.patterns

import ca.objectobject.hexlr.eval.Runtime
import ca.objectobject.hexlr.eval.actions.Pattern
import ca.objectobject.hexlr.eval.iotas.BooleanIota
import ca.objectobject.hexlr.eval.iotas.Iota
import ca.objectobject.hexlr.eval.iotas.NumberIota
import ca.objectobject.hexlr.eval.iotas.VectorIota

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

data class OpVector(val x: Number = 0, val y: Number = 0, val z: Number = 0) : ConstPattern(VectorIota(x, y, z))
