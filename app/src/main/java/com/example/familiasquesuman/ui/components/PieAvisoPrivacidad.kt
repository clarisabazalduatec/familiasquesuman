
package com.example.familiasquesuman.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.familiasquesuman.ui.theme.AzulMarino

@Composable
fun PieAvisoPrivacidad(navController: NavHostController, modifier: Modifier = Modifier) {
    Column(modifier = modifier
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalDivider(color = AzulMarino.copy(alpha = 0.12f))
        Spacer(modifier = Modifier.height(12.dp))
        TextButton(onClick = { navController.navigate("aviso_privacidad") { launchSingleTop = true } }) {
            Text(text = "Aviso de Privacidad",
                color = AzulMarino,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
