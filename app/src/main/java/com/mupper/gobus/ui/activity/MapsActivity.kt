package com.mupper.gobus.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.SupportMapFragment
import com.mupper.gobus.R
import com.mupper.gobus.repository.UserLocationRepository
import com.mupper.gobus.viewmodel.MapsViewModel
import com.mupper.gobus.viewmodel.MapsViewModelFactory

class MapsActivity : AppCompatActivity() {

    private val mapsViewModel: MapsViewModel by lazy {
        ViewModelProviders.of(this, MapsViewModelFactory(this)).get(MapsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(mapsViewModel.getMapReady())
    }
}
