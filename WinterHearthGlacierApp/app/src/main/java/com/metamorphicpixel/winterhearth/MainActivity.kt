package com.metamorphicpixel.winterhearth

import android.app.WallpaperManager
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.AlarmClock
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat

data class GlacierIconItem(
    val id: String,
    val label: String,
    val drawableRes: Int,
    val launchIntent: () -> Intent
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WinterHearthThemeApp()
        }
    }
}

@Composable
fun WinterHearthThemeApp() {
    val context = LocalContext.current
    var statusBanner by remember { mutableStateOf("Ready to install on your phone — no 3rd-party app needed.") }

    val iconItems = remember {
        listOf(
            GlacierIconItem("phone", "Phone", R.drawable.glacier_phone) {
                Intent(Intent.ACTION_DIAL)
            },
            GlacierIconItem("messages", "Messages", R.drawable.glacier_messages) {
                Intent(Intent.ACTION_MAIN).apply { addCategory(Intent.CATEGORY_APP_MESSAGING) }
            },
            GlacierIconItem("camera", "Camera", R.drawable.glacier_camera) {
                Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA)
            },
            GlacierIconItem("music", "Music", R.drawable.glacier_music) {
                Intent(Intent.ACTION_MAIN).apply { addCategory(Intent.CATEGORY_APP_MUSIC) }
            },
            GlacierIconItem("browser", "Browser", R.drawable.glacier_browser) {
                Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"))
            },
            GlacierIconItem("settings", "Settings", R.drawable.glacier_settings) {
                Intent(Settings.ACTION_SETTINGS)
            },
            GlacierIconItem("maps", "Maps", R.drawable.glacier_maps) {
                Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q="))
            },
            GlacierIconItem("clock", "Clock", R.drawable.glacier_clock) {
                Intent(AlarmClock.ACTION_SHOW_ALARMS)
            },
            GlacierIconItem("notes", "Notes", R.drawable.glacier_notes) {
                Intent(Intent.ACTION_MAIN).apply { addCategory(Intent.CATEGORY_APP_CALENDAR) }
            },
            GlacierIconItem("mail", "Mail", R.drawable.glacier_mail) {
                Intent(Intent.ACTION_MAIN).apply { addCategory(Intent.CATEGORY_APP_EMAIL) }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF070C14), Color(0xFF0B121B), Color(0xFF111C2B))
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 24.dp, bottom = 36.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(22.dp))
                        .background(Color(0xFF0B121B))
                        .border(1.dp, Color(0x55A5F3FC), RoundedCornerShape(22.dp))
                        .padding(18.dp)
                ) {
                    Text(
                        text = "WINTER HEARTH & GLACIER",
                        color = Color(0xFFFFB347),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.4.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Standalone Theme Studio",
                        color = Color(0xFFF0F6FC),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = statusBanner,
                        color = Color(0xFFA5F3FC),
                        fontSize = 13.sp
                    )
                }
            }

            // 1. Wallpaper Card
            item {
                ThemeActionCard(
                    title = "1. Apply Winter Hearth Wallpaper",
                    subtitle = "1-tap se Home & Lock screen wallpaper set karein",
                    buttonText = "Apply Wallpaper",
                    accentColor = Color(0xFF38BDF8),
                    onClick = {
                        try {
                            WallpaperManager.getInstance(context)
                                .setResource(R.drawable.winter_hearth_wallpaper_9x16)
                            statusBanner = "Wallpaper applied successfully!"
                            Toast.makeText(context, "Wallpaper applied!", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            statusBanner = "Wallpaper error: ${e.message}"
                        }
                    }
                )
            }

            // 2. Native Melting Glacier Battery Widget
            item {
                ThemeActionCard(
                    title = "2. Pin Melting Glacier Battery Widget",
                    subtitle = "Native Android widget (KWGT ki koi zaroorat nahi)",
                    buttonText = "Add Battery Widget",
                    accentColor = Color(0xFFFF6B1A),
                    onClick = {
                        val appWidgetManager = AppWidgetManager.getInstance(context)
                        val provider = ComponentName(context, GlacierBatteryWidgetProvider::class.java)
                        if (appWidgetManager.isRequestPinAppWidgetSupported) {
                            appWidgetManager.requestPinAppWidget(provider, null, null)
                            statusBanner = "Confirm 'Add' popup to pin Battery Widget on Home Screen."
                        } else {
                            statusBanner = "Long-press Home Screen -> Widgets -> Melting Glacier Battery."
                        }
                    }
                )
            }

            // 3. Dynamic Island Permissions
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color(0xFF0B121B))
                        .border(1.dp, Color(0x33A5F3FC), RoundedCornerShape(18.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        text = "3. Enable Campfire Dynamic Island",
                        color = Color(0xFFF0F6FC),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Live notification pill ke liye dono permissions on karein:",
                        color = Color(0xFF94A3B8),
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedButton(
                            onClick = {
                                val intent = Intent(
                                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                    Uri.parse("package:${context.packageName}")
                                )
                                context.startActivity(intent)
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("1. Overlay", color = Color(0xFFA5F3FC), fontSize = 12.sp)
                        }
                        OutlinedButton(
                            onClick = {
                                context.startActivity(
                                    Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
                                )
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("2. Notifications", color = Color(0xFFFFB347), fontSize = 12.sp)
                        }
                    }
                }
            }

            // 4. Glacier App Icons Grid
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color(0xFF0B121B))
                        .border(1.dp, Color(0x33A5F3FC), RoundedCornerShape(18.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        text = "4. Add 10 Glacier Icons to Home Screen",
                        color = Color(0xFFF0F6FC),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Kisi bhi icon par tap karke direct Home Screen par pin karein:",
                        color = Color(0xFF94A3B8),
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(5),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(185.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(iconItems) { item ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable {
                                        pinGlacierShortcut(context, item) { msg ->
                                            statusBanner = msg
                                        }
                                    }
                                    .padding(4.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = item.drawableRes),
                                    contentDescription = item.label,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = item.label,
                                    color = Color(0xFFF0F6FC),
                                    fontSize = 11.sp,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ThemeActionCard(
    title: String,
    subtitle: String,
    buttonText: String,
    accentColor: Color,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFF0B121B))
            .border(1.dp, Color(0x33A5F3FC), RoundedCornerShape(18.dp))
            .padding(16.dp)
    ) {
        Text(
            text = title,
            color = Color(0xFFF0F6FC),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = subtitle, color = Color(0xFF94A3B8), fontSize = 13.sp)
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = accentColor,
                contentColor = Color(0xFF070C14)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = buttonText, fontWeight = FontWeight.Bold)
        }
    }
}

private fun pinGlacierShortcut(
    context: Context,
    item: GlacierIconItem,
    onResult: (String) -> Unit
) {
    if (!ShortcutManagerCompat.isRequestPinShortcutSupported(context)) {
        onResult("Your launcher does not support pinning shortcuts.")
        return
    }
    val shortcut = ShortcutInfoCompat.Builder(context, "glacier_${item.id}")
        .setShortLabel(item.label)
        .setIcon(IconCompat.createWithResource(context, item.drawableRes))
        .setIntent(item.launchIntent())
        .build()

    ShortcutManagerCompat.requestPinShortcut(context, shortcut, null)
    onResult("Tap 'Add' on the popup to place ${item.label} Glacier icon on Home Screen!")
}
