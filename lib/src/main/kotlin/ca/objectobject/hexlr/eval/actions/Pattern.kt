package ca.objectobject.hexlr.eval.actions

import ca.objectobject.hexlr.eval.Runtime
import ca.objectobject.hexlr.eval.iotas.Iota
import kotlin.reflect.KCallable

typealias EvalFn = KCallable<Iterable<Iota>>

interface Pattern : Action {
    val name get() = this::class.simpleName ?: this.toString()
}

abstract class TypedPattern : Pattern {
    abstract val eval: EvalFn

    override fun eval(runtime: Runtime) {
        val inputs = popInputs(runtime)

        val outputs = eval.call(*inputs)
        outputs.forEach { runtime.stack.push(it) }
    }

    private fun popInputs(runtime: Runtime): Array<Any> = eval.parameters.let { params ->
        var numInputs = params.count()
        if (numInputs == 0) return arrayOf()

        val passRuntime = params[0].type.classifier?.equals(Runtime::class) ?: false
        if (passRuntime) numInputs -= 1

        val inputs = mutableListOf<Any>()
        for (i in 0 until numInputs) {
            if (runtime.stack.empty()) {
                val s = if (numInputs == 1) { "" } else { "s" }
                throw IllegalArgumentException("$name expected $numInputs input$s but got $i")
            }
            inputs.add(runtime.stack.pop())
        }

        if (passRuntime) inputs += runtime
        return inputs.asReversed().toTypedArray()
    }
}
