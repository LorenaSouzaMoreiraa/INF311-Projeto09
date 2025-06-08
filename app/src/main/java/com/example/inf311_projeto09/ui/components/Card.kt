package com.example.inf311_projeto09.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inf311_projeto09.model.Event
import com.example.inf311_projeto09.ui.utils.AppColors
import com.example.inf311_projeto09.ui.utils.AppDateFormatter
import com.example.inf311_projeto09.ui.utils.AppFonts
import com.example.inf311_projeto09.ui.utils.AppIcons
import kotlinx.coroutines.delay
import java.util.Date
import java.util.Locale

@Composable
fun EventCard(event: Event, isCurrentEvent: Boolean, modifier: Modifier = Modifier) {
    val backgroundCardColor = if (isCurrentEvent) AppColors().darkGreen else AppColors().transparent
    val titleColor = if (isCurrentEvent) AppColors().white else AppColors().darkGreen
    val subtitleColor = if (isCurrentEvent) AppColors().lightGrey else AppColors().grey

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                BorderStroke(
                    width = 1.dp,
                    color = AppColors().darkGreen
                ),
                shape = RoundedCornerShape(15.dp)
            ),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundCardColor)
    ) {
        Column(
            modifier = Modifier.padding(
                start = 15.dp,
                end = 15.dp,
                top = 15.dp,
                bottom = 10.dp
            )
        ) {
            Text(
                text = event.title,
                fontFamily = AppFonts().montserrat,
                fontWeight = FontWeight.SemiBold,
                color = titleColor,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "${
                    AppDateFormatter().getTimeFormatted(event.beginTime)
                } - ${
                    AppDateFormatter().getTimeFormatted(event.endTime)
                } | ${event.type}",
                fontFamily = AppFonts().montserrat,
                fontWeight = FontWeight.Normal,
                color = subtitleColor,
                fontSize = 10.sp
            )

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                EventActionButton(
                    label = "Check-in",
                    eventTime = event.checkInEnable,
                    checkTime = event.checkInTime,
                    isEnabled = event.checkInEnable != null,
                    isCurrentEvent = isCurrentEvent,
                    modifier = Modifier.weight(1f)
                ) {}

                EventActionButton(
                    label = "Check-out",
                    eventTime = event.checkOutEnable,
                    checkTime = event.checkOutTime,
                    isEnabled = event.checkOutEnable != null,
                    isCurrentEvent = isCurrentEvent,
                    modifier = Modifier.weight(1f)
                ) {}
            }

            Spacer(modifier = Modifier.height(10.dp))

            val progressPercent = getEventProgressPercentage(event.beginTime, event.endTime)

            LinearProgressIndicator(
                progress = { progressPercent },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(50)),
                color = AppColors().lightGreen,
                trackColor = subtitleColor,
                strokeCap = StrokeCap.Butt,
                gapSize = 0.dp,
                drawStopIndicator = {}
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "${(progressPercent * 100).toInt()}%",
                fontFamily = AppFonts().montserrat,
                fontWeight = FontWeight.Medium,
                color = subtitleColor,
                fontSize = 8.sp,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

@Composable
fun getEventProgressPercentage(beginTime: Date, endTime: Date): Float {
    val now = rememberUpdatedState(newValue = System.currentTimeMillis())

    val totalDuration = endTime.time - beginTime.time
    val elapsed = now.value - beginTime.time

    return when {
        now.value < beginTime.time -> 0f
        now.value > endTime.time -> 1f
        else -> elapsed.toFloat() / totalDuration
    }.coerceIn(0f, 1f)
}

@Composable
private fun EventActionButton(
    label: String,
    eventTime: Date?,
    checkTime: Date?,
    isEnabled: Boolean,
    isCurrentEvent: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val currentBorderColor = when {
        checkTime != null && isCurrentEvent -> AppColors().lightGreen
        isCurrentEvent -> AppColors().white
        else -> AppColors().darkGreen
    }

    val currentBackgroundColor = when {
        checkTime != null && isCurrentEvent -> AppColors().transparent
        isCurrentEvent -> AppColors().greyTransparent
        else -> AppColors().transparent
    }

    val titleColor = if (isCurrentEvent) AppColors().white else AppColors().darkGreen
    val subtitleColor = if (isCurrentEvent) AppColors().lightGrey else AppColors().grey

    Column(
        modifier = modifier
            .border(
                BorderStroke(1.dp, currentBorderColor),
                RoundedCornerShape(5.dp)
            )
            .background(
                currentBackgroundColor,
                RoundedCornerShape(5.dp)
            )
            .clickable { onClick() }
            .padding(10.dp)
            .height(IntrinsicSize.Min)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isCurrentEvent && checkTime != null) {
                AppIcons.Filled.CircleCheck(20.dp, AppColors().green, AppColors().darkGreen)
            } else {
                AppIcons.Outline.CircleCheck(20.dp, titleColor)
            }

            Text(
                text = label,
                fontFamily = AppFonts().montserrat,
                fontWeight = FontWeight.SemiBold,
                color = titleColor,
                fontSize = 10.sp,
                modifier = Modifier.padding(start = 5.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            EventActionStatus(isEnabled, eventTime, checkTime, titleColor, subtitleColor)
        }
    }
}

