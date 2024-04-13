package ca.objectobject.hexlr.eval.patterns.arithmetic

import ca.objectobject.hexlr.eval.Pattern
import ca.objectobject.hexlr.eval.Runtime
import ca.objectobject.hexlr.eval.iotas.toIota

data object OpRandom : Pattern {
    override fun eval(runtime: Runtime) {
        runtime.stack.push(Math.random().toIota())
    }
}