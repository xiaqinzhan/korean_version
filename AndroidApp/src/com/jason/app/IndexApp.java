package com.jason.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class IndexApp extends ListActivity {
	
	public static String TAG="IndexApp";
	
	private static String CATEGORY_DEMO="com.jason.app.category.main";
	private static String LABEL="label";
	private static String INTENT="intent";
	
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		
		Map<String,Object> map = (Map<String, Object>)l.getItemAtPosition(position);
		Intent intent  = (Intent) map.get(INTENT);
		this.startActivity(intent);
		
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		SimpleAdapter adapter = new SimpleAdapter(this,getData(),android.R.layout.simple_list_item_1,new String[]{LABEL},new int[]{android.R.id.text1});
		this.setListAdapter(adapter);
		
	}
	
	
	private List<Map<String,Object>> getData(){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		
		Intent filterIntent = new Intent(Intent.ACTION_MAIN);
		filterIntent.addCategory(CATEGORY_DEMO);
		
		PackageManager manager = this.getPackageManager();
		
		List<ResolveInfo> infos = manager.queryIntentActivities(filterIntent, 0);
		
		for(ResolveInfo info:infos){
			Map<String,Object> map = new HashMap<String,Object>();
			Intent intent = new Intent();
			intent.setClassName(info.activityInfo.packageName, info.activityInfo.name);
			String label = info.loadLabel(manager).toString();
			map.put(INTENT, intent);
			map.put(LABEL, label);
			
			list.add(map);
		}
		
		
		return list;
		
	}
}