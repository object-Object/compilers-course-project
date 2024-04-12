package ca.objectobject.hexlr.parser

import org.antlr.v4.runtime.BaseErrorListener
import org.antlr.v4.runtime.RecognitionException
import org.antlr.v4.runtime.Recognizer
import org.antlr.v4.runtime.misc.ParseCancellationException

class HexlrErrorListener : BaseErrorListener() {
    override fun syntaxError(
        recognizer: Recognizer<*, *>?,
        offendingSymbol: Any?,
        line: Int,
        char: Int,
        msg: String?,
        e: RecognitionException?,
    ) = throw ParseCancellationException("line $line, char $char: $msg", e)
}

fun <T: Recognizer<*, *>> T.setErrorListener(errorListener: BaseErrorListener) = this.apply {
    this.removeErrorListeners()
    this.addErrorListener(errorListener)
}
