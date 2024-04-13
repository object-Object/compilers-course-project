package ca.objectobject.hexlr.eval.patterns

import ca.objectobject.hexlr.eval.EvalUnit
import ca.objectobject.hexlr.eval.Runtime
import ca.objectobject.hexlr.eval.TypedPatternUnit
import ca.objectobject.hexlr.eval.iotas.NumberIota

// source: https://github.com/FallingColors/HexMod/blob/c8510ed83d/Common/src/main/java/at/petrak/hexcasting/common/casting/operators/stack/OpAlwinfyHasAscendedToABeingOfPureMath.kt
data object OpSwindle : TypedPatternUnit() {
    override val eval: EvalUnit = ::evalUnit

    fun evalUnit(runtime: Runtime, codeIota: NumberIota) {
        val code = codeIota.value.toInt()
        if (code < 0) throw IllegalArgumentException("$codeIota.value")

        val strides = mutableListOf<Int>()
        for (f in FactorialIter()) {
            if (f <= code)
                strides.add(f)
            else
                break
        }

        runtime.stack.also { stack ->
            if (strides.size > runtime.stack.size)
                throw IllegalArgumentException("$name expected ${strides.size} input(s) but got ${stack.size}")
            var editTarget = stack.subList(stack.size - strides.size, stack.size)
            val swap = editTarget.toMutableList()
            var radix = code
            for (divisor in strides.asReversed()) {
                val index = radix / divisor
                radix %= divisor
                editTarget[0] = swap.removeAt(index)
                editTarget = editTarget.subList(1, editTarget.size)
            }
        }
    }

    private class FactorialIter : Iterator<Int> {
        var acc = 1
        var n = 1
        override fun hasNext(): Boolean = true

        override fun next(): Int {
            val out = this.acc
            this.acc *= this.n
            this.n++
            return out
        }
    }
}