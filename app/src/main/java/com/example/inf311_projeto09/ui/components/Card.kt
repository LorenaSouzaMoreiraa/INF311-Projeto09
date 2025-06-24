package com.example.inf311_projeto09.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.inf311_projeto09.helper.EventAuthenticationHelper
import com.example.inf311_projeto09.model.Event
import com.example.inf311_projeto09.ui.ScreenType
import com.example.inf311_projeto09.ui.utils.AppColors
import com.example.inf311_projeto09.ui.utils.AppDateHelper
import com.example.inf311_projeto09.ui.utils.AppFonts
import com.example.inf311_projeto09.ui.utils.AppIcons
import kotlinx.coroutines.delay
import java.util.Date
import java.util.Locale

@Composable
fun EventCard(
    event: Event,
    isCurrentEvent: Boolean,
    isAdminHome: Boolean,
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
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
            CardTitle(event, titleColor, isAdminHome)

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "${
                    AppDateHelper().getTimeFormatted(event.beginTime)
                } - ${
                    AppDateHelper().getTimeFormatted(event.endTime)
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
                    eventTime = event.checkInEnabled,
                    checkTime = event.checkInTime,
                    isEnabled = event.checkInEnabled != null,
                    isCurrentEvent = isCurrentEvent,
                    isAdminHome = isAdminHome,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        handleCheckInClick(
                            event,
                            isAdminHome,
                            navController
                        )
                    }
                )

                EventActionButton(
                    label = "Check-out",
                    eventTime = event.checkOutEnabled,
                    checkTime = event.checkOutTime,
                    isEnabled = event.checkOutEnabled != null,
                    isCurrentEvent = isCurrentEvent,
                    isAdminHome = isAdminHome,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        handleCheckOutClick(
                            event,
                            isAdminHome,
                            navController
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            val progressPercent by rememberUpdatedProgress(event.beginTime, event.endTime)

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
fun CardTitle(
    event: Event,
    titleColor: Color,
    isAdminHome: Boolean
) {
    var showCode by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = event.title,
            fontFamily = AppFonts().montserrat,
            fontWeight = FontWeight.SemiBold,
            color = titleColor,
            fontSize = 12.sp,
            modifier = if (isAdminHome) {
                Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            } else {
                Modifier
            }
        )

        if (isAdminHome) {
            IconButton(
                onClick = { showCode = true },
                modifier = Modifier.size(24.dp)
            ) {
                if (event.verificationMethod == Event.EventVerificationMethod.VERIFICATION_CODE)
                    AppIcons.Outline.VerificationCode(24.dp)
                else
                    AppIcons.Outline.QRCode(24.dp)
            }
        }
    }

    if (showCode) {
        if (event.verificationMethod == Event.EventVerificationMethod.VERIFICATION_CODE)
            VerificationCodeDialog(event = event, onDismiss = { showCode = false })
        else
            QRCodeDialog(event = event, onDismiss = { showCode = false })
    }
}

@Composable
fun VerificationCodeDialog(
    event: Event,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val clipboard =
                        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("Código de verificação", event.checkInCode)
                    clipboard.setPrimaryClip(clip)
                    Toast.makeText(context, "Código copiado!", Toast.LENGTH_SHORT)
                        .show()
                }
            ) {
                Text(
                    text = "Copiar código",
                    color = AppColors().darkGreen,
                    fontFamily = AppFonts().montserrat,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Fechar",
                    color = AppColors().darkGreen,
                    fontFamily = AppFonts().montserrat,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
        },
        title = {
            Text(
                text = "Código de Verificação",
                color = AppColors().darkGreen,
                fontFamily = AppFonts().montserrat,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        },
        text = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = event.checkInCode,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = AppFonts().montserrat,
                    color = AppColors().darkGreen
                )
            }
        },
        containerColor = AppColors().white
    )
}

@Composable
fun QRCodeDialog(
    event: Event,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val qrBitmap = remember {
        EventAuthenticationHelper.generateQRCode(event.checkInCode, 800)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                saveBitmapToGallery(context, qrBitmap, "qr_code_${event.course}.png")
            }) {
                Text(
                    text = "Salvar na galeria",
                    color = AppColors().darkGreen,
                    fontFamily = AppFonts().montserrat,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Fechar",
                    color = AppColors().darkGreen,
                    fontFamily = AppFonts().montserrat,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
        },
        title = {
            Text(
                text = "QR Code do evento",
                color = AppColors().darkGreen,
                fontFamily = AppFonts().montserrat,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        },
        text = {
            Image(
                bitmap = qrBitmap.asImageBitmap(),
                contentDescription = "QR Code",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )
        },
        containerColor = AppColors().white
    )
}

