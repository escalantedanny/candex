package com.escalantedanny.candesk

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.escalantedanny.candesk.auth.activities.LoginActivity
import com.escalantedanny.candesk.auth.models.User
import com.escalantedanny.candesk.databinding.ActivityMainBinding
import com.escalantedanny.candesk.dogs.activities.DogListActivity
import com.escalantedanny.candesk.responses.ApiServiceInterceptor
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GetTokenResult
import android.Manifest;
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.*
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.lifecycle.LifecycleOwner
import com.escalantedanny.candesk.dogs.activities.DogDetailActivity
import com.escalantedanny.candesk.dogs.activities.DogDetailActivity.Companion.DOG_KEY
import com.escalantedanny.candesk.dogs.activities.DogDetailActivity.Companion.IS_RECOGNITION_KEY
import com.escalantedanny.candesk.dogs.activities.WholeImageActivity
import com.escalantedanny.candesk.dogs.activities.WholeImageActivity.Companion.PHOTO_PATH_KEY
import com.escalantedanny.candesk.dogs.api.ApiResponseStatus
import com.escalantedanny.candesk.dogs.models.Dog
import com.escalantedanny.candesk.dogs.viewmodels.MainViewModel
import com.escalantedanny.candesk.machinelearning.Classifier
import com.escalantedanny.candesk.machinelearning.DogRecognition
import com.escalantedanny.candesk.utils.Constants.EXTENSION_FILE
import com.escalantedanny.candesk.utils.Constants.LABEL_PATH
import com.escalantedanny.candesk.utils.Constants.MODEL_PATH
import com.google.common.util.concurrent.ListenableFuture
import org.tensorflow.lite.support.common.FileUtil
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var imageCapture: ImageCapture
    private lateinit var cameraExecutor: ExecutorService
    private var isCameraReady = false
    private lateinit var classifier: Classifier
    private val viewModelDog: MainViewModel by viewModels()

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                isCameraReady = true
                setupCamera()
            }
        }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        requestCameraPerimission()

        val user = User.getLoggedInUser(this)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setIcon(R.mipmap.ic_launcher)

        Log.wtf("USER_SHAREDPREFERENCE", user.toString())

        if (BuildConfig.ACTIVE_AUTH_FIREBASE) {
            if (user == null) {
                openLoginActivity()
                return
            } else {
                auth = FirebaseAuth.getInstance()
                auth.getAccessToken(true)
                    .addOnCompleteListener(OnCompleteListener<GetTokenResult?> { task ->
                        if (task.isSuccessful) {
                            val idToken: String? = task.result.token
                            ApiServiceInterceptor.setSessionToken(sessionToken = idToken!!)
                        } else {
                            task.exception
                        }
                    })
            }
        } else {
            if (user == null) {
                openLoginActivity()
                return
            } else {
                ApiServiceInterceptor.setSessionToken(sessionToken = user.authenticationToken)
            }
        }

        //binding.tlMain.text = getString(R.string.saludo_main, "Hi ${user?.email}")
        binding.tlMain.text = getString(R.string.camara_quieta)

        binding.ListDogs.setOnClickListener {
            openListAnimalsActivity()
        }

        binding.logout.setOnClickListener {

            if (BuildConfig.ACTIVE_AUTH_FIREBASE) {
                auth.signOut()
            } else {
                User.logout(this)
            }
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        binding.takePhoto.setOnClickListener {
            if (isCameraReady)
                takePhoto()
            else
                Toast.makeText(
                    this,
                    "Debes aceptar los permision de uso de Camara",
                    Toast.LENGTH_LONG
                ).show()
        }

        viewModelDog.status.observe(this) {
            when (it) {
                is ApiResponseStatus.Error -> {
                    binding.dogUpProgress.visibility = View.GONE
                }
                is ApiResponseStatus.Loading -> binding.dogUpProgress.visibility = View.VISIBLE
                is ApiResponseStatus.Success -> {
                    binding.dogUpProgress.visibility = View.GONE
                }
            }
        }

        viewModelDog.dog.observe(this) { dog ->
            if (dog != null) {
                openDogDetailActivity(dog)
            }
        }

    }

    fun setupCamera() {
        binding.cameraPreview.post {
            imageCapture = ImageCapture.Builder()
                .setTargetRotation(binding.cameraPreview.display.rotation)
                .build()
            isCameraReady = true
            cameraExecutor = Executors.newSingleThreadExecutor()
            startCameraPreview()
        }
    }

    override fun onStart() {
        super.onStart()
        classifier = Classifier(
            FileUtil.loadMappedFile(this@MainActivity, MODEL_PATH),
            FileUtil.loadLabels(this@MainActivity, LABEL_PATH)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::cameraExecutor.isInitialized)
            cameraExecutor.shutdown()
    }

    private fun getOutputPhotoFile(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name) + EXTENSION_FILE).apply { mkdir() }
        }
        return if (mediaDir != null && mediaDir.exists()) {
            mediaDir
        } else {
            filesDir
        }
    }

    fun takePhoto() {
        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(getOutputPhotoFile()).build()
        imageCapture.takePicture(outputFileOptions, cameraExecutor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(error: ImageCaptureException) {
                    Toast.makeText(
                        this@MainActivity,
                        "Error al iniciar la aplicación",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    //val pathUrl = outputFileResults.savedUri
                    //val bitMap = BitmapFactory.decodeFile(pathUrl?.path)
                    //val dogFoundIt = classifier.recognizeImage(bitMap).first()

                    //mandar imagen al activity
                    //openWholeImageActivity(pathUrl.toString())
                }
            })
    }

    fun enableTakePhotoButton(dogFoundIt: DogRecognition) {
        Log.wtf("CONFIDENCE", "confidence ->"+dogFoundIt.confidence)
        if (dogFoundIt.confidence > 70.0){
            binding.takePhoto.alpha = 1f
            binding.takePhoto.setOnClickListener {
                viewModelDog.getDogByMlId(dogFoundIt.id)
            }
        } else {
            binding.takePhoto.alpha = 0.2f
            binding.takePhoto.setOnClickListener(null)
        }
    }


    private fun startCameraPreview() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()
            val preview: Preview = Preview.Builder()
                .build()

            val cameraSelector: CameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()

            preview.setSurfaceProvider(binding.cameraPreview.getSurfaceProvider())

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy ->
                //imageProxy.imageInfo.rotationDegrees
                val bitmap = convertImageProxyToBitMap(imageProxy)
                if ( bitmap != null ) {
                    val dogRecognition = classifier.recognizeImage(bitmap).first()
                    enableTakePhotoButton(dogRecognition)
                }


                imageProxy.close()
            }

            cameraProvider.bindToLifecycle(
                this as LifecycleOwner,
                cameraSelector,
                imageAnalysis,
                preview
            )

            cameraProvider.bindToLifecycle(
                this,
                cameraSelector,
                preview,
                imageCapture,
                imageAnalysis
            )

        }, ContextCompat.getMainExecutor(this))
    }

    @SuppressLint("UnsafeOptInUsageError")
    fun convertImageProxyToBitMap(imageProxy: ImageProxy): Bitmap? {
        val image = imageProxy.image ?: return null

        val yBuffer = image.planes[0].buffer
        val uBuffer = image.planes[1].buffer
        val vBuffer = image.planes[2].buffer

        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()

        val nv21 = ByteArray(ySize + uSize + vSize)

        yBuffer.get(nv21, 0, ySize)
        uBuffer.get(nv21, 0, uSize)
        vBuffer.get(nv21, 0, vSize)

        val yuvImage = YuvImage(nv21, ImageFormat.NV21, image.width, image.height, null)
        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(
            Rect(0,0,yuvImage.width, yuvImage.height), 100,
            out
        )

        val imageBytes = out.toByteArray()

        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

    }

    fun requestCameraPerimission() {

        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                setupCamera()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                AlertDialog.Builder(this)
                    .setTitle(R.string.accept_camera_title)
                    .setMessage(R.string.camera_permission)
                    .setPositiveButton(android.R.string.ok) { _, _ ->
                        requestPermissionLauncher.launch(
                            Manifest.permission.CAMERA
                        )
                    }
                    .setNegativeButton(android.R.string.cancel) { _, _ ->
                    }
                    .show()
            }
            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.CAMERA
                )
            }
        }

    }

    private fun openListAnimalsActivity() {
        startActivity(Intent(this, DogListActivity::class.java))
    }

    private fun openLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    private fun openWholeImageActivity(pathUrl: String) {
        val intent = Intent(this, WholeImageActivity::class.java)
        intent.putExtra(PHOTO_PATH_KEY, pathUrl)
        startActivity(intent)
    }

    fun openDogDetailActivity(dog: Dog) {
        val intent = Intent(this, DogDetailActivity::class.java)
        intent.putExtra(DOG_KEY, dog)
        intent.putExtra(IS_RECOGNITION_KEY, true)
        startActivity(intent)
    }


}