@Composable
private fun EventActionStatus(
    isEnabled: Boolean,
    eventTime: Date?,
    checkTime: Date?,
    titleColor: Color,
    subtitleColor: Color
) {
    if (isEnabled && eventTime != null && checkTime == null) {
        EventActionCountdownStatus(eventTime, titleColor, subtitleColor)
    } else {
        EventActionNormalStatus(eventTime, checkTime, titleColor, subtitleColor)
    }
}

@Composable
private fun EventActionNormalStatus(
    eventTime: Date?,
    checkTime: Date?,
    titleColor: Color,
    subtitleColor: Color
) {
    val statusAction = if (eventTime == null || checkTime == null) "aguarde"
    else if (checkTime.time - eventTime.time > 10 * 60 * 1000L) "atrasado"
    else "no horário"

    Text(
        text = AppDateFormatter().getTimeFormattedWithSeconds(checkTime),
        fontFamily = AppFonts().montserrat,
        fontWeight = FontWeight.Medium,
        color = titleColor,
        fontSize = 16.sp
    )

    VerticalDivider(
        color = subtitleColor,
        thickness = 0.5.dp,
        modifier = Modifier
            .fillMaxHeight()
            .padding(vertical = 5.dp, horizontal = 5.dp),
    )

    Text(
        text = statusAction,
        fontFamily = AppFonts().montserrat,
        fontWeight = FontWeight.Medium,
        color = subtitleColor,
        fontSize = 10.sp
    )
}

@Composable
private fun EventActionCountdownStatus(
    eventTime: Date,
    titleColor: Color,
    subtitleColor: Color
) {
    var remainingSeconds by remember { mutableLongStateOf(0L) }
    val eventStarted = Date() > eventTime

    LaunchedEffect(eventTime) {
        while (true) {
            val now = System.currentTimeMillis()
            val eventStartMillis = eventTime.time

            val diffSeconds = if (eventStarted) (now - eventStartMillis) / 1000
            else (eventStartMillis - now) / 1000

            if (diffSeconds > 0) {
                remainingSeconds = diffSeconds
            } else {
                remainingSeconds = 0L
                break
            }

            delay(1000)
        }
    }

    val hours = remainingSeconds / 3600
    val minutes = (remainingSeconds % 3600) / 60
    val seconds = remainingSeconds % 60

    val formattedTime = String.format(Locale("pt", "BR"), "%02d:%02d:%02d", hours, minutes, seconds)

    Text(
        text = if (eventStarted) "iniciou há: " else "inicia em: ",
        fontFamily = AppFonts().montserrat,
        fontWeight = FontWeight.Medium,
        color = subtitleColor,
        fontSize = 10.sp
    )

    Text(
        text = formattedTime,
        fontFamily = AppFonts().montserrat,
        fontWeight = FontWeight.Medium,
        color = titleColor,
        fontSize = 16.sp
    )
}

@Composable
fun EmptyEventCard(message: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .drawBehind {
                val cornerRadiusPx = 15.dp.toPx()
                val borderWidthPx = 1.dp.toPx()

                drawRoundRect(
                    color = AppColors().darkGreen,
                    topLeft = Offset(borderWidthPx / 2, borderWidthPx / 2),
                    size = Size(size.width - borderWidthPx, size.height - borderWidthPx),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(
                        cornerRadiusPx,
                        cornerRadiusPx
                    ),
                    style = Stroke(
                        width = borderWidthPx,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 0f)
                    )
                )
            },
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors().transparent)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = message,
                fontFamily = AppFonts().montserrat,
                fontWeight = FontWeight.SemiBold,
                color = AppColors().grey,
                fontSize = 10.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
