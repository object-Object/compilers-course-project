package ca.objectobject.hexlr.eval.actions

import ca.objectobject.hexlr.TypeError
import ca.objectobject.hexlr.eval.Runtime
import ca.objectobject.hexlr.eval.iotas.Iota
import kotlin.reflect.KClass

interface Pattern : Action

abstract class TypedPattern : Pattern {
    /**
     * Input iota types. The rightmost value is at the top of the stack.
     */
    open val inputTypes: List<KClass<out Iota>> = listOf()
    /**
     * Output iota types. The rightmost value is at the top of the stack.
     */
    open val outputTypes: List<KClass<out Iota>> = listOf()

    abstract fun eval(runtime: Runtime, inputs: List<Iota>): List<Iota>

    override fun eval(runtime: Runtime) {
        val inputs = inputTypes.asReversed().map { type ->
            val iota = runtime.stack.pop()
            if (!type.isInstance(iota)) throw TypeError(iota, type)
            iota
        }

        val outputs = eval(runtime, inputs)

        if (outputs.count() != outputTypes.count()) {
            throw RuntimeException("Expected ${outputTypes.count()} returns, got ${outputs.count()}")
        }

        for ((type, iota) in outputTypes.zip(outputs)) {
            if (!type.isInstance(iota)) throw TypeError(iota, type)
            runtime.stack.push(iota)
        }
    }
}

abstract class Pattern1<T0: Iota>(t0: KClass<T0>) : TypedPattern() {
    override val inputTypes = listOf(t0)

    abstract fun eval(runtime: Runtime, input0: T0): List<Iota>

    @Suppress("UNCHECKED_CAST")
    override fun eval(runtime: Runtime, inputs: List<Iota>): List<Iota> {
        return eval(runtime, inputs[0] as T0)
    }
}

abstract class Pattern2<T0: Iota, T1: Iota>(t0: KClass<T0>, t1: KClass<T1>) : TypedPattern() {
    override val inputTypes = listOf(t0, t1)

    abstract fun eval(runtime: Runtime, input0: T0, input1: T1): List<Iota>

    @Suppress("UNCHECKED_CAST")
    override fun eval(runtime: Runtime, inputs: List<Iota>): List<Iota> {
        return eval(runtime, inputs[0] as T0, inputs[1] as T1)
    }
}
