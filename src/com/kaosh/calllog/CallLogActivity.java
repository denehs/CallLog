package com.kaosh.calllog;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.kaosh.calllog.R;

public class CallLogActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calllog);
		
		PhoneLog.update(this);
		SMSLog.update(this);

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
		int countInnetSMS = log.getInt("countInnetSMS", 0);
		int countOutnetSMS = log.getInt("countOutnetSMS", 0);
		
		TextView innetDurationText = (TextView) findViewById (R.id.innetDuration);
		TextView outnetDurationText = (TextView) findViewById (R.id.outnetDuration);
		TextView otherDurationText = (TextView) findViewById (R.id.otherDuration);
		TextView hotDurationText = (TextView) findViewById (R.id.hotDuration);
		
		TextView innetCostText = (TextView) findViewById (R.id.innetCost);
		TextView outnetCostText = (TextView) findViewById (R.id.outnetCost);
		TextView otherCostText = (TextView) findViewById (R.id.otherCost);
		TextView hotCostText = (TextView) findViewById (R.id.hotCost);
		
		TextView innetSMSText = (TextView) findViewById (R.id.innetSMS);
		TextView outnetSMSText = (TextView) findViewById (R.id.outnetSMS);
		
		TextView sumText = (TextView) findViewById (R.id.sum);

		innetDurationText.setText(Utils.durationToString(durationInnet));
		outnetDurationText.setText(Utils.durationToString(durationOutnet));
		otherDurationText.setText(Utils.durationToString(durationOther));
		hotDurationText.setText(Utils.durationToString(durationHot));
		
		innetCostText.setText(Utils.costToString(costInnet));
		outnetCostText.setText(Utils.costToString(costOutnet));
		otherCostText.setText(Utils.costToString(costOther));
		hotCostText.setText(Utils.costToString(costHot));

		innetSMSText.setText(Utils.costToString(costInnetSMS) + " (" + countInnetSMS + ")");
		outnetSMSText.setText(Utils.costToString(costOutnetSMS) + " (" + countOutnetSMS + ")");
		
		sumText.setText(Utils.costToString(costInnet + costOutnet + costOther + costHot + costInnetSMS + costOutnetSMS));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.option_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.rate_setting:
	    	startActivity (new Intent().setClass(this, RateSettingActivity.class));
	        return true;
	    case R.id.np_setting:
	    	startActivity (new Intent().setClass(this, NPSettingActivity.class));
	    	return true;
	    case R.id.hot_setting:
	    	startActivity (new Intent().setClass(this, HotlineSettingActivity.class));
	    	return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
}
