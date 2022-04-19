package com.rickyandrean.a2320j2802_submissionintermediate.ui.main

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rickyandrean.a2320j2802_submissionintermediate.R
import com.rickyandrean.a2320j2802_submissionintermediate.adapter.StoryAdapter
import com.rickyandrean.a2320j2802_submissionintermediate.databinding.ActivityMainBinding
import com.rickyandrean.a2320j2802_submissionintermediate.helper.ViewModelFactory
import com.rickyandrean.a2320j2802_submissionintermediate.storage.UserPreference
import com.rickyandrean.a2320j2802_submissionintermediate.ui.add.AddActivity
import com.rickyandrean.a2320j2802_submissionintermediate.ui.login.LoginActivity
import com.rickyandrean.a2320j2802_submissionintermediate.ui.login.LoginViewModel
import com.rickyandrean.a2320j2802_submissionintermediate.ui.setting.SettingActivity
import kotlinx.coroutines.NonCancellable.cancel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        binding.rvStories.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[MainViewModel::class.java]

        mainViewModel.getUser().observe(this) {
            mainViewModel.getStories(it.token)
        }

        mainViewModel.stories.observe(this) {
            binding.rvStories.adapter = StoryAdapter(it)
        }

        binding.fabAdd.setOnClickListener(this)
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

        supportActionBar?.title = "Story App"
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.fab_add) startActivity(Intent(this, AddActivity::class.java))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_setting -> {
                startActivity(Intent(this@MainActivity, SettingActivity::class.java))
            }
            R.id.app_bar_logout -> {
                AlertDialog.Builder(this).apply {
                    setTitle(R.string.logout)
                    setMessage(R.string.logout_confirmation)
                    setPositiveButton(R.string.yes) { _, _ ->
                        mainViewModel.logout()

                        val intent = Intent(this@MainActivity, LoginActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                        finish()
                    }
                    setNegativeButton(R.string.cancel) { _, _ ->

                    }
                    create()
                    show()
                }
            }
        }

        return true
    }
}