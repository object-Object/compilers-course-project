package ca.objectobject.hexlr.eval.actions.patterns

import ca.objectobject.hexlr.eval.Runtime
import ca.objectobject.hexlr.eval.actions.Pattern2
import ca.objectobject.hexlr.eval.iotas.ArithmeticIota
import ca.objectobject.hexlr.eval.iotas.NumberIota

abstract class ArithmeticPattern :
    Pattern2<ArithmeticIota, ArithmeticIota>(ArithmeticIota::class, ArithmeticIota::class) {
    override val outputTypes = listOf(ArithmeticIota::class)

    override fun eval(runtime: Runtime, input0: ArithmeticIota, input1: ArithmeticIota) = listOf(when (input0) {
        is NumberIota -> when (input1) {
            is NumberIota -> operate(input0.number, input1.number)
        }
    })

    abstract fun operate(left: Double, right: Double): ArithmeticIota
}

data object OpAdd : ArithmeticPattern() {
    override fun operate(left: Double, right: Double) = NumberIota(left + right)
}

data object OpSubtract : ArithmeticPattern() {
    override fun operate(left: Double, right: Double) = NumberIota(left - right)
}

data object OpMultiply : ArithmeticPattern() {
    override fun operate(left: Double, right: Double) = NumberIota(left * right)
}

data object OpDivide : ArithmeticPattern() {
    override fun operate(left: Double, right: Double) = NumberIota(left / right)
}
