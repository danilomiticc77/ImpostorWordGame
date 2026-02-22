package com.example.impostorgame.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.impostorgame.ui.theme.*

/**
 * Large, gradient-filled primary button for party game use.
 */
@Composable
fun GameButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    gradient: Brush = Brush.horizontalGradient(listOf(PrimaryPurple, PrimaryPurpleLight))
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 52.dp)
                .then(
                    if (enabled) Modifier.background(gradient, RoundedCornerShape(16.dp))
                    else Modifier.background(DarkSurfaceVariant, RoundedCornerShape(16.dp))
                )
                .padding(horizontal = 12.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (enabled) TextWhite else TextMuted,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
        }
    }
}

/**
 * Secondary outlined button style.
 */
@Composable
fun GameOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        border = ButtonDefaults.outlinedButtonBorder(enabled = true),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = PrimaryPurpleLight
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )
    }
}

/**
 * Glass-style card container for sections.
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkSurface.copy(alpha = 0.7f)
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp, DividerColor.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            content = content
        )
    }
}

/**
 * + / - counter for player count and impostor count.
 */
@Composable
fun CounterSelector(
    label: String,
    value: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = TextLight,
            fontWeight = FontWeight.Medium
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Minus button
            IconButton(
                onClick = onDecrement,
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(DarkSurfaceVariant)
            ) {
                Icon(
                    Icons.Default.Remove,
                    contentDescription = "Decrease",
                    tint = TextLight
                )
            }

            // Value display
            Text(
                text = value.toString(),
                style = MaterialTheme.typography.headlineMedium,
                color = PrimaryPurpleLight,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.widthIn(min = 40.dp),
                textAlign = TextAlign.Center
            )

            // Plus button
            IconButton(
                onClick = onIncrement,
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(DarkSurfaceVariant)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Increase",
                    tint = TextLight
                )
            }
        }
    }
}

/**
 * Selectable chip for category selection.
 */
@Composable
fun CategoryChip(
    name: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bgColor = if (selected) PrimaryPurple.copy(alpha = 0.3f) else DarkSurfaceVariant
    val borderColor = if (selected) PrimaryPurpleLight else DividerColor.copy(alpha = 0.5f)
    val textColor = if (selected) PrimaryPurpleLight else TextMuted

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.labelLarge,
            color = textColor,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

/**
 * Hold-to-reveal button — shows content only while pressed.
 * Uses pointer input to detect press/release for reliable hold behavior.
 */
@Composable
fun HoldToRevealButton(
    text: String,
    onPressStart: () -> Unit,
    onPressEnd: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(PrimaryPurple.copy(alpha = 0.8f), AccentCyan.copy(alpha = 0.6f))
                )
            )
            .border(2.dp, PrimaryPurpleLight.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        onPressStart()
                        tryAwaitRelease()
                        onPressEnd()
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge,
            color = TextWhite,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
    }
}

/**
 * Toggle switch with label and optional description.
 */
@Composable
fun ToggleOption(
    label: String,
    description: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = TextLight,
                fontWeight = FontWeight.Medium
            )
            if (description != null) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = PrimaryPurpleLight,
                checkedTrackColor = PrimaryPurple.copy(alpha = 0.4f),
                uncheckedThumbColor = TextMuted,
                uncheckedTrackColor = DarkSurfaceVariant
            )
        )
    }
}
