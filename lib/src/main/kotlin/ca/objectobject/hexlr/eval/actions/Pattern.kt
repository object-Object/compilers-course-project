package ca.objectobject.hexlr.eval.actions

import ca.objectobject.hexlr.eval.Runtime
import ca.objectobject.hexlr.eval.iotas.Iota
import ca.objectobject.hexlr.eval.iotas.PatternIota
import kotlin.reflect.KCallable

typealias EvalFn = KCallable<Iterable<Iota>?>

interface Pattern : Action {
    val name get() = this::class.simpleName ?: this.toString()

    /**
     * Pops n values from the stack, returning the lowest iota first.
     */
    fun pop(runtime: Runtime, n: Int = 1): List<Iota> {
        val size = runtime.stack.count()
        if (n > size) throw IllegalArgumentException("$name expected $n input(s) but got $size")
        return runtime.pop(n)
    }

    override fun toIota() = PatternIota(this)
}

abstract class TypedPattern : Pattern {
    abstract val eval: EvalFn

    override fun eval(runtime: Runtime) {
        val inputs = popInputs(runtime)

        val outputs = eval.call(*inputs)
        outputs?.forEach { runtime.stack.push(it) }
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
