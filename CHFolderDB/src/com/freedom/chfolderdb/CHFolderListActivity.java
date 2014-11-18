package com.freedom.chfolderdb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;

import com.freedom.chfolderdb.model.CHFolder;
import com.freedom.chfolderdb.utils.CHFolderListAdapter;
import com.freedom.chfolderdb.utils.CHFolderListInterceptor;
import com.freedom.chfolderdb.utils.CHFolderSQLiteUtils;

public class CHFolderListActivity extends ListActivity {

	String TAG = "CHFolder";
	private CHFolderListAdapter adapter = null;
	CHFolderSQLiteUtils db = new CHFolderSQLiteUtils(this);
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.chfolder_list);
		adapter = new CHFolderListAdapter(this, getData());
		setListAdapter(adapter);
		CHFolderListInterceptor tlv = (CHFolderListInterceptor) getListView();
		tlv.setDropListener(onDrop);
		tlv.getAdapter();

	}

	private CHFolderListInterceptor.DropListener onDrop = new CHFolderListInterceptor.DropListener() {
		@SuppressWarnings("unchecked")
		@Override
		public void drop(int from, int to) {
			@SuppressWarnings("rawtypes")
			Map item = adapter.getItem(from);
			adapter.remove(item);
			adapter.insert(item, to);

		}
	};
	
	private ArrayList<Map<String, Object>> getData() {
		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		CHFolder folder = db.queryByName("默认文件夹");
		Log.i(TAG, folder.toString());
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("chfolder_name", folder.getFolder_name());
		map.put("chfolder_size", "共0张图片，约占用"+folder.getFolder_size()+"M");
		map.put("chfolder_image", R.drawable.folder);
		map.put("chfolder_forword", R.drawable.forword);
		list.add(map);
		
		return list;
	}

}