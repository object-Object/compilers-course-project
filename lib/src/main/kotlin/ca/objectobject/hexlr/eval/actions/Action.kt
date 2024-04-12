package ca.objectobject.hexlr.eval.actions

import ca.objectobject.hexlr.eval.Runtime

interface Action {
    fun eval(runtime: Runtime)
}

interface Pattern : Action
