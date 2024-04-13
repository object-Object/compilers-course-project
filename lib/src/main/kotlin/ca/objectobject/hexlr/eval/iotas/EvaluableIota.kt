package ca.objectobject.hexlr.eval.iotas

import ca.objectobject.hexlr.eval.Pattern

/**
 * Iotas that can be evaluated by Hermes' Gambit.
 */
sealed interface EvaluableIota : Iota

data class PatternIota(val value: Pattern) : EvaluableIota {
    override fun toRevealString() = value.name
}

data class ListIota(val values: List<Iota> = listOf()) : EvaluableIota {
    constructor(vararg values: Iota) : this(values.toList())

    override fun toRevealString() = "[" + values.joinToString { it.toRevealString() } + "]"
}
