package com.kaosh.calllog;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;

public class PhoneLog {

	static public void update (Context context) {

		final SharedPreferences rate_settings = context.getSharedPreferences("Rate", 0);
		final SharedPreferences log = context.getSharedPreferences("log", 0);
		int vendorId = rate_settings.getInt("vendorId", 0);
		float innet = rate_settings.getFloat("innet", 0.06f);
		float outnet = rate_settings.getFloat("outnet", 0.1128f);
		float other = rate_settings.getFloat("other", 0.1056f);

    	Calendar calendar = Calendar.getInstance();
    	calendar.set(Calendar.DAY_OF_MONTH, 1);
    	calendar.set(Calendar.HOUR_OF_DAY, 0);
    	calendar.set(Calendar.MINUTE, 0);
    	calendar.set(Calendar.SECOND, 0);
    	long start_millis = calendar.getTimeInMillis();
    	
		String fields[] = {android.provider.CallLog.Calls.NUMBER, android.provider.CallLog.Calls.DURATION};
		
		String selection = android.provider.CallLog.Calls.TYPE + "=?" +
							" AND " + android.provider.CallLog.Calls.DATE + ">=?";
		
		String selectionArgs[] = {
				"" + android.provider.CallLog.Calls.OUTGOING_TYPE,
				"" + start_millis
		};
		
		Cursor c = context.getContentResolver().query(
				android.provider.CallLog.Calls.CONTENT_URI,
				fields,
				selection,
				selectionArgs,
				android.provider.CallLog.Calls.DATE + " DESC"
				);
		
		int numberColumn = c.getColumnIndex(android.provider.CallLog.Calls.NUMBER);
		int durationColumn = c.getColumnIndex(android.provider.CallLog.Calls.DURATION);
		
		float costInnet = 0;
		float costOutnet = 0;
		float costOther = 0;
		float costHot = 0;
		int durationInnet = 0;
		int durationOutnet = 0;
		int durationOther = 0;
		int durationHot = 0;
		
		ArrayList <NPObj> nps = new ArrayList <NPObj>();
		NP.get_nps(context, nps);

		ArrayList <HotlineObj> hotlines = new ArrayList <HotlineObj>();
		Hotline.get_hotlines(context, hotlines);
		
		if (c.moveToFirst()) {
			do {
				String number = c.getString(numberColumn);
				long duration = c.getLong(durationColumn);
				int callVendorId = PhoneUtils.getVendorIdByNumber(number, nps);
				
				boolean isHotline = false;
				for (int i=0 ;i<hotlines.size() ;i++) {
					HotlineObj hotline = hotlines.get(i);
					if (number.equals(hotline.mNumber)) {
						costHot += hotline.mRate * duration;
						durationHot += duration;
						isHotline = true;
						break;
					}
				}
				
				if (isHotline)
					continue;
				
				if (callVendorId==800)
					continue;
				
				if (callVendorId == -1) {
					durationOther += duration;
				}
				else if (callVendorId == vendorId) {
					durationInnet += duration;
				}
				else {
					durationOutnet += duration;
				}
			} while (c.moveToNext());
		}
		
		costOther = other * durationOther;
		costInnet = innet * durationInnet;
		costOutnet = outnet * durationOutnet;
		
		c.close();
		
		log.edit()
		.putFloat("costInnet", costInnet)
		.putFloat("costOutnet", costOutnet)
		.putFloat("costOther", costOther)
		.putFloat("costHot", costHot)
		.putInt("durationInnet", durationInnet)
		.putInt("durationOutnet", durationOutnet)
		.putInt("durationOther", durationOther)
		.putInt("durationHot", durationHot)
		.commit();
	}
}
