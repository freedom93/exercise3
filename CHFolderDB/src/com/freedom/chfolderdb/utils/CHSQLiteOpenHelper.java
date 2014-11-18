package com.freedom.chfolderdb.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.freedom.chfolderdb.model.CHFolder;

public class CHSQLiteOpenHelper extends SQLiteOpenHelper {
	public CHSQLiteOpenHelper(Context context) {
		super(context, "privatecalc.db", null, 1);//<包>/databases/
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		CHFolder folder = new CHFolder("默认文件夹", 0);
		db.beginTransaction();
		try {
			db.execSQL("CREATE TABLE CHFolder(hashid integer primary key, folder_name varchar(20), folder_size integer null)");
			db.execSQL("INSERT INTO CHFolder(hashid, folder_name, folder_size) values(?,?,?)",
					new Object[] { folder.getHashid(), folder.getFolder_name(), folder.getFolder_size() });
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("ALTER TABLE CHFolder ADD is_virtual integer");
	}

	
}
