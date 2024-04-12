package ca.objectobject.hexlr.eval.iotas

import ca.objectobject.hexlr.util.Single

interface Iota

fun <T: Iota> T.toSingle() = Single(this)

data class BooleanIota(val value: Boolean) : Iota
