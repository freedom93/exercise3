package com.freedom.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.w3c.dom.Text;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;
import android.widget.TextView;

public class AdapterActivity extends Activity {
	
	private ListView mList;
//	private MyAdapter adapter;
	private List<String> data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.folder_list_container);
		mList = (ListView)findViewById(R.id.folder_list);
		data = new ArrayList<String>(Arrays.asList("hello","world","sorry"));
//		adapter = new MyAdapter(this, data);
//		mList.setAdapter(adapter);
		//简化过得CommonAdapter已经可以不需要单独写一个Adapter了，直接在Activity里写一个匿名内部类即可
		mList.setAdapter(new CommonAdapter<String>(getApplicationContext(), data, R.layout.list_item) {

			@Override
			public void convert(ViewHolder holder, String item) {
//				TextView view = holder.getView(R.id.folder_name);
//				view.setText(item);
				holder.setText(R.id.folder_name, item);
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.adapter, menu);
		return true;
	}

}
