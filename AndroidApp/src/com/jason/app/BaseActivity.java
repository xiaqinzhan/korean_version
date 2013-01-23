package com.jason.app;

import android.app.ListActivity;

import com.jason.app.db.DbHandler;

public abstract class BaseActivity extends ListActivity {

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		DbHandler.getInstance(this).close();
		
		
	}
	
	public String getResourceString(int id){
		return this.getResources().getString(id);
	}
}
