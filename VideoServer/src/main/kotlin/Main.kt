package com.videoserver

import com.videoserver.CameraServer
import com.videoserver.ViewerWebSocketServer
import com.videoserver.MJpegServer

fun main(args: Array<String>) {

    val cameraServer = CameraServer()

    val viewerServer = ViewerWebSocketServer(object : ViewerWebSocketServer.Listener {
        override fun onConnection(listener: CameraServer.OnFrameAvailable) {
            println("Connection")
            cameraServer.addListener(listener)
        }

        override fun onDisconnection(listener: CameraServer.OnFrameAvailable) {
            println("Disconnection")
            cameraServer.removeListener(listener)
        }
    })

    val mjpegServer = MJpegServer(object: ViewerConnectionListener {
        override fun onConnect(onFrameAvailable: CameraServer.OnFrameAvailable) {
            cameraServer.addListener(onFrameAvailable)
        }

        override fun onDisconnect(onFrameAvailable: CameraServer.OnFrameAvailable) {
            cameraServer.removeListener(onFrameAvailable)
        }
    })

    viewerServer.start()
    mjpegServer.start()
    cameraServer.start()
}
