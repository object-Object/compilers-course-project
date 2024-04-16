package ca.objectobject.hexlr.eval

import ca.objectobject.hexlr.TypeError
import ca.objectobject.hexlr.eval.iotas.Iota
import ca.objectobject.hexlr.eval.iotas.ListIota
import ca.objectobject.hexlr.eval.iotas.NullIota
import ca.objectobject.hexlr.eval.iotas.PatternIota
import ca.objectobject.hexlr.eval.patterns.OpEscape
import ca.objectobject.hexlr.eval.patterns.OpLeftParen
import ca.objectobject.hexlr.eval.patterns.OpRightParen
import java.util.*

open class Runtime(
    val stack: Stack<Iota> = Stack(),
    var ravenmind: Iota = NullIota
) {
    var escapeNext = false
    var escapeLevel = 0
    var shouldHalt = false
    protected val newListContents = mutableListOf<Iota>()

    val isEscaping get() = escapeNext || escapeLevel > 0

    fun execute(iotas: List<Iota>) {
        for (iota in iotas) {
            execute(iota)
            if (shouldHalt) return
        }
    }

    fun execute(iota: Iota) {
        shouldHalt = false

        if (escapeNext) {
            pushEscaped(iota)
            escapeNext = false
            return
        }

        if (escapeLevel > 0 && iota != OpEscape.toIota()) {
            when (iota) {
                OpLeftParen.toIota() -> escapeLevel += 1
                OpRightParen.toIota() -> escapeLevel -= 1
                else -> {}
            }
            if (escapeLevel > 0) {
                pushEscaped(iota)
            } else {
                stack.push(ListIota(newListContents.toList()))
                newListContents.clear()
            }
            return
        }

        if (iota !is PatternIota) throw TypeError(iota, PatternIota::class)
        executePattern(iota.value)
    }

    open fun executePattern(pattern: Pattern) {
        beforeExecute(pattern)
        pattern.eval(this)
        afterExecute(pattern)
    }

    open fun beforeExecute(pattern: Pattern) {}

    open fun afterExecute(pattern: Pattern) {}

    /**
     * Pops n values from the stack, returning the lowest iota first.
     */
    fun pop(n: Int = 1) = (0 until n).map { stack.pop() }.asReversed()

    /**
     * Pops n values from the stack, returning the lowest iota first. Returns null if the stack has less than n iotas.
     */
    fun popOrNull(n: Int = 1) = if (stack.count() >= n) { pop(n) } else { null }

    fun cancelEscape() {
        escapeNext = false
        escapeLevel = 0
        newListContents.clear()
    }

    open fun println(value: Any) {
        kotlin.io.println(value)
    }

    private fun pushEscaped(iota: Iota) {
        if (escapeLevel > 0) {
            newListContents.add(iota)
        } else {
            stack.push(iota)
        }
    }
}
