package ca.objectobject.hexlr.eval

import ca.objectobject.hexlr.TypeError
import ca.objectobject.hexlr.eval.actions.Action
import ca.objectobject.hexlr.eval.actions.Pattern
import ca.objectobject.hexlr.eval.iotas.Iota
import ca.objectobject.hexlr.eval.iotas.PatternIota
import java.util.Stack

data class Runtime(
    val stack: Stack<Iota> = Stack(),
    var escapeSingle: Boolean = false,
) {
    fun execute(actions: List<Action>) = actions.forEach(::execute)

    fun execute(action: Action) {
        if (escapeSingle) {
            pushEscaped(action)
            escapeSingle = false
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

    private fun pushEscaped(action: Action): Iota {
        val iota = when (action) {
            is Pattern -> PatternIota(action)
            else -> throw TypeError(action, "escapable value")
        }
        return stack.push(iota)
    }
}
