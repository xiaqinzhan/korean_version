package com.jason.smsredirect;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharePreferenceHandler {
	public static final String PREF_NAME="share_pref_sms_redirect";
	public static final String TARGET = "target";
	public static final String ENABLE="enable";
	public static final String STARTCHAR="startChar";
	public static final String ENDCHAR="endChar";
	public static final String PARTIAL = "partial";
	public static final String NUMBERS = "numbers";
	
	private SharedPreferences pref;
	private Context context;
	
	public SharePreferenceHandler(Context context){
		this.context = context;
	}
	
	public void putEndChar(String endChar){
		pref = context.getSharedPreferences(PREF_NAME,0);
		Editor editor = pref.edit();
		editor.putString(ENDCHAR, endChar);
		editor.commit();
	}
	
	public String getEndChar(){
		pref = context.getSharedPreferences(PREF_NAME,0);
		return pref.getString(ENDCHAR, "E");
	}
	
	public void putStartChar(String startChar){
		pref = context.getSharedPreferences(PREF_NAME,0);
		Editor editor = pref.edit();
		editor.putString(STARTCHAR, startChar);
		editor.commit();
	}
	
	public String getStartChar(){
		pref = context.getSharedPreferences(PREF_NAME,0);
		return pref.getString(STARTCHAR, "S");
	}
	
	
	public String getTarget(){
		pref = context.getSharedPreferences(PREF_NAME,0);
		return pref.getString(TARGET, null);
	}
	
	public boolean getEnable(){
		pref = context.getSharedPreferences(PREF_NAME,0);
		return pref.getBoolean(ENABLE, false);
	}
	
	public void putTarget(String target){
		pref = context.getSharedPreferences(PREF_NAME,0);
		Editor editor = pref.edit();
		editor.putString(TARGET, target);
		editor.commit();
	}
	
	public void putEnable(boolean enable){
		pref = context.getSharedPreferences(PREF_NAME,0);
		Editor editor = pref.edit();
		editor.putBoolean(ENABLE, enable);
		editor.commit();
	}
	
	public void putFilter(boolean isPartial){
		pref = context.getSharedPreferences(PREF_NAME,0);
		Editor editor = pref.edit();
		editor.putBoolean(PARTIAL, isPartial);
		editor.commit();
	}
	
	public boolean getFilter(){
		pref = context.getSharedPreferences(PREF_NAME,0);
		return pref.getBoolean(PARTIAL, false);
	}
	
	public void putNumbers(String numbers){
		pref = context.getSharedPreferences(PREF_NAME,0);
		Editor editor = pref.edit();
		editor.putString(NUMBERS, numbers);
		editor.commit();
	}
	
	public String getNumbers(){
		pref = context.getSharedPreferences(PREF_NAME,0);
		return pref.getString(NUMBERS, null);
	}
}
