package com.metamorphicpixel.winterhearth

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.widget.RemoteViews

class GlacierBatteryWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        val manager = AppWidgetManager.getInstance(context)
        val ids = manager.getAppWidgetIds(
            ComponentName(context, GlacierBatteryWidgetProvider::class.java)
        )
        if (ids.isNotEmpty()) {
            onUpdate(context, manager, ids)
        }
    }

    companion object {
        fun updateWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val batteryStatus = context.registerReceiver(
                null,
                IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            )
            val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, 80) ?: 80
            val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, 100) ?: 100
            val pct = (level * 100 / scale.coerceAtLeast(1)).coerceIn(0, 100)

            val status = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
            val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL

            val stateText = if (isCharging) "Re-freezing" else "Melting"
            val spriteRes = when {
                pct > 80 -> R.drawable.glacier_100
                pct > 60 -> R.drawable.glacier_80
                pct > 40 -> R.drawable.glacier_50
                pct > 20 -> R.drawable.glacier_30
                else -> R.drawable.glacier_10
            }

            val views = RemoteViews(context.packageName, R.layout.widget_glacier_battery).apply {
                setTextViewText(R.id.tv_battery_percent, "$pct% · $stateText")
                setProgressBar(R.id.pb_glacier, 100, pct, false)
                setImageViewResource(R.id.iv_glacier_sprite, spriteRes)

                val openAppIntent = Intent(context, MainActivity::class.java)
                val pendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    openAppIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                setOnClickPendingIntent(R.id.widget_root, pendingIntent)
            }

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
