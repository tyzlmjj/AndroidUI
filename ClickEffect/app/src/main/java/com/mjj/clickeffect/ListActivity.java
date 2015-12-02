package com.mjj.clickeffect;


import android.os.Bundle;
import android.widget.ListView;

import com.mjj.clickeffect.adapter.ListAdapter;

public class ListActivity extends ABSActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		ListView list = (ListView) findViewById(R.id.listView);
		list.setAdapter(new ListAdapter(this));
	}
}
