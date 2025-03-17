package com.ipcamera

import android.annotation.SuppressLint
import android.graphics.ImageFormat
import android.hardware.camera2.*
import android.media.ImageReader
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.Range
import android.view.SurfaceHolder
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import com.ipcamera.databinding.StreamActivityBinding
import java.io.DataOutputStream
import java.net.Socket
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.Executors


class StreamActivity : AppCompatActivity() {

    private lateinit var binding: StreamActivityBinding
    private val TAG = "StreamTag"
    private lateinit var imageReader: ImageReader

    @Volatile
    private var isStreaming = false

    @Volatile
    private var socket: Socket? = null

    private val executor = Executors.newSingleThreadExecutor()

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        EdgeToEdge.setDecorFitsSystemWindows(
            window = window,
            fitSystemWindows = false,
        )

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

        val cameraManager = getSystemService(CameraManager::class.java)

        val cameraId = cameraManager.cameraIdList[0]

        val surfaceView = binding.surfaceView

        val mainHandler = Handler(Looper.getMainLooper())

        imageReader = ImageReader.newInstance(1280, 720, ImageFormat.JPEG, 3)

        val queue = ConcurrentLinkedQueue<ByteArray>()

        surfaceView.holder.setFixedSize(1920, 1080)

        val ipAddress = SettingsPreferences(this.applicationContext).getIpAddress()!!

        binding.btnSave.setOnClickListener {
            if (isStreaming) {
                isStreaming = !isStreaming

                executor.execute {
                    socket?.close()
                    socket = null

                    mainHandler.post {
                        binding.tvStatus.text = "Status: Disconnected"
                        binding.btnSave.text = "Start streaming"
                    }
                }


            } else {
                binding.tvStatus.text = "Connecting..."

                executor.execute {
                    try {
                        val ip = ipAddress.split(":")[0]
                        val port = ipAddress.split(":")[1]

                        socket = Socket(ip, port.toInt())

                        mainHandler.post {
                            binding.tvStatus.text = "Streaming to: $ipAddress"
                            binding.btnSave.text = "Stop streaming"
                        }

                        isStreaming = !isStreaming

                        val socketWriter = DataOutputStream(socket!!.getOutputStream())
                        val stack = ArrayDeque<Int>(10)
                        var size = 0
                        var start = 0L

                        while (isStreaming) {
                            val frame = try {
                                queue.remove()
                            } catch (ex: java.util.NoSuchElementException) {
                                Log.d(TAG, "Empty queue")
                                continue
                            }

                            Log.d(TAG, "Buffer size: ${queue.size}")
                            start = System.currentTimeMillis()

                            socketWriter.writeInt(frame.size)
                            socketWriter.write(frame)

                            socketWriter.flush()
                            Log.d(TAG, "Sent to server: ${frame.size} bytes")
                            Log.d(TAG, "Elapsed to send: ${System.currentTimeMillis() - start}")
                        }

                    } catch (exception: java.lang.Exception) {
                        exception.printStackTrace()

                        socket?.close()
                        socket = null
                        isStreaming = false

                        mainHandler.post {
                            Toast.makeText(
                                this,
                                "Could not connect to: $ipAddress",
                                Toast.LENGTH_LONG
                            )
                                .show()
                            binding.tvStatus.text = "Status: Disconnected"
                            binding.btnSave.text = "Start streaming"
                        }
                    }
                }
            }
        }

        surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                Log.d(TAG, "surfaceCreated: ")

                imageReader.setOnImageAvailableListener(object :
                    ImageReader.OnImageAvailableListener {
                    override fun onImageAvailable(reader: ImageReader?) {

                        val image = reader?.acquireNextImage() ?: return

                        if (!isStreaming) {
                            image.close()
                            return
                        }

                        val buffer = image.planes[0].buffer

                        if (buffer.hasArray()) {
                            queue.add(buffer.array())
                        } else {
                            val array = ByteArray(buffer.remaining())
                            buffer.get(array)

                            queue.add(array)
                        }

                        image.close()
                    }
                }, null)

                cameraManager.openCamera(cameraId, object : CameraDevice.StateCallback() {
                    override fun onOpened(camera: CameraDevice) {
                        Log.d(TAG, "onOpened")

                        val captureRequest =
                            camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)

                        captureRequest.set(CaptureRequest.JPEG_QUALITY, 20)
                        val range = Range(24, 24)

                        captureRequest.set(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, range)

                        val callback = object : CameraCaptureSession.CaptureCallback() {

                            override fun onCaptureProgressed(
                                session: CameraCaptureSession,
                                request: CaptureRequest,
                                partialResult: CaptureResult,
                            ) {
                                super.onCaptureProgressed(session, request, partialResult)
                            }
                        }

                        val captureSession = object : CameraCaptureSession.StateCallback() {
                            override fun onConfigured(session: CameraCaptureSession) {
                                captureRequest.addTarget(imageReader.surface)
                                captureRequest.addTarget(surfaceView.holder.surface)
                                session.setRepeatingRequest(
                                    captureRequest.build(),
                                    callback,
                                    mainHandler
                                )
                            }

                            override fun onConfigureFailed(session: CameraCaptureSession) {

                            }
                        }


                        camera.createCaptureSession(
                            listOf(
                                surfaceView.holder.surface,
                                imageReader.surface
                            ), captureSession, mainHandler
                        )

                    }

                    override fun onDisconnected(camera: CameraDevice) {

                    }

                    override fun onError(camera: CameraDevice, error: Int) {

                    }

                }, mainHandler)
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {

            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {

            }

        })
    }
}