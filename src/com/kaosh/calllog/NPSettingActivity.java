package com.kaosh.calllog;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.kaosh.calllog.R;

public class NPSettingActivity extends Activity {
	
	NPAdapter mAdapter;
	ArrayList<NPObj> mData;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.np_setting);
		
		mData = new ArrayList<NPObj>();
		NP.get_nps(this, mData);
		mAdapter = new NPAdapter(this, mData, R.layout.np_item);
		
		ListView listView = (ListView) findViewById (R.id.listView);
		listView.setAdapter(mAdapter);
		
		final Spinner vendorSpinner = (Spinner) findViewById (R.id.vendorSpinner);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item, PhoneUtils.sVendors);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		vendorSpinner.setAdapter(adapter);
		
		Button addButton = (Button) findViewById (R.id.addButton);
		final EditText numberText = (EditText) findViewById (R.id.number);
		
		addButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				int vendorId = vendorSpinner.getSelectedItemPosition();
				String number = numberText.getText().toString();
				
				NP.add(NPSettingActivity.this, number, vendorId);
				vendorSpinner.setSelection(0);
				numberText.setText("");
				
				NP.get_nps(NPSettingActivity.this, mData);
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
    			NP.delete(this, mData.get(info.position).mNumber);
    			NP.get_nps(this, mData);
    			mAdapter.notifyDataSetChanged();
    			return true;
    		default:
    			return super.onContextItemSelected(item);
    	}
    }
}
