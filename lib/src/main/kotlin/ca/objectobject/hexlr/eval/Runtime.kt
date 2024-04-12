package ca.objectobject.hexlr.eval

import ca.objectobject.hexlr.eval.actions.Action
import ca.objectobject.hexlr.eval.iotas.Iota
import java.util.Stack

sealed class EscapeMode {
    data object NONE : EscapeMode()
    data object SINGLE : EscapeMode()
    data class MULTIPLE(val check: (Action) -> Boolean) : EscapeMode() {
        constructor(closer: Action) : this({ action -> action == closer })
    }
}

class Runtime {
    val stack: Stack<Iota> = Stack()
    var escapeMode: EscapeMode = EscapeMode.NONE

    private val callStack: Stack<Action> = Stack()

    fun execute(actions: List<Action>) = actions.forEach(::execute)

    fun execute(action: Action) {}

    override fun toString(): String {
        return "${super.toString()}(stack=$stack, escapeMode=$escapeMode)"
    }
}
