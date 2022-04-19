package com.rickyandrean.a2320j2802_submissionintermediate.ui.add

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.rickyandrean.a2320j2802_submissionintermediate.R
import com.rickyandrean.a2320j2802_submissionintermediate.databinding.ActivityAddBinding
import com.rickyandrean.a2320j2802_submissionintermediate.helper.ViewModelFactory
import com.rickyandrean.a2320j2802_submissionintermediate.helper.reduceFileImage
import com.rickyandrean.a2320j2802_submissionintermediate.helper.uriToFile
import com.rickyandrean.a2320j2802_submissionintermediate.model.FileUploadResponse
import com.rickyandrean.a2320j2802_submissionintermediate.network.ApiConfig
import com.rickyandrean.a2320j2802_submissionintermediate.storage.UserPreference
import com.rickyandrean.a2320j2802_submissionintermediate.ui.camera.CameraActivity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")

class AddActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityAddBinding
    private lateinit var addViewModel: AddViewModel
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

        addViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[AddViewModel::class.java]

        addViewModel.getUser().observe(this) {
            addViewModel.token = it.token
        }

        binding.btnCamera.setOnClickListener(this)
        binding.btnGallery.setOnClickListener(this)
        binding.btnUpload.setOnClickListener(this)
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
                val intent = Intent(this, CameraActivity::class.java)
                launcherIntentCameraX.launch(intent)
            }
            R.id.btn_gallery -> {
                val intent = Intent().also {
                    it.action = Intent.ACTION_GET_CONTENT
                    it.type = "image/*"
                }

                val chooser = Intent.createChooser(intent, "Choose a Picture")
                launcherIntentGallery.launch(chooser)
            }
            R.id.btn_upload -> {
                uploadImage()
            }
        }
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File

            getFile = myFile

            val result = BitmapFactory.decodeFile(myFile.path)
            binding.ivNewStory.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val selectedImg: Uri = it.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@AddActivity)

            getFile = myFile

            binding.ivNewStory.setImageURI(selectedImg)
        }
    }

    private fun uploadImage() {
        if (getFile != null && binding.etDescription.text.toString() != "") {
            val file = reduceFileImage(getFile as File)

            val description = binding.etDescription.text.toString().toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData("photo", file.name, requestImageFile)

            val service = ApiConfig.getApiService().uploadImage("Bearer ${addViewModel.token}", imageMultipart, description)
            service.enqueue(object : Callback<FileUploadResponse> {
                override fun onResponse(
                    call: Call<FileUploadResponse>,
                    response: Response<FileUploadResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            if (!responseBody.error) {
                                Toast.makeText(this@AddActivity, responseBody.message, Toast.LENGTH_SHORT).show()
                                finish()
                            } else {
                                Toast.makeText(this@AddActivity, "Error message: ${responseBody.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(this@AddActivity, "Error message: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                    Toast.makeText(this@AddActivity, "Failed to upload image", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "Silakan masukkan berkas gambar ataupun deskripsi terlebih dahulu.", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}