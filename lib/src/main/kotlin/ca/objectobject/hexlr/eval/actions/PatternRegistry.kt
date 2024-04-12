package ca.objectobject.hexlr.eval.actions

import ca.objectobject.hexlr.eval.actions.patterns.*
import ca.objectobject.hexlr.eval.actions.patterns.arithmetic.*

object PatternRegistry {
    private val PATTERNS: Map<String, Action> = mapOf(
        *addAll(OpEscape, "Consideration", "\\"),
        *addAll(OpLeftParen, "Introspection", "{"),
        *addAll(OpRightParen, "Retrospection", "}"),

        "Additive Distillation" to OpAdd,
        "Subtractive Distillation" to OpSubtract,
        "Multiplicative Distillation" to OpMultiply,
        "Division Distillation" to OpDivide,

        "Vector Exaltation" to OpSlurpVec,
        "Vector Disintegration" to OpSplatVec,

        "Vector Reflection Zero" to OpVector(),
        "Vector Reflection +X" to OpVector(x=1),
        "Vector Reflection -X" to OpVector(x=-1),
        "Vector Reflection +Y" to OpVector(y=1),
        "Vector Reflection -Y" to OpVector(y=-1),
        "Vector Reflection +Z" to OpVector(z=1),
        "Vector Reflection -Z" to OpVector(z=-1),

        "Vacant Reflection" to OpEmptyList,
        "Single's Purification" to OpSingletonList,
        "Flock's Gambit" to OpSlurpList,
        "Flock's Disintegration" to OpSplatList,

        "True Reflection" to OpTrue,
        "False Reflection" to OpFalse,

        "Augur's Exaltation" to OpIf,
        "Hermes' Gambit" to OpEval,
    )

    private val PATTERNS_WITH_ARG: Map<String, Map<String, Action>> = mapOf(

    )

    fun get(name: String) = PATTERNS[name] ?: TODO(name)

    fun get(name: String, arg: String) = PATTERNS_WITH_ARG[name]?.get(arg) ?: TODO("$name: $arg")

    private fun addAll(action: Action, vararg names: String) = names.map { it to action }.toTypedArray()
}
