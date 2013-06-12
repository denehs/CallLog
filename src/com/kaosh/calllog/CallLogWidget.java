/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kaosh.calllog;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.kaosh.calllog.R;

/**
 * Define a simple widget that shows the Wiktionary "Word of the day." To build
 * an update we spawn a background {@link Service} to perform the API queries.
 */
public class CallLogWidget extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // To prevent any ANR timeouts, we perform the update in a service
        context.startService(new Intent(context, UpdateService.class));
    }

    public static class UpdateService extends Service {
        @Override
        public void onStart(Intent intent, int startId) {
            // Build the widget update for today
            RemoteViews updateViews = buildUpdate(this);

            // Push update for this widget to the home screen
            ComponentName thisWidget = new ComponentName(this, CallLogWidget.class);
            AppWidgetManager manager = AppWidgetManager.getInstance(this);
            manager.updateAppWidget(thisWidget, updateViews);
        }

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        /**
         * Build a widget update to show the current Wiktionary
         * "Word of the day." Will block until the online API returns.
         */
        public RemoteViews buildUpdate(Context context) {
			// Build an update that holds the updated widget contents
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
			
			Intent viewMapIntent = new Intent(context, CallLogActivity.class);
			PendingIntent callLogPendingIntent = PendingIntent.getActivity(context,
					0 /* no requestCode */, viewMapIntent, 0 /* no flags */);
			views.setOnClickPendingIntent(R.id.widget, callLogPendingIntent);

			PhoneLog.update(context);
			SharedPreferences log = getSharedPreferences("log", 0);

			float costInnet = log.getFloat("costInnet", 0f);
			float costOutnet = log.getFloat("costOutnet", 0f);
			float costOther = log.getFloat("costOther", 0f);
			float costHot = log.getFloat("costHot", 0f);
			float costInnetSMS = log.getFloat("costInnetSMS", 0f);
			float costOutnetSMS = log.getFloat("costOutnetSMS", 0f);
			int durationInnet = log.getInt("durationInnet", 0);
			int durationOutnet = log.getInt("durationOutnet", 0);
			int durationOther = log.getInt("durationOther", 0);
			int durationHot = log.getInt("durationHot", 0);
			
			int duration = durationInnet + durationOutnet + durationOther + durationHot;
			float cost = costInnet + costOutnet + costOther + costHot + costInnetSMS + costOutnetSMS;
			
			String costText = String.format("$%.2f", cost);
			int s = duration;
			int m = s/60;
			int h = m/60;
			s %= 60;
			m %= 60;
			
			String durationText = String.format("%02d:%02d:%02d", h, m, s);
			
			views.setTextViewText(R.id.duration, durationText);
			views.setTextViewText(R.id.cost, costText);
			return views;
        }
    }
}
