package ca.objectobject.hexlr.debug

import ca.objectobject.hexlr.eval.Runtime

class DebugRuntime(val writeStdout: (String) -> Unit) : Runtime() {
    val newListContentsView get() = newListContents.asSequence()

    override fun println(value: Any) {
        writeStdout("$value\n")
    }
}