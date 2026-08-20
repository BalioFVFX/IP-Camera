import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import java.lang.Exception
import java.net.InetSocketAddress
import java.util.concurrent.ConcurrentHashMap

class ViewerWebSocketServer(
    private val connectionListener: Listener,
    private val port: Int = 1234
) {

    interface Listener {
        fun onConnection(listener: CameraServer.OnFrameAvailable)
        fun onDisconnection(listener: CameraServer.OnFrameAvailable)
    }

    private val listeners = ConcurrentHashMap<WebSocket, CameraServer.OnFrameAvailable>()

    private val server = object : WebSocketServer(InetSocketAddress(port)) {

        override fun onOpen(conn: WebSocket?, handshake: ClientHandshake?) {
            println("ViewerWebSocketServer: onOpen")

            val listener = object : CameraServer.OnFrameAvailable {
                override fun onAvailable(frame: ByteArray) {
                    conn?.send(frame)
                }
            }

            connectionListener.onConnection(listener)
            if (conn != null) {
                listeners[conn] = listener
            }
        }

        override fun onClose(conn: WebSocket?, code: Int, reason: String?, remote: Boolean) {
            println("ViewerWebSocketServer: onClose")
            if (conn == null) return
            val listener = listeners.remove(conn)
            if (listener != null) {
                connectionListener.onDisconnection(listener)
            }
        }

        override fun onMessage(conn: WebSocket?, message: String?) {
            println("ViewerWebSocketServer: onMessage")
        }

        override fun onError(conn: WebSocket?, ex: Exception?) {
            println("ViewerWebSocketServer: onError")
            ex?.printStackTrace()
        }

        override fun onStart() {
            println("ViewerWebSocketServer: onStart (port $port)")
        }
    }

    fun start() {
        server.start()
    }
}