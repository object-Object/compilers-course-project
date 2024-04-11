/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package ca.objectobject.hexlr

import kotlin.test.Test
import kotlin.test.assertEquals

class HexlrTest {
    @Test fun emptyProgramReturnsEmptyList() {
        val program = ""
        val actions = parse(program)
        assertEquals(actions, listOf())
    }
}