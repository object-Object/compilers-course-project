package ca.objectobject.hexlr.eval.patterns

import ca.objectobject.hexlr.eval.EvalFn
import ca.objectobject.hexlr.eval.Runtime
import ca.objectobject.hexlr.eval.TypedPattern
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

data object OpSelect : TypedPattern() {
    override val eval: EvalFn = ::eval
    fun eval(list: ListIota, index: NumberIota) = list.values[index.value.toInt()].toSingle()
}

data object OpSlice : TypedPattern() {
    override val eval: EvalFn = ::eval
    fun eval(list: ListIota, from: NumberIota, to: NumberIota) =
        ListIota(list.values.subList(from.value.toInt(), to.value.toInt())).toSingle()
}

data object OpAppend : TypedPattern() {
    override val eval: EvalFn = ::eval
    fun eval(list: ListIota, iota: Iota) = ListIota(list.values + iota).toSingle()
}

data object OpConcat : TypedPattern() {
    override val eval: EvalFn = ::eval
    fun eval(left: ListIota, right: ListIota) = ListIota(left.values + right.values).toSingle()
}

data object OpListLen : TypedPattern() {
    override val eval: EvalFn = ::eval
    fun eval(list: ListIota) = list.values.count().toIota().toSingle()
}

data object OpReverse : TypedPattern() {
    override val eval: EvalFn = ::eval
    fun eval(list: ListIota) = ListIota(list.values.asReversed()).toSingle()
}

data object OpIndexOf : TypedPattern() {
    override val eval: EvalFn = ::eval
    fun eval(list: ListIota, iota: Iota) = list.values.indexOf(iota).toIota().toSingle()
}

data object OpRemove : TypedPattern() {
    override val eval: EvalFn = ::eval

    fun eval(list: ListIota, index: NumberIota) = ListIota(list.values.toMutableList().apply {
        removeAt(index.value.toInt())
    }).toSingle()
}

data object OpInsert : TypedPattern() {
    override val eval: EvalFn = ::eval

    fun eval(list: ListIota, index: NumberIota, iota: Iota) = ListIota(list.values.toMutableList().apply {
        set(index.value.toInt(), iota)
    }).toSingle()
}

data object OpPushFirst : TypedPattern() {
    override val eval: EvalFn = ::eval
    fun eval(list: ListIota, iota: Iota) = ListIota(listOf(iota) + list.values).toSingle()
}

data object OpPopFirst : TypedPattern() {
    override val eval: EvalFn = ::eval

    fun eval(list: ListIota) = list.values.run {
        listOf(ListIota(subList(1, lastIndex + 1)), get(0))
    }
}
