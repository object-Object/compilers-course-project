package ca.objectobject.hexlr.eval.iotas

import ca.objectobject.hexlr.eval.actions.Pattern

sealed interface EvaluableIota : Iota

data class PatternIota(val value: Pattern) : EvaluableIota

data class ListIota(val values: List<Iota> = listOf()) : EvaluableIota {
    constructor(vararg values: Iota) : this(values.toList())
}
