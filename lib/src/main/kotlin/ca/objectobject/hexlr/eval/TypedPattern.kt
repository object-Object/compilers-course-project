package ca.objectobject.hexlr.eval

import ca.objectobject.hexlr.eval.iotas.Iota
import ca.objectobject.hexlr.util.Single
import kotlin.reflect.KCallable

abstract class TypedPattern<R> : Pattern {
    abstract val eval: KCallable<R?>

    abstract fun getOutputs(output: R): Iterable<Iota>

    override fun eval(runtime: Runtime) {
        val inputs = popInputs(runtime)
        val outputs = eval.call(*inputs) ?: return
        runtime.stack.addAll(getOutputs(outputs))
    }

    private fun popInputs(runtime: Runtime): Array<Any> = eval.parameters.let { params ->
        var numInputs = params.count()
        if (numInputs == 0) return arrayOf()

        val passRuntime = params[0].type.classifier?.equals(Runtime::class) ?: false
        if (passRuntime) numInputs -= 1

        val inputs = pop(runtime, numInputs).toTypedArray<Any>()
        return if (passRuntime) {
            arrayOf(runtime, *inputs)
        } else {
            inputs
        }
    }
}

typealias EvalUnit = KCallable<Unit>

abstract class TypedPatternUnit : TypedPattern<Unit>() {
    abstract override val eval: EvalUnit

    override fun getOutputs(output: Unit) = listOf<Iota>()
}

typealias EvalSingle = KCallable<Iota?>

abstract class TypedPatternSingle : TypedPattern<Iota>() {
    abstract override val eval: EvalSingle

    override fun getOutputs(output: Iota) = Single(output)
}

typealias EvalMulti = KCallable<Iterable<Iota>?>

abstract class TypedPatternMulti : TypedPattern<Iterable<Iota>>() {
    abstract override val eval: EvalMulti

    override fun getOutputs(output: Iterable<Iota>) = output
}
