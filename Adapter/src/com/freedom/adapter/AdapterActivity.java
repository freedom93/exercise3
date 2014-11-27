package com.freedom.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;

public class AdapterActivity extends Activity {
	
	private ListView mList;
	private MyAdapter adapter;
	private List<String> data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.folder_list_container);
		mList = (ListView)findViewById(R.id.folder_list);
		data = new ArrayList<String>(Arrays.asList("hello","world","sorry"));
		adapter = new MyAdapter(this, data);
		mList.setAdapter(adapter);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.adapter, menu);
		return true;
	}

}
