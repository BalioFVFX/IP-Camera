fun main(args: Array<String>) {
    val cameraPort = args.getOrNull(0)?.toIntOrNull()
        ?: System.getenv("CAMERA_PORT")?.toIntOrNull()
        ?: 4321
    val mjpegPort = args.getOrNull(1)?.toIntOrNull()
        ?: System.getenv("MJPEG_PORT")?.toIntOrNull()
        ?: 4444
    val wsPort = args.getOrNull(2)?.toIntOrNull()
        ?: System.getenv("WS_PORT")?.toIntOrNull()
        ?: 1234

    println("VideoServer ports: camera=$cameraPort  mjpeg=$mjpegPort  websocket=$wsPort")

    val cameraServer = CameraServer(cameraPort)

    val viewerServer = ViewerWebSocketServer(
        connectionListener = object : ViewerWebSocketServer.Listener {
            override fun onConnection(listener: CameraServer.OnFrameAvailable) {
                println("WebSocket viewer connected")
                cameraServer.addListener(listener)
            }

            override fun onDisconnection(listener: CameraServer.OnFrameAvailable) {
                println("WebSocket viewer disconnected")
                cameraServer.removeListener(listener)
            }
        },
        port = wsPort
    )

    val mjpegServer = MJpegServer(
        viewerListener = object : ViewerConnectionListener {
            override fun onConnect(onFrameAvailable: CameraServer.OnFrameAvailable) {
                cameraServer.addListener(onFrameAvailable)
            }

            override fun onDisconnect(onFrameAvailable: CameraServer.OnFrameAvailable) {
                cameraServer.removeListener(onFrameAvailable)
            }
        },
        port = mjpegPort
    )

    viewerServer.start()
    mjpegServer.start()
    cameraServer.start()
}
