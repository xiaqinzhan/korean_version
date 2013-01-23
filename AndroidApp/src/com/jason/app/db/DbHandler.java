package com.jason.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jason.app.bean.helper.EventHelper;
import com.jason.app.bean.helper.MessageHelper;
import com.jason.app.bean.helper.ScheduleHelper;
import com.jason.app.bean.helper.SmsContactHelper;

public class DbHandler extends SQLiteOpenHelper {

	private static final String DB_NAME = "BATCH_MESSAGE.db";
	private static final int DB_VERSION = 9;
	
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
		db.execSQL(EventHelper.getInstance().getCreateSQL());
		
//		db.execSQL(DBScript.CREATE_SMSGROUP);
		db.execSQL(MessageHelper.getInstance().getCreateSQL());
		db.execSQL(SmsContactHelper.getInstance().getCreateSQL());
		db.execSQL(ScheduleHelper.getInstance().getCreateSQL());
//		db.execSQL(DBScript.CREATE_SCHEDULE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(EventHelper.getInstance().getDropSQL());
		db.execSQL(MessageHelper.getInstance().getDropSQL());
		db.execSQL(SmsContactHelper.getInstance().getDropSQL());
		db.execSQL(ScheduleHelper.getInstance().getDropSQL());
		this.onCreate(db);
	}

	
}
