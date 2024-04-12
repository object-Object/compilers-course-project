package ca.objectobject.hexlr.eval.actions

import ca.objectobject.hexlr.eval.actions.patterns.*
import org.antlr.v4.runtime.Token

object ActionRegistry {
    private val ACTIONS: Map<String, Action> = mapOf(
        *addAll(OpEscape, "Consideration", "\\"),
    )

    private val ACTIONS_WITH_ARG: Map<String, Map<String, Action>> = mapOf(

    )

    fun get(name: Token) = get(name.text)
    fun get(name: String) = ACTIONS[name] ?: TODO()

    fun get(name: Token, arg: Token) = get(name.text, arg.text)
    fun get(name: String, arg: String) = ACTIONS_WITH_ARG[name]?.get(arg) ?: TODO()

    private fun addAll(action: Action, vararg names: String) =
        names.map { name -> name to action }.toTypedArray()
}
