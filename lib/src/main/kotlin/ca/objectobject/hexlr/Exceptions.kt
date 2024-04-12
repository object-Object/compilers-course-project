package ca.objectobject.hexlr

import ca.objectobject.hexlr.eval.actions.Action

open class HexlrException(message: String? = null, cause: Throwable? = null) : Exception(message, cause) {
    constructor(cause: Throwable) : this(null, cause)
}

open class HexlrRuntimeError(message: String? = null, cause: Throwable? = null) : HexlrException(message, cause) {
    constructor(cause: Throwable) : this(null, cause)
}

class TypeError(message: String? = null, cause: Throwable? = null) : HexlrRuntimeError(message, cause) {
    constructor(cause: Throwable) : this(null, cause)
    constructor(action: Action, want: String) : this("Expected $want, got ${action.javaClass.kotlin}")
}
