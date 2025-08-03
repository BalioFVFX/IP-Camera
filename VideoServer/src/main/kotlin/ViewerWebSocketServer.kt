package com.videoserver

import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import java.lang.Exception
import java.net.InetSocketAddress
import java.util.concurrent.ConcurrentHashMap

class ViewerWebSocketServer(val connectionListener: Listener) {

    interface Listener {
        fun onConnection(listener: CameraServer.OnFrameAvailable)
        fun onDisconnection(listener: CameraServer.OnFrameAvailable)
    }

    private val listeners = ConcurrentHashMap<WebSocket, CameraServer.OnFrameAvailable>()

    private val server = object: WebSocketServer(InetSocketAddress(1234)) {
        override fun onOpen(conn: WebSocket?, handshake: ClientHandshake?) {
            println("ViewerWebSocketServer: New connection opened from ${conn?.remoteSocketAddress}")

            val listener = object: CameraServer.OnFrameAvailable {
                override fun onAvailable(frame: ByteArray) {
                    conn?.send(frame)
                }
            }

            connectionListener.onConnection(listener)
            listeners[conn!!] = listener
        }

        override fun onClose(conn: WebSocket?, code: Int, reason: String?, remote: Boolean) {
            println("ViewerWebSocketServer: onClose")
            val listener = listeners[conn!!]

            connectionListener.onDisconnection(listener!!)
            listeners.remove(conn)
        }

        override fun onMessage(conn: WebSocket?, message: String?) {
            println("ViewerWebSocketServer: onMessage")
        }

        override fun onError(conn: WebSocket?, ex: Exception?) {
            println("ViewerWebSocketServer: onError")
            ex?.printStackTrace()
        }

        override fun onStart() {
            println("ViewerWebSocketServer: onStart")
        }
    }

    fun start () {
        server.start()
    }

}
