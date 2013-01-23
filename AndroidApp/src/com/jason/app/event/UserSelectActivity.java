package com.jason.app.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Contacts.Phones;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.jason.app.BaseActivity;
import com.jason.app.R;

public class UserSelectActivity extends BaseActivity implements OnClickListener {

	List<Map<String, Object>> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = this.getLayoutInflater();
		View footer = inflater.inflate(R.layout.layout_control, null);

		this.getListView().addHeaderView(footer);
		initData();
		refreshView();

	}

	private static String NAME = "name";
	private static String PHONE = "phone";
	private static String CHECKED = "checked";

	private void initData() {
		list = new ArrayList<Map<String, Object>>();

		Cursor c = getContentResolver().query(Phones.CONTENT_URI, null, null,
				null, null);
		startManagingCursor(c);
		while (c.moveToNext()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put(NAME, c.getString(c.getColumnIndex(Phones.NAME)));
			map.put(PHONE, c.getString(c.getColumnIndex(Phones.NUMBER)));
			map.put(CHECKED, false);
			list.add(map);
		}

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);

		this.list.get(position-1).put(CHECKED,
				!(Boolean) (this.list.get(position-1).get(CHECKED)));
		this.refreshView();

	}

	private void refreshView() {
		SimpleAdapter adapter = new SimpleAdapter(this, list,
				R.layout.layout_checkable_item, new String[] { NAME, PHONE,
						CHECKED }, new int[] { R.id.text1, R.id.text2,
						R.id.CheckBox01 });

		setListAdapter(adapter);

	}
	
	public static final String RETURN_LIST="returnList";

	public void onClick(View v) {
		
		Log.i("onClick",""+v.getId());
		
		switch(v.getId()){
		case R.id.buttonOk:
			Intent intent = new Intent();
			String s = "";
			for (int i=0;i<list.size();i++){
				Map<String,Object> map = (Map<String,Object>)list.get(i);
				
				if((Boolean)map.get(CHECKED)){
					s=s+map.get(NAME)+":"+map.get(PHONE)+"#";
				}
			}
			intent.putExtra(RETURN_LIST, s);
			Log.i("retr",s);
			this.setResult(Activity.RESULT_OK, intent);
			this.finish();
		}

	}
	

}
