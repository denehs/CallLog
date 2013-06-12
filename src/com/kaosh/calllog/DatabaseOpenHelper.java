package com.kaosh.calllog;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "calllog.db";
	private static final int DATABASE_VERSION = 1;

	public static final String NP_TABLE_NAME = "nps";
	public static final String HOTLINE_TABLE_NAME = "hotliness";
	
	private static final String NP_TABLE_CREATE = 
				"CREATE TABLE " + NP_TABLE_NAME + " (" +
				"number TEXT, " +
				"vendorId INT" +
				");";

	private static final String HOTLINE_TABLE_CREATE = 
				"CREATE TABLE " + HOTLINE_TABLE_NAME + " (" +
				"number TEXT, " +
				"rate FLOAT" +
				");";
	
	DatabaseOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(NP_TABLE_CREATE);
		db.execSQL(HOTLINE_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
