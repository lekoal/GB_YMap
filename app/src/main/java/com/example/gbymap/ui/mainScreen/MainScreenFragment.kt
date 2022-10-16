package com.example.gbymap.ui.mainScreen

import android.Manifest.permission.ACCESS_COARSE_LOCATION
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
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.gbymap.BuildConfig
import com.example.gbymap.R
import com.example.gbymap.databinding.FragmentMainScreenBinding
import com.example.gbymap.domain.marks.MarksEntity
import com.example.gbymap.utils.ADialog
import com.example.gbymap.utils.MapManager
import com.example.gbymap.utils.RequestPermissions
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.mapview.MapView
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named

const val LOCATION_PERMISSION = "location_permission"
const val LOCATION_ENABLE = "location_enable"

class MainScreenFragment : Fragment() {

    private var _binding: FragmentMainScreenBinding? = null
    private val binding get() = _binding!!

    private lateinit var mapManager: MapManager

    private var locationManager: LocationManager? = null
    private var locationListener: LocationListener? = null

    private lateinit var mapView: MapView

    private lateinit var alertDialog: ADialog

    private val viewModel: MarksViewModel by viewModel(named("marks_view_model"))

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
        mapView = binding.mapView
        mapManager = MapManager(mapView)
        mapManager.moveTo(59.939918, 30.316089)
        myLocationSetOnClick()
        addMarkSetOnClick()
        openMarksManagerOnClick()
        alertDialog = ADialog(requireActivity())
        val mapKit: MapKit = MapKitFactory.getInstance()
        val userPin = mapKit.createUserLocationLayer(mapView.mapWindow)
        userPin.isVisible = true
    }

    override fun onStart() {
        super.onStart()
        mapManager.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onResume() {
        super.onResume()
        checkSavedMarks()
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

    private fun addMarkSetOnClick() {
        binding.btnAddMark.setOnClickListener {
            addMarkScreenVisibility(true)

            addMarkNoButtonSetOnClick()
            addMarkYesButtonSetOnClick()
        }
    }

    private fun addMarkNoButtonSetOnClick() {
        binding.addMarkCancel.setOnClickListener {
            addMarkScreenVisibility(false)
        }
    }

    private fun addMarkScreenVisibility(isVisible: Boolean) {
        if (isVisible) {
            binding.blockScreen.visibility = View.VISIBLE
            binding.addMarkScreen.visibility = View.VISIBLE
            binding.blockScreen.isClickable = true
        } else {
            binding.blockScreen.isClickable = false
            binding.addMarkScreen.visibility = View.GONE
            binding.blockScreen.visibility = View.GONE
        }
    }

    private fun addMarkYesButtonSetOnClick() {
        binding.addMarkAccept.setOnClickListener {
            if (isDouble(binding.markLat.text.toString()) &&
                    isDouble(binding.markLong.text.toString())) {
                if (!binding.markName.text.isNullOrEmpty()) {
                    val lat = binding.markLat.text.toString().toDouble()
                    val lon = binding.markLong.text.toString().toDouble()
                    val markName = binding.markName.text.toString()
                    val mark = MarksEntity(
                        name = markName,
                        latitude = lat,
                        longitude = lon
                    )
                    addMarkScreenVisibility(false)
                    mapManager.addMark(lat, lon)
                    viewModel.insertMark(mark)
                } else {
                    Toast.makeText(requireContext(), "Введите имя", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(requireContext(), "Некорректное значение", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun isDouble(value: String): Boolean {
        return value.toDoubleOrNull() != null
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

    private fun checkSavedMarks() {
        viewModel.getMarks()
        viewModel.marks.observe(viewLifecycleOwner) {
            mapManager.addListMarks(it)
        }
    }

    private fun showRationaleDialog() {
        alertDialog.show(
            LOCATION_PERMISSION,
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
                LOCATION_ENABLE,
                getString(R.string.gps_activation_title),
                getString(R.string.gps_activation_message),
                getString(R.string.gps_activation_positive_button_text),
                getString(R.string.gps_activation_negative_button_text)
            )
        }
        return permissionGranted
    }

    private fun openMarksManagerOnClick() {
        binding.btnMarksManager.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_mainScreenFragment_to_marksManagerFragment)
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}