package com.kaosh.calllog;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;

public class SMSLog {

	static public void update (Context context) {

		final SharedPreferences rate_settings = context.getSharedPreferences("Rate", 0);
		final SharedPreferences log = context.getSharedPreferences("log", 0);
		int vendorId = rate_settings.getInt("vendorId", 0);
		float innet = rate_settings.getFloat("innetsms", 1.1707f);
		float outnet = rate_settings.getFloat("outnetsms", 1.5353f);

    	Calendar calendar = Calendar.getInstance();
    	calendar.set(Calendar.DAY_OF_MONTH, 1);
    	calendar.set(Calendar.HOUR_OF_DAY, 0);
    	calendar.set(Calendar.MINUTE, 0);
    	calendar.set(Calendar.SECOND, 0);
    	long start_millis = calendar.getTimeInMillis();
    	
		String fields[] = {"date", "address"};
		
		String selection = "date>=?";
		
		String selectionArgs[] = {
				"" + start_millis
		};
		
		Cursor c = context.getContentResolver().query(
				Uri.parse("content://sms/sent/"),
				fields,
				selection,
				selectionArgs,
				"date DESC"
				);
		
		int numberColumn = c.getColumnIndex("address");
		
		float costInnet = 0;
		float costOutnet = 0;
		int countInnet = 0;
		int countOutnet = 0;
		int countMMS = 0;
		
		ArrayList <NPObj> nps = new ArrayList <NPObj>();
		NP.get_nps(context, nps);

		if (c.moveToFirst()) {
			do {
				String number = c.getString(numberColumn);
				int callVendorId = PhoneUtils.getVendorIdByNumber(number, nps);
			
				if (callVendorId == vendorId) {
					costInnet += innet;
					countInnet += 1;
				}
				else {
					costOutnet += outnet;
					countOutnet += 1;
				}
			} while (c.moveToNext());
		}
		
		c.close();
		
		log.edit()
		.putFloat("costInnetSMS", costInnet)
		.putFloat("costOutnetSMS", costOutnet)
		.putInt("countInnetSMS", countInnet)
		.putInt("countOutnetSMS", countOutnet)
		.putInt("countMMS", countMMS)
		.commit();
	}
}
