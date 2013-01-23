package com.jason.app.bean;

import com.jason.app.bean.helper.BaseBeanHelper;

public abstract class BaseBean {
	
	private int _id;
	private String create_date_time;
	private String update_date_time;

	public int get_id() {
		return _id;
	}


	public void set_id(int id) {
		_id = id;
	}
	

	public String getCreate_date_time() {
		return create_date_time;
	}


	public void setCreate_date_time(String createDateTime) {
		create_date_time = createDateTime;
	}


	public String getUpdate_date_time() {
		return update_date_time;
	}


	public void setUpdate_date_time(String updateDateTime) {
		update_date_time = updateDateTime;
	}


	public abstract BaseBeanHelper getHelper();
}
