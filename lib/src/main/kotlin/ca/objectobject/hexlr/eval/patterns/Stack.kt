package ca.objectobject.hexlr.eval.patterns

import ca.objectobject.hexlr.eval.EvalFn
import ca.objectobject.hexlr.eval.Pattern
import ca.objectobject.hexlr.eval.Runtime
import ca.objectobject.hexlr.eval.TypedPattern
import ca.objectobject.hexlr.eval.iotas.Iota
import ca.objectobject.hexlr.eval.iotas.NumberIota
import ca.objectobject.hexlr.eval.iotas.toSingle
import ca.objectobject.hexlr.util.Single

data class OpMask(val mask: List<Boolean>) : Pattern {
    constructor(mask: String) : this(mask.map { it == '-' })

    override fun eval(runtime: Runtime) {
        val iotas = pop(runtime, mask.count())
        for ((iota, keep) in iotas.zip(mask)) {
            if (keep) runtime.stack.push(iota)
        }
    }
}

data object OpCopy : TypedPattern() {
    override val eval: EvalFn = ::eval
    fun eval(iota: Iota) = listOf(iota, iota)
}

data object OpCopyFromBelow : TypedPattern() {
    override val eval: EvalFn = ::eval
    fun eval(a: Iota, b: Iota) = listOf(a, b, a)
}

data object OpCopyToBelow : TypedPattern() {
    override val eval: EvalFn = ::eval
    fun eval(a: Iota, b: Iota) = listOf(b, a, b)
}

data object OpCopyN : TypedPattern() {
    override val eval: EvalFn = ::eval
    fun eval(iota: Iota, n: NumberIota) = List(n.value.toInt()){ iota }
}

data object Op2Dup : TypedPattern() {
    override val eval: EvalFn = ::eval
    fun eval(a: Iota, b: Iota) = listOf(a, b, a, b)
}

data object OpSwap : TypedPattern() {
    override val eval: EvalFn = ::eval
    fun eval(a: Iota, b: Iota) = listOf(b, a)
}

data object OpYankUp : TypedPattern() {
    override val eval: EvalFn = ::eval
    fun eval(a: Iota, b: Iota, c: Iota) = listOf(b, c, a)
}

data object OpYankDown : TypedPattern() {
    override val eval: EvalFn = ::eval
    fun eval(a: Iota, b: Iota, c: Iota) = listOf(c, a, b)
}

data object OpFisherman : TypedPattern() {
    override val eval: EvalFn = ::eval

    fun eval(runtime: Runtime, n: NumberIota): Single<Iota> {
        val index = n.value.toInt() - 1
        return runtime.stack.run { removeAt(lastIndex - index) }.toSingle()
    }
}

data object OpFishermanCopy : TypedPattern() {
    override val eval: EvalFn = ::eval

    fun eval(runtime: Runtime, n: NumberIota): Single<Iota> {
        val index = n.value.toInt() // bug-for-bug parity :(
        return runtime.stack.run { get(lastIndex - index) }.toSingle()
    }
}

data object OpStackLen : Pattern {
    override fun eval(runtime: Runtime) {
        runtime.stack.push(NumberIota(runtime.stack.count()))
    }
}
