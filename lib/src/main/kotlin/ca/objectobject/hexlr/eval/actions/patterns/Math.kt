package ca.objectobject.hexlr.eval.actions.patterns

import ca.objectobject.hexlr.eval.Runtime
import ca.objectobject.hexlr.eval.actions.Pattern
import ca.objectobject.hexlr.eval.iotas.NumberIota

data class OpNumber(val number: Double) : Pattern {
    constructor(number: String) : this(number.toDouble())

    override fun eval(runtime: Runtime) {
        runtime.stack.push(NumberIota(number))
    }
}
