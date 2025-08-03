package com.videoserver

import java.io.DataInputStream
import java.io.EOFException
import java.io.File
import java.io.IOException
import java.net.ServerSocket
import java.util.NoSuchElementException
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.CopyOnWriteArrayList

class CameraServer {

    interface OnFrameAvailable {
        fun onAvailable(frame: ByteArray)
    }

    private val deviceOfflineImage = object {}.javaClass.getResource("/device_offline.jpg")?.readBytes()
        ?: error("device_offline.jpg not found in resources!")

    private val server = ServerSocket(4321)
    
    private val queue = ConcurrentLinkedQueue<ByteArray>()

    private val listeners = CopyOnWriteArrayList<OnFrameAvailable>()

    @Volatile
    private var isDelegatingFrames = false

    @Volatile
    private var cancelFrameDelegation = false

    @Volatile
    private var deviceConnected = false

    fun start() {

        cancelFrameDelegation = true

        while (isDelegatingFrames) {
            // Wait till all frames are delegated
        }

        cancelFrameDelegation = false
        isDelegatingFrames = false

        createFrameDelegatorThread().start()

        val thread = Thread() {
            println("CameraServer: Starting the cameraserver")

            println("CameraServer: Waiting for clients...")
            val client = server.accept()
            deviceConnected = true
            val connectionTime = System.currentTimeMillis()

            println("CameraServer: Client connected")
            val timestamp = System.currentTimeMillis()
            println("Connected at: $timestamp")

            val reader = DataInputStream(client.getInputStream())

            var start = 0L
            var length = 0

            while (true) {
                try {
                    start = System.currentTimeMillis()
                    length = reader.readInt()

                    queue.add(reader.readNBytes(length))

                    println("Elapsed: ${System.currentTimeMillis() - start}")

                } catch (exception: IOException) {
                    start()
                    deviceConnected = false
                    exception.printStackTrace()
                    return@Thread
                }

            }
        }
        thread.start()
    }

    fun addListener(onFrameAvailable: OnFrameAvailable) {
        this.listeners.add(onFrameAvailable)
    }

    fun removeListener(onFrameAvailable: OnFrameAvailable) {
        this.listeners.remove(onFrameAvailable)
    }

    private fun createFrameDelegatorThread() : Thread {
        val runnable = Runnable {

            isDelegatingFrames = true

            while (!cancelFrameDelegation) {
                val iterator = listeners.iterator()

                val frame = try {
                    queue.remove()
                } catch (ex: NoSuchElementException) {
                    if (!deviceConnected) {
                        iterator.forEach { listener ->
                            listener.onAvailable(deviceOfflineImage)
                        }
                        println("Device is offline - sending fallback image")
                        Thread.sleep(1000 / 24)
                    }
                    continue
                }

                iterator.forEach { listener ->
                    listener.onAvailable(frame)
                }
                println("Queue size: ${queue.size}")
            }

            isDelegatingFrames = false
        }

        return Thread(runnable, "Frame Delegator Thread")
    }

    private fun saveToFile(frame: Frame, timestamp: Long) {
        val file = File("$timestamp.txt")

        file.createNewFile()

        var existingLines = file.readText()

        if (existingLines.isNotEmpty()) {
            existingLines += "|\n"
        }

        frame.data.forEach {
            existingLines += it.toString() + "\n"
        }

        file.writeText(existingLines)
    }

}
