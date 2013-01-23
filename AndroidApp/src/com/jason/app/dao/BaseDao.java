package com.jason.app.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jason.app.bean.BaseBean;
import com.jason.app.bean.helper.BaseBeanHelper;
import com.jason.app.db.DbHandler;

public abstract class BaseDao {
	private Context context;
	DbHandler dbHandler;
	
	public abstract BaseBean getSingleFromCurrentCursor(Cursor cursor);
	
	public Cursor getAllAsCursor(String orderBy) {
		SQLiteDatabase db = dbHandler.getReadableDatabase();
		Cursor cursor = db.query(this.getHelper().getTableName(), this.getHelper().getColumns(),
				null, null, null, null, orderBy);
		return cursor;
	}

	public abstract BaseBeanHelper getHelper();
	public abstract void fillContentValuesWithValue(ContentValues values,BaseBean t);
	
	public ContentValues getContentValues(BaseBean t){
		ContentValues values = new ContentValues();
		values.put(this.getHelper().COLUMN_ID, t.get_id());
		values.put(this.getHelper().COLUMN_CREATE_DATE_TIME, this.getCurrentDateTime());
		fillContentValuesWithValue(values,t);
		return values;
		
	}
	
	public BaseDao(Context context){
		this.context = context;
		dbHandler = DbHandler.getInstance(context);
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public DbHandler getDbHandler() {
		return dbHandler;
	}

	public SQLiteDatabase getReadableDatabase(){
		return dbHandler.getReadableDatabase();
	}
	
	public SQLiteDatabase getWriteableDatabase(){
		return dbHandler.getWritableDatabase();
	}
	
	
	public String getCurrentDateTime(){
		Calendar calendar = Calendar.getInstance();
		String created = calendar.get(Calendar.YEAR) + "-"
		+ calendar.get(Calendar.MONTH) + "-"
		      + calendar.get(Calendar.DAY_OF_MONTH) + " "
		+ calendar.get(Calendar.HOUR_OF_DAY) + ":"
		+ calendar.get(Calendar.MINUTE) + ":"
		+ calendar.get(Calendar.SECOND);
		return created;
	}
	

	
	public long save(BaseBean bean){
		SQLiteDatabase db = dbHandler.getWritableDatabase();
		ContentValues values = getContentValues(bean);
		values.remove(BaseBeanHelper.COLUMN_ID);
		long rowId = db.insert(bean.getHelper().getTableName(), null, values);
		Log.i("save",bean.getClass()+"id is "+rowId);
		return rowId;
	}
	public boolean update(BaseBean bean){
		SQLiteDatabase db = dbHandler.getWritableDatabase();
		ContentValues values = getContentValues(bean);
		db.update(bean.getHelper().getTableName(), values, bean.getHelper().COLUMN_ID+"="+bean.get_id(), null);
		Log.i("save"+bean.getClass(),"id is "+bean.get_id());
		return true;
	}
	
	public boolean delete(long id){
		SQLiteDatabase db = dbHandler.getWritableDatabase();
		db.execSQL("delete from "+this.getHelper().getTableName()+" where _id="+id);
		Log.i("delete","delete from "+this.getHelper().getTableName()+" where _id="+id);
		return true;
	}
	
	public BaseBean getById(long id){
		SQLiteDatabase db = dbHandler.getReadableDatabase();
		Cursor cursor = db.query(this.getHelper().getTableName(), this.getHelper().getColumns(),
				"_id="+id, null, null, null, null);
		if(cursor.moveToFirst()){
			BaseBean bean = this.getSingleFromCurrentCursor(cursor);
			cursor.close();
			return bean;
		}
		
		return null;
	}
	
	public List getAllAsObject(String orderBy){
		Cursor cursor = getAllAsCursor(orderBy);
		List<BaseBean> list = new ArrayList<BaseBean>();
		while(cursor.moveToNext()){
			BaseBean bean = getSingleFromCurrentCursor(cursor);
			list.add(bean);
		}
		cursor.close();
		return list;
	}

	protected List getAllBy(String columnName,String value){
		SQLiteDatabase db = dbHandler.getReadableDatabase();
		Cursor cursor = db.query(this.getHelper().getTableName(), this.getHelper().getColumns(),
				columnName+"=?", new String[]{value}, null, null, null);
		List<BaseBean> list = new ArrayList<BaseBean>();
		while(cursor.moveToNext()){
			BaseBean bean = getSingleFromCurrentCursor(cursor);
			list.add(bean);
		}
		cursor.close();
		return list;
	}
	
	protected Cursor getCursorBy(String columnName,String value){
		SQLiteDatabase db = dbHandler.getReadableDatabase();
		Cursor cursor = db.query(this.getHelper().getTableName(), this.getHelper().getColumns(),
				columnName+"=?", new String[]{value}, null, null, null);
		return cursor;
	}
	
	public int getIntFromCursor(Cursor c, String columnName){
		return c.getInt(getHelper().getColumnIndexByName(columnName));
	}
	
	public String getStringFromCursor(Cursor c, String columnName){
		return c.getString(getHelper().getColumnIndexByName(columnName));
	}
}
