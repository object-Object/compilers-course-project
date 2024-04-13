package ca.objectobject.hexlr.eval.patterns

import ca.objectobject.hexlr.eval.*
import ca.objectobject.hexlr.eval.iotas.Iota
import ca.objectobject.hexlr.eval.iotas.ListIota
import ca.objectobject.hexlr.eval.iotas.NumberIota
import ca.objectobject.hexlr.eval.iotas.toIota

data object OpEmptyList : ConstPattern(ListIota())

data object OpSingletonList : TypedPatternSingle() {
    override val eval: EvalSingle = ::eval
    fun eval(iota: Iota) = ListIota(iota)
}

data object OpSlurpList : TypedPatternSingle() {
    override val eval: EvalSingle = ::eval

    fun eval(runtime: Runtime, length: NumberIota): ListIota {
        val values = pop(runtime, length.value.toInt())
        return ListIota(values)
    }
}

data object OpSplatList : TypedPatternMulti() {
    override val eval: EvalMulti = ::eval
    fun eval(list: ListIota) = list.values
}

data object OpSelect : TypedPatternSingle() {
    override val eval: EvalSingle = ::eval
    fun eval(list: ListIota, index: NumberIota) = list.values[index.value.toInt()]
}

data object OpSlice : TypedPatternSingle() {
    override val eval: EvalSingle = ::eval
    fun eval(list: ListIota, from: NumberIota, to: NumberIota) =
        ListIota(list.values.subList(from.value.toInt(), to.value.toInt()))
}

data object OpAppend : TypedPatternSingle() {
    override val eval: EvalSingle = ::eval
    fun eval(list: ListIota, iota: Iota) = ListIota(list.values + iota)
}

data object OpConcat : TypedPatternSingle() {
    override val eval: EvalSingle = ::eval
    fun eval(left: ListIota, right: ListIota) = ListIota(left.values + right.values)
}

data object OpListLen : TypedPatternSingle() {
    override val eval: EvalSingle = ::eval
    fun eval(list: ListIota) = list.values.count().toIota()
}

data object OpReverse : TypedPatternSingle() {
    override val eval: EvalSingle = ::eval
    fun eval(list: ListIota) = ListIota(list.values.asReversed())
}

data object OpIndexOf : TypedPatternSingle() {
    override val eval: EvalSingle = ::eval
    fun eval(list: ListIota, iota: Iota) = list.values.indexOf(iota).toIota()
}

data object OpRemove : TypedPatternSingle() {
    override val eval: EvalSingle = ::eval

    fun eval(list: ListIota, index: NumberIota) = ListIota(list.values.toMutableList().apply {
        removeAt(index.value.toInt())
    })
}

data object OpInsert : TypedPatternSingle() {
    override val eval: EvalSingle = ::eval

    fun eval(list: ListIota, index: NumberIota, iota: Iota) = ListIota(list.values.toMutableList().apply {
        set(index.value.toInt(), iota)
    })
}

data object OpPushFirst : TypedPatternSingle() {
    override val eval: EvalSingle = ::eval
    fun eval(list: ListIota, iota: Iota) = ListIota(listOf(iota) + list.values)
}

data object OpPopFirst : TypedPatternMulti() {
    override val eval: EvalMulti = ::eval

    fun eval(list: ListIota) = list.values.run {
        listOf(ListIota(subList(1, lastIndex + 1)), get(0))
    }
}
