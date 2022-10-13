package com.example.gbymap.ui

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.gbymap.BuildConfig
import com.example.gbymap.R
import com.example.gbymap.databinding.FragmentMainScreenBinding
import com.example.gbymap.utils.ADialog
import com.example.gbymap.utils.MapManager
import com.example.gbymap.utils.RequestPermissions
import com.yandex.mapkit.MapKitFactory

class MainScreenFragment : Fragment() {

    private var _binding: FragmentMainScreenBinding? = null
    private val binding get() = _binding!!

    private lateinit var mapManager: MapManager

    private var locationManager: LocationManager? = null
    private var locationListener: LocationListener? = null

    private lateinit var alertDialog: ADialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey(BuildConfig.YANDEX_MAPKIT_API)
        MapKitFactory.initialize(requireContext())
        setupLocation()
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
        alertDialog = ADialog(requireActivity())
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
        when {
            (ContextCompat.checkSelfPermission(
                requireActivity(),
                ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED) &&
                    (ContextCompat.checkSelfPermission(
                        requireActivity(),
                        ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED) &&
                    checkGPS() -> {
                getLocation()
            }
            shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)
            -> {
                showRationaleDialog()
            }
            else -> {
                RequestPermissions.location(requireActivity())
            }
        }
    }

    private fun showRationaleDialog() {
        alertDialog.show(
            getString(R.string.location_permission_title),
            getString(R.string.location_permission_message),
            getString(R.string.location_permission_positive_button_text),
            getString(R.string.location_permission_negative_button_text)
        )
    }

    private fun setupLocation() {
        locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as
                LocationManager
        locationListener = LocationListener { location ->
            Log.i(
                "MY_TAG",
                "Current Location -  ${location.latitude}:${location.longitude}"
            )
            mapManager.moveTo(location.latitude, location.longitude)
            locationManager!!.removeUpdates(locationListener!!)
        }
    }

    private fun getLocation() {
        try {
            locationManager!!.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener!!
            )
            locationManager!!.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 0L, 0f, locationListener!!
            )
        } catch (e: SecurityException) {
            Log.d("", "Security exception, no location")
        }
    }

    private fun checkGPS(): Boolean {
        val permissionGranted =
            locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) ?: false
        if (!permissionGranted) {
            alertDialog.show(
                getString(R.string.gps_activation_title),
                getString(R.string.gps_activation_message),

            )
            val intent = Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        }
        return permissionGranted
    }

}