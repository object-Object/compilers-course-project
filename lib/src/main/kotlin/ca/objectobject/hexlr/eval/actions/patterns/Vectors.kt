package ca.objectobject.hexlr.eval.actions.patterns

import ca.objectobject.hexlr.eval.actions.EvalFn
import ca.objectobject.hexlr.eval.actions.TypedPattern
import ca.objectobject.hexlr.eval.iotas.NumberIota
import ca.objectobject.hexlr.eval.iotas.VectorIota
import ca.objectobject.hexlr.eval.iotas.toSingle

data class OpVector(val x: Number = 0, val y: Number = 0, val z: Number = 0) : ConstPattern(VectorIota(x, y, z))

data object OpSlurpVec : TypedPattern() {
    override val eval: EvalFn = ::eval

    fun eval(x: NumberIota, y: NumberIota, z: NumberIota) = VectorIota(x, y, z).toSingle()
}

data object OpSplatVec : TypedPattern() {
    override val eval: EvalFn = ::eval

    fun eval(vec: VectorIota) = listOf(vec.x, vec.y, vec.z).map(::NumberIota)
}
