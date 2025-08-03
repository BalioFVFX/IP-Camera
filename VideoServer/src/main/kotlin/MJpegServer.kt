package com.videoserver

import java.io.IOException
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.ConcurrentHashMap

class MJpegServer(private val viewerListener: ViewerConnectionListener) {

    private val clients = ConcurrentHashMap<Socket, CameraServer.OnFrameAvailable>()

    fun start() {
        val serverSocket = ServerSocket(4444)
        println("MJPEG Server started")

        val thread = Thread() {
            while (true) {
                val client = serverSocket.accept()
                println("Client connected to MJPEG server")

                try {
                    val headers = "HTTP/1.0 200 OK\r\n" +
                            "Connection: close\r\n" +
                            "Max-Age: 0\r\n" +
                            "Expires: 0\r\n" +
                            "Cache-Control: no-cache, private\r\n" +
                            "Pragma: no-cache\r\n" +
                            "Content-Type: multipart/x-mixed-replace; boundary=frame\r\n\r\n"
                    client.getOutputStream().write(headers.toByteArray())
                    client.getOutputStream().flush()

                    onConnect(client)
                } catch (ex: IOException) {
                    println("Failed to send headers to MJPEG client")
                    ex.printStackTrace()
                }
            }
        }

        thread.start()
    }

    private fun onConnect(client: Socket) {
        val onFrameAvailable = object: CameraServer.OnFrameAvailable {
            override fun onAvailable(frame: ByteArray) {
                try {

                    val boundary = "--frame\r\n"
                    client.getOutputStream().write(boundary.toByteArray())
                    client.getOutputStream().write("Content-Type: image/jpeg\r\n".toByteArray())
                    client.getOutputStream().write("Content-Length: ${frame.size}\r\n\r\n".toByteArray())
                    client.getOutputStream().write(frame)
                    client.getOutputStream().write("\r\n".toByteArray())
                    client.getOutputStream().flush()

                    println("Frame sent to MJPEG client")

                } catch (exception: Exception) {
                    println("Failed to send video frame to MJPEG client")
                    onDisconnect(client)
                }
            }
        }

        clients[client] = onFrameAvailable
        viewerListener.onConnect(onFrameAvailable)
    }

    private fun onDisconnect(client: Socket) {
        val onFrameAvailable = clients.remove(client)!!
        viewerListener.onDisconnect(onFrameAvailable)
    }
}
