package ca.objectobject.hexlr.eval.patterns

import ca.objectobject.hexlr.eval.Pattern
import ca.objectobject.hexlr.eval.Runtime

data class OpMask(val mask: List<Boolean>) : Pattern {
    constructor(mask: String) : this(mask.map { it == '-' })

    override fun eval(runtime: Runtime) {
        val iotas = pop(runtime, mask.count())
        for ((iota, keep) in iotas.zip(mask)) {
            if (keep) runtime.stack.push(iota)
        }
    }
}