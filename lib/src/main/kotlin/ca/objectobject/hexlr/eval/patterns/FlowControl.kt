package ca.objectobject.hexlr.eval.patterns

import ca.objectobject.hexlr.eval.*
import ca.objectobject.hexlr.eval.iotas.*

data object OpIf : TypedPatternSingle() {
    override val eval: EvalSingle = ::eval

    fun eval(condition: BooleanIota, valueIf: Iota, valueElse: Iota) = if (condition.value) {
        valueIf
    } else {
        valueElse
    }
}

data object OpEval : TypedPatternUnit() {
    override val eval: EvalUnit = ::evalUnit

    fun evalUnit(runtime: Runtime, iota: EvaluableIota) {
        when (iota) {
            is PatternIota -> runtime.execute(iota)
            is ListIota -> runtime.execute(iota.values)
        }
    }
}

data object OpFor : TypedPatternSingle() {
    override val eval: EvalSingle = ::eval

    fun eval(runtime: Runtime, patterns: ListIota, data: ListIota): ListIota {
        val initialStack = runtime.stack.toList()

        val outputs = mutableListOf<Iota>()
        for (iota in data.values) {
            runtime.stack.push(iota)
            val keepIterating = runtime.execute(patterns.values)
            outputs.addAll(runtime.stack)
            runtime.stack.clear()
            runtime.stack.addAll(initialStack)
            if (!keepIterating) break
        }

        return ListIota(outputs)
    }
}

data object OpHalt : Pattern {
    override fun eval(runtime: Runtime) = throw NotImplementedError()
}
