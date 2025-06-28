package com.example.inf311_projeto09.ui.screens.user

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.inf311_projeto09.ui.utils.AppColors
import com.example.inf311_projeto09.ui.utils.AppDateHelper
import com.example.inf311_projeto09.ui.utils.AppFonts
import com.example.inf311_projeto09.ui.utils.AppIcons
import com.example.inf311_projeto09.ui.utils.rememberLocationHelper
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.delay

@OptIn(ExperimentalGetImage::class)
@Composable
fun QrScannerScreen(
    onBack: () -> Unit = {},
    navController: NavHostController
) {
    val locationHelper = rememberLocationHelper()
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = {}
    )
    LaunchedEffect(Unit) {
        locationHelper.getCurrentLocation(permissionLauncher)
    }

    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

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

    val scannedState = remember { mutableStateOf(false) }
    val currentTime = remember { mutableStateOf(AppDateHelper().getCurrentTimeWithSeconds()) }

    LaunchedEffect(Unit) {
        if (!cameraPermissionGranted) {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }

        while (true) {
            currentTime.value = AppDateHelper().getCurrentTimeWithSeconds()
            delay(1000)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        if (cameraPermissionGranted) {
            CameraPreview(context, lifecycleOwner, navController, scannedState)
        } else {
            CameraPermissionText()
        }

        QrScannerUI(onBack, currentTime.value)
    }
}

@Composable
fun CameraPreview(
    ctx: Context,
    lifecycleOwner: LifecycleOwner,
    navController: NavHostController,
    scannedState: MutableState<Boolean>
) {
    AndroidView(
        factory = {
            val previewView = PreviewView(ctx)
            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also {
                    it.surfaceProvider = previewView.surfaceProvider
                }

                val barcodeScanner = BarcodeScanning.getClient()

                val imageAnalysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()

                imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(ctx)) { imageProxy ->
                    analyzeImage(
                        imageProxy,
                        ctx,
                        barcodeScanner,
                        navController,
                        scannedState
                    )
                }

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalysis
                    )
                } catch (exc: Exception) {
                    println("Falha ao iniciar CameraX: ${exc.message}")
                }
            }, ContextCompat.getMainExecutor(ctx))
            previewView
        },
        modifier = Modifier.fillMaxSize()
    )
}

@OptIn(ExperimentalGetImage::class)
fun analyzeImage(
    imageProxy: ImageProxy,
    ctx: Context,
    barcodeScanner: BarcodeScanner,
    navController: NavHostController,
    scannedState: MutableState<Boolean>
) {
    if (scannedState.value) {
        imageProxy.close()
        return
    }

    val mediaImage = imageProxy.image
    if (mediaImage != null) {
        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
        barcodeScanner.process(image)
            .addOnSuccessListener { barcodes ->
                for (barcode in barcodes) {
                    val rawValue = barcode.rawValue ?: continue
                    val boundingBox = barcode.boundingBox ?: continue

                    val imageWidth = image.width
                    val imageHeight = image.height

                    val dpToPx = ctx.resources.displayMetrics.density
                    val boxSizePx = 250 * dpToPx

                    val imageCenterX = imageWidth / 2
                    val imageCenterY = imageHeight / 2

                    val scanAreaLeft = imageCenterX - boxSizePx / 2
                    val scanAreaTop = imageCenterY - boxSizePx / 2
                    val scanAreaRight = imageCenterX + boxSizePx / 2
                    val scanAreaBottom = imageCenterY + boxSizePx / 2

                    val centerX = boundingBox.centerX().toFloat()
                    val centerY = boundingBox.centerY().toFloat()

                    val insideScanArea = centerX in scanAreaLeft..scanAreaRight &&
                            centerY in scanAreaTop..scanAreaBottom

                    if (insideScanArea) {
                        navController.previousBackStackEntry?.savedStateHandle?.set(
                            "scannedCode",
                            rawValue
                        )
                        navController.popBackStack()
                        scannedState.value = true
                        break
                    } else {
                        Log.e("QR_SCANNER", "QR Code fora da área de leitura central")
                    }
                }
            }
            .addOnFailureListener {
                Log.e("QR_SCANNER", "Erro na leitura: ${it.message}")
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    } else {
        imageProxy.close()
    }
}

@Composable
fun CameraPermissionText() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
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

@Composable
fun QrScannerUI(onBack: () -> Unit, currentTime: String) {
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
                onClick = onBack,
                modifier = Modifier.size(30.dp)
            ) {
                AppIcons.Filled.CircleClose(
                    boxSize = 30.dp,
                    colorIcon = AppColors().lightGreen,
                    backgroundColorIcon = AppColors().darkGreen
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

                    drawLine(cornerColor, Offset(0f, cornerSize), Offset(0f, 0f), strokeWidth)
                    drawLine(cornerColor, Offset(cornerSize, 0f), Offset(0f, 0f), strokeWidth)
                    drawLine(
                        cornerColor,
                        Offset(size.width, cornerSize),
                        Offset(size.width, 0f),
                        strokeWidth
                    )
                    drawLine(
                        cornerColor,
                        Offset(size.width - cornerSize, 0f),
                        Offset(size.width, 0f),
                        strokeWidth
                    )
                    drawLine(
                        cornerColor,
                        Offset(0f, size.height - cornerSize),
                        Offset(0f, size.height),
                        strokeWidth
                    )
                    drawLine(
                        cornerColor,
                        Offset(cornerSize, size.height),
                        Offset(0f, size.height),
                        strokeWidth
                    )
                    drawLine(
                        cornerColor,
                        Offset(size.width, size.height - cornerSize),
                        Offset(size.width, size.height),
                        strokeWidth
                    )
                    drawLine(
                        cornerColor,
                        Offset(size.width - cornerSize, size.height),
                        Offset(size.width, size.height),
                        strokeWidth
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
                        text = currentTime,
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

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, showSystemUi = true)
@Composable
fun QrScannerScreenPreview() {
    QrScannerScreen(navController = rememberNavController())
}