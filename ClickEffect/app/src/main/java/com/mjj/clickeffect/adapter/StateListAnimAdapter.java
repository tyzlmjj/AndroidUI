package com.mjj.clickeffect.adapter;



import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mjj.clickeffect.R;

public class StateListAnimAdapter extends RecyclerView.Adapter<ViewHolder> {

	@Override
	public int getItemCount() {
		return 100;
	}

	@Override
	public void onBindViewHolder(ViewHolder arg0, int arg1) {
		
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int arg1) {
		
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_statelist_item,parent,false);
		return new ViewHolder(view) {};
	}

	
	
	

}
