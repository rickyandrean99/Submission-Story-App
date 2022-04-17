package com.rickyandrean.a2320j2802_submissionintermediate.ui.login

import android.content.Context
import android.content.Intent
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
import com.rickyandrean.a2320j2802_submissionintermediate.R
import com.rickyandrean.a2320j2802_submissionintermediate.databinding.ActivityLoginBinding
import com.rickyandrean.a2320j2802_submissionintermediate.helper.ViewModelFactory
import com.rickyandrean.a2320j2802_submissionintermediate.storage.UserPreference
import com.rickyandrean.a2320j2802_submissionintermediate.ui.register.RegisterActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")

class LoginActivity : AppCompatActivity(), View.OnClickListener {
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
            loginValidation(it, loginViewModel.passwordValid.value!!)
        }

        loginViewModel.passwordValid.observe(this) {
            loginValidation(loginViewModel.emailValid.value!!, it)
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

        binding.btnLogin.setOnClickListener(this)
        binding.tvRegisterHyperlink.setOnClickListener(this)
    }

    private fun setEmailValidation() {
        binding.tvErrorEmail.visibility = if (binding.customEmail.valid) View.GONE else View.VISIBLE
        loginViewModel.updateEmailStatus(binding.customEmail.valid)
    }

    private fun setPasswordValidation() {
        binding.tvErrorPassword.visibility = if (binding.customPassword.valid) View.GONE else View.VISIBLE
        loginViewModel.updatePasswordStatus(binding.customPassword.valid)
    }

    private fun loginValidation(emailValidation: Boolean, passwordValidation: Boolean) {
        binding.btnLogin.isEnabled = emailValidation && passwordValidation
        binding.btnLogin.changeStatus(emailValidation && passwordValidation)
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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_login -> {
                Toast.makeText(this, "Valid", Toast.LENGTH_LONG).show()
            }
            R.id.tv_register_hyperlink -> {
                startActivity(Intent(this, RegisterActivity::class.java))
            }
        }
    }
}