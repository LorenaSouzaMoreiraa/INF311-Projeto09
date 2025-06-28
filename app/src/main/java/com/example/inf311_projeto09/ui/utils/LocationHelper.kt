package com.example.inf311_projeto09.ui.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

@Composable
fun rememberLocationHelper(): LocationHelper {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    return remember { LocationHelper(context, fusedLocationClient) }
}

class LocationHelper(
    private val context: Context,
    private val fusedLocationClient: FusedLocationProviderClient
) {

    suspend fun getCurrentLocation(
        activityLauncher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>
    ): LatLng? {
        val hasFine = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val hasCoarse = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasFine && !hasCoarse) {
            val result = requestPermissions(activityLauncher)
            val fineGranted = result[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
            val coarseGranted = result[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

            if (!fineGranted && !coarseGranted) {
                return null
            }
        }

        return getLastKnownLocation()
    }

    private suspend fun requestPermissions(
        activityLauncher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>
    ): Map<String, Boolean> = suspendCancellableCoroutine { cont ->
        activityLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    suspend fun getLastKnownLocation(): LatLng? = suspendCancellableCoroutine { cont ->
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                cont.resume(LatLng(location.latitude, location.longitude))
            } else {
                cont.resume(null)
            }
        }.addOnFailureListener {
            cont.resume(null)
        }
    }

    fun isWithinDistance(
        currentLatLng: LatLng,
        targetLatLngString: String
    ): Boolean {
        val maxDistanceMeters = 200f
        val regex = Regex("""Lat:\s*(-?\d+(\.\d+)?),\s*Lng:\s*(-?\d+(\.\d+)?)""")
        val match = regex.find(targetLatLngString) ?: return false
        val (lat, _, lng, _) = match.destructured

        val targetLocation = Location("").apply {
            latitude = lat.toDouble()
            longitude = lng.toDouble()
        }

        val currentLocation = Location("").apply {
            latitude = currentLatLng.latitude
            longitude = currentLatLng.longitude
        }

        val distance = currentLocation.distanceTo(targetLocation)
        return distance <= maxDistanceMeters
    }

    fun hasLocationPermission(): Boolean {
        val fine = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarse = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return fine || coarse
    }

    suspend fun getFreshLocation(): LatLng? = suspendCancellableCoroutine { cont ->
        val locationRequest = com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY

        fusedLocationClient.getCurrentLocation(locationRequest, null)
            .addOnSuccessListener { location ->
                if (location != null) {
                    cont.resume(LatLng(location.latitude, location.longitude))
                } else {
                    cont.resume(null)
                }
            }
            .addOnFailureListener {
                cont.resume(null)
            }
    }
}
