package com.rickyandrean.a2320j2802_submissionintermediate.ui.main

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
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
import com.rickyandrean.a2320j2802_submissionintermediate.adapter.LoadingStateAdapter
import com.rickyandrean.a2320j2802_submissionintermediate.adapter.StoryAdapter
import com.rickyandrean.a2320j2802_submissionintermediate.databinding.ActivityMainBinding
import com.rickyandrean.a2320j2802_submissionintermediate.helper.ViewModelFactory
import com.rickyandrean.a2320j2802_submissionintermediate.storage.UserPreference
import com.rickyandrean.a2320j2802_submissionintermediate.ui.add.AddActivity
import com.rickyandrean.a2320j2802_submissionintermediate.ui.login.LoginActivity
import com.rickyandrean.a2320j2802_submissionintermediate.ui.map.MapsActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: StoryAdapter
    private lateinit var mainViewModel: MainViewModel

    override fun onResume() {
        super.onResume()
        adapter.refresh()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = ViewModelProvider(
            this@MainActivity,
            ViewModelFactory.getInstance(UserPreference.getInstance(dataStore))
        )[MainViewModel::class.java]

        adapter = StoryAdapter()
        binding.rvStories.layoutManager = LinearLayoutManager(this@MainActivity)
        binding.rvStories.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )

        setupView()
        observeData()

        binding.fabAdd.setOnClickListener(this)
    }

    private fun observeData() {
        // Make sure the token has been obtained before taking the story data
        mainViewModel.getUser().observe(this) { user ->
            TOKEN = "Bearer ${user.token}"

            mainViewModel.story.observe(this) { listStory ->
                adapter.submitData(lifecycle, listStory)
                removeLoading()
            }
        }
    }

    private fun removeLoading() {
        with(binding) {
            pbLoading.visibility = View.INVISIBLE
            bgLoading.visibility = View.INVISIBLE
        }
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
        supportActionBar?.title = resources.getString(R.string.app_name)
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
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
            R.id.app_bar_map -> {
                startActivity(Intent(this, MapsActivity::class.java))
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

    companion object {
        var TOKEN = "token"
    }
}