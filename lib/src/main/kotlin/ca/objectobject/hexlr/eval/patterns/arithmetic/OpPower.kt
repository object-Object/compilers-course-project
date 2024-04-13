package ca.objectobject.hexlr.eval.patterns.arithmetic

import ca.objectobject.hexlr.eval.iotas.NumberIota
import ca.objectobject.hexlr.eval.iotas.VectorIota
import ca.objectobject.hexlr.eval.iotas.times
import kotlin.math.pow

data object OpPower : ArithmeticPattern() {
    override fun operate(left: Double, right: Double) = NumberIota(left.pow(right))

    override fun operate(left: Double, right: VectorIota) = right.run {
        VectorIota(left.pow(x), left.pow(y), left.pow(z))
    }

    override fun operate(left: VectorIota, right: Double) = left.run {
        VectorIota(x.pow(right), y.pow(right), z.pow(right))
    }

    // projection
    override fun operate(left: VectorIota, right: VectorIota) = (left.dot(right) / left.dot(left)) * left
}