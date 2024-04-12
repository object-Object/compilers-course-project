package ca.objectobject.hexlr.eval.actions.patterns.arithmetic

import ca.objectobject.hexlr.eval.actions.EvalFn
import ca.objectobject.hexlr.eval.actions.TypedPattern
import ca.objectobject.hexlr.eval.iotas.ArithmeticIota
import ca.objectobject.hexlr.eval.iotas.NumberIota
import ca.objectobject.hexlr.eval.iotas.VectorIota

abstract class ArithmeticPattern : TypedPattern() {
    override val eval: EvalFn = ::eval

    fun eval(left: ArithmeticIota, right: ArithmeticIota) = listOf(
        when (left) {
            is NumberIota -> when (right) {
                is NumberIota -> operate(left.value, right.value)
                is VectorIota -> operate(left.value, right)
            }
            is VectorIota -> when (right) {
                is NumberIota -> operate(left, right.value)
                is VectorIota -> operate(left, right)
            }
        }
    )

    open fun operate(left: Double, right: Double): ArithmeticIota = throw IllegalArgumentException()
    open fun operate(left: Double, right: VectorIota): ArithmeticIota = throw IllegalArgumentException()
    open fun operate(left: VectorIota, right: Double): ArithmeticIota = throw IllegalArgumentException()
    open fun operate(left: VectorIota, right: VectorIota): ArithmeticIota = throw IllegalArgumentException()
}