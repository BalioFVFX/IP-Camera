package com.videoserver

interface ViewerConnectionListener {
    fun onConnect(onFrameAvailable: CameraServer.OnFrameAvailable)
    fun onDisconnect(onFrameAvailable: CameraServer.OnFrameAvailable)
}
