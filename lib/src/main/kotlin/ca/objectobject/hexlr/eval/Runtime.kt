package ca.objectobject.hexlr.eval

import ca.objectobject.hexlr.TypeError
import ca.objectobject.hexlr.eval.actions.Action
import ca.objectobject.hexlr.eval.actions.Pattern
import ca.objectobject.hexlr.eval.actions.patterns.OpEscape
import ca.objectobject.hexlr.eval.actions.patterns.OpLeftParen
import ca.objectobject.hexlr.eval.actions.patterns.OpRightParen
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

    fun execute(actions: List<Action>) = actions.forEach(::execute)

    fun execute(action: Action) {
        if (escapeNext) {
            pushEscaped(action)
            escapeNext = false
            return
        }

        if (escapeLevel > 0 && action != OpEscape) {
            when (action) {
                OpLeftParen -> escapeLevel += 1
                OpRightParen -> escapeLevel -= 1
            }
            if (escapeLevel > 0) {
                pushEscaped(action)
            } else {
                stack.push(ListIota(newListContents.toList()))
                newListContents.clear()
            }
            return
        }

        action.eval(this)
    }

    /**
     * Pops n values from the stack, returning the lowest iota first.
     */
    fun pop(n: Int = 1) = (0 until n).map { stack.pop() }.asReversed()

    /**
     * Pops n values from the stack, returning the lowest iota first. Returns null if the stack has less than n iotas.
     */
    fun popOrNull(n: Int = 1) = if (stack.count() >= n) { pop(n) } else { null }

    private fun pushEscaped(action: Action) {
        val iota = action.toIota()
        if (escapeLevel > 0) {
            newListContents.add(iota)
        } else {
            stack.push(iota)
        }
    }
}
