package com.taufik.prayerclock.presentation.activity.main

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.taufik.prayerclock.R
import com.taufik.prayerclock.application.util.DateTimeUtil
import com.taufik.prayerclock.application.util.GeocoderUtil
import com.taufik.prayerclock.application.util.openAppSettings
import com.taufik.prayerclock.data.worker.DailyPrayerSchedulePrefUpdaterWorker
import com.taufik.prayerclock.data.worker.MonthlyPrayerScheduleSynchronizationWorker
import com.taufik.prayerclock.presentation.screen.main.MainScreen
import com.taufik.prayerclock.presentation.theme.PrayerClockTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val fusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(
            this
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PrayerClockTheme {
                val viewModel: MainActivityViewModel = hiltViewModel()
                val state by viewModel.state.collectAsStateWithLifecycle()
                LaunchedEffect(state.currentRegencyCity) {
                    if (state.currentRegencyCity.isNotBlank()) {
                        viewModel.getMonthlySchedules(state.currentRegencyCity)
                    }
                }
                LaunchedEffect(state.lastRegencyCity) {
                    if (state.lastRegencyCity.isNotBlank()) {
                        val workManager = WorkManager.getInstance(this@MainActivity)

                        val monthlyPrayerScheduleSynchronizationWorkerRequest =
                            OneTimeWorkRequestBuilder<MonthlyPrayerScheduleSynchronizationWorker>()
                                .setInitialDelay(
                                    DateTimeUtil.getTimeDiffMillisOf(1),
                                    TimeUnit.MILLISECONDS
                                )
                                .setConstraints(Constraints(requiredNetworkType = NetworkType.CONNECTED))
                                .addTag(MonthlyPrayerScheduleSynchronizationWorker.NAME_TAG)
                                .build()

                        val dailyPrayerSchedulePrefUpdaterWorker =
                            OneTimeWorkRequestBuilder<DailyPrayerSchedulePrefUpdaterWorker>()
                                .setInitialDelay(
                                    DateTimeUtil.getTimeDiffMillisOf(1),
                                    TimeUnit.MILLISECONDS
                                )
                                .addTag(DailyPrayerSchedulePrefUpdaterWorker.NAME_TAG)
                                .build()

                        workManager.enqueueUniqueWork(
                            MonthlyPrayerScheduleSynchronizationWorker.NAME_TAG,
                            ExistingWorkPolicy.KEEP,
                            monthlyPrayerScheduleSynchronizationWorkerRequest
                        )

                        workManager.enqueueUniqueWork(
                            DailyPrayerSchedulePrefUpdaterWorker.NAME_TAG,
                            ExistingWorkPolicy.KEEP,
                            dailyPrayerSchedulePrefUpdaterWorker
                        )
                    }
                }
                Content(
                    locationRationaleHasBeenShow = state.locationRationaleHasBeenShow,
                    onShouldShowRationale = {
                        viewModel.markRationaleHasBeenShow()
                    },
                    onOpenAppSettings = {
                        openAppSettings()
                    },
                    fusedLocationProviderClient = fusedLocationProviderClient,
                    onLastLocationChange = {
                        viewModel.getLocationAddress(GeocoderUtil.getDefaultGeoCoder(this), it)
                    }
                )
            }
        }
    }
}

@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun Content(
    locationRationaleHasBeenShow: Boolean,
    onShouldShowRationale: () -> Unit,
    onOpenAppSettings: () -> Unit,
    fusedLocationProviderClient: FusedLocationProviderClient,
    onLastLocationChange: (Location) -> Unit
) {
    val context = LocalContext.current
    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    if (locationPermissionsState.allPermissionsGranted) {
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            onLastLocationChange(location)
        }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            MainScreen()
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val allPermissionsRevoked =
                locationPermissionsState.permissions.size ==
                        locationPermissionsState.revokedPermissions.size

            val textToShow = if (!allPermissionsRevoked) {
                // If not all the permissions are revoked, it's because the user accepted the COARSE
                // location permission, but not the FINE one.
                if (locationPermissionsState.shouldShowRationale && !locationRationaleHasBeenShow) {
                    onShouldShowRationale()
                }
                context.getString(R.string.fine_location_revoked_rationale)
            } else if (locationPermissionsState.shouldShowRationale) {
                // Both location permissions have been denied
                if (!locationRationaleHasBeenShow) {
                    onShouldShowRationale()
                }
                context.getString(R.string.fine_and_coarse_location_revoked_rationale)
            } else {
                // First time the user sees this feature or the user doesn't want to be asked again
                context.getString(R.string.initial_location_rationale)
            }

            val buttonText = if (!allPermissionsRevoked) {
                if (locationPermissionsState.shouldShowRationale.not() && locationRationaleHasBeenShow) {
                    context.getString(R.string.open_app_settings_button_text)
                } else {
                    context.getString(R.string.fine_location_revoked_button_text)
                }
            } else {
                if (locationPermissionsState.shouldShowRationale.not() && locationRationaleHasBeenShow) {
                    context.getString(R.string.open_app_settings_button_text)
                } else {
                    context.getString(R.string.initial_request_permissions_button_text)
                }
            }

            Text(text = textToShow, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                if (locationPermissionsState.shouldShowRationale.not() && locationRationaleHasBeenShow) {
                    onOpenAppSettings()
                } else {
                    locationPermissionsState.launchMultiplePermissionRequest()
                }
            }) {
                Text(buttonText)
            }
        }
    }
}