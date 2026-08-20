import java.io.IOException
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.ConcurrentHashMap

class MJpegServer(
    private val viewerListener: ViewerConnectionListener,
    private val port: Int = 4444
) {

    private val clients = ConcurrentHashMap<Socket, CameraServer.OnFrameAvailable>()

    fun start() {
        val serverSocket = ServerSocket(port)
        println("MJPEG Server started on port $port")

        val thread = Thread {
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
        val onFrameAvailable = object : CameraServer.OnFrameAvailable {
            override fun onAvailable(frame: ByteArray) {
                try {
                    val out = client.getOutputStream()
                    out.write("--frame\r\n".toByteArray())
                    out.write("Content-Type: image/jpeg\r\n".toByteArray())
                    out.write("Content-Length: ${frame.size}\r\n\r\n".toByteArray())
                    out.write(frame)
                    out.write("\r\n".toByteArray())
                    out.flush()
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
        val onFrameAvailable = clients.remove(client) ?: return
        viewerListener.onDisconnect(onFrameAvailable)
        try {
            client.close()
        } catch (_: Exception) {
        }
    }
}