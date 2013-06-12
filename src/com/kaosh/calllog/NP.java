package com.kaosh.calllog;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class NP {

	private static DatabaseOpenHelper helper = null;
	
	private static void check_helper (Context context) {
		if (helper==null) {
			helper = new DatabaseOpenHelper(context);
		}
	}
	
	public static boolean add (Context context, String number, int vendorId) {
		check_helper(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put("number", number);
		values.put("vendorId", vendorId);
		return (db.insert(DatabaseOpenHelper.NP_TABLE_NAME, null, values) > 0);
	}
	
	public static int delete (Context context, String number) {
		check_helper(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		return db.delete(DatabaseOpenHelper.NP_TABLE_NAME, "number=?", new String[] {number});
	}
	
	public static int get_nps (Context context, ArrayList<NPObj> nps) {
		check_helper(context);
		SQLiteDatabase db = helper.getReadableDatabase();
		
		Cursor c = db.query(
				DatabaseOpenHelper.NP_TABLE_NAME, 
				new String[] {"number", "vendorId"},
				null,
				null,
				null,
				null,
				"number ASC");
		
		nps.clear();
		c.moveToFirst();
		for (int i=0 ;i<c.getCount();i++) {
			String number = c.getString(0);
			int vendorId = c.getInt(1);
			nps.add(new NPObj (number, vendorId));
			c.moveToNext();
		}
		int count = c.getCount();
		c.close();
		return count;
	}
	

}
