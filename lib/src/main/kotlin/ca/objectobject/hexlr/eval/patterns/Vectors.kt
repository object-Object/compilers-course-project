package ca.objectobject.hexlr.eval.patterns

import ca.objectobject.hexlr.eval.EvalMulti
import ca.objectobject.hexlr.eval.EvalSingle
import ca.objectobject.hexlr.eval.TypedPatternMulti
import ca.objectobject.hexlr.eval.TypedPatternSingle
import ca.objectobject.hexlr.eval.iotas.NumberIota
import ca.objectobject.hexlr.eval.iotas.VectorIota

data class OpVector(val x: Number = 0, val y: Number = 0, val z: Number = 0) : ConstPattern(VectorIota(x, y, z))

data object OpSlurpVec : TypedPatternSingle() {
    override val eval: EvalSingle = ::eval

    fun eval(x: NumberIota, y: NumberIota, z: NumberIota) = VectorIota(x, y, z)
}

data object OpSplatVec : TypedPatternMulti() {
    override val eval: EvalMulti = ::eval

    fun eval(vec: VectorIota) = listOf(vec.x, vec.y, vec.z).map(::NumberIota)
}
