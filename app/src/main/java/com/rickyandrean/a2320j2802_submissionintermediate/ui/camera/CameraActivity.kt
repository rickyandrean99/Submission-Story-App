package com.rickyandrean.a2320j2802_submissionintermediate.ui.camera

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rickyandrean.a2320j2802_submissionintermediate.databinding.ActivityCameraBinding

class CameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}