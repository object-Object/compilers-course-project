package ca.objectobject.hexlr.eval.patterns

import ca.objectobject.hexlr.eval.Runtime
import ca.objectobject.hexlr.eval.Pattern

data object OpEscape : Pattern {
    override fun eval(runtime: Runtime) {
        runtime.escapeNext = true
    }
}

data object OpLeftParen : Pattern {
    override fun eval(runtime: Runtime) {
        runtime.escapeLevel += 1
    }
}

data object OpRightParen : Pattern {
    override fun eval(runtime: Runtime) {
        TODO() // replace with mishap exception
    }
}
