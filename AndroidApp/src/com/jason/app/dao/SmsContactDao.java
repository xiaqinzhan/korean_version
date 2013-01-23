package com.jason.app.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.jason.app.bean.BaseBean;
import com.jason.app.bean.SmsContact;
import com.jason.app.bean.helper.SmsContactHelper;

public class SmsContactDao extends BaseDao {

	public SmsContactDao(Context context) {
		super(context);
	}
	
	
	public List<SmsContact> getContactByGroup(int id){
		return (List<SmsContact>)getAllBy(this.getHelper().COLUMN_GROUP_ID,id+"");
	}

	@Override
	public void fillContentValuesWithValue(ContentValues values, BaseBean t) {
		// TODO Auto-generated method stub
		SmsContact contact = (SmsContact)t;
		values.put(this.getHelper().COLUMN_PHONE_NUMBER, contact.getPhoneNumber());
		values.put(this.getHelper().COLUMN_CONTACT_NAME, contact.getContactName());
		values.put(this.getHelper().COLUMN_GROUP_ID, contact.getGroupId());
	}

	@Override
	public SmsContactHelper getHelper() {
		// TODO Auto-generated method stub
		return SmsContactHelper.getInstance();
	}

	public int deleteAll(int groupId){
		return getWriteableDatabase().delete(this.getHelper().getTableName(), this.getHelper().COLUMN_GROUP_ID+"=?", new String[]{groupId+""});
	}
	
	@Override
	public SmsContact getSingleFromCurrentCursor(Cursor cursor) {
		SmsContact contact = new SmsContact();
		contact.set_id(getIntFromCursor(cursor,this.getHelper().COLUMN_ID));
		contact.setCreate_date_time(this.getStringFromCursor(cursor, this.getHelper().COLUMN_CREATE_DATE_TIME));
		contact.setContactName(this.getStringFromCursor(cursor, this.getHelper().COLUMN_CONTACT_NAME));
		contact.setPhoneNumber(this.getStringFromCursor(cursor, this.getHelper().COLUMN_PHONE_NUMBER));
		contact.setGroupId(this.getIntFromCursor(cursor, this.getHelper().COLUMN_GROUP_ID));
		return contact;
	}

	public Cursor getCursorByGroupId(int groupId){
		return getCursorBy(this.getHelper().COLUMN_GROUP_ID, groupId+"");
	}
}
