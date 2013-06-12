package com.kaosh.calllog;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class Hotline {

	private static DatabaseOpenHelper helper = null;
	
	private static void check_helper (Context context) {
		if (helper==null) {
			helper = new DatabaseOpenHelper(context);
		}
	}
	
	public static boolean add (Context context, String number, float rate) {
		check_helper(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put("number", number);
		values.put("rate", rate);
		return (db.insert(DatabaseOpenHelper.HOTLINE_TABLE_NAME, null, values) > 0);
	}
	
	public static int delete (Context context, String number) {
		check_helper(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		return db.delete(DatabaseOpenHelper.HOTLINE_TABLE_NAME, "number=?", new String[] {number});
	}
	
	public static int get_hotlines (Context context, ArrayList<HotlineObj> hotlines) {
		check_helper(context);
		SQLiteDatabase db = helper.getReadableDatabase();
		
		Cursor c = db.query(
				DatabaseOpenHelper.HOTLINE_TABLE_NAME, 
				new String[] {"number", "rate"},
				null,
				null,
				null,
				null,
				"number ASC");
		
		hotlines.clear();
		c.moveToFirst();
		for (int i=0 ;i<c.getCount();i++) {
			String number = c.getString(0);
			float rate = c.getFloat(1);
			hotlines.add(new HotlineObj (number, rate));
			c.moveToNext();
		}
		int count = c.getCount();
		c.close();
		return count;
	}
	

}
