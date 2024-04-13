package ca.objectobject.hexlr.eval

import ca.objectobject.hexlr.eval.patterns.*
import ca.objectobject.hexlr.eval.patterns.arithmetic.*

object PatternRegistry {
    private val PATTERNS: Map<String, Pattern> = mapOf(
        *addAll(OpEscape, "Consideration", "\\"),
        *addAll(OpLeftParen, "Introspection", "{"),
        *addAll(OpRightParen, "Retrospection", "}"),

        "Additive Distillation" to OpAdd,
        "Subtractive Distillation" to OpSubtract,
        "Multiplicative Distillation" to OpMultiply,
        "Division Distillation" to OpDivide,
        "Modulus Distillation" to OpModulus,
        "Power Distillation" to OpPower,

        "Length Purification" to OpAbsLength,
        "Floor Purification" to OpFloor,
        "Ceiling Purification" to OpCeil,
        "Axial Purification" to OpSignAxis,

        "Entropy Reflection" to OpRandom,

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
        "Selection Distillation" to OpSelect,
        "Selection Exaltation" to OpSlice,
        "Integration Distillation" to OpAppend,
        "Combination Distillation" to OpConcat,
        "Abacus Purification" to OpListLen,
        "Retrograde Purification" to OpReverse,
        "Locator's Distillation" to OpIndexOf,
        "Excisor's Distillation" to OpRemove,
        "Surgeon's Exaltation" to OpInsert,
        "Speaker's Distillation" to OpPushFirst,
        "Speaker's Decomposition" to OpPopFirst,

        "Flock's Gambit" to OpSlurpList,
        "Flock's Disintegration" to OpSplatList,
        "Flock's Reflection" to OpStackLen,

        "Gemini Decomposition" to OpCopy,
        "Gemini Gambit" to OpCopyN,
        "Prospector's Gambit" to OpCopyFromBelow,
        "Undertaker's Gambit" to OpCopyToBelow,
        "Dioscuri Gambit" to Op2Dup,
        "Jester's Gambit" to OpSwap,
        "Rotation Gambit" to OpYankUp,
        "Rotation Gambit II" to OpYankDown,
        "Fisherman's Gambit" to OpFisherman,
        "Fisherman's Gambit II" to OpFishermanCopy,

        "True Reflection" to OpTrue,
        "False Reflection" to OpFalse,
        "Nullary Reflection" to OpNull,

        "Augur's Exaltation" to OpIf,

        "Muninn's Reflection" to OpReadRavenmind,
        "Huginn's Gambit" to OpWriteRavenmind,

        "Hermes' Gambit" to OpEval,
        "Thoth's Gambit" to OpFor,

        "Reveal" to OpReveal,
    )

    private val PATTERNS_WITH_ARG: Map<String, Map<String, Pattern>> = mapOf(

    )

    fun get(name: String) = PATTERNS[name] ?: TODO(name)

    fun get(name: String, arg: String) = PATTERNS_WITH_ARG[name]?.get(arg) ?: TODO("$name: $arg")

    private fun addAll(pattern: Pattern, vararg names: String) = names.map { it to pattern }.toTypedArray()
}
