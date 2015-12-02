package com.mjj.clickeffect.adapter;




import android.annotation.SuppressLint;
import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mjj.clickeffect.R;

public class ListAdapter extends BaseAdapter{
	
	
	private Context mContext;
	
	private TypedValue mTypedValue;
	private TypedValue mTypedValue_Borderless;
	
	public ListAdapter(Context context) {
		mContext =context;
		
		mTypedValue = new TypedValue();
		mContext.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
		mTypedValue_Borderless = new TypedValue();
		mContext.getTheme().resolveAttribute(R.attr.selectableItemBackgroundBorderless, mTypedValue_Borderless, true);
	}

	@Override
	public int getCount() {
		return 100;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MyViewHolder viewHolder = null;
		if(convertView == null){
			viewHolder = new MyViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_1,parent,false);
			viewHolder.textView = (TextView) convertView.findViewById(android.R.id.text1);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (MyViewHolder) convertView.getTag();
		}

		if(position%2 == 0){
			convertView.setBackgroundResource(mTypedValue_Borderless.resourceId);
			viewHolder.textView.setText("无边界的波纹效果，5.0以上");
		}else{
			convertView.setBackgroundResource(mTypedValue.resourceId);
			viewHolder.textView.setText("有边界的波纹效果，5.0以上");
		}
		
		return convertView;
	}
	
	class MyViewHolder{
		TextView textView;
	}
	

}
