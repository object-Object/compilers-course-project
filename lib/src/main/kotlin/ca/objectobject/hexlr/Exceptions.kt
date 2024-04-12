package ca.objectobject.hexlr

import kotlin.reflect.KClass

class TypeError(message: String? = null, cause: Throwable? = null) : RuntimeException(message, cause) {
    constructor(cause: Throwable) : this(null, cause)

    constructor(got: Any, want: String) : this("Expected $want, got ${got.javaClass.kotlin}")

    constructor(got: Any, want: KClass<*>) : this("Expected ${want.simpleName}, got ${got.javaClass.kotlin}")
}
