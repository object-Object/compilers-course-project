import ca.objectobject.hexlr.debug.HexlrDebugServer
import java.net.ServerSocket

fun main() {
    val serverSocket = ServerSocket(4444)
    while (true) {
        HexlrDebugServer(serverSocket).acceptClient()
    }
}