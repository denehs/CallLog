package com.kaosh.calllog;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class HotlineSettingActivity extends Activity {
	
	HotlineAdapter mAdapter;
	ArrayList<HotlineObj> mData;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hotline_setting);
		
		mData = new ArrayList<HotlineObj>();
		Hotline.get_hotlines(this, mData);
		mAdapter = new HotlineAdapter(this, mData, R.layout.hotline_item);
		
		ListView listView = (ListView) findViewById (R.id.listView);
		listView.setAdapter(mAdapter);
		
		Button addButton = (Button) findViewById (R.id.addButton);
		final EditText numberText = (EditText) findViewById (R.id.number);
		final EditText rateText = (EditText) findViewById (R.id.rate);
		
		addButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				float rate = Float.parseFloat(rateText.getText().toString());
				String number = numberText.getText().toString();
				
				Hotline.add(HotlineSettingActivity.this, number, rate);
				numberText.setText("");
				rateText.setText("");

				Hotline.get_hotlines(HotlineSettingActivity.this, mData);
				mAdapter.notifyDataSetChanged();
			}
			
		});

        registerForContextMenu (listView);
	}


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    	super.onCreateContextMenu(menu, v, menuInfo);
    	getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    	switch (item.getItemId()) {
    		case R.id.delete:
    			Hotline.delete(this, mData.get(info.position).mNumber);
				Hotline.get_hotlines(HotlineSettingActivity.this, mData);
    			mAdapter.notifyDataSetChanged();
    			return true;
    		default:
    			return super.onContextItemSelected(item);
    	}
    }
}
