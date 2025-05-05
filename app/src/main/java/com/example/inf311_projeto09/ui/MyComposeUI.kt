package com.example.inf311_projeto09.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inf311_projeto09.R

@Composable
fun MyScreen() {
    var count by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFF0F0F0)), // Cor de fundo personalizada (ou use Color.White)
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo ou ícone do aplicativo
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground), // Substitua por sua imagem
            contentDescription = "Logo",
            modifier = Modifier
                .size(150.dp)
                .padding(bottom = 32.dp)
        )

        // Título do aplicativo
        Text(
            text = "Presença App",
            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 32.sp),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Instrução para o QR Code
        Text(
            text = "Escaneie o QR Code para marcar presença",
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Tela de contador como exemplo, mas pode ser substituído pelo scanner de QR Code
        Text(
            text = "Contador: $count",
            style = MaterialTheme.typography.headlineSmall.copy(fontSize = 24.sp),
            modifier = Modifier.padding(bottom = 20.dp)
        )

        // Botão com design moderno
        Button(
            onClick = { count++ },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .padding(horizontal = 16.dp),
            shape = MaterialTheme.shapes.medium.copy(CornerSize(12.dp)), // Botão com bordas arredondadas
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(
                text = "Marcar Presença",
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                color = Color.White
            )
        }
    }
}
