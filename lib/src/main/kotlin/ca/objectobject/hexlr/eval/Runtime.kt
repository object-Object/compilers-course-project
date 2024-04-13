package ca.objectobject.hexlr.eval

import ca.objectobject.hexlr.TypeError
import ca.objectobject.hexlr.eval.patterns.OpEscape
import ca.objectobject.hexlr.eval.patterns.OpLeftParen
import ca.objectobject.hexlr.eval.patterns.OpRightParen
import ca.objectobject.hexlr.eval.iotas.Iota
import ca.objectobject.hexlr.eval.iotas.ListIota
import ca.objectobject.hexlr.eval.iotas.PatternIota
import java.util.Stack

data class Runtime(
    val stack: Stack<Iota> = Stack(),
) {
    var escapeNext = false
    var escapeLevel = 0
    private val newListContents = mutableListOf<Iota>()

    val isEscaping get() = escapeNext || escapeLevel > 0

    fun execute(iotas: List<Iota>) = iotas.forEach(::execute)

    fun execute(iota: Iota) {
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

        if (iota is PatternIota) {
            iota.value.eval(this)
        } else {
            throw TypeError(iota, PatternIota::class)
        }
    }

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

    private fun pushEscaped(iota: Iota) {
        if (escapeLevel > 0) {
            newListContents.add(iota)
        } else {
            stack.push(iota)
        }
    }
}
