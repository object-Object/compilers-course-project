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

    private fun pushEscaped(action: Action): Iota {
        val iota = when (action) {
            is Pattern -> PatternIota(action)
            else -> throw TypeError(action, "escapable value")
        }
        return stack.push(iota)
    }
}
