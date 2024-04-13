package ca.objectobject.hexlr.eval

import ca.objectobject.hexlr.eval.iotas.Iota
import ca.objectobject.hexlr.eval.iotas.PatternIota

interface Pattern {
    val name get() = this::class.simpleName ?: this.toString()

    fun eval(runtime: Runtime)

    /**
     * Pops n values from the stack, returning the lowest iota first.
     */
    fun pop(runtime: Runtime, n: Int = 1): List<Iota> {
        val size = runtime.stack.count()
        if (n > size) throw IllegalArgumentException("$name expected $n input(s) but got $size")
        return runtime.pop(n)
    }

    fun toIota() = PatternIota(this)
}
