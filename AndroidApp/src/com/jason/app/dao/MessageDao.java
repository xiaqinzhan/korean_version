package com.jason.app.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jason.app.bean.BaseBean;
import com.jason.app.bean.Message;
import com.jason.app.bean.helper.MessageHelper;

public class MessageDao extends BaseDao {
	
	private MessageHelper helper;
	
	
	
	public MessageDao(Context context) {
		super(context);
	}


	public Message getSingleFromCurrentCursor(Cursor cursor) {
		Message message = new Message();
		message.set_id(cursor.getInt(this.getHelper()
				.getColumnIndexByName(this.getHelper().COLUMN_ID)));
		message.setContext(cursor.getString(this.getHelper()
				.getColumnIndexByName(this.getHelper().COLUMN_CONTEXT)));
		message.setCreate_date_time(cursor.getString(this.getHelper()
				.getColumnIndexByName(this.getHelper().COLUMN_CREATE_DATE_TIME)));
		return message;
	}


	@Override
	public Cursor getAllAsCursor(String orderBy) {
		SQLiteDatabase db = dbHandler.getReadableDatabase();
		Cursor cursor = db.query(this.getHelper().TABLE_NAME, this.getHelper().getColumns(),
				null, null, null, null, orderBy);
		return cursor;
	}

	@Override
	public MessageHelper getHelper() {
		// TODO Auto-generated method stub
		
		return MessageHelper.getInstance();
	}

	@Override
	public void fillContentValuesWithValue(ContentValues values, BaseBean t) {
		Message message = (Message)t;
		values.put(this.getHelper().COLUMN_CONTEXT, message.getContext());
	}

	

	
}
