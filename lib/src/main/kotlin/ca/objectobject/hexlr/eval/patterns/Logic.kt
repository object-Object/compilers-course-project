package ca.objectobject.hexlr.eval.patterns

import ca.objectobject.hexlr.eval.EvalSingle
import ca.objectobject.hexlr.eval.TypedPatternSingle
import ca.objectobject.hexlr.eval.iotas.*

data object OpIf : TypedPatternSingle() {
    override val eval: EvalSingle = ::eval

    fun eval(condition: BooleanIota, valueIf: Iota, valueElse: Iota) = if (condition.value) {
        valueIf
    } else {
        valueElse
    }
}

data object OpTruthy : TypedPatternSingle() {
    override val eval: EvalSingle = ::eval

    fun eval(iota: Iota) = BooleanIota(when (iota) {
        BooleanIota(false), NullIota, NumberIota(0), ListIota() -> false
        else -> true
    })
}

data object OpNot : TypedPatternSingle() {
    override val eval: EvalSingle = ::eval
    fun eval(bool: BooleanIota) = BooleanIota(!bool.value)
}

data object OpOr : TypedPatternSingle() {
    override val eval: EvalSingle = ::eval
    fun eval(left: BooleanIota, right: BooleanIota) = BooleanIota(left.value || right.value)
}

data object OpAnd : TypedPatternSingle() {
    override val eval: EvalSingle = ::eval
    fun eval(left: BooleanIota, right: BooleanIota) = BooleanIota(left.value && right.value)
}

data object OpXor : TypedPatternSingle() {
    override val eval: EvalSingle = ::eval
    fun eval(left: BooleanIota, right: BooleanIota) = BooleanIota(left.value.xor(right.value))
}

data object OpEqual : TypedPatternSingle() {
    override val eval: EvalSingle = ::eval
    fun eval(left: Iota, right: Iota) = BooleanIota(left == right)
}

data object OpNotEqual : TypedPatternSingle() {
    override val eval: EvalSingle = ::eval
    fun eval(left: Iota, right: Iota) = BooleanIota(left != right)
}

data object OpGreaterThan : TypedPatternSingle() {
    override val eval: EvalSingle = ::eval
    fun eval(left: NumberIota, right: NumberIota) = BooleanIota(left.value > right.value)
}

data object OpLessThan : TypedPatternSingle() {
    override val eval: EvalSingle = ::eval
    fun eval(left: NumberIota, right: NumberIota) = BooleanIota(left.value < right.value)
}

data object OpGreaterEqual : TypedPatternSingle() {
    override val eval: EvalSingle = ::eval
    fun eval(left: NumberIota, right: NumberIota) = BooleanIota(left.value >= right.value)
}

data object OpLessEqual : TypedPatternSingle() {
    override val eval: EvalSingle = ::eval
    fun eval(left: NumberIota, right: NumberIota) = BooleanIota(left.value <= right.value)
}
