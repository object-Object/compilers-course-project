package ca.objectobject.hexlr.eval.actions.patterns

import ca.objectobject.hexlr.eval.Runtime
import ca.objectobject.hexlr.eval.actions.EvalFn
import ca.objectobject.hexlr.eval.actions.TypedPattern
import ca.objectobject.hexlr.eval.iotas.*
import ca.objectobject.hexlr.util.Single

data object OpEmptyList : ConstPattern(ListIota())

data object OpSingletonList : TypedPattern() {
    override val eval: EvalFn = ::eval

    fun eval(iota: Iota) = ListIota(iota).toSingle()
}

data object OpSlurpList : TypedPattern() {
    override val eval: EvalFn = ::eval

    fun eval(runtime: Runtime, length: NumberIota): Single<ListIota> {
        val values = pop(runtime, length.value.toInt())
        return ListIota(values).toSingle()
    }
}

data object OpSplatList : TypedPattern() {
    override val eval: EvalFn = ::eval

    fun eval(list: ListIota) = list.values
}
