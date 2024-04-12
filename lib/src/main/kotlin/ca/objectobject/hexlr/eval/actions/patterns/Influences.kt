package ca.objectobject.hexlr.eval.actions.patterns

import ca.objectobject.hexlr.eval.EscapeMode
import ca.objectobject.hexlr.eval.Runtime
import ca.objectobject.hexlr.eval.actions.Action

data object OpEscape : Action {
    override fun eval(runtime: Runtime) {
        runtime.escapeMode = EscapeMode.SINGLE
    }
}