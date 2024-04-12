package ca.objectobject.hexlr.eval.actions.patterns.arithmetic

import ca.objectobject.hexlr.eval.iotas.NumberIota
import ca.objectobject.hexlr.eval.iotas.VectorIota

data object OpMultiply : ArithmeticPattern() {
    override fun operate(left: Double, right: Double) = NumberIota(left * right)

    override fun operate(left: Double, right: VectorIota) = right.let { (x, y, z) ->
        VectorIota(left * x, left * y, left * z)
    }

    override fun operate(left: VectorIota, right: Double) = operate(right, left)

    // dot product
    override fun operate(left: VectorIota, right: VectorIota) =
        NumberIota(left.x * right.x + left.y * right.y + left.z * right.z)
}