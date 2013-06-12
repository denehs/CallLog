package com.kaosh.calllog;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.kaosh.calllog.R;

public class RateSettingActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rate_setting);
		
		final SharedPreferences rate_settings = getSharedPreferences("Rate", 0);
		int vendorId = rate_settings.getInt("vendorId", 0);
		
		Spinner vendorSpinner = (Spinner) findViewById (R.id.vendorSpinner);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item, PhoneUtils.sVendors);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		vendorSpinner.setAdapter(adapter);
		vendorSpinner.setSelection(vendorId);
		vendorSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,
					int position, long id) {
				rate_settings.edit().putInt("vendorId", position).commit();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {				
			}
			
		});

		float innet = rate_settings.getFloat("innet", 0.06f);
		float outnet = rate_settings.getFloat("outnet", 0.1128f);
		float other = rate_settings.getFloat("other", 0.1056f);
		float innetsms = rate_settings.getFloat("innetsms", 1.1707f);
		float outnetsms = rate_settings.getFloat("outnetsms", 1.5353f);
		int freetime = rate_settings.getInt("freetime", 0);
		
		EditText innetEditText = (EditText) findViewById (R.id.innet);
		EditText outnetEditText = (EditText) findViewById (R.id.outnet);
		EditText otherEditText = (EditText) findViewById (R.id.other);
		EditText innetSMSEditText = (EditText) findViewById (R.id.innetsms);
		EditText outnetSMSEditText = (EditText) findViewById (R.id.outnetsms);
		EditText freetimeEditText = (EditText) findViewById (R.id.freetime);

		innetEditText.setText (String.valueOf(innet));
		outnetEditText.setText (String.valueOf(outnet));
		otherEditText.setText (String.valueOf(other));
		innetSMSEditText.setText (String.valueOf(innetsms));
		outnetSMSEditText.setText (String.valueOf(outnetsms));
		freetimeEditText.setText (String.valueOf(freetime));
	}

	@Override
	protected void onPause() {

		EditText innetEditText = (EditText) findViewById (R.id.innet);
		EditText outnetEditText = (EditText) findViewById (R.id.outnet);
		EditText otherEditText = (EditText) findViewById (R.id.other);
		EditText innetSMSEditText = (EditText) findViewById (R.id.innetsms);
		EditText outnetSMSEditText = (EditText) findViewById (R.id.outnetsms);
		EditText freetimeEditText = (EditText) findViewById (R.id.freetime);

		final SharedPreferences rate_settings = getSharedPreferences("Rate", 0);
		
		rate_settings.edit()
		.putFloat("innet", Float.parseFloat(innetEditText.getText().toString()))
		.putFloat("outnet", Float.parseFloat(outnetEditText.getText().toString()))
		.putFloat("other", Float.parseFloat(otherEditText.getText().toString()))
		.putFloat("innetsms", Float.parseFloat(innetSMSEditText.getText().toString()))
		.putFloat("outnetsms", Float.parseFloat(outnetSMSEditText.getText().toString()))
		.putInt("freetime", Integer.parseInt(freetimeEditText.getText().toString()))
		.commit();
		super.onPause();
	}
}
