package com.taufik.prayerclock.presentation.shared

import android.graphics.Paint
import android.graphics.Path
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.taufik.prayerclock.application.util.ClockUtil
import com.taufik.prayerclock.application.util.getDateFormatted
import com.taufik.prayerclock.domain.model.ClockHand
import com.taufik.prayerclock.domain.model.PrayerTime
import com.taufik.prayerclock.presentation.theme.frameColor
import com.taufik.prayerclock.presentation.theme.gray
import com.taufik.prayerclock.presentation.theme.redOrange
import com.taufik.prayerclock.presentation.theme.white
import java.util.Calendar
import java.util.Date
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun PrayerClock(
    currentTimeMillis: () -> Long,
    prayerTimes: List<Pair<PrayerTime, String>>,
    subuhColor: Color,
    dzuhurColor: Color,
    asharColor: Color,
    maghribColor: Color,
    isyaColor: Color,
    clockHands: Set<ClockHand> = setOf(
        ClockHand.HOUR,
        ClockHand.MINUTE,
        ClockHand.SECOND
    )
) {
    val infiniteTransition = rememberInfiniteTransition(label = "infiniteTransition")

    val animatedSubuhColor by infiniteTransition.animateColor(
        initialValue = subuhColor,
        targetValue = white,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "animatedSubuhColor"
    )

    val animatedDzuhurColor by infiniteTransition.animateColor(
        initialValue = dzuhurColor,
        targetValue = white,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "animatedDzuhurColor"
    )

    val animatedAsharColor by infiniteTransition.animateColor(
        initialValue = asharColor,
        targetValue = white,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "animatedAsharColor"
    )

    val animatedMaghribColor by infiniteTransition.animateColor(
        initialValue = maghribColor,
        targetValue = white,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "animatedMaghribColor"
    )

    val animatedIsyaColor by infiniteTransition.animateColor(
        initialValue = isyaColor,
        targetValue = white,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "animatedIsyaColor"
    )

    var canvasHeight by remember {
        mutableStateOf(1.dp)
    }

    var centerOffset by remember {
        mutableStateOf(Offset.Zero)
    }

    var frameThickness by remember {
        mutableFloatStateOf(0f)
    }

    var contentRadius by remember {
        mutableFloatStateOf(0f)
    }

    var contentDiameter by remember {
        mutableFloatStateOf(0f)
    }

    val date = Date(currentTimeMillis())
    val calendar = Calendar.getInstance().apply {
        time = date
    }
    val hours = calendar[Calendar.HOUR_OF_DAY]
    val minutes = calendar[Calendar.MINUTE]
    val seconds = calendar[Calendar.SECOND]
    val meridiem = calendar[Calendar.AM_PM]

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(canvasHeight)
    ) {
        canvasHeight = size.width.toDp()
        centerOffset = size.center
        frameThickness = contentRadius * 0.0625f
        contentRadius = (size.width / 2f) - frameThickness
        contentDiameter = contentRadius * 2

        drawCircle(
            style = Stroke(
                width = frameThickness
            ),
            brush = SolidColor(frameColor),
            radius = contentRadius + frameThickness / 2f,
            center = centerOffset
        )

        drawCircle(
            brush = SolidColor(white),
            radius = contentRadius,
            center = centerOffset
        )

        drawCircle(
            color = gray,
            radius = contentDiameter * 0.02f,
            center = centerOffset
        )

        if (prayerTimes.isEmpty().not()) {
            drawPrayerTimesArea(
                currentHours = hours,
                currentMinutes = minutes,
                meridiem = meridiem,
                prayerTimes = prayerTimes,
                contentDiameter = contentDiameter,
                frameThickness = frameThickness,
                subuhColor = subuhColor,
                animatedSubuhColor = animatedSubuhColor,
                dzuhurColor = dzuhurColor,
                animatedDzuhurColor = animatedDzuhurColor,
                asharColor = asharColor,
                animatedAsharColor = animatedAsharColor,
                maghribColor = maghribColor,
                animatedMaghribColor = animatedMaghribColor,
                isyaColor = isyaColor,
                animatedIsyaColor = animatedIsyaColor
            )
        }

        drawTimeLines(
            contentDiameter = contentDiameter,
            contentRadius = contentRadius,
            centerOffset = centerOffset,
            timeLinesColor = gray
        )

        drawClockHands(
            clockHands = clockHands,
            seconds = seconds,
            minutes = minutes,
            hours = hours,
            contentRadius = contentRadius,
            centerOffset = centerOffset,
            colorForHour = gray,
            colorForMinute = gray,
            colorForSecond = redOrange
        )
    }
}

