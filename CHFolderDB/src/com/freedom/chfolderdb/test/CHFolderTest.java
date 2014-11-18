package com.freedom.chfolderdb.test;

import java.util.List;

import android.test.AndroidTestCase;
import android.util.Log;

import com.freedom.chfolderdb.model.CHFolder;
import com.freedom.chfolderdb.utils.CHFolderSQLiteUtils;
import com.freedom.chfolderdb.utils.CHSQLiteOpenHelper;

public class CHFolderTest extends AndroidTestCase {
	private static final String TAG = "PersonServiceTest";
	
	public void testCreateDB() throws Exception{
		CHSQLiteOpenHelper dbOpenHelper = new CHSQLiteOpenHelper(getContext());
		dbOpenHelper.getWritableDatabase();
	}
	
	public void testSave() throws Exception{
		CHFolderSQLiteUtils service = new CHFolderSQLiteUtils(this.getContext());
		for(int i = 0 ; i < 20 ; i++){
			CHFolder person = new CHFolder("test"+ i, 10);
			service.save(person);
		}
	}
	
	public void testDelete() throws Exception{
		CHFolderSQLiteUtils service = new CHFolderSQLiteUtils(this.getContext());
		service.delete(21);
	}
	
	public void testUpdate() throws Exception{
		CHFolderSQLiteUtils service = new CHFolderSQLiteUtils(this.getContext());
		CHFolder person = service.query(1);
		person.setFolder_name("freedomtest");
		service.update(person);
	}
	
	public void testFind() throws Exception{
		CHFolderSQLiteUtils service = new CHFolderSQLiteUtils(this.getContext());
		CHFolder person = service.query(1);
		Log.i(TAG, person.toString());
	}
	public void testFind2() throws Exception{
		CHFolderSQLiteUtils service = new CHFolderSQLiteUtils(this.getContext());
		CHFolder person = service.queryByName("默认相册");
		Log.i(TAG, person.toString());
	}
	
	public void testScrollData() throws Exception{
		CHFolderSQLiteUtils service = new CHFolderSQLiteUtils(this.getContext());
		List<CHFolder> persons = service.getScrollData(0, 5);
		for(CHFolder person : persons){
			Log.i(TAG, person.toString());
		}
	}
	
	public void testCount() throws Exception{
		CHFolderSQLiteUtils service = new CHFolderSQLiteUtils(this.getContext());
		long result = service.getCount();
		Log.i(TAG, result+"");
	}
	
	

}
