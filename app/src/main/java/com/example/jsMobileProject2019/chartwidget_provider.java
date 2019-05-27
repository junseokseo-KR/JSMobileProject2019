package com.example.jsMobileProject2019;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.github.mikephil.charting.charts.RadarChart;

/**
 * Implementation of App Widget functionality.
 */
public class chartwidget_provider extends AppWidgetProvider {
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.chart_widget);
        views.setImageViewResource(R.id.widgetChart, R.color.white);
        views.setTextViewText(R.id.widgetText, "로그인하여 차트를 등록해보세요!");
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
//        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.chart_widget);
//        remoteViews.setImageViewResource(R.id.viewChart, R.drawable.testimg);
//        Intent intent = new Intent(context, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        remoteViews.setOnClickPendingIntent(R.id.widgetBtn, pendingIntent);
//        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
//        super.onUpdate(context, appWidgetManager,appWidgetIds);
        // There may be multiple widgets active, so update all of  them
//        for (int appWidgetId : appWidgetIds) {
//            updateAppWidget(context, appWidgetManager, appWidgetId);
//        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.chart_widget);
        super.onReceive(context, intent);
        if(intent.getExtras().get("bit")!=null){
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, chartwidget_provider.class));
//            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widgetBtn);
            remoteViews.setImageViewBitmap(R.id.widgetChart, (Bitmap)intent.getExtras().get("bit"));
            remoteViews.setTextViewText(R.id.widgetText, "");
//            appWidgetManager.updateAppWidget(appWidgetId, views);
            appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
        }
    }
}