fun DrawScope.drawPrayerTimesArea(
    currentHours: Int,
    currentMinutes: Int,
    meridiem: Int,
    prayerTimes: List<Pair<PrayerTime, String>>,
    contentDiameter: Float,
    frameThickness: Float,
    subuhColor: Color,
    animatedSubuhColor: Color,
    dzuhurColor: Color,
    animatedDzuhurColor: Color,
    asharColor: Color,
    animatedAsharColor: Color,
    maghribColor: Color,
    animatedMaghribColor: Color,
    isyaColor: Color,
    animatedIsyaColor: Color
) {
    prayerTimes.forEachIndexed { index, prayerTime ->
        val prayerTimeHour = prayerTime.second.getDateFormatted(outputFormat = "HH").toInt()
        val prayerTimeMinute = prayerTime.second.getDateFormatted(outputFormat = "mm").toInt()
        val startAngle = ClockUtil.calculateAngleForHour(prayerTimeHour, prayerTimeMinute) - 90f
        val sweepAngle = when (prayerTime.first) {
            PrayerTime.ISYA, PrayerTime.DZUHUR -> {
                val nextPrayerSchedule = ClockUtil.getNextPrayerSchedule(prayerTimes, index)
                val nextPrayerTimeHour =
                    nextPrayerSchedule.second.getDateFormatted(outputFormat = "HH").toInt()
                val nextPrayerTimeMinute =
                    nextPrayerSchedule.second.getDateFormatted(outputFormat = "mm").toInt()

                (360f - (startAngle + 90)) + ClockUtil.calculateAngleForHour(
                    nextPrayerTimeHour,
                    nextPrayerTimeMinute
                )
            }

            else -> {
                val nextPrayerSchedule = ClockUtil.getNextPrayerSchedule(prayerTimes, index)
                val nextPrayerTimeHour =
                    nextPrayerSchedule.second.getDateFormatted(outputFormat = "HH").toInt()
                val nextPrayerTimeMinute =
                    nextPrayerSchedule.second.getDateFormatted(outputFormat = "mm").toInt()

                ClockUtil.calculateAngleForHour(
                    nextPrayerTimeHour,
                    nextPrayerTimeMinute
                ) - (startAngle + 90)
            }
        }

        if (prayerTime.first != PrayerTime.TERBIT) {
            val strokeWidth = contentDiameter / 8f
            val offset = when (prayerTime.first) {
                PrayerTime.DZUHUR, PrayerTime.ASHAR -> {
                    frameThickness + (strokeWidth / 2) + strokeWidth
                }

                else -> {
                    frameThickness + (strokeWidth / 2)
                }
            }
            val size = when (prayerTime.first) {
                PrayerTime.DZUHUR, PrayerTime.ASHAR -> {
                    contentDiameter - strokeWidth * 3
                }

                else -> {
                    contentDiameter - strokeWidth
                }
            }
            val currentHoursAngle = ClockUtil.calculateAngleForHour(currentHours, currentMinutes)
            val staticColor = when (prayerTime.first) {
                PrayerTime.SUBUH -> {
                    subuhColor
                }

                PrayerTime.DZUHUR -> {
                    dzuhurColor
                }

                PrayerTime.ASHAR -> {
                    asharColor
                }

                PrayerTime.MAGHRIB -> {
                    maghribColor
                }

                else -> {
                    isyaColor
                }
            }
            val animatedColor = when (prayerTime.first) {
                PrayerTime.SUBUH -> {
                    animatedSubuhColor
                }

                PrayerTime.DZUHUR -> {
                    animatedDzuhurColor
                }

                PrayerTime.ASHAR -> {
                    animatedAsharColor
                }

                PrayerTime.MAGHRIB -> {
                    animatedMaghribColor
                }

                else -> {
                    animatedIsyaColor
                }
            }
            val colorStartDegree = startAngle + 90f
            val colorEndDegree = if (prayerTime.first == PrayerTime.ISYA || prayerTime.first == PrayerTime.DZUHUR) {
                (colorStartDegree + sweepAngle) - 360f
            } else {
                colorStartDegree + sweepAngle
            }
            val color = when (prayerTime.first) {
                PrayerTime.SUBUH -> {
                    if (currentHoursAngle in colorStartDegree..colorEndDegree && meridiem == Calendar.AM) {
                        animatedColor
                    } else {
                        staticColor
                    }
                }

                PrayerTime.ASHAR, PrayerTime.MAGHRIB -> {
                    if (currentHoursAngle in colorStartDegree..colorEndDegree && meridiem == Calendar.PM) {
                        animatedColor
                    } else {
                        staticColor
                    }
                }

                else -> {
                    if (currentHoursAngle in colorStartDegree..360f && meridiem == Calendar.PM || currentHoursAngle <= colorEndDegree && meridiem == Calendar.AM) {
                        animatedColor
                    } else {
                        staticColor
                    }
                }
            }

            drawArc(
                color = color,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = Offset(
                    x = offset,
                    y = offset
                ),
                size = Size(size, size),
                style = Stroke(strokeWidth)
            )

            drawIntoCanvas {
                val path = Path().apply {
                    addArc(
                        frameThickness,
                        frameThickness,
                        contentDiameter + frameThickness,
                        contentDiameter + frameThickness,
                        startAngle,
                        sweepAngle
                    )
                }

                it.nativeCanvas.drawTextOnPath(
                    prayerTime.first.toString(),
                    path,
                    0f,
                    if (prayerTime.first == PrayerTime.DZUHUR || prayerTime.first == PrayerTime.ASHAR) {
                        strokeWidth * 2f - (strokeWidth / 3f)
                    } else {
                        strokeWidth - 10f
                    },
                    Paint().apply {
                        this.textSize = when (prayerTime.first) {
                            PrayerTime.SUBUH, PrayerTime.MAGHRIB -> {
                                ((strokeWidth / 7) / 1.2).toInt().sp.toPx()
                            }

                            else -> {
                                (strokeWidth / 7).toInt().sp.toPx()
                            }
                        }
                        this.textAlign = Paint.Align.CENTER
                        this.color = white.toArgb()
                        this.isFakeBoldText = true
                        this.letterSpacing =
                            if (prayerTime.first == PrayerTime.DZUHUR || prayerTime.first == PrayerTime.ASHAR) {
                                0.50f
                            } else {
                                0.25f
                            }
                    }
                )
            }
        }
    }
}

