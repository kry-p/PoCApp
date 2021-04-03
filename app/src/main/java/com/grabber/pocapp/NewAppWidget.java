package com.grabber.pocapp;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.RemoteViews;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.grabber.pocapp.database.AppDatabase;
import com.grabber.pocapp.database.pojo.CategoryProp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {

    private static ArrayList<PieEntry> yValues;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        AppDatabase db;

        PieChart pieChart;
        PieData data;
        // initialize db
        db = AppDatabase.getDatabase(context);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
//        views.addView();
        // add view
        pieChart = new PieChart(context);

        // set pie chart
        pieChart.setUsePercentValues(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.getViewPortHandler().setChartDimens(600,600);

        PieDataSet dataSet;
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Runnable getUpdate = () -> {
            yValues = new ArrayList<>();
            List<CategoryProp> temp = db.propDao().getByCategory();

            for (CategoryProp item : temp) {
                yValues.add(new PieEntry((float) (item.getAmount()), item.getCategory()));
            }
        };

        Future<?> future = executor.submit(getUpdate);

        try {
            future.get();
            dataSet = new PieDataSet(yValues, "");
            dataSet.setSliceSpace(3f);
            dataSet.setSelectionShift(5f);
            dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

            data = new PieData((dataSet));
            data.notifyDataChanged();
            data.setValueTextSize(10f);
            data.setValueTextColor(Color.BLACK);

            pieChart.notifyDataSetChanged();
            pieChart.setData(data);

            pieChart.invalidate();
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }

        Bitmap chartBitmap = pieChart.getChartBitmap();

        Handler delayHandler = new Handler();
        delayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                views.setImageViewBitmap(R.id.widgetImage, chartBitmap);
            }
        }, 1000);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}