package ca.objectobject.hexlr.eval.iotas

import java.util.*

sealed interface ArithmeticIota : Iota

data class NumberIota(val value: Double) : ArithmeticIota {
    constructor(value: Number) : this(value.toDouble())

    constructor(value: String) : this(value.toDouble())

    override fun toRevealString() = "%.4f".format(Locale.ROOT, value)
}

fun Number.toIota() = NumberIota(this)

data class VectorIota(val x: Double, val y: Double, val z: Double) : ArithmeticIota {
    constructor(x: Number, y: Number, z: Number) : this(x.toDouble(), y.toDouble(), z.toDouble())

    constructor(x: String, y: String, z: String) : this(x.toDouble(), y.toDouble(), z.toDouble())

    constructor(x: NumberIota, y: NumberIota, z: NumberIota) : this(x.value, y.value, z.value)

    /**
     * Dot product.
     */
    fun dot(other: VectorIota) = x * other.x + y * other.y + z * other.z

    operator fun times(num: Double) = VectorIota(x * num, y * num, z * num)

    override fun toRevealString() = "(%.4f, %.4f, %.4f)".format(Locale.ROOT, x, y, z)
}

operator fun Double.times(vec: VectorIota) = vec * this
