package ca.objectobject.hexlr.eval.actions.patterns.arithmetic

import ca.objectobject.hexlr.eval.iotas.NumberIota
import ca.objectobject.hexlr.eval.iotas.VectorIota

data object OpDivide : ArithmeticPattern() {
    override fun operate(left: Double, right: Double) = NumberIota(left / right)

    override fun operate(left: Double, right: VectorIota) = right.let { (x, y, z) ->
        VectorIota(left / x, left / y, left / z)
    }

    override fun operate(left: VectorIota, right: Double) = left.let { (x, y, z) ->
        VectorIota(x / right, y / right, z / right)
    }

    // cross product
    override fun operate(left: VectorIota, right: VectorIota) = VectorIota(
        left.y * right.z - left.z * right.y,
        left.z * right.x - left.x * right.z,
        left.x * right.y - left.y * right.x,
    )
}