package com.ipcamera

import android.annotation.SuppressLint
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import android.media.Image
import android.media.ImageReader
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Log
import android.util.Range
import android.view.SurfaceHolder
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import com.ipcamera.databinding.StreamActivityBinding
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.net.InetSocketAddress
import java.net.Socket
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

class StreamActivity : AppCompatActivity() {

    private lateinit var binding: StreamActivityBinding

    private val TAG = "StreamTag"
    //private val STREAM_WIDTH = 1280
    //private val STREAM_HEIGHT = 720

    private val cameraThread = HandlerThread("camera").also { it.start() }
    private val cameraHandler = Handler(cameraThread.looper)
    private val mainHandler = Handler(Looper.getMainLooper())

    private lateinit var imageReader: ImageReader
    private var cameraDevice: CameraDevice? = null
    private var captureSession: CameraCaptureSession? = null

    private val isStreaming = AtomicBoolean(false)
    @Volatile private var socket: Socket? = null

    private val frameQueue = ConcurrentLinkedQueue<ByteArray>()
    private val executor = Executors.newSingleThreadExecutor()

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        EdgeToEdge.setDecorFitsSystemWindows(window = window, fitSystemWindows = false)
        EdgeToEdge.enableImmersiveMode(window = window)

        binding = StreamActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        EdgeToEdge.setInsetsHandler(
            root = binding.root,
            handler = StreamActivityInsetsHandler { systemBarInsets ->
                binding.btnSave.updateLayoutParams<ConstraintLayout.LayoutParams> {
                    bottomMargin += systemBarInsets.bottom
                }
                binding.tvStatus.updateLayoutParams<ConstraintLayout.LayoutParams> {
                    topMargin += systemBarInsets.top
                }
            }
        )

        val streamPrefs = SettingsPreferences(applicationContext)
        val streamWidth = streamPrefs.getStreamWidth()
        val streamHeight = streamPrefs.getStreamHeight()
        Log.d(TAG, "Stream resolution: ${streamWidth}x${streamHeight}")

        imageReader = ImageReader.newInstance(
            streamWidth,
            streamHeight,
            ImageFormat.YUV_420_888,
            4
        )

        imageReader.setOnImageAvailableListener({ reader ->
            val image = reader.acquireLatestImage() ?: return@setOnImageAvailableListener
            try {
                if (!isStreaming.get()) return@setOnImageAvailableListener
                val jpeg = yuv420ToJpeg(image, 40) ?: return@setOnImageAvailableListener
                while (frameQueue.size > 2) frameQueue.poll()
                frameQueue.offer(jpeg)
            } catch (e: Exception) {
                Log.e(TAG, "onImageAvailable error", e)
            } finally {
                image.close()
            }
        }, cameraHandler)

        val surfaceView = binding.surfaceView
        surfaceView.holder.setFixedSize(streamWidth, streamHeight)

        val ipAddress = SettingsPreferences(applicationContext).getIpAddress()
            ?: "192.168.0.101:4321"

        binding.btnSave.setOnClickListener {
            if (isStreaming.get()) {
                stopStreaming()
            } else {
                startStreaming(ipAddress)
            }
        }

        surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                Log.d(TAG, "surfaceCreated")
                openCamera(holder)
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
                Log.d(TAG, "surfaceChanged ${width}x$height")
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                Log.d(TAG, "surfaceDestroyed")
                closeCamera()
            }
        })
    }

    private fun startStreaming(ipAddress: String) {
        binding.tvStatus.text = "Connecting..."
        binding.btnSave.isEnabled = false

        executor.execute {
            try {
                val parts = ipAddress.split(":")
                val ip = parts[0]
                val port = parts[1].toInt()

                val s = Socket()
                s.tcpNoDelay = true
                s.connect(InetSocketAddress(ip, port), 8000)
                socket = s

                isStreaming.set(true)
                frameQueue.clear()

                mainHandler.post {
                    binding.tvStatus.text = "Streaming to: $ipAddress"
                    binding.btnSave.text = "Stop streaming"
                    binding.btnSave.isEnabled = true
                }

                val writer = DataOutputStream(s.getOutputStream())
                while (isStreaming.get()) {
                    val frame = frameQueue.poll()
                    if (frame == null) {
                        try {
                            Thread.sleep(5)
                        } catch (_: InterruptedException) {
                        }
                        continue
                    }
                    writer.writeInt(frame.size)
                    writer.write(frame)
                    writer.flush()
                    Log.d(TAG, "Sent ${frame.size} bytes, q=${frameQueue.size}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Stream error", e)
                isStreaming.set(false)
                try {
                    socket?.close()
                } catch (_: Exception) {
                }
                socket = null
                mainHandler.post {
                    Toast.makeText(
                        this,
                        "Could not connect to: $ipAddress",
                        Toast.LENGTH_LONG
                    ).show()
                    binding.tvStatus.text = "Status: Disconnected"
                    binding.btnSave.text = "Start streaming"
                    binding.btnSave.isEnabled = true
                }
            }
        }
    }

    private fun stopStreaming() {
        isStreaming.set(false)
        executor.execute {
            try {
                socket?.close()
            } catch (_: Exception) {
            }
            socket = null
            frameQueue.clear()
            mainHandler.post {
                binding.tvStatus.text = "Status: Disconnected"
                binding.btnSave.text = "Start streaming"
                binding.btnSave.isEnabled = true
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun openCamera(holder: SurfaceHolder) {
        val cameraManager = getSystemService(CameraManager::class.java)
        val cameraId = cameraManager.cameraIdList.firstOrNull() ?: run {
            Toast.makeText(this, "No camera", Toast.LENGTH_LONG).show()
            return
        }

        try {
            cameraManager.openCamera(cameraId, object : CameraDevice.StateCallback() {
                override fun onOpened(camera: CameraDevice) {
                    Log.d(TAG, "onOpened")
                    cameraDevice = camera
                    createSession(camera, holder)
                }

                override fun onDisconnected(camera: CameraDevice) {
                    Log.w(TAG, "onDisconnected")
                    camera.close()
                    cameraDevice = null
                }

                override fun onError(camera: CameraDevice, error: Int) {
                    Log.e(TAG, "Camera error $error")
                    camera.close()
                    cameraDevice = null
                }
            }, cameraHandler)
        } catch (e: Exception) {
            Log.e(TAG, "openCamera failed", e)
        }
    }

    private fun createSession(camera: CameraDevice, holder: SurfaceHolder) {
        try {
            val previewSurface = holder.surface
            val readerSurface = imageReader.surface

            camera.createCaptureSession(
                listOf(previewSurface, readerSurface),
                object : CameraCaptureSession.StateCallback() {
                    override fun onConfigured(session: CameraCaptureSession) {
                        Log.d(TAG, "onConfigured OK")
                        captureSession = session
                        try {
                            val req = camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                            req.addTarget(previewSurface)
                            req.addTarget(readerSurface)
                            try {
                                req.set(
                                    CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE,
                                    Range(15, 30)
                                )
                            } catch (_: Exception) {
                            }
                            session.setRepeatingRequest(req.build(), null, cameraHandler)
                        } catch (e: Exception) {
                            Log.e(TAG, "setRepeatingRequest failed", e)
                        }
                    }

                    override fun onConfigureFailed(session: CameraCaptureSession) {
                        Log.e(TAG, "onConfigureFailed")
                        mainHandler.post {
                            Toast.makeText(
                                this@StreamActivity,
                                "Camera session failed",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                },
                cameraHandler
            )
        } catch (e: Exception) {
            Log.e(TAG, "createSession failed", e)
        }
    }

    private fun closeCamera() {
        try {
            captureSession?.close()
        } catch (_: Exception) {
        }
        captureSession = null
        try {
            cameraDevice?.close()
        } catch (_: Exception) {
        }
        cameraDevice = null
    }

    /**
     * Conversione YUV_420_888 -> NV21 -> JPEG.
     * Gestisce rowStride/pixelStride dei plane.
     */
    private fun yuv420ToJpeg(image: Image, quality: Int): ByteArray? {
        return try {
            val width = image.width
            val height = image.height
            val yPlane = image.planes[0]
            val uPlane = image.planes[1]
            val vPlane = image.planes[2]

            val ySize = width * height
            val uvSize = width * height / 4
            val nv21 = ByteArray(ySize + uvSize * 2)

            // Y
            val yBuffer = yPlane.buffer
            val yRowStride = yPlane.rowStride
            var pos = 0
            if (yRowStride == width) {
                yBuffer.get(nv21, 0, ySize)
                pos = ySize
            } else {
                var rowStart = 0
                for (row in 0 until height) {
                    yBuffer.position(rowStart)
                    yBuffer.get(nv21, pos, width)
                    pos += width
                    rowStart += yRowStride
                }
            }

            // VU interleaved (NV21)
            val vBuffer = vPlane.buffer
            val uBuffer = uPlane.buffer
            val vRowStride = vPlane.rowStride
            val uRowStride = uPlane.rowStride
            val vPixelStride = vPlane.pixelStride
            val uPixelStride = uPlane.pixelStride

            var uvPos = ySize
            for (row in 0 until height / 2) {
                var vRow = row * vRowStride
                var uRow = row * uRowStride
                for (col in 0 until width / 2) {
                    nv21[uvPos++] = vBuffer.get(vRow + col * vPixelStride)
                    nv21[uvPos++] = uBuffer.get(uRow + col * uPixelStride)
                }
            }

            val yuvImage = YuvImage(nv21, ImageFormat.NV21, width, height, null)
            val out = ByteArrayOutputStream()
            yuvImage.compressToJpeg(Rect(0, 0, width, height), quality, out)
            out.toByteArray()
        } catch (e: Exception) {
            Log.e(TAG, "yuv420ToJpeg failed", e)
            null
        }
    }

    override fun onDestroy() {
        stopStreaming()
        closeCamera()
        try {
            imageReader.close()
        } catch (_: Exception) {
        }
        cameraThread.quitSafely()
        executor.shutdownNow()
        super.onDestroy()
    }
}