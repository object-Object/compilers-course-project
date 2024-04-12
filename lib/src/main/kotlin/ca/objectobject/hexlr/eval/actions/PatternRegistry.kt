package ca.objectobject.hexlr.eval.actions

import ca.objectobject.hexlr.eval.actions.patterns.*

object PatternRegistry {
    private val PATTERNS: Map<String, Action> = mapOf(
        *addAll(OpEscape, "Consideration", "\\"),

        "Additive Distillation" to OpAdd,
        "Subtractive Distillation" to OpSubtract,
        "Multiplicative Distillation" to OpMultiply,
        "Division Distillation" to OpDivide,

        "Vector Exaltation" to OpCreateVec,
    )

    private val PATTERNS_WITH_ARG: Map<String, Map<String, Action>> = mapOf(

    )

    fun get(name: String) = PATTERNS[name] ?: TODO()

    fun get(name: String, arg: String) = PATTERNS_WITH_ARG[name]?.get(arg) ?: TODO()

    private fun addAll(action: Action, vararg names: String) = names.map { it to action }.toTypedArray()
}
