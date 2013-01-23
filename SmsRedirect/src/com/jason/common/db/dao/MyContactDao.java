package com.jason.common.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.jason.common.bean.BaseBean;
import com.jason.common.bean.MyContact;
import com.jason.common.bean.helper.MyContactHelper;

public class MyContactDao extends BaseDao {

	public MyContactDao(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public MyContactHelper getHelper() {
		// TODO Auto-generated method stub
		return MyContactHelper.getInstance();
	}

	@Override
	public BaseBean getSingleFromCurrentCursor(Cursor cursor) {
		MyContact mycontact = new MyContact();
		mycontact.set_id(this.getIntFromCursor(cursor, this.getHelper().COLUMN_ID));
		mycontact.setCreate_date_time(cursor.getString(this.getHelper()
				.getColumnIndexByName(this.getHelper().COLUMN_CREATE_DATE_TIME)));
		mycontact.setShortNumber(this.getStringFromCursor(cursor, this.getHelper().COLUMN_SHORT_NUMBER));
		mycontact.setFullNumber(this.getStringFromCursor(cursor, this.getHelper().COLUMN_FULL_NUMBER));
		return mycontact;
	}

	@Override
	public void fillContentValuesWithValue(ContentValues values,
			BaseBean t) {
		MyContact contact = (MyContact)t;
		values.put(this.getHelper().COLUMN_SHORT_NUMBER, contact.getShortNumber());
		values.put(this.getHelper().COLUMN_FULL_NUMBER, contact.getFullNumber());
	}

	public MyContact getByFullNumber(String fullNumber){
		MyContact contact = null;
		Cursor c = this.getReadableDatabase().query(this.getHelper().getTableName(), this.getHelper().getColumns(), this.getHelper().COLUMN_FULL_NUMBER+"=?", new String[]{fullNumber}, null, null, null);
		if(c.moveToFirst()){
			contact = (MyContact) this.getSingleFromCurrentCursor(c);
		}
		c.close();
		return contact;
	}

	public String getFullNumberByShort(String target) {
		MyContact contact = null;
		Cursor c = this.getReadableDatabase().query(this.getHelper().getTableName(), this.getHelper().getColumns(), this.getHelper().COLUMN_ID+"=?", new String[]{target}, null, null, null);
		if(c.moveToFirst()){
			contact = (MyContact) this.getSingleFromCurrentCursor(c);
		}
		c.close();
		if(contact != null){
			return contact.getFullNumber();
		}
		return null;
	}
	
	
}
