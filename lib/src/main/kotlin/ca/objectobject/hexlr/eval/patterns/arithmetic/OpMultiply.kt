package ca.objectobject.hexlr.eval.patterns.arithmetic

import ca.objectobject.hexlr.eval.iotas.NumberIota
import ca.objectobject.hexlr.eval.iotas.VectorIota
import ca.objectobject.hexlr.eval.iotas.times
import ca.objectobject.hexlr.eval.iotas.toIota

data object OpMultiply : ArithmeticPattern() {
    override fun operate(left: Double, right: Double) = NumberIota(left * right)

    override fun operate(left: Double, right: VectorIota) = left * right

    override fun operate(left: VectorIota, right: Double) = left * right

    // dot product
    override fun operate(left: VectorIota, right: VectorIota) = left.dot(right).toIota()
}