package com.jason.app.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jason.app.bean.BaseBean;
import com.jason.app.bean.Event;
import com.jason.app.bean.helper.EventHelper;

public class EventDao extends BaseDao {

	EventHelper helper;

	private static EventDao dao;
	
	
	private EventDao(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	
	public static  EventDao getInstance(Context context){
		if(dao==null){
			dao = new EventDao(context);
		}
		return dao;
	}
	
	@Override
	public EventHelper getHelper() {
		// TODO Auto-generated method stub
		return EventHelper.getInstance();
	}

	@Override
	public BaseBean getSingleFromCurrentCursor(Cursor cursor) {
		// TODO Auto-generated method stub
		Event event = new Event();
		event.set_id(cursor.getInt(this.getHelper().getColumnIndexByName(
				this.getHelper().COLUMN_ID)));
		event.setEnable(cursor.getInt(this.getHelper().getColumnIndexByName(
				this.getHelper().COLUMN_ENABLE)));
		event.setGroupId(cursor.getInt(this.getHelper().getColumnIndexByName(
				this.getHelper().COLUMN_GROUP_ID)));
		event.setMessageId(cursor.getInt(this.getHelper().getColumnIndexByName(
				this.getHelper().COLUMN_MESSAGE_ID)));
		event.setName(cursor.getString(this.getHelper().getColumnIndexByName(
				this.getHelper().COLUMN_NAME)));
		event.setScheduleId(cursor.getInt(this.getHelper()
				.getColumnIndexByName(this.getHelper().COLUMN_SCHEDULE_ID)));
		event.setCreate_date_time(cursor
				.getString(this.getHelper().getColumnIndexByName(
						this.getHelper().COLUMN_CREATE_DATE_TIME)));
		return event;
	}

	@Override
	public void fillContentValuesWithValue(ContentValues values, BaseBean t) {
		Event event = (Event) t;

		values.put(this.getHelper().COLUMN_ENABLE, event.getEnable());
		values.put(this.getHelper().COLUMN_GROUP_ID, event.getGroupId());
		values.put(this.getHelper().COLUMN_MESSAGE_ID, event.getMessageId());
		values.put(this.getHelper().COLUMN_NAME, event.getName());
		values.put(this.getHelper().COLUMN_SCHEDULE_ID, event.getScheduleId());
	}
	
	private static final String UPDATE_EVENT_ENABLE = "update " + EventHelper.TABLE_NAME + " set "+EventHelper.COLUMN_ENABLE+"=? where "
	+EventHelper.COLUMN_ID+"=?";
	
	public void updateEventStatus(boolean enable,long eventId){
		Object[] params;
		if(enable){
			params = new Object[]{1,eventId};
		}else{
			params = new Object[]{0,eventId};
		}
		SQLiteDatabase db = this.getWriteableDatabase();
		db.execSQL(UPDATE_EVENT_ENABLE, params);

	
		Log.i(this.getClass().getName(),UPDATE_EVENT_ENABLE+"("+params);
	}

}
