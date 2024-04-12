package ca.objectobject.hexlr.eval.actions.patterns

import ca.objectobject.hexlr.TypeError
import ca.objectobject.hexlr.eval.Runtime
import ca.objectobject.hexlr.eval.actions.Pattern2
import ca.objectobject.hexlr.eval.iotas.ArithmeticIota
import ca.objectobject.hexlr.eval.iotas.NumberIota
import ca.objectobject.hexlr.eval.iotas.VectorIota

abstract class ArithmeticPattern :
    Pattern2<ArithmeticIota, ArithmeticIota>(ArithmeticIota::class, ArithmeticIota::class) {
    override val outputTypes = listOf(ArithmeticIota::class)

    override fun eval(runtime: Runtime, input0: ArithmeticIota, input1: ArithmeticIota) = listOf(
        when (input0) {
            is NumberIota -> when (input1) {
                is NumberIota -> operate(input0.number, input1.number)
                is VectorIota -> operate(input0.number, input1)
            }

            is VectorIota -> when (input1) {
                is NumberIota -> operate(input0, input1.number)
                is VectorIota -> operate(input0, input1)
            }
        }
    )

    open fun operate(left: Double, right: Double): ArithmeticIota = throw TypeError()
    open fun operate(left: Double, right: VectorIota): ArithmeticIota = throw TypeError()
    open fun operate(left: VectorIota, right: Double): ArithmeticIota = throw TypeError()
    open fun operate(left: VectorIota, right: VectorIota): ArithmeticIota = throw TypeError()
}

data object OpAdd : ArithmeticPattern() {
    override fun operate(left: Double, right: Double) = NumberIota(left + right)

    override fun operate(left: Double, right: VectorIota) = right.let { (x, y, z) ->
        VectorIota(left + x, left + y, left + z)
    }

    override fun operate(left: VectorIota, right: Double) = operate(right, left)

    override fun operate(left: VectorIota, right: VectorIota) =
        VectorIota(left.x + right.x, left.y + right.y, left.z + right.z)
}

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
