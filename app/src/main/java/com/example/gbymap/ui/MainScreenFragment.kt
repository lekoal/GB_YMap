package com.example.gbymap.ui

import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.gbymap.BuildConfig
import com.example.gbymap.R
import com.example.gbymap.databinding.FragmentMainScreenBinding
import com.example.gbymap.utils.MapManager
import com.yandex.mapkit.MapKitFactory

class MainScreenFragment : Fragment() {

    private var _binding: FragmentMainScreenBinding? = null
    private val binding get() = _binding!!

    private lateinit var mapManager: MapManager

    private var locationManager: LocationManager? = null
    private var locationListener: LocationListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey(BuildConfig.YANDEX_MAPKIT_API)
        MapKitFactory.initialize(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapManager = MapManager(binding.mapView)
        mapManager.moveTo(59.939918, 30.316089)

        myLocationSetOnClick()
    }

    override fun onStart() {
        super.onStart()
        mapManager.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onStop() {
        mapManager.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    private fun myLocationSetOnClick() {
        binding.btnSearchMyLocation.setOnClickListener {
            checkLocationPermission()
        }
    }

    private fun checkLocationPermission() {
        activity?.let {
            when {
                ContextCompat.checkSelfPermission(it, ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED -> {
                    getLocation()
                }
                shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)
                -> {
                    showRationaleDialog()
                }
                else -> {
                    requestPermission()
                }
            }
        }
    }

    private fun showRationaleDialog() {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle(getString(R.string.location_permission_title))
                .setMessage(getString(R.string.location_permission_message))
                .setPositiveButton(
                    getString(R.string.location_permission_positive_button_text)
                ) { _, _ ->
                    requestPermission()
                }
                .setNegativeButton(
                    getString(R.string.location_permission_negative_button_text)
                ) { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }
    }

    private fun requestPermission() {
        val permissions = ArrayList<String>()
        permissions.add(ACCESS_FINE_LOCATION)
        ActivityCompat.requestPermissions(requireActivity(), permissions.toTypedArray(), 1)
    }

    private fun setupLocation() {
        locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as
                LocationManager
        locationListener = LocationListener { location ->
            Log.i("MY_TAG", "Current Location -  ${location.latitude}:${location.longitude}")
        }
    }

    private fun getLocation() {
        setupLocation()
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermission()
        } else {
            try {
                locationManager!!.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 0L, 0f, locationListener!!
                )
            } catch (e: SecurityException) {
                Log.d("", "Security exception, no location")
            }
        }
    }

}