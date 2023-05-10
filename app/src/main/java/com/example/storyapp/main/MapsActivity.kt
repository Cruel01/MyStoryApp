package com.example.storyapp.main

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityMapsBinding
import com.example.storyapp.utils.UserPref
import com.example.storyapp.viewModel.UniversalFactory
import com.example.storyapp.viewModel.UniversalVM
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var viewModel: UniversalVM
    private lateinit var binding: ActivityMapsBinding
    private lateinit var userPref: UserPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        userPref = UserPref(this)
        viewModel = ViewModelProvider(this, UniversalFactory(this))[UniversalVM::class.java]

        viewModel.storyList.observe(this) {storyList ->
            googleMap.clear()

            if (storyList != null) {
                for (story in storyList) {
                    if (story.lat != null && story.lon != null) {
                        val markerOptions = MarkerOptions()
                            .position(LatLng(story.lat as Double, story.lon as Double))
                            .title(story.name)
                            .snippet(story.description)
                            .icon(bitmapDescriptorFromVector(this, R.drawable.marker))
                        googleMap.addMarker(markerOptions)
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        if (userPref.getToken() != null) {
            val homeIntent = Intent(this, HomeActivity::class.java)
            startActivity(homeIntent)
        } else {
            super.onBackPressed()
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        val defaultLocation = LatLng(-6.9034424, 107.5607555)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 6f))
        viewModel.loadStories()
    }

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }
}