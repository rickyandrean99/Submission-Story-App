package com.rickyandrean.a2320j2802_submissionintermediate.ui.add

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.rickyandrean.a2320j2802_submissionintermediate.R
import com.rickyandrean.a2320j2802_submissionintermediate.databinding.ActivityAddBinding
import com.rickyandrean.a2320j2802_submissionintermediate.helper.createCustomTempFile
import com.rickyandrean.a2320j2802_submissionintermediate.helper.rotateBitmap
import com.rickyandrean.a2320j2802_submissionintermediate.ui.camera.CameraActivity
import java.io.File

class AddActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityAddBinding
    private lateinit var currentPhotoPath: String
    private var getFile: File? = null

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(this, "Tidak mendapatkan permission.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        // Check permission
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        binding.btnCamera.setOnClickListener(this)
        binding.btnGallery.setOnClickListener(this)
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.title = "New Story"
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_camera -> {
//                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//                intent.resolveActivity(packageManager)
//
//                createCustomTempFile(application).also {
//                    val photoURI: Uri = FileProvider.getUriForFile(
//                        this@AddActivity,
//                        "com.rickyandrean.a2320j2802_submissionintermediate",
//                        it
//                    )
//                    currentPhotoPath = it.absolutePath
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
//                    launcherIntentCamera.launch(intent)
//                }

                val intent = Intent(this, CameraActivity::class.java)
                launcherIntentCameraX.launch(intent)
            }
            R.id.btn_gallery -> {

            }
            R.id.btn_upload -> {

            }
        }
    }

//    private val launcherIntentCamera = registerForActivityResult(
//        ActivityResultContracts.StartActivityForResult()
//    ) {
//        if (it.resultCode == RESULT_OK) {
//            val myFile = File(currentPhotoPath)
//            val result = rotateBitmap(BitmapFactory.decodeFile(myFile.path), true)
//
//            getFile = myFile
//
//            binding.ivNewStory.setImageBitmap(result)
//        }
//    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            getFile = myFile

            val result = rotateBitmap(BitmapFactory.decodeFile(myFile.path), isBackCamera)
            binding.ivNewStory.setImageBitmap(result)
        }
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}