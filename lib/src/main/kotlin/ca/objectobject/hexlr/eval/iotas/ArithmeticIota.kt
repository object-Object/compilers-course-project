package ca.objectobject.hexlr.eval.iotas

sealed interface ArithmeticIota : Iota

data class NumberIota(val value: Double) : ArithmeticIota {
    constructor(value: Number) : this(value.toDouble())
}

data class VectorIota(val x: Double, val y: Double, val z: Double) : ArithmeticIota {
    constructor(x: Number, y: Number, z: Number) : this(x.toDouble(), y.toDouble(), z.toDouble())

    constructor(x: NumberIota, y: NumberIota, z: NumberIota) : this(x.value, y.value, z.value)
}
