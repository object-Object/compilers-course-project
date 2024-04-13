package ca.objectobject.hexlr.eval.patterns

import ca.objectobject.hexlr.eval.*
import ca.objectobject.hexlr.eval.iotas.Iota
import ca.objectobject.hexlr.eval.iotas.NumberIota

data class OpMask(val mask: List<Boolean>) : Pattern {
    constructor(mask: String) : this(mask.map { it == '-' })

    override fun eval(runtime: Runtime) {
        val iotas = pop(runtime, mask.count())
        for ((iota, keep) in iotas.zip(mask)) {
            if (keep) runtime.stack.push(iota)
        }
    }
}

data object OpCopy : TypedPatternMulti() {
    override val eval: EvalMulti = ::eval
    fun eval(iota: Iota) = listOf(iota, iota)
}

data object OpCopyFromBelow : TypedPatternMulti() {
    override val eval: EvalMulti = ::eval
    fun eval(a: Iota, b: Iota) = listOf(a, b, a)
}

data object OpCopyToBelow : TypedPatternMulti() {
    override val eval: EvalMulti = ::eval
    fun eval(a: Iota, b: Iota) = listOf(b, a, b)
}

data object OpCopyN : TypedPatternMulti() {
    override val eval: EvalMulti = ::eval
    fun eval(iota: Iota, n: NumberIota) = List(n.value.toInt()){ iota }
}

data object Op2Dup : TypedPatternMulti() {
    override val eval: EvalMulti = ::eval
    fun eval(a: Iota, b: Iota) = listOf(a, b, a, b)
}

data object OpSwap : TypedPatternMulti() {
    override val eval: EvalMulti = ::eval
    fun eval(a: Iota, b: Iota) = listOf(b, a)
}

data object OpYankUp : TypedPatternMulti() {
    override val eval: EvalMulti = ::eval
    fun eval(a: Iota, b: Iota, c: Iota) = listOf(b, c, a)
}

data object OpYankDown : TypedPatternMulti() {
    override val eval: EvalMulti = ::eval
    fun eval(a: Iota, b: Iota, c: Iota) = listOf(c, a, b)
}

data object OpFisherman : TypedPatternSingle() {
    override val eval: EvalSingle = ::eval

    fun eval(runtime: Runtime, n: NumberIota): Iota {
        val index = n.value.toInt() - 1
        return runtime.stack.run { removeAt(lastIndex - index) }
    }
}

data object OpFishermanCopy : TypedPatternSingle() {
    override val eval: EvalSingle = ::eval

    fun eval(runtime: Runtime, n: NumberIota): Iota {
        val index = n.value.toInt() // bug-for-bug parity :(
        return runtime.stack.run { get(lastIndex - index) }
    }
}

data object OpStackLen : Pattern {
    override fun eval(runtime: Runtime) {
        runtime.stack.push(NumberIota(runtime.stack.count()))
    }
}
