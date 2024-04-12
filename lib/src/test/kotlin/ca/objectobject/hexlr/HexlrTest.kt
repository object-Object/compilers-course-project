/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package ca.objectobject.hexlr

import ca.objectobject.hexlr.eval.actions.patterns.OpEscape
import ca.objectobject.hexlr.eval.actions.patterns.OpLeftParen
import ca.objectobject.hexlr.eval.actions.patterns.OpRightParen
import ca.objectobject.hexlr.eval.actions.patterns.OpTrue
import ca.objectobject.hexlr.eval.iotas.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals

class HexlrTest {
    @ParameterizedTest
    @MethodSource("getData")
    fun programProducesExpectedStack(program: String, want: List<Iota>) {
        val runtime = execute(program.trimIndent())
        assertEquals(want.asReversed(), runtime.stack.toList())
    }

    companion object {
        @JvmStatic
        fun getData() = listOf(
            "" to listOf(),
            "Numerical Reflection: 0" to listOf(NumberIota(0)),
            "Numerical Reflection: 1" to listOf(NumberIota(1)),
            "Numerical Reflection: -2.5" to listOf(NumberIota(-2.5)),
            "Numerical Reflection: 1e2" to listOf(NumberIota(100)),
            """
                Numerical Reflection: 0
                Numerical Reflection: 1
            """ to listOf(
                NumberIota(1),
                NumberIota(0),
            ),
            """
                Numerical Reflection: 1
                Numerical Reflection: 2
                Additive Distillation
            """ to listOf(NumberIota(3)),
            """
                Numerical Reflection: 1
                Numerical Reflection: 2
                Subtractive Distillation
            """ to listOf(NumberIota(-1)),
            """
                Numerical Reflection: 2
                Numerical Reflection: 3
                Multiplicative Distillation
            """ to listOf(NumberIota(6)),
            """
                Numerical Reflection: 1
                Numerical Reflection: 2
                Division Distillation
            """ to listOf(NumberIota(0.5)),
            """
                Numerical Reflection: 0
                Numerical Reflection: 1
                Numerical Reflection: 2
                Vector Exaltation
            """ to listOf(VectorIota(0, 1, 2)),
            "True Reflection" to listOf(BooleanIota(true)),
            "False Reflection" to listOf(BooleanIota(false)),
            """
                True Reflection
                Numerical Reflection: 1
                Numerical Reflection: 2
                Augur's Exaltation
            """ to listOf(NumberIota(1)),
            """
                False Reflection
                Numerical Reflection: 1
                Numerical Reflection: 2
                Augur's Exaltation
            """ to listOf(NumberIota(2)),
            "Consideration: True Reflection" to listOf(OpTrue.toIota()),
            """
                Consideration: True Reflection
                Hermes' Gambit
            """ to listOf(BooleanIota(true)),
            """
                Consideration: Numerical Reflection: 1
                Hermes' Gambit
            """ to listOf(NumberIota(1)),
            """
                Vector Reflection -X
                Vector Reflection +Y
                Vector Reflection +Z
                Numerical Reflection: 2
                Multiplicative Distillation
                Additive Distillation
                Additive Distillation
            """ to listOf(VectorIota(-1, 1, 2)),
            """
                Vector Reflection -X
                Vector Reflection +Y
                Vector Reflection +Z
                Numerical Reflection: 2
                Multiplicative Distillation
                Additive Distillation
                Additive Distillation
                Vector Disintegration
            """ to listOf(
                NumberIota(2),
                NumberIota(1),
                NumberIota(-1),
            ),
            "Vacant Reflection" to listOf(ListIota()),
            """
                Numerical Reflection: 0
                Single's Purification
            """ to listOf(ListIota(NumberIota(0))),
            """
                Numerical Reflection: 0
                Numerical Reflection: 1
                Numerical Reflection: 2
                Flock's Gambit
            """ to listOf(ListIota(NumberIota(0), NumberIota(1))),
            """
                Numerical Reflection: 0
                Numerical Reflection: 1
                Numerical Reflection: 2
                Flock's Gambit
                Flock's Disintegration
            """ to listOf(
                NumberIota(1),
                NumberIota(0),
            ),
            """
                Consideration: Numerical Reflection: 1
                Consideration: Numerical Reflection: 2
                Consideration: Additive Distillation
                Numerical Reflection: 3
                Flock's Gambit
                Hermes' Gambit
            """ to listOf(NumberIota(3)),
            """
                {
                }
            """ to listOf(ListIota()),
            """
                {
                    True Reflection
                }
            """ to listOf(ListIota(OpTrue.toIota())),
            """
                {
                    {
                    }
                }
            """ to listOf(ListIota(OpLeftParen.toIota(), OpRightParen.toIota())),
            """
                {
                    Consideration: Introspection
                }
            """ to listOf(ListIota(OpLeftParen.toIota())),
            """
                {
                    Consideration: Retrospection
                }
            """ to listOf(ListIota(OpRightParen.toIota())),
            """
                {
                    Consideration: {
                        Consideration: Consideration
                    Consideration: }
                }
            """ to listOf(ListIota(OpLeftParen.toIota(), OpEscape.toIota(), OpRightParen.toIota())),
            """
                {
                    Consideration: Consideration
                    True Reflection 
                }
                Hermes' Gambit
            """ to listOf(OpTrue.toIota()),
            """
                Consideration: Introspection
                True Reflection
                Consideration: Retrospection
                Numerical Reflection: 3
                Flock's Gambit
            """ to listOf(ListIota(OpLeftParen.toIota(), BooleanIota(true), OpRightParen.toIota())),
//            """
//                Consideration: Introspection
//                True Reflection
//                Consideration: Retrospection
//                Numerical Reflection: 3
//                Flock's Gambit
//                Hermes' Gambit
//            """ to listOf(ListIota(BooleanIota(true))),
        ).map { Arguments.of(it.first, it.second) }
    }
}
