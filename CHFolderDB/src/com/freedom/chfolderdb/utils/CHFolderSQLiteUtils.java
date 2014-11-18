package com.freedom.chfolderdb.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.freedom.chfolderdb.model.CHFolder;

public class CHFolderSQLiteUtils {
	
	private CHSQLiteOpenHelper dbOpenHelper;
	
	public CHFolderSQLiteUtils(Context context){
		
		this.dbOpenHelper = new CHSQLiteOpenHelper(context);
		
	}
	
	/**
	 * 添加一条记录
	 * @param chFolder
	 */
	public void save(CHFolder chFolder){
		
		dbOpenHelper.getWritableDatabase()
		.execSQL("insert into CHFolder(hashid, folder_name, folder_size) values(?,?,?)", 
				new Object[]{chFolder.getHashid(), chFolder.getFolder_name(), chFolder.getFolder_size()});
	
	}
	
	/**
	 * 删除一条记录
	 * @param chFolder
	 */
	public void delete(Integer id){
		
		dbOpenHelper.getWritableDatabase()
		.execSQL("delete from CHFolder where hashid=?", 
				new Object[]{id});
	
	}
	
	/**
	 * 删除多条记录
	 * @param chFolder
	 */
	public void deleteSeveral(List<Integer> ids){
		
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
	    db.beginTransaction();
		try {
			for(int i = 0; i < ids.size(); i++){
				delete(ids.get(i));
			}
			
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		
	}
	
	/**
	 * 修改一条记录
	 * @param chFolder
	 */
	public void update(CHFolder chFolder){
		
		dbOpenHelper.getWritableDatabase()
		.execSQL("update CHFolder set folder_name=?,folder_size=? where hashid=?", 
				new Object[]{chFolder.getFolder_name(), chFolder.getFolder_size(), chFolder.getHashid()});
		
	}
	
	/**
	 * 根据hashid查询一条记录
	 * @param id
	 * @return
	 */
	public CHFolder query(Integer id){
		Cursor cursor = dbOpenHelper.getReadableDatabase()
				.rawQuery("select * from CHFolder where hashid=?", 
						new String[]{id.toString()});
		if(cursor.moveToFirst()){
			int hashid = cursor.getInt(cursor.getColumnIndex("hashid"));
			String folder_name = cursor.getString(cursor.getColumnIndex("folder_name"));
		    int folder_size = cursor.getInt(cursor.getColumnIndex("folder_size"));
		    return new CHFolder(hashid, folder_name, folder_size);
		}
		cursor.close();
		return null;
		
	}
	/**
	 * 根据文件夹名字查询一条记录
	 * @param id
	 * @return
	 */
	public CHFolder queryByName(String foldername){
		Cursor cursor = dbOpenHelper.getReadableDatabase()
				.rawQuery("select * from CHFolder where folder_name=?", 
						new String[]{foldername});
		if(cursor.moveToFirst()){
			int hashid = cursor.getInt(cursor.getColumnIndex("hashid"));
			String folder_name = cursor.getString(cursor.getColumnIndex("folder_name"));
		    int folder_size = cursor.getInt(cursor.getColumnIndex("folder_size"));
		    return new CHFolder(hashid, folder_name, folder_size);
		}
		cursor.close();
		return null;
		
	}
	/**
	 * 分页SQL语句,跳过前面多少条记录
	 * @param offset
	 * @param maxResult
	 * @return
	 */
	public List<CHFolder> getScrollData(int offset, int maxResult){
		Cursor cursor = dbOpenHelper.getReadableDatabase()
				.rawQuery("select * from CHFolder order by hashid asc limit ?,?", 
						new String[]{String.valueOf(offset), String.valueOf(maxResult)});
		List<CHFolder> folderList = new ArrayList<CHFolder>();
		if(cursor.moveToNext()){
			int hashid = cursor.getInt(cursor.getColumnIndex("hashid"));
			String folder_name = cursor.getString(cursor.getColumnIndex("folder_name"));
		    int folder_size = cursor.getInt(cursor.getColumnIndex("folder_size"));
		    folderList.add(new CHFolder(hashid, folder_name, folder_size));
		}
		cursor.close();
		return folderList;
		
	}
	
	/**
	 * 得到总记录数
	 * @return
	 */
	public long getCount(){
		Cursor cursor = dbOpenHelper.getReadableDatabase()
				.rawQuery("select * from CHFolder", null);
		cursor.moveToFirst();
		long count = cursor.getLong(0);
		cursor.close();
		return count;
	}

}
