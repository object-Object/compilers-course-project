package ca.objectobject.hexlr.eval.patterns

import ca.objectobject.hexlr.eval.*
import ca.objectobject.hexlr.eval.iotas.EvaluableIota
import ca.objectobject.hexlr.eval.iotas.Iota
import ca.objectobject.hexlr.eval.iotas.ListIota
import ca.objectobject.hexlr.eval.iotas.PatternIota

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

    fun eval(runtime: Runtime, patterns: ListIota, data: ListIota) = runtime.run {
        val initialStack = stack.toList()

        val outputs = mutableListOf<Iota>()
        for (iota in data.values) {
            stack.push(iota)
            execute(patterns.values)
            outputs.addAll(stack)
            stack.clear()
            stack.addAll(initialStack)
            if (shouldHalt) break
        }

        ListIota(outputs)
    }
}

data object OpHalt : Pattern {
    override fun eval(runtime: Runtime) {
        runtime.shouldHalt = true
    }
}
