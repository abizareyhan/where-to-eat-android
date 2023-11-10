package com.akbarsya.wheretoeat.view.activity

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.lifecycleScope
import com.akbarsya.wheretoeat.R
import com.akbarsya.wheretoeat.common.abstracts.BaseActivity
import com.akbarsya.wheretoeat.common.abstracts.ResourceState
import com.akbarsya.wheretoeat.databinding.ActivityPlacesMapBinding
import com.akbarsya.wheretoeat.extension.observe
import com.akbarsya.wheretoeat.util.MapboxUtil
import com.akbarsya.wheretoeat.viewmodel.PlaceMapViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mapbox.geojson.Point
import com.mapbox.maps.EdgeInsets
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class PlacesMapActivity: BaseActivity<ActivityPlacesMapBinding>(
    ActivityPlacesMapBinding::inflate
) {
    @Inject
    lateinit var mapboxUtil: MapboxUtil

    private val viewModel: PlaceMapViewModel by viewModels()

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var isUserHasMoveMap: Boolean = false

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) ||
                    permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false)-> {

            }
            else -> {

            }
        }
    }

    private val onIndicatorPositionChangedListener = OnIndicatorPositionChangedListener {
        with(binding) {
            if(!isUserHasMoveMap) {
                val pointPlaceList: MutableList<Point> = viewModel.fetchPlaceLiveData.value?.data?.recommendedPlaces?.map {
                    Point.fromLngLat(it.place.longitude, it.place.latitude)
                }?.toMutableList() ?: mutableListOf()

                pointPlaceList.add(it)

                val cameraPosition = mapView.getMapboxMap().cameraForCoordinates(pointPlaceList, EdgeInsets(240.0, 240.0, 240.0, 240.0))

                mapView.getMapboxMap().flyTo(cameraPosition)
            }
        }
    }


    override fun init() {
        initFusedLocationClient()
        initializeMapbox()
        checkLocationPermission()
        observePlace()
    }

    private fun initFusedLocationClient() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this@PlacesMapActivity)
    }

    private fun initializeMapbox() {
        with(binding) {
            mapboxUtil.mapboxUpdateAttributionLogo(mapView, false)
            mapboxUtil.mapboxUpdateBaseUISetting(mapView, false)
            mapboxUtil.mapboxUpdateGestureSetting(mapView, true)
            mapboxUtil.mapboxUpdateLocationSetting(mapView, true, onIndicatorPositionChangedListener)

            val mapboxMap = mapView.getMapboxMap()

            mapboxMap.loadStyleUri(HomeActivity.MAPBOX_STYLE) {
                lifecycleScope.launch(Dispatchers.Main) {
                    delay(1000)
                    viewModel.fetchPlace()
                }
            }
        }
    }

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this@PlacesMapActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this@PlacesMapActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionRequest.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION))
            return
        }

        initializeMapbox()
//        getUserLocation()
//        startLocationUpdates()
    }

    private fun observePlace() {
//        observe(viewModel.fetchPlaceLiveData) { resource ->
//            when(resource.status) {
//                ResourceState.SUCCESS -> {
//                    resource.data.map { place ->
//                        addCustomMarkerToMap(place)
//                    }
//                }
//                else -> {
//
//                }
//            }
//        }
    }

//    private fun addCustomMarkerToMap(place: DirectusPlaceEntity) {
//        try {
//            val annotationApi = binding.mapView.annotations
//            val pointAnnotationManager = annotationApi.createPointAnnotationManager(binding.mapView)
//// Set options for the resulting symbol layer.
//            val bitmap = ContextCompat.getDrawable(this@PlacesMapActivity, R.drawable.bg_marker_place)?.toBitmap()
//
//            bitmap?.let {
//                val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
//                    // Define a geographic coordinate.
//                    .withPoint(
//                        Point.fromLngLat(place.location.coordinates.longitude, place.location.coordinates.latitude)
//                    )
//                    // Specify the bitmap you assigned to the point annotation
//                    // The bitmap will be added to map style automatically.
//                    .withIconImage(bitmap)
//
////                pointAnnotationManager.create(pointAnnotationOptions)
//
//                val bitmapImage = createImage(120, 120, place.preferenceTag.firstOrNull()?.preferenceTagId?.icon ?: "A")
//
//                bitmapImage?.let {
//                    val pointAnnotationOptionsText: PointAnnotationOptions = PointAnnotationOptions()
//                        // Define a geographic coordinate.
//                        .withPoint(
//                            Point.fromLngLat(place.location.coordinates.longitude, place.location.coordinates.latitude)
//                        )
//                        // Specify the bitmap you assigned to the point annotation
//                        // The bitmap will be added to map style automatically.
//                        .withIconImage(bitmapImage)
//
//                    pointAnnotationManager.create(pointAnnotationOptionsText)
//                    pointAnnotationManager.create(pointAnnotationOptions)
//
//                }
//            }
//
//// Add the resulting pointAnnotation to the map.
//
////            binding.mapView.viewAnnotationManager.addViewAnnotation(
////                resId = R.layout.marker_place_with_icon,
////                options = viewAnnotationOptions {
////                    geometry(Point.fromLngLat(place.location.coordinates.longitude, place.location.coordinates.latitude))
////                    allowOverlap(true)
////                },
////                asyncInflater = AsyncLayoutInflater(this@PlacesMapActivity)
////            ) {
////                it.findViewById<MaterialTextView>(R.id.tvIcon).text = place.preferenceTag.firstOrNull()?.preferenceTagId?.icon
////                it.setOnClickListener {
////                    viewModel.fetchPlaceLiveData.value?.data?.forEach {
////                        binding.mapView.viewAnnotationManager.getViewAnnotationByFeatureId(it.id)?.let {
////                            it.findViewById<AppCompatImageView>(R.id.ivIcon).setImageResource(R.drawable.bg_marker_place)
////                        }
////                    }
////                    it.findViewById<AppCompatImageView>(R.id.ivIcon).setImageResource(R.drawable.bg_marker_place_selected)
////                }
////            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }

    fun createImage(width: Int, height: Int, name: String): Bitmap? {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint2 = Paint()
        paint2.setColor(Color.TRANSPARENT)
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint2)
        val paint = Paint()
        paint.setColor(Color.WHITE)
        paint.setTextSize(72F)
        paint.setTextScaleX(1F)
        canvas.drawText(name, 75F - 25F, 75F + 20F, paint)
        return bitmap
    }
}
