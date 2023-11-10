package com.akbarsya.wheretoeat.util

import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.attribution.attribution
import com.mapbox.maps.plugin.compass.compass
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.plugin.logo.logo
import com.mapbox.maps.plugin.scalebar.scalebar
import javax.inject.Inject

class MapboxUtil @Inject constructor() {
    fun mapboxUpdateAttributionLogo(mapView: MapView, isEnabled: Boolean) {
        mapView.attribution.updateSettings {
            enabled = isEnabled
        }
        mapView.logo.updateSettings {
            enabled = isEnabled
        }
    }

    fun mapboxUpdateBaseUISetting(mapView: MapView, isEnabled: Boolean) {
        mapView.scalebar.updateSettings {
            enabled = isEnabled
        }
        mapView.compass.updateSettings {
            enabled = isEnabled
        }
    }

    fun mapboxUpdateGestureSetting(mapView: MapView, isEnabled: Boolean) {
        mapView.gestures.updateSettings {
            rotateEnabled = isEnabled
            pinchToZoomEnabled = isEnabled
            scrollEnabled = isEnabled
            simultaneousRotateAndPinchToZoomEnabled = isEnabled
            pitchEnabled = isEnabled
            doubleTapToZoomInEnabled = isEnabled
            doubleTouchToZoomOutEnabled = isEnabled
            quickZoomEnabled = isEnabled
            pinchToZoomDecelerationEnabled = isEnabled
            rotateDecelerationEnabled = isEnabled
            scrollDecelerationEnabled = isEnabled
            increaseRotateThresholdWhenPinchingToZoom = isEnabled
            increasePinchToZoomThresholdWhenRotating = isEnabled
            pinchScrollEnabled = isEnabled
        }
    }

    fun mapboxUpdateLocationSetting(mapView: MapView, isEnabled: Boolean, listener: OnIndicatorPositionChangedListener) {
        mapView.location.updateSettings {
            enabled = isEnabled
        }

        if(isEnabled) {
            mapView.location.addOnIndicatorPositionChangedListener(listener)
        }
    }
}