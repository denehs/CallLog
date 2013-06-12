package com.kaosh.calllog;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NPAdapter extends BaseAdapter {
	private int mResource;
	private static LayoutInflater mInflater = null;
	private Context mContext;
	private ArrayList<NPObj> mData;

	public NPAdapter(Context context,
			ArrayList<NPObj> data, int resource) {

		mData = data;
		mResource = resource;
		mContext = context;

		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return mData.size();
	}

	public Object getItem(int position) {
		return mData.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (convertView == null) {
			view = mInflater.inflate(mResource, null);
			HotlineItemCache cache = new HotlineItemCache();
			cache.mNumber = (TextView) view.findViewById(R.id.number);
			cache.mVendor = (TextView) view.findViewById(R.id.vendor);
			view.setTag(cache);
		}

		final HotlineItemCache cache = (HotlineItemCache)view.getTag();
		
		String number = mData.get(position).mNumber;
		String vendor = PhoneUtils.sVendors[mData.get(position).mVendorId];

		number = PhoneUtils.getNameByNumber(number, mContext);
		
		cache.mNumber.setText(number);
		cache.mVendor.setText(vendor);
		
		return view;
	}
	
	final static class HotlineItemCache {
		public TextView mNumber;
		public TextView mVendor;
	}
	
}
