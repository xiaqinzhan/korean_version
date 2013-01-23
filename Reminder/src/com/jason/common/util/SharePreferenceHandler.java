package com.jason.common.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharePreferenceHandler {
	public static final String PREF_NAME = "share_pref_reminder";
	public static final String STRING_DEFAULT = "-xsfaadfa@ere";
	public static final int INT_DEFAULT = -3432891;
	
	private SharedPreferences pref;
	private Context context;

	public SharePreferenceHandler(Context context) {
		this.context = context;
	}

	public String getAsString(String name) {
		pref = context.getSharedPreferences(PREF_NAME, 0);
		return pref.getString(name, STRING_DEFAULT);
	}

	public void setAsString(String name,String value) {
		pref = context.getSharedPreferences(PREF_NAME, 0);
		Editor editor = pref.edit();
		editor.putString(name, value);
		editor.commit();
	}
	
	public int getAsInt(String name) {
		pref = context.getSharedPreferences(PREF_NAME, 0);
		return pref.getInt(name, INT_DEFAULT);
	}

	public void setAsInt(String name,int value) {
		pref = context.getSharedPreferences(PREF_NAME, 0);
		Editor editor = pref.edit();
		editor.putInt(name, value);
		editor.commit();
	}
	
	public boolean getAsBoolean(String name) {
		pref = context.getSharedPreferences(PREF_NAME, 0);
		return pref.getBoolean(name, false);
	}

	public void setAsBoolean(String name,boolean value) {
		pref = context.getSharedPreferences(PREF_NAME, 0);
		Editor editor = pref.edit();
		editor.putBoolean(name, value);
		editor.commit();
	}

	
}
