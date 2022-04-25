package com.rickyandrean.a2320j2802_submissionintermediate.ui.map

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.rickyandrean.a2320j2802_submissionintermediate.R
import com.rickyandrean.a2320j2802_submissionintermediate.databinding.ActivityMapsBinding
import com.rickyandrean.a2320j2802_submissionintermediate.helper.ViewModelFactory
import com.rickyandrean.a2320j2802_submissionintermediate.storage.UserPreference
import com.rickyandrean.a2320j2802_submissionintermediate.ui.detail.DetailActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var mapsViewModel: MapsViewModel

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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun showMapInfo() {
        AlertDialog.Builder(this).apply {
            setTitle(R.string.info)
            setMessage(R.string.info_map)
            setPositiveButton(R.string.ok) { _, _ -> }
            create()
            show()
        }
    }

    private fun setMapStyle() {
        try {
            val success = mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        mapsViewModel = ViewModelProvider(
            this@MapsActivity,
            ViewModelFactory.getInstance(UserPreference.getInstance(dataStore))
        )[MapsViewModel::class.java]

        mapsViewModel.getUser().observe(this) {
            mapsViewModel.getStoriesMap(it.token)
        }

        mapsViewModel.errorMessage.observe(this) {
            if (it != "") {
                AlertDialog.Builder(this).apply {
                    setTitle(R.string.data_error)
                    setMessage(R.string.data_error_message.toString() + " $it")
                    setPositiveButton(R.string.ok) { _, _ -> }
                    create()
                    show()
                }
            }
        }

        mapsViewModel.stories.observe(this) {
            if (it.size == 0) {
                AlertDialog.Builder(this).apply {
                    setTitle(R.string.info)
                    setMessage(R.string.empty_story)
                    setPositiveButton(R.string.ok) { _, _ -> }
                    create()
                    show()
                }
            }

            val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        setMapStyle()

        with(mMap.uiSettings) {
            isZoomControlsEnabled = true
            isCompassEnabled = true
            isMapToolbarEnabled = true
        }

        if (mapsViewModel.stories.value != null) {
            val stories = mapsViewModel.stories.value!!

            stories.forEachIndexed { index, story ->
                if (story.lat != null && story.lon != null) {
                    val coordinate = LatLng(story.lat.toDouble(), story.lon.toDouble())
                    mMap.addMarker(
                        MarkerOptions()
                            .position(coordinate)
                            .title(story.name)
                            .alpha(0.3F)
                            .snippet(story.description)
                    )?.tag = index

                    // Animate camera to the last story location
                    if (index == stories.size - 1) {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 5f))
                        showMapInfo()
                    }
                }
            }
        }

        mMap.setOnInfoWindowClickListener(this)
    }

    override fun onInfoWindowClick(m: Marker) {
        // Get the tag (index) so the data to be sent to the detail activity can be determined
        val index = m.tag.toString().toInt()

        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.STORY, mapsViewModel.stories.value?.get(index))
        startActivity(intent)
    }

    companion object {
        private const val TAG = "MapsActivity"
    }
}