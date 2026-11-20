package com.example.counterflameassistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Bg = Color(0xFF10131B)
private val Panel = Color(0xFF1B2130)
private val Accent = Color(0xFF63E6BE)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { AssistantApp() }
    }
}

@Composable
fun AssistantApp() {
    var espDemo by remember { mutableStateOf(false) }
    var aimDrill by remember { mutableStateOf(false) }
    var training by remember { mutableStateOf(true) }
    var sensitivity by remember { mutableFloatStateOf(50f) }
    var crosshairSize by remember { mutableFloatStateOf(8f) }
    var hits by remember { mutableIntStateOf(0) }

    MaterialTheme(colorScheme = darkColorScheme(
        background = Bg, surface = Panel, primary = Accent,
        onBackground = Color.White, onSurface = Color.White
    )) {
        Column(
            Modifier.fillMaxSize().background(Bg).verticalScroll(rememberScrollState()).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("COUNTERFLAME", color = Accent, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Text("Assistant", color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Bold)
            Text("Тренировочный помощник • версия 1.0", color = Color.LightGray)

            SectionCard("1. ESP — демонстрационный режим",
                "Показывает учебные маркеры только внутри этого приложения. Не читает память игры и не показывает игроков через стены.") {
                SettingSwitch("Включить учебные маркеры", espDemo) { espDemo = it }
                if (espDemo) DemoMarkers()
            }

            SectionCard("2. AIM — тренировка прицеливания",
                "Настрой чувствительность и потренируй точность на учебной мишени. Нет автоматического наведения в игре.") {
                SettingSwitch("Режим тренировки", aimDrill) { aimDrill = it }
                Text("Чувствительность: ${sensitivity.toInt()}%", color = Color.White)
                Slider(value = sensitivity, onValueChange = { sensitivity = it }, valueRange = 1f..100f)
                Button(onClick = { hits++ }, enabled = aimDrill, colors = ButtonDefaults.buttonColors(containerColor = Accent)) {
                    Text("Попадание по учебной мишени", color = Bg)
                }
                Text("Учебные попадания: $hits", color = Color.LightGray)
            }

            SectionCard("3. TRAINING — тренировочные настройки",
                "Настрой внешний вид учебного прицела и используй его как ориентир при тренировке.") {
                SettingSwitch("Показать учебный прицел", training) { training = it }
                Text("Размер прицела: ${crosshairSize.toInt()}", color = Color.White)
                Slider(value = crosshairSize, onValueChange = { crosshairSize = it }, valueRange = 3f..20f)
                if (training) Crosshair(crosshairSize)
            }

            Text("Важно: приложение работает отдельно и не вмешивается в CounterFlame. Совместимость с конкретной сборкой игры не проверена.",
                color = Color(0xFFB7C0D0), fontSize = 12.sp)
        }
    }
}

@Composable
fun SectionCard(title: String, description: String, content: @Composable ColumnScope.() -> Unit) {
    Card(colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(18.dp)) {
        Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(title, color = Accent, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(description, color = Color(0xFFCAD0DC), fontSize = 13.sp)
            content()
        }
    }
}

@Composable
fun SettingSwitch(label: String, checked: Boolean, onChecked: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = Color.White, modifier = Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = onChecked)
    }
}

@Composable
fun DemoMarkers() {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        listOf("Мишень A", "Мишень B", "Мишень C").forEach {
            Surface(color = Color(0xFF263B43), shape = RoundedCornerShape(10.dp)) {
                Text(it, color = Accent, modifier = Modifier.padding(10.dp), fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun Crosshair(size: Float) {
    Box(Modifier.fillMaxWidth().height(90.dp).background(Color(0xFF111722), RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
        Canvas(Modifier.size(80.dp)) {
            val c = center
            val arm = size * 1.8f
            drawLine(Accent, Offset(c.x - arm - 8, c.y), Offset(c.x - 8, c.y), strokeWidth = 3f)
            drawLine(Accent, Offset(c.x + 8, c.y), Offset(c.x + arm + 8, c.y), strokeWidth = 3f)
            drawLine(Accent, Offset(c.x, c.y - arm - 8), Offset(c.x, c.y - 8), strokeWidth = 3f)
            drawLine(Accent, Offset(c.x, c.y + 8), Offset(c.x, c.y + arm + 8), strokeWidth = 3f)
        }
    }
}
