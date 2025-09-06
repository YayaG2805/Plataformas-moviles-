package com.example.lab07

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { App() }
    }
}

@Composable
private fun App() {
    AppTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            NotificationCenterScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NotificationCenterScreen() {
    val all = remember { generateFakeNotifications() }

    // null = mostrar TODOS; si no null = filtrar por ese tipo
    var selected by rememberSaveable { mutableStateOf<NotificationType?>(null) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Notificaciones Diego Guevara24128") }) }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                "Tipos de notificaciones",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            FilterRow(
                selected = selected,
                onSelect = { selected = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )

            val filtered = remember(selected, all) {
                if (selected == null) all else all.filter { it.type == selected }
            }

            NotificationList(items = filtered, modifier = Modifier.fillMaxSize())
        }
    }
}

@Composable
private fun FilterRow(
    selected: NotificationType?,                   // ahora nullable
    onSelect: (NotificationType?) -> Unit,        // acepta null para "Todos"
    modifier: Modifier = Modifier
) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = modifier) {
        // Informativas
        FilterChip(
            selected = selected == NotificationType.GENERAL,
            onClick = {
                onSelect(if (selected == NotificationType.GENERAL) null else NotificationType.GENERAL)
            },
            label = { Text("Informativas") },
            leadingIcon = {
                if (selected == NotificationType.GENERAL) {
                    Icon(Icons.Default.Notifications, contentDescription = null)
                }
            }
        )
        // Capacitaciones
        FilterChip(
            selected = selected == NotificationType.NEW_MEETING,
            onClick = {
                onSelect(if (selected == NotificationType.NEW_MEETING) null else NotificationType.NEW_MEETING)
            },
            label = { Text("Capacitaciones") },
            leadingIcon = {
                if (selected == NotificationType.NEW_MEETING) {
                    Icon(Icons.Default.School, contentDescription = null)
                }
            }
        )
    }
}

@Composable
private fun NotificationList(items: List<Notification>, modifier: Modifier = Modifier) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = modifier) {
        items(items.size, key = { i -> items[i].id }) { i ->
            NotificationCard(items[i])
        }
    }
}

@Composable
private fun NotificationCard(item: Notification) {
    val (bg, fg) = colorsForType(item.type)
    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val iconBg = bg
            val iconTint = if (iconBg.luminance() > 0.5f) Color(0xFF1A1A1A) else Color.White

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                val icon = when (item.type) {
                    NotificationType.GENERAL -> Icons.Filled.Notifications
                    NotificationType.NEW_MEETING -> Icons.Filled.School
                }
                Icon(icon, contentDescription = null, tint = iconTint)
            }

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                    maxLines = 1, overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = item.body,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2, overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(Modifier.width(8.dp))

            AssistChip(
                onClick = { },
                label = { Text(item.sendAt) },
                colors = AssistChipDefaults.assistChipColors(
                    labelColor = fg,
                    containerColor = bg.copy(alpha = 0.2f)
                )
            )
        }
    }
}

@Composable
private fun colorsForType(type: NotificationType): Pair<Color, Color> {
    val s = MaterialTheme.colorScheme
    return when (type) {
        NotificationType.GENERAL -> s.primaryContainer to s.onPrimaryContainer
        NotificationType.NEW_MEETING -> s.tertiaryContainer to s.onTertiaryContainer
    }
}

/* ===== Tema Material You con fallback para API < 31 ===== */
@Composable
private fun AppTheme(
    dark: Boolean = isSystemInDarkTheme(),
    useDynamic: Boolean = true,
    content: @Composable () -> Unit
) {
    val ctx = LocalContext.current
    val dynamicAvailable = useDynamic && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val colors =
        if (dynamicAvailable) {
            if (dark) dynamicDarkColorScheme(ctx) else dynamicLightColorScheme(ctx)
        } else {
            if (dark) darkColorScheme() else lightColorScheme()
        }

    MaterialTheme(colorScheme = colors, content = content)
}
