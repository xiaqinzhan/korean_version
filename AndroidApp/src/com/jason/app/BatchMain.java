package com.jason.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.AdapterContextMenuInfo;
import any4media_27.challenge.KafUtil;

import com.jason.app.bean.Event;
import com.jason.app.bean.helper.EventHelper;
import com.jason.app.dao.EventDao;
import com.jason.app.event.EventMain;
import com.jason.app.log.LogMain;
import com.jason.app.message.MessageMain;
import com.jason.app.schedule.ScheduleMain;
import com.jason.app.smsgroup.SmsGroupMain;
import com.kaf.KafManager;

public class BatchMain extends BaseActivity implements OnClickListener {

	public static final int MENU_MESSAGE = 1;
	public static final int MENU_SCHEDULE = 2;
	public static final int MENU_GROUP = 3;
	public static final int MENU_LOG = 4;
	public static final int MENU_ABOUT = 5;

	public static final int DIALOG_ABOUT = 5;
	public static final int DIALOG_EVENT_ENTRY = 6;

	EventDao dao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.main);

		KafUtil.KafInit(this, KafManager.INIT_COPYRIGHT);

		dao = EventDao.getInstance(this);

		this.registerForContextMenu(getListView());
	}

	private void initData() {
		Cursor eventCursor = dao.getAllAsCursor(null);
		// if(eventCursor.getCount())
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				R.layout.layout_item, eventCursor, new String[] {
						EventHelper.COLUMN_NAME,
						EventHelper.COLUMN_CREATE_DATE_TIME }, new int[] {
						R.id.ItemName, R.id.ItemSubProp });
		this.setListAdapter(adapter);

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);

		MenuInflater inflater = this.getMenuInflater();

		inflater.inflate(R.menu.menu_basic, menu);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		initData();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);

		Intent intent = new Intent(this, EventMain.class);
		intent.setAction(Intent.ACTION_INSERT);
		intent.putExtra(EventHelper.COLUMN_ID, id);
		this.startActivity(intent);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();

		switch (item.getItemId()) {
		case R.id.menuEdit:
			Intent intent = new Intent(this, EventMain.class);
			intent.setAction(Intent.ACTION_EDIT);
			intent.putExtra(EventHelper.COLUMN_ID, info.id);
			this.startActivity(intent);
			break;
		case R.id.menuDelete:
			dao.delete(info.id);
			initData();
			break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch (id) {
		case DIALOG_ABOUT:
			builder.setTitle(R.string.about);
			builder.setIcon(R.drawable.icon_about);
			builder.setMessage(R.string.about_message);
			builder.setNegativeButton(R.string.button_ok, null);
			dialog = builder.create();
			return dialog;
		case DIALOG_EVENT_ENTRY:
			builder.setTitle(R.string.input_new_event);
			LayoutInflater inflator = LayoutInflater.from(this);
			final View entry_view = inflator.inflate(
					R.layout.layout_dialog_event_entry, null);

			builder.setView(entry_view);

			builder.setNegativeButton(R.string.button_cancel,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {

						}
					});
			builder.setPositiveButton(R.string.button_ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							EditText text = (EditText) entry_view
									.findViewById(R.id.edit_event_name);
							String s = text.getText().toString();
							Log.i("s is ", s);
							if (!s.trim().equals("")) {
								Intent intent = new Intent(BatchMain.this,
										EventMain.class);
								intent.putExtra(EventHelper.COLUMN_NAME, s);
								EventDao dao = EventDao
										.getInstance(BatchMain.this);
								Event event = new Event();
								event.setName(s);
								event.setEnable(1);
								long eventId = dao.save(event);
								intent.putExtra(EventHelper.COLUMN_ID, eventId);
								BatchMain.this.startActivity(intent);
							}
						}
					});
			return builder.create();
		}
		// TODO Auto-generated method stub
		return super.onCreateDialog(id);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		// menu.add(0, MENU_MESSAGE, 0,
		// R.string.menu_message).setIcon(R.drawable.icon_message_normal);
		// menu.add(0, MENU_GROUP, 0,
		// R.string.menu_group).setIcon(R.drawable.icon_group_normal);
		// menu.add(0, MENU_SCHEDULE, 0,
		// R.string.menu_schedule).setIcon(R.drawable.icon_schedule_normal);
		// menu.add(0, MENU_LOG, 0,
		// R.string.menu_log).setIcon(R.drawable.icon_log_normal);
		menu.add(0, MENU_ABOUT, 0, R.string.menu_about).setIcon(
				R.drawable.icon_about);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		switch (item.getItemId()) {
		case MENU_MESSAGE:
			intent = new Intent(this, MessageMain.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			break;
		case MENU_GROUP:
			intent = new Intent(this, SmsGroupMain.class);
			break;
		case MENU_SCHEDULE:
			intent = new Intent(this, ScheduleMain.class);
			break;
		case MENU_LOG:
			intent = new Intent(this, LogMain.class);
			break;
		case MENU_ABOUT:
			showAboutDialog();
		default:
			return false;
		}
		this.startActivity(intent);
		return true;
	}

	private void showAboutDialog() {
		this.showDialog(DIALOG_ABOUT);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ButtonNewEvent:
			this.showDialog(DIALOG_EVENT_ENTRY);
			//			
			break;
		}

	}

}
