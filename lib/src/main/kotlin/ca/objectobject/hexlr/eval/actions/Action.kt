package ca.objectobject.hexlr.eval.actions

import ca.objectobject.hexlr.eval.Runtime
import ca.objectobject.hexlr.eval.iotas.Iota

interface Action {
    fun eval(runtime: Runtime)
    fun toIota(): Iota
}