fun DrawScope.drawTimeLines(
    contentDiameter: Float,
    contentRadius: Float,
    centerOffset: Offset,
    timeLinesColor: Color
) {
    val hourLineLength = contentDiameter / 16f
    val minuteLineLength = hourLineLength / 2f

    for (i in 1..60) {
        val angleInDegrees = ClockUtil.calculateAngleForSecond(i)
        val angleInRadius = angleInDegrees * PI / 180f + PI / 2f
        val lineLength = if (i % 5 == 0) hourLineLength else minuteLineLength
        val lineThickness = if (i % 5 == 0) 5f else 2.5f

        val start = Offset(
            x = (contentRadius * cos(angleInRadius) + centerOffset.x).toFloat(),
            y = (contentRadius * sin(angleInRadius) + centerOffset.y).toFloat()
        )

        val end = Offset(
            x = (contentRadius * cos(angleInRadius) + centerOffset.x).toFloat(),
            y = (contentRadius * sin(angleInRadius) + lineLength + centerOffset.y).toFloat()
        )

        rotate(
            angleInDegrees + 180,
            pivot = start
        ) {
            drawLine(
                color = timeLinesColor,
                start = start,
                end = end,
                strokeWidth = lineThickness.dp.toPx()
            )
        }
    }
}

fun DrawScope.drawClockHands(
    clockHands: Set<ClockHand>,
    seconds: Int,
    minutes: Int,
    hours: Int,
    contentRadius: Float,
    centerOffset: Offset,
    colorForHour: Color,
    colorForMinute: Color,
    colorForSecond: Color
) {
    clockHands.forEach { clockHand ->
        val angleInDegrees = when (clockHand) {
            ClockHand.SECOND -> {
                ClockUtil.calculateAngleForSecond(seconds)
            }

            ClockHand.MINUTE -> {
                ClockUtil.calculateAngleForMinute(minutes, seconds)
            }

            ClockHand.HOUR -> {
                ClockUtil.calculateAngleForHour(hours, minutes)
            }
        }

        val lineLength = when (clockHand) {
            ClockHand.SECOND -> {
                contentRadius - ((contentRadius * 2) / 8f)
            }

            ClockHand.MINUTE -> {
                contentRadius - ((contentRadius * 2) / 8f)
            }

            ClockHand.HOUR -> {
                contentRadius - ((contentRadius * 2) / 8f) * 2
            }
        }
        val lineThickness = when (clockHand) {
            ClockHand.SECOND -> {
                2.5f
            }

            ClockHand.MINUTE -> {
                2.5f
            }

            ClockHand.HOUR -> {
                5f
            }
        }
        val start = Offset(
            x = centerOffset.x,
            y = centerOffset.y
        )

        val end = Offset(
            x = centerOffset.x,
            y = lineLength + centerOffset.y
        )

        rotate(
            angleInDegrees - 180,
            pivot = start
        ) {
            drawLine(
                color = when (clockHand) {
                    ClockHand.HOUR -> colorForHour
                    ClockHand.MINUTE -> colorForMinute
                    else -> colorForSecond
                },
                start = start,
                end = end,
                strokeWidth = lineThickness.dp.toPx()
            )
        }

        if (clockHand == ClockHand.SECOND) {
            drawCircle(
                color = colorForSecond,
                radius = contentRadius * 2 * 0.01f,
                center = centerOffset
            )
        }
    }
}