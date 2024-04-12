package ca.objectobject.hexlr.eval.patterns

import ca.objectobject.hexlr.eval.Runtime
import ca.objectobject.hexlr.eval.EvalFn
import ca.objectobject.hexlr.eval.TypedPattern
import ca.objectobject.hexlr.eval.iotas.*
import ca.objectobject.hexlr.util.Single

data object OpIf : TypedPattern() {
    override val eval: EvalFn = ::eval

    fun eval(condition: BooleanIota, valueIf: Iota, valueElse: Iota) = if (condition.value) {
        valueIf
    } else {
        valueElse
    }.toSingle()
}

data object OpEval : TypedPattern() {
    override val eval: EvalFn = ::eval

    fun eval(runtime: Runtime, iota: EvaluableIota): Iterable<Iota>? {
        when (iota) {
            is PatternIota -> runtime.execute(iota)
            is ListIota -> runtime.execute(iota.values)
        }
        return null
    }
}

data object OpFor : TypedPattern() {
    override val eval: EvalFn = ::eval

    fun eval(runtime: Runtime, patterns: ListIota, data: ListIota): Single<ListIota> {
        val initialStack = runtime.stack.toList()

        val outputs = mutableListOf<Iota>()
        for (iota in data.values) {
            runtime.stack.push(iota)
            runtime.execute(patterns.values)
            outputs.addAll(runtime.stack)
            runtime.stack.clear()
            runtime.stack.addAll(initialStack)
        }

        return ListIota(outputs).toSingle()
    }
}
