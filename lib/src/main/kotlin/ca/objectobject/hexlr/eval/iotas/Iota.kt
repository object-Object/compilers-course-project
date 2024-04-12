package ca.objectobject.hexlr.eval.iotas

import ca.objectobject.hexlr.eval.actions.Pattern

interface Iota

data class BooleanIota(val value: Boolean) : Iota

data class PatternIota(val value: Pattern) : Iota

sealed interface ArithmeticIota : Iota

data class NumberIota(val value: Double) : ArithmeticIota {
    constructor(value: Number) : this(value.toDouble())
}

data class VectorIota(val x: Double, val y: Double, val z: Double) : ArithmeticIota {
    constructor(x: Number, y: Number, z: Number) : this(x.toDouble(), y.toDouble(), z.toDouble())
}
