package com.yl.MyTracks.adapter;

import java.util.List;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yl.MyTracks.db.bean.TracksBean;
import com.yl.MyTracks.R;

public class TrackListAdapter extends BaseAdapter{
	public final String TAG = "TrackListAdapter";
	
	private List<TracksBean> itemBeanList;
	private Activity activity;
	
	public int getCount() {
		return itemBeanList.size();
	}
	
	public TrackListAdapter(Activity activity,List<TracksBean> itemBeanList) {
			this.itemBeanList = itemBeanList;
			this.activity = activity;
	}
	
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		// 设定布局 the views from XML
		View rowView = convertView;
		
		if (rowView == null) {
			LayoutInflater inflater = activity.getLayoutInflater();
			try {
				rowView = inflater.inflate(R.layout.track_row,null);
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.getMessage());
			}
		}
		
		try {
			TextView nameTv = (TextView)rowView.findViewById(R.id.name);
			TextView createdTv = (TextView)rowView.findViewById(R.id.created);
			TextView descTv = (TextView)rowView.findViewById(R.id.desc);
			
			TracksBean tracksBean = this.itemBeanList.get(position);
			
			nameTv.setText(tracksBean.getName());
			createdTv.setText(tracksBean.getCreated_at());
			descTv.setText(tracksBean.getDesc());
			rowView.setTag(tracksBean);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.getMessage());
		}

		return rowView;
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
