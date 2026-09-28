package com.example.familiasquesuman.ui.screens.actividad

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.familiasquesuman.ui.navigation.Rutas
import com.example.familiasquesuman.ui.theme.AmarilloOscuro
import com.example.familiasquesuman.ui.theme.AzulOscuro
import com.example.familiasquesuman.ui.theme.FondoClaro
import com.example.familiasquesuman.ui.theme.TextoGrisActividad

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActividadSeleccionParticipantesScreen(navController: NavHostController, actividadId: Int) {
    var seleccionado by remember { mutableStateOf(true) }

    Scaffold(
        containerColor = FondoClaro,
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar", tint = AzulOscuro)
                    }
                },
                title = {
                    Text(
                        text = "Familias que Suman+",
                        fontWeight = FontWeight.Bold,
                        color = AzulOscuro,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notificaciones", tint = AzulOscuro)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = FondoClaro
                )
            )
        },
        bottomBar = {
            Surface(
                color = FondoClaro,
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 16.dp
            ) {
                Button(
                    onClick = { navController.navigate(Rutas.ActividadConfirmacion.crearRuta(actividadId)) },
                    colors = ButtonDefaults.buttonColors(containerColor = AmarilloOscuro), 
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .height(56.dp)
                ) {
                    Icon(Icons.Default.Groups, contentDescription = null, tint = AzulOscuro)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Confirmar participación", color = AzulOscuro, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Selecciona quiénes participarán",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = AzulOscuro,
                textAlign = TextAlign.Center,
                lineHeight = 34.sp
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Puedes elegir a los miembros que asistirán.",
                fontSize = 16.sp,
                color = AzulOscuro,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Tarjeta de "Tú"
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth(),
                onClick = { seleccionado = !seleccionado }
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Avatar Placeholder
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(Color.LightGray, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(32.dp))
                    }
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Tú", fontWeight = FontWeight.Bold, color = AzulOscuro, fontSize = 16.sp)
                        Text("Adulto", color = TextoGrisActividad, fontSize = 14.sp)
                    }
                    
                    if (seleccionado) {
                        Icon(Icons.Default.CheckCircle, contentDescription = "Seleccionado", tint = AmarilloOscuro, modifier = Modifier.size(28.dp))
                    } else {
                        Icon(Icons.Outlined.Circle, contentDescription = "No seleccionado", tint = AzulOscuro, modifier = Modifier.size(28.dp))
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Botón Agregar Persona
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color.Transparent,
                border = BorderStroke(1.dp, AmarilloOscuro), // Debería ser dashed pero Compose no lo soporta nativo fácil, lo dejamos sólido
                modifier = Modifier.fillMaxWidth(),
                onClick = { /*TODO*/ }
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = AmarilloOscuro)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Agregar persona", color = AmarilloOscuro, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}