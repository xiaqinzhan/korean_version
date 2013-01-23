package com.jason.app.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.jason.app.bean.BaseBean;
import com.jason.app.bean.Schedule;
import com.jason.app.bean.helper.ScheduleHelper;

public class ScheduleDao extends BaseDao {

	public ScheduleDao(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void fillContentValuesWithValue(ContentValues values, BaseBean t) {
		Schedule schedule = (Schedule)t;
		values.put(this.getHelper().COLUMN_SCHEDULE, schedule.getSchedule());

	}

	@Override
	public ScheduleHelper getHelper() {
		// TODO Auto-generated method stub
		return ScheduleHelper.getInstance();
	}

	@Override
	public Schedule getSingleFromCurrentCursor(Cursor cursor) {
		// TODO Auto-generated method stub
		Schedule schedule = new Schedule();
		schedule.set_id(getIntFromCursor(cursor,this.getHelper().COLUMN_ID));
		schedule.setCreate_date_time(this.getStringFromCursor(cursor, this.getHelper().COLUMN_CREATE_DATE_TIME));
		schedule.setSchedule(this.getStringFromCursor(cursor, this.getHelper().COLUMN_SCHEDULE));
		return schedule;
	}

}
