package ca.objectobject.hexlr.eval.actions.patterns

import ca.objectobject.hexlr.eval.Runtime
import ca.objectobject.hexlr.eval.actions.Pattern

data object OpEscape : Pattern {
    override fun eval(runtime: Runtime) {
        runtime.escapeSingle = true
    }
}
