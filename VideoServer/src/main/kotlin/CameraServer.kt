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

    private val deviceOfflineImage: ByteArray = File("device_offline.jpg").readBytes()

    private val server = ServerSocket(4321).apply {
        receiveBufferSize = 900000000
    }
    
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
            println("Camera server: Starting camera server")

            println("Camera server: Waiting for clients...")
            val client = server.accept()
            deviceConnected = true
            val connectionTime = System.currentTimeMillis()

            println("CameraServer: Client connected")
            val timestamp = System.currentTimeMillis()
            println("Connected at: $timestamp")

            val reader = DataInputStream(client.getInputStream())

            var current: Int
            var start = 0L
            var length = 0
            val builder = StringBuilder(10)
            var dataSize = 0

            while (true) {
                try {
                    start = System.currentTimeMillis()
                    length = reader.read()

                    if (length == -1) {
                        throw IOException("Device offline")
                    }

                    for (i in 0 until length) {
                        val read = reader.read()

                        if (read == -1) {
                            throw IOException("Device offline")
                        }

                        builder.append(read)
                    }

                    dataSize = builder.toString().toInt()
                    builder.clear()

                    val byteArray = ByteArray(dataSize)

                    var i = 0

                    while (i < dataSize) {
                        byteArray[i++] = reader.read().toByte()
                    }

                    queue.add(byteArray)

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
                        println("Device offline, sending \"Device offline image\"")
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