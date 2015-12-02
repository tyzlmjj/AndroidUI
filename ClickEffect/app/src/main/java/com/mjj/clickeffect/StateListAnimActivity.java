package com.mjj.clickeffect;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mjj.clickeffect.adapter.StateListAnimAdapter;

public class StateListAnimActivity extends ABSActivity {
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statelistanim);
		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setAdapter(new StateListAnimAdapter());
		
	}
	
	public void itemClick(View v){
		
	}

}
