package ca.objectobject.hexlr.eval.actions.patterns

import ca.objectobject.hexlr.eval.actions.EvalFn
import ca.objectobject.hexlr.eval.actions.TypedPattern
import ca.objectobject.hexlr.eval.iotas.NumberIota
import ca.objectobject.hexlr.eval.iotas.VectorIota

data object OpCreateVec : TypedPattern() {
    override val eval: EvalFn = ::eval

    fun eval(x: NumberIota, y: NumberIota, z: NumberIota) = listOf(VectorIota(x, y, z))
}

data object OpSplatVec : TypedPattern() {
    override val eval: EvalFn = ::eval

    fun eval(vec: VectorIota) = listOf(vec.x, vec.y, vec.z).map(::NumberIota)
}
