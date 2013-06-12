package com.kaosh.calllog;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

public class PhoneUtils {
	static String sVendors[] = {
		"中華電信", 		//0
		"遠傳電信", 		//1
		"和信電訊", 		//2
		"台灣大哥大",		//3
		"東信電訊",		//4
		"泛亞電訊",		//5
		"大眾電訊",		//6
		"亞太行動寬頻"	//7
	};
	
	static String sVendorPrefixs[][] = {
		{"0910", "0911", "0912", "0919", "0921", "0928", "0932", "0933", "0934", "0937", "0963", "0972"},
		{"0916", "0917", "0926", "0930", "0936", "0954", "0955", "09310", "09311", "09312", "09313", "09605", "09606"},
		{"0913", "0915", "0925", "0927", "0938"},
		{"0914", "0918", "0920", "0922", "0935", "0939", "0952", "0953", "0958", "0961", "0970"},
		{"0923", "09314", "09315", "09316"},
		{"0924", "0929", "0956", "0971", "09317", "09318", "09319", "09600", "09601", "09602", "09603", "09604"},
		{"0968"},
		{"0982"}
	};
	
	//-1: 市話, 800: 0800
	static int getVendorIdByNumber (String number, ArrayList <NPObj> nps) {
		int defaultVendorId = -1;
		
		if (number.startsWith("0800"))
			return 800;

		if (nps!=null) {
			for (int i=0 ;i<nps.size();i++) {
				NPObj np = nps.get(i);
				if (number.equals(np.mNumber)) {
					return np.mVendorId;
				}
			}
		}
		
		for (int i=0 ;i<sVendors.length ;i++) {
			for (int j=0 ;j<sVendorPrefixs[i].length ;j++) {
				if (number.startsWith(sVendorPrefixs[i][j]))
					return i;
			}
		}
		
		return defaultVendorId;
	}
	
	static String getNameByNumber (String number, Context context) {

	    String WHERE_CONDITION = ContactsContract.Data.MIMETYPE + " = '" +   ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "'" +
	    						" AND " + ContactsContract.Data.DATA1 + " = '" + number + "'";
	    String[] PROJECTION = {ContactsContract.Data.DISPLAY_NAME, ContactsContract.Data.DATA1};

	    Cursor c = context.getContentResolver().query(
	            ContactsContract.Data.CONTENT_URI,
	            PROJECTION,
	            WHERE_CONDITION,
	            null,
	            null);
	    
	    if (c.moveToFirst()) {
	    	number = c.getString(0);
	    }
	    
	    return number;
	}
}
