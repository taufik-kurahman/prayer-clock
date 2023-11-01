package com.taufik.prayerclock.presentation.widget

import android.content.Context
import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxHeight
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.taufik.prayerclock.R
import com.taufik.prayerclock.application.util.ClockUtil
import com.taufik.prayerclock.data.repository.UserPreferencesRepository
import com.taufik.prayerclock.data.worker.WidgetUpdaterWorker
import com.taufik.prayerclock.domain.model.PrayerTime
import com.taufik.prayerclock.domain.model.UserPreferences
import com.taufik.prayerclock.presentation.activity.main.MainActivity
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.delay
import java.util.Date

class PrayerClockWidget : GlanceAppWidget() {
    @EntryPoint
    @InstallIn(SingletonComponent::class)
    fun interface PrayerClockWidgetEntryPoint {
        fun userPreferencesRepository(): UserPreferencesRepository
    }

    private lateinit var userPreferencesRepository: UserPreferencesRepository

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val hiltEntryPoint = EntryPointAccessors.fromApplication(
            context,
            PrayerClockWidgetEntryPoint::class.java
        )

        userPreferencesRepository = hiltEntryPoint.userPreferencesRepository()

        provideContent {
            LaunchedEffect(Unit) {
                val widgetUpdaterWorkRequest = OneTimeWorkRequestBuilder<WidgetUpdaterWorker>()
                    .addTag(WidgetUpdaterWorker.NAME_TAG)
                    .build()

                WorkManager.getInstance(context).enqueueUniqueWork(
                    WidgetUpdaterWorker.NAME_TAG,
                    ExistingWorkPolicy.REPLACE,
                    widgetUpdaterWorkRequest
                )
            }

            WidgetContent()
        }
    }

    override suspend fun onDelete(context: Context, glanceId: GlanceId) {
        super.onDelete(context, glanceId)
        WorkManager.getInstance(context).cancelUniqueWork(WidgetUpdaterWorker.NAME_TAG)
    }

    @Composable
    private fun WidgetContent() {
        val userPreferences by userPreferencesRepository.userPreferences.collectAsState(
            UserPreferences.empty()
        )

        val colors by remember {
            derivedStateOf {
                listOf(
                    Pair(
                        PrayerTime.SUBUH,
                        Color(
                            userPreferences.subuhColorR,
                            userPreferences.subuhColorG,
                            userPreferences.subuhColorB
                        )
                    ),
                    Pair(
                        PrayerTime.DZUHUR,
                        Color(
                            userPreferences.dzuhurColorR,
                            userPreferences.dzuhurColorG,
                            userPreferences.dzuhurColorB
                        )
                    ),
                    Pair(
                        PrayerTime.ASHAR,
                        Color(
                            userPreferences.asharColorR,
                            userPreferences.asharColorG,
                            userPreferences.asharColorB
                        )
                    ),
                    Pair(
                        PrayerTime.MAGHRIB,
                        Color(
                            userPreferences.maghribColorR,
                            userPreferences.maghribColorG,
                            userPreferences.maghribColorB
                        )
                    ),
                    Pair(
                        PrayerTime.ISYA,
                        Color(
                            userPreferences.isyaColorR,
                            userPreferences.isyaColorG,
                            userPreferences.isyaColorB
                        )
                    )
                )
            }
        }

        val prayerTimes by remember {
            derivedStateOf {
                listOf(
                    Pair(PrayerTime.SUBUH, userPreferences.subuhTime),
                    Pair(PrayerTime.TERBIT, userPreferences.terbitTime),
                    Pair(PrayerTime.DZUHUR, userPreferences.dzuhurTime),
                    Pair(PrayerTime.ASHAR, userPreferences.asharTime),
                    Pair(PrayerTime.MAGHRIB, userPreferences.maghribTime),
                    Pair(PrayerTime.ISYA, userPreferences.isyaTime)
                )
            }
        }

        var currentTimeMillis by remember {
            mutableLongStateOf(System.currentTimeMillis())
        }

        val currentJavaDate by remember {
            derivedStateOf {
                Date(currentTimeMillis)
            }
        }

        val currentTime by remember {
            derivedStateOf {
                ClockUtil.getFormattedString(currentJavaDate, "HH:mm")
            }
        }

        val currentDate by remember {
            derivedStateOf {
                ClockUtil.getFormattedString(currentJavaDate, "EEEE, dd MMM yyyy")
            }
        }

        LaunchedEffect(Unit) {
            while (true) {
                delay(1000L)
                currentTimeMillis = System.currentTimeMillis()
            }
        }

        Column(
            modifier = GlanceModifier.width(340.dp)
                .clickable(actionStartActivity<MainActivity>())
        ) {
            Row(
                modifier = GlanceModifier.fillMaxWidth().height(50.dp)
            ) {
                Box(
                    modifier = GlanceModifier.width(85.dp).fillMaxHeight(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = currentTime,
                        style = TextStyle(
                            color = ColorProvider(MaterialTheme.colorScheme.onPrimary),
                            fontWeight = FontWeight.Bold,
                            fontSize = TextUnit(32f, TextUnitType.Sp)
                        )
                    )
                }
                Column(
                    modifier = GlanceModifier.width(255.dp).fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalAlignment = Alignment.End
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            provider = ImageProvider(R.drawable.baseline_location_on_24),
                            contentDescription = null,
                            modifier = GlanceModifier.size(10.dp)
                        )
                        Spacer(modifier = GlanceModifier.width(4.dp))
                        Text(
                            text = userPreferences.currentRegencyCity,
                            style = TextStyle(
                                color = ColorProvider(MaterialTheme.colorScheme.onPrimary),
                                fontWeight = FontWeight.Bold,
                                fontSize = TextUnit(10f, TextUnitType.Sp)
                            )
                        )
                    }
                    Text(
                        text = currentDate,
                        style = TextStyle(
                            color = ColorProvider(MaterialTheme.colorScheme.onPrimary),
                            fontWeight = FontWeight.Bold,
                            fontSize = TextUnit(14f, TextUnitType.Sp)
                        )
                    )
                }
            }
            val prayerTimeItemsModifier = remember {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    GlanceModifier.width(340.dp)
                        .cornerRadius(16.dp)
                } else {
                    GlanceModifier.width(340.dp)
                        .background(ImageProvider(R.drawable.bg_widget))
                }
            }
            Row(
                modifier = prayerTimeItemsModifier
            ) {
                prayerTimes.forEach { prayerTime ->
                    PrayerTimeItem(
                        prayerTime = prayerTime,
                        color = colors.find { it.first == prayerTime.first }?.second
                    )
                }
            }
        }
    }

    @Composable
    private fun PrayerTimeItem(
        prayerTime: Pair<PrayerTime, String>,
        color: Color?
    ) {
        if (prayerTime.first != PrayerTime.TERBIT) {
            Column(
                modifier = GlanceModifier.width(68.dp)
                    .background(color ?: MaterialTheme.colorScheme.primary)
                    .padding(vertical = 9.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = prayerTime.first.toString(),
                    style = TextStyle(
                        color = ColorProvider(MaterialTheme.colorScheme.onPrimary),
                        fontWeight = FontWeight.Bold,
                        fontSize = TextUnit(8f, TextUnitType.Sp)
                    )
                )
                Text(
                    text = prayerTime.second,
                    style = TextStyle(color = ColorProvider(MaterialTheme.colorScheme.onPrimary))
                )
            }
        }
    }
}
