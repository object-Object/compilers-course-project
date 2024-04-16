package ca.objectobject.hexlr.debug

import org.eclipse.lsp4j.debug.*
import org.eclipse.lsp4j.debug.launch.DSPLauncher
import org.eclipse.lsp4j.debug.services.IDebugProtocolClient
import org.eclipse.lsp4j.debug.services.IDebugProtocolServer
import org.eclipse.lsp4j.jsonrpc.Launcher
import java.net.ServerSocket
import java.util.concurrent.CancellationException
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Future
import kotlin.system.exitProcess

fun main() {
    val serverSocket = ServerSocket(4444)
    while (true) {
        HexlrDebugServer(serverSocket).acceptClient()
    }
}

class HexlrDebugServer(private val serverSocket: ServerSocket) : IDebugProtocolServer {
    private lateinit var launcher: Launcher<IDebugProtocolClient>
    private lateinit var listenerFuture: Future<Void>
    private lateinit var initArgs: InitializeRequestArguments
    private lateinit var launchArgs: LaunchArgs
    private lateinit var debugger: HexlrDebugger

    private val remoteProxy: IDebugProtocolClient get() = launcher.remoteProxy

    fun acceptClient() {
        println("Listening on port ${serverSocket.localPort}...")
        val clientSocket = serverSocket.accept()
        println("Client connected!")

        val input = clientSocket.getInputStream()
        val output = clientSocket.getOutputStream()

        launcher = DSPLauncher.createServerLauncher(this, input, output)
        try {
            listenerFuture = launcher.startListening()
            listenerFuture.get()
        } catch (_: CancellationException) {}
    }

    // lifecycle requests

    override fun initialize(args: InitializeRequestArguments): CompletableFuture<Capabilities> {
        printRequest("initialize", args)
        initArgs = args
        return Capabilities().apply {
            supportsConfigurationDoneRequest = true
        }.toFuture()
    }

    override fun launch(args: Map<String, Any>): CompletableFuture<Void> {
        printRequest("launch", args)

        launchArgs = LaunchArgs(args)
        val runtime = DebugRuntime(::writeStdout)
        debugger = HexlrDebugger(initArgs, launchArgs, runtime)

        remoteProxy.initialized()
        return futureOf()
    }

    override fun setBreakpoints(args: SetBreakpointsArguments): CompletableFuture<SetBreakpointsResponse> {
        printRequest("setBreakpoints", args)
        return SetBreakpointsResponse().apply {
            breakpoints = debugger.setBreakpoints(args.breakpoints).toTypedArray()
        }.toFuture()
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

    override fun configurationDone(args: ConfigurationDoneArguments?): CompletableFuture<Void> {
        if (launchArgs.stopOnEntry) {
            remoteProxy.stopped(StoppedEventArguments().apply {
                threadId = 0
                reason = "entry"
            })
        } else {
            handleDebuggerStep(debugger.`continue`(), "breakpoint")
        }
        return futureOf()
    }

    override fun next(args: NextArguments): CompletableFuture<Void> {
        printRequest("next", args)
        handleDebuggerStep(debugger.next(), "step")
        return futureOf()
    }

    override fun continue_(args: ContinueArguments): CompletableFuture<ContinueResponse> {
        printRequest("continue", args)
        handleDebuggerStep(debugger.`continue`(), "breakpoint")
        return futureOf()
    }

    private fun handleDebuggerStep(continueDebugging: Boolean, reason: String) {
        if (continueDebugging) {
            remoteProxy.stopped(StoppedEventArguments().apply {
                threadId = 0
                this.reason = reason
            })
        } else {
            remoteProxy.exited(ExitedEventArguments().apply { exitCode = 0 })
            remoteProxy.terminated(TerminatedEventArguments())
        }
    }

    override fun pause(args: PauseArguments): CompletableFuture<Void> {
        printRequest("pause", args)
        return futureOf()
    }

    override fun disconnect(args: DisconnectArguments): CompletableFuture<Void> {
        printRequest("disconnect", args)
        if (!args.restart) {
            exitProcess(0)
        }
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
            scopes = debugger.getScopes(args.frameId).toTypedArray()
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

    // helpers

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
