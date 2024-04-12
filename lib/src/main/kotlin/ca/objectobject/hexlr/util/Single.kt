package ca.objectobject.hexlr.util

/**
 * Like `Pair` or `Triple`, but for a single value.
 */
data class Single<T>(val first: T) : Iterator<T>, Iterable<T> {
    private var nextCalled = false

    override fun hasNext() = !nextCalled

    override fun next(): T {
        if (nextCalled) {
            throw NoSuchElementException()
        }
        nextCalled = true
        return first
    }

    override fun iterator() = this
}
