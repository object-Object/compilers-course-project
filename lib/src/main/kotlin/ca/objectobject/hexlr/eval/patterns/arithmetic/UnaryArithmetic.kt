package ca.objectobject.hexlr.eval.patterns.arithmetic

import ca.objectobject.hexlr.eval.EvalSingle
import ca.objectobject.hexlr.eval.TypedPatternSingle
import ca.objectobject.hexlr.eval.iotas.ArithmeticIota
import ca.objectobject.hexlr.eval.iotas.NumberIota
import ca.objectobject.hexlr.eval.iotas.VectorIota
import ca.objectobject.hexlr.eval.iotas.toIota
import kotlin.math.*

abstract class UnaryArithmeticPattern : TypedPatternSingle() {
    override val eval: EvalSingle = ::eval

    fun eval(iota: ArithmeticIota) = when (iota) {
            is NumberIota -> operate(iota.value)
            is VectorIota -> operate(iota)
    }

    open fun operate(num: Double): ArithmeticIota = throw IllegalArgumentException()
    open fun operate(vec: VectorIota): ArithmeticIota = throw IllegalArgumentException()
}

data object OpAbsLength : UnaryArithmeticPattern() {
    override fun operate(num: Double) = num.absoluteValue.toIota()
    override fun operate(vec: VectorIota) = vec.run {
        sqrt(x.pow(2) + y.pow(2) + z.pow(2))
    }.toIota()
}

data object OpFloor : UnaryArithmeticPattern() {
    override fun operate(num: Double) = floor(num).toIota()
    override fun operate(vec: VectorIota) = vec.run { VectorIota(floor(x), floor(y), floor(z)) }
}

data object OpCeil : UnaryArithmeticPattern() {
    override fun operate(num: Double) = ceil(num).toIota()
    override fun operate(vec: VectorIota) = vec.run { VectorIota(ceil(x), ceil(y), ceil(z)) }
}

data object OpSignAxis : UnaryArithmeticPattern() {
    override fun operate(num: Double) = sign(num).toIota()
    override fun operate(vec: VectorIota) = vec.run {
        if (x == 0.0 && y == 0.0 && z == 0.0) vec
        else if (abs(x) > abs(y) && abs(x) > abs(z)) VectorIota(sign(x), 0, 0)
        else if (abs(y) > abs(x) && abs(y) > abs(z)) VectorIota(0, sign(y), 0)
        else VectorIota(0, 0, sign(z))
    }
}
