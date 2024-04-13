package ca.objectobject.hexlr.eval.patterns.arithmetic

import ca.objectobject.hexlr.eval.iotas.NumberIota
import ca.objectobject.hexlr.eval.iotas.VectorIota

data object OpModulus : ArithmeticPattern() {
    override fun operate(left: Double, right: Double) = NumberIota(left % right)

    override fun operate(left: Double, right: VectorIota) = right.run {
        VectorIota(left % x, left % y, left % z)
    }

    override fun operate(left: VectorIota, right: Double) = left.run {
        VectorIota(x % right, y % right, z % right)
    }

    // cross product
    override fun operate(left: VectorIota, right: VectorIota) =
        VectorIota(left.x % right.x, left.y % right.y, left.z % right.z)
}