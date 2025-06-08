package com.example.inf311_projeto09.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.inf311_projeto09.ui.utils.AppFonts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.IconButton
import com.example.inf311_projeto09.ui.utils.AppColors
import com.example.inf311_projeto09.ui.utils.AppDateFormatter
import com.example.inf311_projeto09.ui.utils.AppIcons
import kotlinx.coroutines.delay

@Composable
fun QrScannerScreen(navController: NavHostController) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var cameraPermissionGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        cameraPermissionGranted = isGranted
    }

    val currentTime = remember { mutableStateOf(AppDateFormatter().getCurrentTimeWithSeconds()) }

    LaunchedEffect(Unit) {
        if (!cameraPermissionGranted) {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }

        while (true) {
            currentTime.value = AppDateFormatter().getCurrentTimeWithSeconds()
            delay(1000)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        if (cameraPermissionGranted) {
            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx)
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()
                        val preview = androidx.camera.core.Preview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }
                        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                        try {
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview)
                        } catch (exc: Exception) {
                            println("Falha ao iniciar CameraX: ${exc.message}")
                        }
                    }, ContextCompat.getMainExecutor(ctx))
                    previewView
                },
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Box(
                modifier = Modifier.fillMaxSize().background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Permissão da câmera necessária para escanear QR Code.",
                    fontFamily = AppFonts().montserrat,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 30.dp)
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp, start = 20.dp, end = 20.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.size(30.dp)
                ) {
                    AppIcons.Filled.CircleClose(
                        boxSize = 30.dp,
                        colorIcon = AppColors().lightGreen,
                        backgroundColorIcon = AppColors().darkGreen,
                        modifier = Modifier
                    )
                }

                Spacer(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(50.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .background(Color(0xAA333333), RoundedCornerShape(8.dp))
                    .padding(vertical = 12.dp, horizontal = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Encontre um código para escanear",
                    fontFamily = AppFonts().montserrat,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(100.dp))

            Box(
                modifier = Modifier
                    .size(250.dp)
                    .drawBehind {
                        val cornerSize = 30.dp.toPx()
                        val strokeWidth = 3.dp.toPx()
                        val cornerColor = AppColors().lightGreen

                        drawLine(
                            color = cornerColor,
                            start = Offset(0f, cornerSize),
                            end = Offset(0f, 0f),
                            strokeWidth = strokeWidth
                        )
                        drawLine(
                            color = cornerColor,
                            start = Offset(cornerSize, 0f),
                            end = Offset(0f, 0f),
                            strokeWidth = strokeWidth
                        )

                        drawLine(
                            color = cornerColor,
                            start = Offset(size.width, cornerSize),
                            end = Offset(size.width, 0f),
                            strokeWidth = strokeWidth
                        )
                        drawLine(
                            color = cornerColor,
                            start = Offset(size.width - cornerSize, 0f),
                            end = Offset(size.width, 0f),
                            strokeWidth = strokeWidth
                        )

                        drawLine(
                            color = cornerColor,
                            start = Offset(0f, size.height - cornerSize),
                            end = Offset(0f, size.height),
                            strokeWidth = strokeWidth
                        )
                        drawLine(
                            color = cornerColor,
                            start = Offset(cornerSize, size.height),
                            end = Offset(0f, size.height),
                            strokeWidth = strokeWidth
                        )

                        drawLine(
                            color = cornerColor,
                            start = Offset(size.width, size.height - cornerSize),
                            end = Offset(size.width, size.height),
                            strokeWidth = strokeWidth
                        )
                        drawLine(
                            color = cornerColor,
                            start = Offset(size.width - cornerSize, size.height),
                            end = Offset(size.width, size.height),
                            strokeWidth = strokeWidth
                        )
                    }
            )

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .padding(bottom = 60.dp)
                    .width(220.dp)
                    .height(55.dp)
                    .background(AppColors().lightGreen, RoundedCornerShape(25.dp)),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AppIcons.Outline.Clock(30.dp, Color.Black)

                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.padding(start = 10.dp)
                    ) {
                        Text(
                            text = currentTime.value,
                            fontFamily = AppFonts().montserrat,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Brasil (UTC-3), Brasília",
                            fontFamily = AppFonts().montserrat,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black.copy(alpha = 0.7f),
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, showSystemUi = true)
@Composable
fun QrScannerScreenPreview() {
    QrScannerScreen(navController = rememberNavController())
}