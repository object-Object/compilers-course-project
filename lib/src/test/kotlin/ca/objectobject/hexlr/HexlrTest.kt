/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package ca.objectobject.hexlr

import ca.objectobject.hexlr.eval.patterns.OpEscape
import ca.objectobject.hexlr.eval.patterns.OpLeftParen
import ca.objectobject.hexlr.eval.patterns.OpRightParen
import ca.objectobject.hexlr.eval.patterns.OpTrue
import ca.objectobject.hexlr.eval.iotas.*
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import kotlin.test.assertEquals

class HexlrTest {
    @ParameterizedTest
    @MethodSource("getData_programProducesExpectedStack")
    fun programProducesExpectedStack(program: String, want: List<Iota>) {
        val runtime = execute(program.trimIndent())
        assertEquals(want.asReversed(), runtime.stack.toList())
    }

    @ParameterizedTest
    @ValueSource(strings = [
        "This Pattern Does Not Exist",
        """
            True Reflection
            Hermes' Gambit
        """,
        """
            True Reflection
            Single's Purification
            Hermes' Gambit
        """,
        "Bookkeeper's Gambit: -",
        "Bookkeeper's Gambit: v",
        """
            True Reflection
            Bookkeeper's Gambit: --
        """,
        """
            True Reflection
            Bookkeeper's Gambit: -v
        """,
        """
            True Reflection
            Bookkeeper's Gambit: v-
        """,
        """
            True Reflection
            Bookkeeper's Gambit: vv
        """,
    ])
    fun invalidProgramThrowsException(program: String) {
        assertThrows<Throwable> {
            execute(program.trimIndent())
        }
    }

    companion object {
        @JvmStatic
        fun getData_programProducesExpectedStack() = listOf(
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
            """
                Consideration: Introspection
                True Reflection
                Consideration: Retrospection
                Numerical Reflection: 3
                Flock's Gambit
                Hermes' Gambit
            """ to listOf(ListIota(BooleanIota(true))),
            """
                Numerical Reflection: 0
                Bookkeeper's Gambit: -
            """ to listOf(NumberIota(0)),
            """
                Numerical Reflection: 0
                Bookkeeper's Gambit: v
            """ to listOf(),
            """
                Numerical Reflection: 0
                Numerical Reflection: 1
                Bookkeeper's Gambit: v
            """ to listOf(NumberIota(0)),
            """
                Numerical Reflection: 0
                Numerical Reflection: 1
                Bookkeeper's Gambit: v-
            """ to listOf(NumberIota(1)),
            """
                Vacant Reflection
                Vacant Reflection
                Thoth's Gambit
            """ to listOf(ListIota()),
            """
                Vacant Reflection
                True Reflection
                Single's Purification
                Thoth's Gambit
            """ to listOf(ListIota(BooleanIota(true))),
            """
                Numerical Reflection: 0
                Vacant Reflection
                Numerical Reflection: 1
                Numerical Reflection: 2
                Numerical Reflection: 2
                Flock's Gambit
                Thoth's Gambit
            """ to listOf(
                ListIota(listOf(0, 1, 0, 2).map(::NumberIota)),
                NumberIota(0),
            ),
            """
                Numerical Reflection: 0
                Numerical Reflection: 1
                Vacant Reflection
                Numerical Reflection: 2
                Numerical Reflection: 3
                Numerical Reflection: 4
                Numerical Reflection: 3
                Flock's Gambit
                Thoth's Gambit
            """ to listOf(
                ListIota(listOf(0, 1, 2, 0, 1, 3, 0, 1, 4).map(::NumberIota)),
                NumberIota(1),
                NumberIota(0),
            ),
            """
                Numerical Reflection: 1
                {
                    Division Distillation                
                }
                Numerical Reflection: 2
                Numerical Reflection: 4
                Numerical Reflection: 2
                Flock's Gambit
                Thoth's Gambit
            """ to listOf(
                ListIota(listOf(1.0/2, 1.0/4).map(::NumberIota)),
                NumberIota(1),
            ),
            """
                #define My Custom Pattern
                {
                    True Reflection
                }
                #enddefine
                My Custom Pattern
            """ to listOf(BooleanIota(true)),
            """
                #define My Custom Pattern
                {
                    True Reflection
                }
                #enddefine
                {
                    My Custom Pattern
                }
            """ to listOf(ListIota(OpTrue.toIota())),
            """
                Consideration: <0>
            """ to listOf(NumberIota(0)),
            """
                {
                    <0>
                }
            """ to listOf(ListIota(NumberIota(0))),
            """
                Consideration: <-1.5>
            """ to listOf(NumberIota(-1.5)),
            """
                Consideration: <
                    1
                >
            """ to listOf(NumberIota(1)),
            """
                Consideration: <(0, 0.5, 1e2)>
            """ to listOf(VectorIota(0, 0.5, 100)),
            """
                Consideration: <true>
            """ to listOf(BooleanIota(true)),
            """
                Consideration: <false>
            """ to listOf(BooleanIota(false)),
            """
                Consideration: <[]>
            """ to listOf(ListIota()),
            """
                Consideration: <[
                    0,
                    1,
                ]>
            """ to listOf(ListIota(NumberIota(0), NumberIota(1))),
            """
                Consideration: <[0, 1, (2, 3, 4), [true]]>
            """ to listOf(
                ListIota(
                    NumberIota(0),
                    NumberIota(1),
                    VectorIota(2, 3, 4),
                    ListIota(BooleanIota(true)),
                ),
            ),
        ).map { Arguments.of(it.first, it.second) }
    }
}