fun saveBitmapToGallery(context: Context, bitmap: Bitmap, filename: String) {
    val resolver = context.contentResolver
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, filename)
        put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/QR_Codes")
        put(MediaStore.Images.Media.IS_PENDING, 1)
    }

    val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    uri?.let {
        resolver.openOutputStream(it)?.use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        }
        contentValues.clear()
        contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
        resolver.update(uri, contentValues, null, null)
        Toast.makeText(context, "Imagem salva na galeria", Toast.LENGTH_SHORT).show()
    }
}

fun handleCheckInClick(
    event: Event,
    isAdminHome: Boolean,
    navController: NavHostController
) {
    val now = Date()

    if (isAdminHome) {
        handleEnableCheckClick(true, navController)
    } else {
        val canCheckIn =
            event.checkInEnabled != null && event.checkInTime == null && now >= event.checkInEnabled
        handleCheckClick(canCheckIn, event.verificationMethod, navController)
    }
}

fun handleCheckOutClick(
    event: Event,
    isAdminHome: Boolean,
    navController: NavHostController
) {
    val now = Date()

    if (isAdminHome) {
        handleEnableCheckClick(true, navController)
    } else {
        val canCheckOut =
            event.checkOutEnabled != null && event.checkInTime != null && event.checkOutTime == null && now >= event.checkOutEnabled
        handleCheckClick(canCheckOut, event.verificationMethod, navController)
    }
}

fun handleEnableCheckClick(
    canCheck: Boolean,
    navController: NavHostController
) {
    if (canCheck) {
        // TODO: integrar para ativar o check-in e check-out
    }
}

fun handleCheckClick(
    canCheck: Boolean,
    verificationMethod: Event.EventVerificationMethod,
    navController: NavHostController
) {
    if (canCheck) {
        when (verificationMethod) {
            Event.EventVerificationMethod.VERIFICATION_CODE -> navController.navigate(ScreenType.VERIFICATION_CODE.route)
            else -> navController.navigate(ScreenType.QR_SCANNER.route)
        }
    }
}

@Composable
fun rememberUpdatedProgress(beginTime: Date, endTime: Date): State<Float> {
    val duration = endTime.time - beginTime.time
    val percent = remember { mutableLongStateOf(0L) }

    LaunchedEffect(beginTime, endTime) {
        while (true) {
            val now = System.currentTimeMillis()
            val elapsed = now - beginTime.time

            val value = when {
                now < beginTime.time -> 0f
                now > endTime.time -> 1f
                else -> elapsed.toFloat() / duration
            }.coerceIn(0f, 1f)

            percent.longValue = (value * 10000).toLong()

            if (value >= 1f) break

            delay(1000)
        }
    }

    return rememberUpdatedState(percent.longValue / 10000f)
}

@Composable
private fun EventActionButton(
    label: String,
    eventTime: Date?,
    checkTime: Date?,
    isEnabled: Boolean,
    isAdminHome: Boolean,
    isCurrentEvent: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    var eventStarted by remember { mutableStateOf(false) }

    LaunchedEffect(eventTime) {
        while (true) {
            eventStarted = Date() > (eventTime ?: Date(Long.MAX_VALUE))
            delay(1000)
            if (eventStarted) break
        }
    }

    val currentBorderColor = when {
        isCurrentEvent && (isAdminHome && !isEnabled) -> AppColors().lightGreen
        isCurrentEvent && (isAdminHome && !eventStarted) -> AppColors().white
        isCurrentEvent && (checkTime != null || isAdminHome) -> AppColors().lightGreen
        isCurrentEvent -> AppColors().white
        else -> AppColors().darkGreen
    }

    val currentBackgroundColor = when {
        isCurrentEvent && isAdminHome && !isEnabled -> AppColors().lightGreen
        isCurrentEvent && isAdminHome && !eventStarted -> AppColors().greyTransparent
        isCurrentEvent && (checkTime != null || isAdminHome) -> AppColors().transparent
        isCurrentEvent -> AppColors().greyTransparent
        else -> AppColors().transparent
    }

    val titleColor = when {
        isAdminHome && !isEnabled -> AppColors().black
        isCurrentEvent -> AppColors().white
        else -> AppColors().darkGreen
    }

    val subtitleColor = when {
        isAdminHome && !isEnabled -> AppColors().black
        isCurrentEvent -> AppColors().lightGrey
        else -> AppColors().grey
    }

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
        if (isCurrentEvent && isAdminHome && !isEnabled) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppIcons.Outline.CircleCheck(30.dp, AppColors().black)
                Column {
                    Text(
                        text = "Iniciar",
                        fontFamily = AppFonts().montserrat,
                        fontWeight = FontWeight.SemiBold,
                        color = titleColor,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(start = 5.dp)
                    )

                    Text(
                        text = label,
                        fontFamily = AppFonts().montserrat,
                        fontWeight = FontWeight.SemiBold,
                        color = titleColor,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 5.dp)
                    )
                }
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if ((isCurrentEvent && checkTime != null) || (isAdminHome && eventStarted)) {
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
                EventActionStatus(
                    isEnabled,
                    isAdminHome,
                    eventTime,
                    checkTime,
                    titleColor,
                    subtitleColor
                )
            }
        }
    }
}

