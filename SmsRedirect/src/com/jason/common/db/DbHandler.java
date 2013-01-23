package com.jason.common.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jason.common.bean.helper.MyContactHelper;

public class DbHandler extends SQLiteOpenHelper {

	private static final String DB_NAME = "BATCH_MESSAGE.db";
	private static final int DB_VERSION = 8;
	
	private DbHandler(Context context){super(context, DB_NAME, null, DB_VERSION);};
	
	private static DbHandler _dbHelper=null;
	
	public static DbHandler getInstance(Context context) {
		if(_dbHelper == null)
			_dbHelper = new DbHandler(context);
		return _dbHelper;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
//		db.execSQL(DBScript.CREATE_CONTACT);
		db.execSQL(MyContactHelper.getInstance().getCreateSQL());

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(MyContactHelper.getInstance().getDropSQL());
		this.onCreate(db);
	}

	
}
