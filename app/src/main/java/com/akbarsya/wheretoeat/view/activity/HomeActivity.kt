package com.akbarsya.wheretoeat.view.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.akbarsya.wheretoeat.R
import com.akbarsya.wheretoeat.adapter.PreferenceTagCircleAdapter
import com.akbarsya.wheretoeat.common.abstracts.BaseActivity
import com.akbarsya.wheretoeat.common.abstracts.ResourceState
import com.akbarsya.wheretoeat.common.constant.WhereToEatActivity
import com.akbarsya.wheretoeat.databinding.ActivityHomeBinding
import com.akbarsya.wheretoeat.domain.entity.NominatimReverseGeoEntity
import com.akbarsya.wheretoeat.domain.entity.PlaceEntity
import com.akbarsya.wheretoeat.extension.observe
import com.akbarsya.wheretoeat.extension.pxFromDp
import com.akbarsya.wheretoeat.extension.setItemDecorations
import com.akbarsya.wheretoeat.extension.showShortToast
import com.akbarsya.wheretoeat.itemdecoration.HorizontalRecyclerItemDecorator
import com.akbarsya.wheretoeat.model.PreferenceTag
import com.akbarsya.wheretoeat.util.MapboxUtil
import com.akbarsya.wheretoeat.viewmodel.HomeViewModel
import com.akbarsya.wheretoeat.viewmodel.ProfileViewModel
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mapbox.geojson.Point
import com.mapbox.maps.EdgeInsets
import com.mapbox.maps.ScreenCoordinate
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.viewannotation.OnViewAnnotationUpdatedListener
import com.mapbox.maps.viewannotation.viewAnnotationOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>(
    ActivityHomeBinding::inflate
) {
    companion object {
        const val MAPBOX_STYLE = "mapbox://styles/akbarsya/cl9cqjt30000o15qn64paclkc"
    }

    private val viewModel: HomeViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()

    private var lastLocation: Location? = null

    private val adapter: PreferenceTagCircleAdapter by lazy {
        PreferenceTagCircleAdapter(::onTagSelected)
    }

    @Inject
    lateinit var mapboxUtil: MapboxUtil

    private lateinit var locationCallback: LocationCallback
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) ||
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false)-> {
                initializeMapbox()
                getUserLocation()
                startLocationUpdates()
            }
            else -> userAddressNotFound()
        }
    }

    private val onIndicatorPositionChangedListener = OnIndicatorPositionChangedListener {
        with(binding) {
            val pointPlaceList: MutableList<Point> = viewModel.fetchPlaceLiveData.value?.nullableData?.recommendedPlaces?.map {
                Point.fromLngLat(it.place.longitude, it.place.latitude)
            }?.toMutableList() ?: mutableListOf()

            pointPlaceList.add(it)

            val cameraPosition = mapView.getMapboxMap().cameraForCoordinates(pointPlaceList, EdgeInsets(120.0, 120.0, 120.0, 120.0))

            mapView.getMapboxMap().flyTo(cameraPosition)
        }
    }

    override fun init() {
        initializeRecyclerView()
        initFusedLocationClient()
        checkLocationPermission()
        updateUserUI()
        observePreferenceTag()
        observeReverseGeocode()
        observePlace()

        viewModel.fetchTags()
    }

    private fun initializeRecyclerView() {
        with(binding) {
            rvTags.layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
            rvTags.adapter = adapter
            rvTags.setItemDecorations(
                HorizontalRecyclerItemDecorator(
                    4.pxFromDp,
                    startEndSpacing = 24.pxFromDp
                )
            )
        }
    }

    private fun initFusedLocationClient() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this@HomeActivity)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.locations.firstOrNull()?.let {
                    geocodeLocation(it)
                }
            }
        }
    }

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this@HomeActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this@HomeActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionRequest.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION))
            return
        }

        initializeMapbox()
        getUserLocation()
        startLocationUpdates()
    }

    private fun initializeMapbox() {
        with(binding) {
            mapboxUtil.mapboxUpdateAttributionLogo(mapView, false)
            mapboxUtil.mapboxUpdateBaseUISetting(mapView, false)
            mapboxUtil.mapboxUpdateGestureSetting(mapView, false)
            mapboxUtil.mapboxUpdateLocationSetting(mapView, true, onIndicatorPositionChangedListener)

            val mapboxMap = mapView.getMapboxMap()

            mapboxMap.addOnMapClickListener {
                openMapsActivity()
                true
            }

            mapboxMap.loadStyleUri(MAPBOX_STYLE) {
                lifecycleScope.launch(Dispatchers.Main) {
//                    delay(1000)
//                    viewModel.fetchPlaceForYou(
//                        lastLocation
//                    )
                }
            }
        }
    }

    private fun observePreferenceTag() {
        observe(viewModel.preferenceTagLiveData) {
            when(it.status) {
                ResourceState.SUCCESS -> {
                    val updatedData = mutableListOf(
                        PreferenceTag.forYouTag(this@HomeActivity)
                    )
                    updatedData.addAll(it.data)

                    adapter.updateItems(updatedData)
                } else -> {
                    showShortToast("Failed / Loading")
                }
            }
        }
    }

    private fun observePlace() {
        observe(viewModel.fetchPlaceLiveData) { resource ->
            when(resource.status) {
                ResourceState.SUCCESS -> {
                    resource.data.recommendedPlaces.map { recommendedPlace ->
                        addCustomMarkerToMap(recommendedPlace.place)
                    }
                }
                else -> userAddressNotFound()
            }
        }
    }

    private fun addCustomMarkerToMap(place: PlaceEntity) {
        try {
            binding.mapView.viewAnnotationManager.addViewAnnotation(
                resId = R.layout.marker_place_with_icon,
                options = viewAnnotationOptions {
                    geometry(Point.fromLngLat(place.longitude, place.latitude))
                    allowOverlap(true)
                },
                asyncInflater = AsyncLayoutInflater(this@HomeActivity)
            ) {
                it.findViewById<MaterialTextView>(R.id.tvIcon).text = place.placeTags.firstOrNull()?.icon
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun onTagSelected(selectedPreferenceTag: PreferenceTag) {
        val preferenceTags = adapter.items.map { preferenceTag ->
            val updatedPreferenceTag = preferenceTag.copy()

            updatedPreferenceTag.isSelected = selectedPreferenceTag == preferenceTag

            updatedPreferenceTag
        }

        adapter.updateItems(preferenceTags)
    }

    private fun observeReverseGeocode() {
        observe(viewModel.reverseGeocodeLiveData) {
            when(it.status) {
                ResourceState.SUCCESS -> updateUserLocationUI(it.data)
                else -> userAddressNotFound()
            }
        }
    }

    private fun updateUserUI() {
        with(binding) {
            val userName = profileViewModel.fetchUserByFirebaseUidLiveData.value?.data?.firstName ?: Firebase.auth.currentUser?.displayName
            tvTitle.text = getString(R.string.welcome_message, userName)
            getUserLocation()
        }
    }

    private fun getUserLocation() {
        lifecycleScope.launch {
            if (ActivityCompat.checkSelfPermission(this@HomeActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this@HomeActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ) {
                locationPermissionRequest.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
                return@launch
            }

            lastLocation = fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                object : CancellationToken() {
                    override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token

                    override fun isCancellationRequested() = false
                }
            ).await()

            viewModel.fetchPlaceForYou(
                lastLocation?.latitude ?: 0.0,
                lastLocation?.longitude ?: 0.0
            )

            geocodeLocation(lastLocation)
        }
    }

    @Suppress("DEPRECATION")
    private fun geocodeLocation(location: Location?) {
        viewModel.reverseGeoCode(location?.latitude ?: 0.0, location?.longitude ?: 0.0)
    }

    private fun updateUserLocationUI(reverseGeoEntity: NominatimReverseGeoEntity) {
        lifecycleScope.launch(Dispatchers.Main) {
            with(binding) {
                val locationStringBuilder = StringBuilder()

                if(reverseGeoEntity.address.neighbourhood.isNotEmpty()) {
                    locationStringBuilder.append(reverseGeoEntity.address.neighbourhood)
                    locationStringBuilder.append(", ")
                } else if(reverseGeoEntity.address.suburb.isNotEmpty()) {
                    locationStringBuilder.append(reverseGeoEntity.address.suburb)
                    locationStringBuilder.append(", ")
                }
                locationStringBuilder.append(reverseGeoEntity.address.cityDistrict)

                tvSubtitle.text = getString(
                    R.string.location_at,
                    locationStringBuilder
                )
                tvSubtitle.visibility = View.VISIBLE
            }
        }
    }

    private fun userAddressNotFound() {
        lifecycleScope.launch(Dispatchers.Main) {
            with(binding) {
                tvSubtitle.visibility = View.INVISIBLE
            }
        }
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            showShortToast("No Access")
            return
        }
        fusedLocationClient.requestLocationUpdates(
            LocationRequest.create().apply {
                interval = 60000
                fastestInterval = 30000
                priority = Priority.PRIORITY_HIGH_ACCURACY
                maxWaitTime = 120000
            },
            locationCallback,
            Looper.getMainLooper())
    }

    private fun openMapsActivity() {
        startActivity(Intent(this, WhereToEatActivity.PLACES_MAP  ))
    }

}