@Composable
private fun EventActionStatus(
    isEnabled: Boolean,
    isAdminHome: Boolean,
    eventTime: Date?,
    checkTime: Date?,
    titleColor: Color,
    subtitleColor: Color
) {
    var eventStarted by remember { mutableStateOf(false) }

    LaunchedEffect(eventTime) {
        while (true) {
            eventStarted = Date() > (eventTime ?: Date(Long.MAX_VALUE))
            delay(1000)
            if (eventStarted) break
        }
    }

    if (isAdminHome && eventStarted) {
        EventActionNormalStatus(eventTime, eventTime, isAdminHome, titleColor, subtitleColor)
    } else if (isEnabled && eventTime != null && checkTime == null) {
        EventActionCountdownStatus(eventTime, titleColor, subtitleColor)
    } else {
        EventActionNormalStatus(eventTime, checkTime, isAdminHome, titleColor, subtitleColor)
    }
}

@Composable
private fun EventActionNormalStatus(
    eventTime: Date?,
    checkTime: Date?,
    isAdminHome: Boolean,
    titleColor: Color,
    subtitleColor: Color
) {
    val statusAction = when {
        isAdminHome -> "iniciado"
        eventTime == null || checkTime == null -> "aguarde"
        checkTime.time - eventTime.time > 10 * 60 * 1000L -> "atrasado"
        else -> "no horário"
    }

    Text(
        text = AppDateHelper().getTimeFormattedWithSeconds(checkTime),
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
    var eventStarted by remember { mutableStateOf(Date() > eventTime) }

    LaunchedEffect(eventTime) {
        while (true) {
            val now = System.currentTimeMillis()
            val eventStartMillis = eventTime.time

            eventStarted = now > eventStartMillis

            val diffSeconds = if (eventStarted) (now - eventStartMillis) / 1000
            else (eventStartMillis - now) / 1000

            remainingSeconds = diffSeconds.coerceAtLeast(0)

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

@Composable
fun StatisticCard(number: Int, message: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .aspectRatio(1.5f)
            .border(
                BorderStroke(
                    width = 1.dp,
                    color = AppColors().darkGreen
                ),
                shape = RoundedCornerShape(5.dp)
            ),
        shape = RoundedCornerShape(5.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors().mediumGreen)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = number.toString(),
                fontFamily = AppFonts().montserrat,
                fontWeight = FontWeight.SemiBold,
                color = AppColors().black,
                fontSize = 40.sp,
                textAlign = TextAlign.Start
            )

            Text(
                text = message,
                fontFamily = AppFonts().montserrat,
                fontWeight = FontWeight.SemiBold,
                color = AppColors().black,
                fontSize = 10.sp,
                textAlign = TextAlign.Start
            )
        }
    }
}

@Composable
fun SeeEventsCard(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    Card(
        modifier = modifier
            .aspectRatio(1.5f)
            .drawBehind {
                val cornerRadiusPx = 5.dp.toPx()
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
        shape = RoundedCornerShape(5.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors().transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp)
                .clickable { /* TODO: navController.navigate(ScreenType.EVENTS?.route) */ },
            contentAlignment = Alignment.BottomStart
        ) {
            Text(
                text = "Visualizar meus\neventos >>",
                fontFamily = AppFonts().montserrat,
                fontWeight = FontWeight.SemiBold,
                color = AppColors().darkGreen,
                fontSize = 10.sp,
                textDecoration = TextDecoration.Underline,
                textAlign = TextAlign.Start
            )
        }
    }
}
