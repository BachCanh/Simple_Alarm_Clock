package com.example.simplealarmclock

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simplealarmclock.ui.theme.SimpleAlarmClockTheme
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimpleAlarmClockTheme {
                AlarmClockApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmClockApp() {
    var selectedHour by remember { mutableStateOf(0) }
    var selectedMinute by remember { mutableStateOf(0) }
    var msg by remember { mutableStateOf("") }

    val currentTime = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.night_sky_1),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Gradient overlay
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0, 0, 0, 150) // Semi-transparent black
        ) {}

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Dream Alarm",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Card for time picker
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1E1E2E).copy(alpha = 0.85f)
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TimePicker(
                        state = timePickerState,
                        colors = TimePickerDefaults.colors(
                            timeSelectorSelectedContainerColor = Color(0xFF9370DB),
                            timeSelectorUnselectedContainerColor = Color(0xFF333344),
                            timeSelectorSelectedContentColor = Color.White,
                            clockDialColor = Color(0xFF272736),
                            clockDialSelectedContentColor = Color.White,
                            clockDialUnselectedContentColor = Color(0xFFCCCCCC),
                            periodSelectorSelectedContainerColor = Color(0xFF9370DB),
                            periodSelectorUnselectedContainerColor = Color(0xFF333344),
                            periodSelectorSelectedContentColor = Color.White,
                            periodSelectorUnselectedContentColor = Color(0xFFCCCCCC)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Card for label
            OutlinedTextField(
                value = msg,
                onValueChange = { msg = it },
                label = { Text("Alarm Label", color = Color.White.copy(0.8f)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    // Updated parameter names
                    unfocusedTextColor = Color.White,
                    focusedTextColor = Color.White,
                    cursorColor = Color(0xFF9370DB),
                    focusedBorderColor = Color(0xFF9370DB),
                    unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                    containerColor = Color(0xFF1E1E2E).copy(alpha = 0.7f)
                )
            )
            Spacer(modifier = Modifier.height(32.dp))

            val context = LocalContext.current

            // Set alarm button
            Button(
                onClick = {
                    selectedHour = timePickerState.hour
                    selectedMinute = timePickerState.minute
                    setAlarm(context, selectedHour, selectedMinute, msg)
                },
                modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF9370DB)
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 8.dp
                )
            ) {
                Text(
                    text = "Set Alarm",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Current time display
            Text(
                text = "Current time: ${String.format("%02d:%02d", currentTime.get(Calendar.HOUR_OF_DAY), currentTime.get(Calendar.MINUTE))}",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

fun setAlarm(context: Context, hour: Int, minute: Int, msg: String) {
    val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
        putExtra(AlarmClock.EXTRA_HOUR, hour)
        putExtra(AlarmClock.EXTRA_MINUTES, minute)
        if(msg.isNotEmpty()) putExtra(AlarmClock.EXTRA_MESSAGE, msg)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    try {
        context.startActivity(intent)
        Toast.makeText(context, "Alarm set for ${String.format("%02d:%02d", hour, minute)}", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Toast.makeText(context, "No alarm app available!", Toast.LENGTH_SHORT).show()
    }
}

@Preview(showBackground = true)
@Composable
fun AlarmClockPreview() {
    SimpleAlarmClockTheme {
        AlarmClockApp()
    }
}