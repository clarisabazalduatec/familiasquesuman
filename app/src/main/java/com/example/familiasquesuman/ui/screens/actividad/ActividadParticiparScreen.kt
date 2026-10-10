package com.example.familiasquesuman.ui.screens.actividad

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.familiasquesuman.ui.components.PantallaPrincipal
import com.example.familiasquesuman.ui.components.PantallaPrincipalConMenu
import com.example.familiasquesuman.ui.navigation.Rutas
import com.example.familiasquesuman.ui.theme.AzulOscuro
import com.example.familiasquesuman.ui.theme.FamiliasQueSumanTheme
import com.example.familiasquesuman.ui.theme.FondoClaro

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActividadParticiparScreen(navController: NavHostController, actividadId: String) {
    PantallaPrincipalConMenu(
        navController = navController,
        pantallaActual = PantallaPrincipal.ACTIVIDADES,
        navbar = false,
        backButton = true
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            
            // Placeholder para la ilustración de la familia
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Groups, 
                    contentDescription = null, 
                    tint = AzulOscuro.copy(alpha = 0.5f), 
                    modifier = Modifier.size(200.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(18.dp))
            
            Text(
                text = "¿Es su primera vez participando?",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = AzulOscuro,
                textAlign = TextAlign.Center,
                lineHeight = 34.sp
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Si es su primera vez, necesitará registrar a su familia.",
                fontSize = 16.sp,
                color = AzulOscuro,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Si ya han participado, puede seleccionar perfiles guardados.",
                fontSize = 16.sp,
                color = AzulOscuro,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // Botón: Es nuestra primera vez
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                border = BorderStroke(1.dp, Color(0xFFE8E8E8)),
                modifier = Modifier.fillMaxWidth(),
                onClick = { navController.navigate(Rutas.Login.ruta) }
            ) {
                Row(
                    modifier = Modifier.padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = AzulOscuro, modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Es nuestra primera vez", 
                        color = AzulOscuro, 
                        fontWeight = FontWeight.Bold, 
                        fontSize = 18.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = AzulOscuro)
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Botón: Ya hemos participado
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = FondoClaro,
                border = BorderStroke(1.dp, AzulOscuro),
                modifier = Modifier.fillMaxWidth(),
                onClick = { navController.navigate(Rutas.ActividadSeleccionParticipantes.crearRuta(actividadId)) }
            ) {
                Row(
                    modifier = Modifier.padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Group, contentDescription = null, tint = AzulOscuro, modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Ya hemos participado", 
                        color = AzulOscuro, 
                        fontWeight = FontWeight.Bold, 
                        fontSize = 18.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = AzulOscuro)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ActividadParticiparScreenPreview() {
    FamiliasQueSumanTheme {
        ActividadParticiparScreen(
            navController = rememberNavController(),
            actividadId = "1"
        )
    }
}
