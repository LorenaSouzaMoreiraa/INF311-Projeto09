package com.inf311_projeto09.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.inf311_projeto09.model.Event
import com.inf311_projeto09.ui.utils.AppColors
import com.inf311_projeto09.ui.utils.AppDateHelper
import com.inf311_projeto09.ui.utils.AppFonts
import com.inf311_projeto09.ui.utils.AppIcons
import kotlinx.coroutines.delay
import java.util.Date
import kotlin.math.roundToInt

@Composable
fun CheckRoomScreen(
    onBack: () -> Unit = {},
    event: Event,
    navController: NavHostController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors().darkGreen)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 50.dp, horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Header(onBack)

            Spacer(Modifier.height(30.dp))

            TimerRing(event)

            Spacer(Modifier.height(30.dp))

            EventDetails(event)

            Spacer(Modifier.weight(1f))

            SwipeToConfirmButton(navController)
        }
    }
}

@Composable
fun Header(
    onBack: () -> Unit = {}
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier.size(30.dp)
        ) {
            AppIcons.Filled.CircleClose(
                boxSize = 30.dp,
                colorIcon = AppColors().lightGreen,
                backgroundColorIcon = AppColors().darkGreen
            )
        }

        Text(
            text = "Check-out",
            fontFamily = AppFonts().montserrat,
            fontWeight = FontWeight.SemiBold,
            color = AppColors().white,
            fontSize = 26.sp,
            modifier = Modifier
                .align(Alignment.TopCenter)
        )
    }
}

@Composable
fun TimerRing(
    event: Event
) {
    val participationSeconds = remember { mutableLongStateOf(0L) }
    val progressValue = remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            val totalDurationMillis = (event.endTime.time - event.beginTime.time) / 1000

            val now = Date()
            val checkIn = event.checkInTime

            val diffMillis = now.time - checkIn.time
            participationSeconds.longValue = diffMillis / 1000

            val progress = participationSeconds.longValue.toFloat() / totalDurationMillis
            progressValue.floatValue = progress.coerceIn(0f, 1f)

            delay(1000)
        }
    }

    val formattedTime = remember(participationSeconds.longValue) {
        val total = participationSeconds.longValue
        val hours = total / 3600
        val minutes = (total % 3600) / 60
        val seconds = total % 60

        if (hours > 0)
            "%02d:%02d:%02d".format(hours, minutes, seconds)
        else
            "%02d:%02d".format(minutes, seconds)
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .fillMaxWidth()
            .padding(40.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 15.dp.toPx()

            drawArc(
                color = AppColors().grey,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth)
            )

            drawArc(
                color = AppColors().lightGreen,
                startAngle = -90f,
                sweepAngle = 360 * progressValue.floatValue,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = formattedTime,
                fontFamily = AppFonts().montserrat,
                fontWeight = FontWeight.SemiBold,
                color = AppColors().darkGrey,
                fontSize = 40.sp
            )

            Spacer(Modifier.height(10.dp))

            Text(
                text = "de participação",
                fontFamily = AppFonts().montserrat,
                fontWeight = FontWeight.Medium,
                color = AppColors().darkGrey,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun EventDetails(
    event: Event
) {
    val currentTime = remember { mutableStateOf(AppDateHelper().getCurrentTimeWithSeconds()) }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime.value = AppDateHelper().getCurrentTimeWithSeconds()
            delay(1000)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Evento:",
            fontFamily = AppFonts().montserrat,
            fontWeight = FontWeight.SemiBold,
            color = AppColors().darkGrey,
            fontSize = 16.sp
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = event.title,
            fontFamily = AppFonts().montserrat,
            fontWeight = FontWeight.SemiBold,
            color = AppColors().white,
            fontSize = 16.sp
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Data:",
            fontFamily = AppFonts().montserrat,
            fontWeight = FontWeight.SemiBold,
            color = AppColors().darkGrey,
            fontSize = 16.sp
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = AppDateHelper().getFullFormattedDate(),
            fontFamily = AppFonts().montserrat,
            fontWeight = FontWeight.SemiBold,
            color = AppColors().white,
            fontSize = 16.sp
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Horário:",
            fontFamily = AppFonts().montserrat,
            fontWeight = FontWeight.SemiBold,
            color = AppColors().darkGrey,
            fontSize = 16.sp
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = "${currentTime.value}\nBrasil (UTC-3), Brasília",
            fontFamily = AppFonts().montserrat,
            fontWeight = FontWeight.SemiBold,
            color = AppColors().white,
            fontSize = 16.sp
        )
    }
}

@Composable
fun SwipeToConfirmButton(
    navController: NavHostController
) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    val buttonHeight: Dp = 50.dp
    val thumbSize: Dp = buttonHeight

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(buttonHeight)
            .padding(horizontal = 40.dp)
            .border(width = 2.dp, color = AppColors().lightGreen, shape = CircleShape)
            .clip(CircleShape)
    ) {
        val boxWidth = constraints.maxWidth.toFloat()
        val thumbWidth = with(LocalDensity.current) { thumbSize.toPx() }
        val maxDragX = boxWidth - thumbWidth

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(with(LocalDensity.current) { (offsetX + (thumbWidth / 2)).toDp() })
                .background(AppColors().lightGreen.copy(alpha = 0.4f))
        )

        Text(
            text = "Arraste para confirmar",
            color = AppColors().white,
            fontSize = 16.sp,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 60.dp)
        )

        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), 0) }
                .size(thumbSize)
                .background(color = AppColors().lightGreen, shape = CircleShape)
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        offsetX = (offsetX + delta).coerceIn(0f, maxDragX)
                    },
                    onDragStopped = {
                        if (offsetX >= maxDragX * 0.95f) {
                            navController.previousBackStackEntry?.savedStateHandle?.set(
                                "checkOutConfirm",
                                true
                            )
                            navController.popBackStack()
                        } else {
                            offsetX = 0f
                        }
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            AppIcons.Outline.ArrowRight(
                boxSize = 24.dp,
                colorIcon = AppColors().black
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A3818)
@Composable
fun CheckRoomScreenPreview() {
    CheckRoomScreen(
        event = Event(
            1,
            "INF 311 - Programação Dispositivos móveis",
            "Aula prática de Kotlin",
            "Online",
            Event.EventVerificationMethod.QR_CODE,
            "ABC123",
            false,
            "Localização",
            AppDateHelper().getDate(2025, 2, 25),
            AppDateHelper().getDate(2025, 2, 25),
            null,
            null,
            null,
            null,
            Event.EventStage.CURRENT,
            listOf()
        ), navController = rememberNavController()
    )
}