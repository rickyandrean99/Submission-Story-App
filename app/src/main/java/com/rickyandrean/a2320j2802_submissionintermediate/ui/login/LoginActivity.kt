package com.rickyandrean.a2320j2802_submissionintermediate.ui.login

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.rickyandrean.a2320j2802_submissionintermediate.databinding.ActivityLoginBinding
import com.rickyandrean.a2320j2802_submissionintermediate.helper.ViewModelFactory
import com.rickyandrean.a2320j2802_submissionintermediate.storage.UserPreference

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[LoginViewModel::class.java]

        loginViewModel.emailValid.observe(this) {
            loginValidation()
        }

        loginViewModel.passwordValid.observe(this) {
            loginValidation()
        }

        // Listener
        with(binding) {
            customEmail.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun afterTextChanged(s: Editable?) {}

                override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    setEmailValidation()
                }
            })

            customPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun afterTextChanged(s: Editable?) {}

                override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    setPasswordValidation()
                }
            })

            customEmail.setOnFocusChangeListener { _, _ ->
                setEmailValidation()
            }

            customPassword.setOnFocusChangeListener { _, _ ->
                setPasswordValidation()
            }
        }
    }

    private fun setEmailValidation() {
        binding.tvErrorEmail.visibility = if (binding.customEmail.valid) View.GONE else View.VISIBLE
        loginViewModel.updateEmailStatus(binding.customEmail.valid)
    }

    private fun setPasswordValidation() {
        binding.tvErrorPassword.visibility = if (binding.customPassword.valid) View.GONE else View.VISIBLE
        loginViewModel.updatePasswordStatus(binding.customPassword.valid)
    }

    private fun loginValidation() {
        binding.btnLogin.isEnabled = loginViewModel.
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
        supportActionBar?.hide()
    }
}