package com.taufik.prayerclock.presentation.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.glance.appwidget.updateAll
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.taufik.prayerclock.domain.model.PrayerTime
import com.taufik.prayerclock.presentation.screen.main.component.PrayerTimeGrid
import com.taufik.prayerclock.presentation.shared.BezierCurveStyle
import com.taufik.prayerclock.presentation.shared.PrayerClock
import com.taufik.prayerclock.presentation.shared.drawBezierCurve
import com.taufik.prayerclock.presentation.widget.PrayerClockWidget
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE) {
                scope.launch {
                    PrayerClockWidget().updateAll(context)
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    val subuhColorPickerController = rememberColorPickerController()
    subuhColorPickerController.setWheelColor(Color.Black)
    LaunchedEffect(subuhColorPickerController.selectedColor.value) {
        viewModel.updateColor(PrayerTime.SUBUH, subuhColorPickerController.selectedColor.value)
    }
    val subuhSheetState = rememberModalBottomSheetState()
    var showSubuhBottomSheet by remember { mutableStateOf(false) }

    val dzuhurColorPickerController = rememberColorPickerController()
    dzuhurColorPickerController.setWheelColor(Color.Black)
    LaunchedEffect(dzuhurColorPickerController.selectedColor.value) {
        viewModel.updateColor(PrayerTime.DZUHUR, dzuhurColorPickerController.selectedColor.value)
    }
    val dzuhurSheetState = rememberModalBottomSheetState()
    var showDzuhurBottomSheet by remember { mutableStateOf(false) }

    val asharColorPickerController = rememberColorPickerController()
    asharColorPickerController.setWheelColor(Color.Black)
    LaunchedEffect(asharColorPickerController.selectedColor.value) {
        viewModel.updateColor(PrayerTime.ASHAR, asharColorPickerController.selectedColor.value)
    }
    val asharSheetState = rememberModalBottomSheetState()
    var showAsharBottomSheet by remember { mutableStateOf(false) }

    val maghribColorPickerController = rememberColorPickerController()
    maghribColorPickerController.setWheelColor(Color.Black)
    LaunchedEffect(maghribColorPickerController.selectedColor.value) {
        viewModel.updateColor(PrayerTime.MAGHRIB, maghribColorPickerController.selectedColor.value)
    }
    val maghribSheetState = rememberModalBottomSheetState()
    var showMaghribBottomSheet by remember { mutableStateOf(false) }

    val isyaColorPickerController = rememberColorPickerController()
    isyaColorPickerController.setWheelColor(Color.Black)
    LaunchedEffect(isyaColorPickerController.selectedColor.value) {
        viewModel.updateColor(PrayerTime.ISYA, isyaColorPickerController.selectedColor.value)
    }
    val isyaSheetState = rememberModalBottomSheetState()
    var showIsyaBottomSheet by remember { mutableStateOf(false) }

    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val bgColor = MaterialTheme.colorScheme.primary

    val prayerTimesGridData by remember {
        derivedStateOf {
            state.prayerTimes.filter { it.first != PrayerTime.TERBIT }
        }
    }

    Scaffold(
        modifier = Modifier.background(color = MaterialTheme.colorScheme.onPrimary),
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Reset") },
                icon = { Icon(Icons.Filled.Refresh, contentDescription = "") },
                onClick = {
                    viewModel.resetColor()
                }
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .onSizeChanged {
                    size = it
                }
                .drawBehind {
                    if (size != IntSize.Zero) {
                        rotate(180f) {
                            drawBezierCurve(
                                size = size,
                                points = listOf(45F, 50F, 55F),
                                fixedMinPoint = 0f,
                                fixedMaxPoint = 100f,
                                style = BezierCurveStyle.Fill(brush = SolidColor(bgColor)),
                            )
                        }
                    }
                }
        ) {
            Box(
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                PrayerClock(
                    currentTimeMillis = { state.currentTimeMillis },
                    prayerTimes = state.prayerTimes,
                    subuhColor = state.subuhColor,
                    dzuhurColor = state.dzuhurColor,
                    asharColor = state.asharColor,
                    maghribColor = state.maghribColor,
                    isyaColor = state.isyaColor
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            PrayerTimeGrid(
                modifier = Modifier.fillMaxSize(),
                columnsCount = 2,
                prayerTimes = prayerTimesGridData,
                onClickChangeColor = {
                    when (it) {
                        PrayerTime.SUBUH -> showSubuhBottomSheet = true
                        PrayerTime.DZUHUR -> showDzuhurBottomSheet = true
                        PrayerTime.ASHAR -> showAsharBottomSheet = true
                        PrayerTime.MAGHRIB -> showMaghribBottomSheet = true
                        PrayerTime.ISYA -> showIsyaBottomSheet = true
                        PrayerTime.TERBIT -> Unit
                    }
                }
            )
        }

        if (showSubuhBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showSubuhBottomSheet = false
                },
                sheetState = subuhSheetState
            ) {
                HsvColorPicker(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    controller = subuhColorPickerController,
                    initialColor = state.subuhColor
                )

                Spacer(modifier = Modifier.height(96.dp))
            }
        }

        if (showDzuhurBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showDzuhurBottomSheet = false
                },
                sheetState = dzuhurSheetState
            ) {
                HsvColorPicker(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    controller = dzuhurColorPickerController,
                    initialColor = state.dzuhurColor
                )

                Spacer(modifier = Modifier.height(96.dp))
            }
        }

        if (showAsharBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showAsharBottomSheet = false
                },
                sheetState = asharSheetState
            ) {
                HsvColorPicker(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    controller = asharColorPickerController,
                    initialColor = state.asharColor
                )

                Spacer(modifier = Modifier.height(96.dp))
            }
        }

        if (showMaghribBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showMaghribBottomSheet = false
                },
                sheetState = maghribSheetState
            ) {
                HsvColorPicker(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    controller = maghribColorPickerController,
                    initialColor = state.maghribColor
                )

                Spacer(modifier = Modifier.height(96.dp))
            }
        }

        if (showIsyaBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showIsyaBottomSheet = false
                },
                sheetState = isyaSheetState
            ) {
                HsvColorPicker(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    controller = isyaColorPickerController,
                    initialColor = state.isyaColor
                )

                Spacer(modifier = Modifier.height(96.dp))
            }
        }
    }
}