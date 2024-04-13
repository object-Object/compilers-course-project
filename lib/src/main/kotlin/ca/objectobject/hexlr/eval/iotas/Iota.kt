package ca.objectobject.hexlr.eval.iotas

interface Iota {
    fun toRevealString(): String
}

data class BooleanIota(val value: Boolean) : Iota {
    override fun toRevealString() = value.toString()
}

data object NullIota : Iota {
    override fun toRevealString() = "NULL"
}
