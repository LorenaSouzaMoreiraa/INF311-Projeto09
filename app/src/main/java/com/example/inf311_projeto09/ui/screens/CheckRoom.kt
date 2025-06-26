package com.example.inf311_projeto09.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.inf311_projeto09.ui.utils.AppColors
import com.example.inf311_projeto09.ui.utils.AppDateHelper
import com.example.inf311_projeto09.ui.utils.AppFonts
import com.example.inf311_projeto09.ui.utils.AppIcons
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun CheckRoomScreen(
    onBack: () -> Unit = {},
    navController: NavHostController
) {
    // Estado para o timer
    var totalSeconds by remember { mutableStateOf(45 * 60 + 36) }

    // Efeito para rodar o timer em segundo plano
    LaunchedEffect(key1 = true) {
        while (totalSeconds > 0) {
            delay(1000L)
            totalSeconds--
        }
    }

    // Formata o tempo para o display
    val formattedTime = remember(totalSeconds) {
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        "%02d:%02d".format(minutes, seconds)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = AppColors().darkGreen,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Header()
            Spacer(Modifier.height(32.dp))
            TimerRing(formattedTime = formattedTime)
            Spacer(Modifier.height(48.dp))
            EventDetails()
            Spacer(Modifier.weight(1f)) // Empurra o botão para baixo
            SwipeToConfirmButton(
                onConfirmed = {
                    println("Ação Confirmada!")
                    // Coloque sua lógica de confirmação aqui
                }
            )
        }
    }
}

@Composable
fun Header() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Check-in", // Título ajustado
            color = AppColors().white,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        IconButton(onClick = { /* Lógica para fechar */ }) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Fechar",
                tint = AppColors().white,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

@Composable
fun TimerRing(formattedTime: String) {
    val progressValue = 0.85f // Valor fixo para corresponder à imagem (entre 0.0 e 1.0)

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f) // Garante que o Box seja um quadrado
    ) {
        // Usamos Canvas para desenhar os arcos do círculo
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 15.dp.toPx()
            // Arco de fundo
            drawArc(
                color = AppColors().grey,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth)
            )
            // Arco de progresso
            drawArc(
                color = AppColors().lightGreen,
                startAngle = -90f,
                sweepAngle = 360 * progressValue,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }
        // Textos dentro do círculo
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = formattedTime,
                color = AppColors().white,
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "de participação",
                color = AppColors().grey,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun EventDetails() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text("Evento:", color = AppColors().lightGrey, fontSize = 16.sp)
        Text(
            "INF 311 - Programação Dispositivos Móveis",
            color = AppColors().white,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(Modifier.height(16.dp))
        Text("Data:", color = AppColors().lightGrey, fontSize = 16.sp)
        Text(
            "29 de fevereiro de 2025",
            color = AppColors().white,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(Modifier.height(16.dp))
        Text("Horário:", color = AppColors().lightGrey, fontSize = 16.sp)
        Text(
            "13:50:03 - Brasil(UTC-3), Brasília",
            color = AppColors().white,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun SwipeToConfirmButton(onConfirmed: () -> Unit) {
    var offsetX by remember { mutableStateOf(0f) }
    val buttonHeight: Dp = 60.dp
    val thumbSize: Dp = buttonHeight

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(buttonHeight)
            .border(width = 2.dp, color = AppColors().lightGreen, shape = CircleShape)
    ) {
        Text(
            text = "Arraste para confirmar",
            color = AppColors().white,
            fontSize = 16.sp,
            modifier = Modifier.align(Alignment.Center)
        )

        val maxDragX = with(LocalDensity.current) {
            // Calcula a largura máxima que o botão pode ser arrastado
            (300.dp - thumbSize).toPx() // Usando um valor aproximado da largura do Box
        }

        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), 0) }
                .size(thumbSize)
                .background(color = AppColors().lightGreen, shape = CircleShape)
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        val newOffsetX = (offsetX + delta).coerceIn(0f, maxDragX)
                        offsetX = newOffsetX
                    },
                    onDragStopped = {
                        if (offsetX >= maxDragX) {
                            onConfirmed()
                        } else {
                            // Retorna à posição inicial se não foi arrastado até o fim
                            offsetX = 0f
                        }
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Arrastar",
                tint = AppColors().green
            )
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xFF0A3818)
@Composable
fun CheckRoomScreenPreview() {
    MaterialTheme { // Use seu tema do app aqui
        CheckRoomScreen(navController = rememberNavController())
    }
}