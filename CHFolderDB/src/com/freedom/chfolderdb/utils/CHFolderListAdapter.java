package com.freedom.chfolderdb.utils;

import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.freedom.chfolderdb.R;
import com.freedom.chfolderdb.model.CHFolderHolder;

public class CHFolderListAdapter extends ArrayAdapter<Map<String, Object>> {
	
	private ArrayList<Map<String, Object>> array;
	
	public CHFolderListAdapter(Context context, ArrayList<Map<String, Object>> array){
		super(context, R.layout.chfolder_item, array);
		this.array = array;
	}
	
	public ArrayList<Map<String, Object>> getList() {
		return array;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CHFolderHolder holder;
		View row = convertView;
		if (row == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			row = inflater.inflate(R.layout.chfolder_item, parent,false);
			holder = new CHFolderHolder();
			holder.chfolder_name = (TextView) row.findViewById(R.id.chfolder_name);
			holder.chfolder_name.setText(array.get(position).get("chfolder_name").toString());
			holder.chfolder_size = (TextView) row.findViewById(R.id.chfolder_size);
			holder.chfolder_size.setText(array.get(position).get("chfolder_size").toString());
			holder.chfolder_image = (ImageView) row.findViewById(R.id.chfolder_image);
			holder.chfolder_image.setImageResource(Integer.valueOf(array.get(position)
					.get("chfolder_image").toString()));
			holder.chfolder_forword = (ImageView) row.findViewById(R.id.chfolder_forword);
			holder.chfolder_forword.setImageResource(Integer.valueOf(array
					.get(position).get("chfolder_forword").toString()));
			row.setTag(holder);
		} else {
			holder = (CHFolderHolder) row.getTag();
		}
		final int i = position;
		holder.chfolder_forword.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				

			}
		});
		return row;
	}

}
