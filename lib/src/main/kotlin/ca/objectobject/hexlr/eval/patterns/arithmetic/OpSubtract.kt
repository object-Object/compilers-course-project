package ca.objectobject.hexlr.eval.patterns.arithmetic

import ca.objectobject.hexlr.eval.iotas.NumberIota
import ca.objectobject.hexlr.eval.iotas.VectorIota

data object OpSubtract : ArithmeticPattern() {
    override fun operate(left: Double, right: Double) = NumberIota(left - right)

    override fun operate(left: Double, right: VectorIota) = right.let { (x, y, z) ->
        VectorIota(left - x, left - y, left - z)
    }

    override fun operate(left: VectorIota, right: Double) = left.let { (x, y, z) ->
        VectorIota(x - right, y - right, z - right)
    }

    override fun operate(left: VectorIota, right: VectorIota) =
        VectorIota(left.x - right.x, left.y - right.y, left.z - right.z)
}