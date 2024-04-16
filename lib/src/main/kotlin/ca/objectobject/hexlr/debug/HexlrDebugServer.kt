package ca.objectobject.hexlr.debug

import org.eclipse.lsp4j.debug.*
import org.eclipse.lsp4j.debug.launch.DSPLauncher
import org.eclipse.lsp4j.debug.services.IDebugProtocolClient
import org.eclipse.lsp4j.debug.services.IDebugProtocolServer
import org.eclipse.lsp4j.jsonrpc.Launcher
import java.io.InputStream
import java.io.OutputStream
import java.net.ServerSocket
import java.util.concurrent.CancellationException
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Future
import kotlin.system.exitProcess

fun main() {
    HexlrDebugServer.launch()
}

class HexlrDebugServer(input: InputStream, output: OutputStream) : IDebugProtocolServer {
    private var launcher: Launcher<IDebugProtocolClient> = DSPLauncher.createServerLauncher(this, input, output)

    private lateinit var listenerFuture: Future<Void>
    private lateinit var initArgs: InitializeRequestArguments
    private lateinit var launchArgs: LaunchArgs
    private lateinit var debugger: HexlrDebugger

    private val remoteProxy: IDebugProtocolClient get() = launcher.remoteProxy

    // lifecycle requests

    override fun initialize(args: InitializeRequestArguments): CompletableFuture<Capabilities> {
        printRequest("initialize", args)
        initArgs = args
        return Capabilities().toFuture()
    }

    override fun launch(args: Map<String, Any>): CompletableFuture<Void> {
        printRequest("launch", args)

        launchArgs = LaunchArgs(args)
        debugger = HexlrDebugger(initArgs, launchArgs, ::writeStdout)

        remoteProxy.initialized()

        remoteProxy.stopped(StoppedEventArguments().apply {
            threadId = 0
            reason = "entry"
        })

        return futureOf()
    }

    override fun next(args: NextArguments): CompletableFuture<Void> {
        printRequest("next", args)

        if (debugger.next()) {
            remoteProxy.stopped(StoppedEventArguments().apply {
                threadId = 0
                reason = "step"
            })
        } else {
            remoteProxy.exited(ExitedEventArguments().apply { exitCode = 0 })
            remoteProxy.terminated(TerminatedEventArguments())
        }

        return futureOf()
    }

    override fun continue_(args: ContinueArguments): CompletableFuture<ContinueResponse> {
        printRequest("continue", args)
        debugger.`continue`()
        remoteProxy.exited(ExitedEventArguments().apply { exitCode = 0 })
        remoteProxy.terminated(TerminatedEventArguments())
        return futureOf()
    }

    override fun pause(args: PauseArguments): CompletableFuture<Void> {
        printRequest("pause", args)
        return futureOf()
    }

    override fun disconnect(args: DisconnectArguments): CompletableFuture<Void> {
        printRequest("disconnect", args)
        listenerFuture.cancel(true)
        return futureOf()
    }

    // runtime data

    override fun threads(): CompletableFuture<ThreadsResponse> {
        // always return the same dummy thread - we don't support multithreading
        return ThreadsResponse().apply {
            threads = arrayOf(Thread().apply {
                id = 0
                name = "Main Thread"
            })
        }.toFuture()
    }

    override fun scopes(args: ScopesArguments): CompletableFuture<ScopesResponse> {
        printRequest("scopes", args)
        return ScopesResponse().apply {
            scopes = debugger.getScopes(args.frameId)
        }.toFuture()
    }

    override fun variables(args: VariablesArguments): CompletableFuture<VariablesResponse> {
        printRequest("variables", args)
        return VariablesResponse().apply {
            variables = debugger.getVariables(args.variablesReference).paginate(args.start, args.count)
        }.toFuture()
    }

    override fun stackTrace(args: StackTraceArguments): CompletableFuture<StackTraceResponse> {
        printRequest("stackTrace", args)
        return StackTraceResponse().apply {
            stackFrames = debugger.getStackFrames().paginate(args.startFrame, args.levels)
        }.toFuture()
    }

    // breakpoint requests

    override fun setBreakpoints(args: SetBreakpointsArguments): CompletableFuture<SetBreakpointsResponse> {
        printRequest("setBreakpoints", args)
        // TODO: implement
        return futureOf()
    }

    override fun setExceptionBreakpoints(args: SetExceptionBreakpointsArguments): CompletableFuture<SetExceptionBreakpointsResponse> {
        printRequest("setExceptionBreakpoints", args)

        // tell the client we didn't enable any of their breakpoints
        val count = args.filters.size + (args.filterOptions?.size ?: 0) + (args.exceptionOptions?.size ?: 0)
        val breakpoints = Array(count) { Breakpoint().apply { isVerified = false } }

        return SetExceptionBreakpointsResponse().apply {
            this.breakpoints = breakpoints
        }.toFuture()
    }

    // helpers

    fun startListening(): Future<Void> {
        listenerFuture = launcher.startListening()
        return listenerFuture
    }

    private fun writeStdout(value: String) {
        remoteProxy.output(OutputEventArguments().apply {
            category = "stdout"
            output = value
        })
    }

    private fun printRequest(name: String, args: Any? = null) {
        val header = "----- $name -----"
        println("\n$header")
        if (args != null) {
            println(args)
        }
        println("-".repeat(header.length))
    }

    // static methods

    companion object {
        fun launch(port: Int = 4444) {
            val serverSocket = ServerSocket(4444)

            println("Listening on port $port...")
            val clientSocket = serverSocket.accept()
            println("Client connected!")

            val input = clientSocket.getInputStream()
            val output = clientSocket.getOutputStream()

            val server = HexlrDebugServer(input, output)
            try {
                server.startListening().get()
            } catch (e: CancellationException) {
                exitProcess(0)
            }
        }
    }
}

fun <T> T.toFuture(): CompletableFuture<T> = CompletableFuture.completedFuture(this)

fun <T> futureOf(value: T): CompletableFuture<T> = CompletableFuture.completedFuture(value)

fun <T> futureOf(): CompletableFuture<T> = CompletableFuture.completedFuture(null)

inline fun <reified T> Sequence<T>.paginate(start: Int?, count: Int?): Array<T> {
    var result = this
    if (start != null && start > 0) {
        result = result.drop(start)
    }
    if (count != null && count > 0) {
        result = result.take(count)
    }
    return result.toList().toTypedArray()
}
