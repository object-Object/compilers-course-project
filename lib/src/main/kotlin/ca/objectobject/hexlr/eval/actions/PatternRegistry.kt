package ca.objectobject.hexlr.eval.actions

import ca.objectobject.hexlr.eval.actions.patterns.*
import org.antlr.v4.runtime.Token

object PatternRegistry {
    private val PATTERNS: Map<String, Action> = mapOf(
        *addAll(OpEscape, "Consideration", "\\"),
    )

    private val PATTERNS_WITH_ARG: Map<String, Map<String, Action>> = mapOf(

    )

    fun get(name: String) = PATTERNS[name] ?: TODO()

    fun get(name: String, arg: String) = PATTERNS_WITH_ARG[name]?.get(arg) ?: TODO()

    private fun addAll(action: Action, vararg names: String) =
        names.map { name -> name to action }.toTypedArray()
}