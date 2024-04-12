package ca.objectobject.hexlr.eval.iotas

import ca.objectobject.hexlr.eval.actions.Pattern

interface Iota

data class PatternIota(val pattern: Pattern) : Iota

sealed interface ArithmeticIota : Iota

data class NumberIota(val number: Double) : ArithmeticIota

data class VectorIota(val x: Double, val y: Double, val z: Double) : ArithmeticIota
