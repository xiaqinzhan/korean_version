package com.jason.app.event;

import java.util.ArrayList;
import java.util.List;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.jason.app.LoadActivity;
import com.jason.app.R;
import com.jason.app.SendingActivity;
import com.jason.app.SendingService;
import com.jason.app.bean.Event;
import com.jason.app.bean.Message;
import com.jason.app.bean.Schedule;
import com.jason.app.bean.SmsContact;
import com.jason.app.bean.helper.EventHelper;
import com.jason.app.bean.helper.SmsContactHelper;
import com.jason.app.dao.EventDao;
import com.jason.app.dao.MessageDao;
import com.jason.app.dao.ScheduleDao;
import com.jason.app.dao.SmsContactDao;
import com.jason.app.db.DbHandler;
import com.jason.app.utils.FileUtil;
import com.jason.app.utils.SmsSender;
import com.jason.app.widget.MessageListener;
import com.jason.app.widget.SuperEditText;

public class EventMain extends TabActivity implements OnClickListener {

	TabHost tabHost;
	ToggleButton toggleEvent;
	TextView textEvent;
	SuperEditText editMessageBox;
	Event event;
	Message message;
	ListView contactView;
	DatePicker datePicker;
	TimePicker timePicker;
	TextView textSchedule;

	EventDao eventDao;
	MessageDao messageDao;
	SmsContactDao smsContactDao;
	ScheduleDao scheduleDao;

	SmsContact currentContact;
	Schedule schedule;

	private int mode;

	private static final int MODE_NEW = 1;
	private static final int MODE_EDIT = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.layout_event);

		Resources res = this.getResources();

		editMessageBox = (SuperEditText) findViewById(R.id.EditMessageBox);
		editMessageBox.setListener(new MessageListener() {
			public void onMessage(String s) {
				enableSaveButton(true);

			}
		});

		toggleEvent = (ToggleButton) findViewById(R.id.ToggleEvent);
		toggleEvent.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				eventDao.updateEventStatus(isChecked, event.get_id());
			}
		});

		contactView = (ListView) this.findViewById(R.id.contactList);
		this.registerForContextMenu(contactView);

		datePicker = (DatePicker) this.findViewById(R.id.DatePicker01);
		timePicker = (TimePicker) this.findViewById(R.id.TimePicker01);
		timePicker.setIs24HourView(true);
		textSchedule = (TextView) findViewById(R.id.TextSchedule);

		tabHost = (TabHost) this.findViewById(android.R.id.tabhost);

		tabHost.addTab(tabHost.newTabSpec("message").setIndicator(
				res.getString(R.string.message),
				res.getDrawable(R.drawable.icon_message)).setContent(
				R.id.tabMessage));
		tabHost.addTab(tabHost.newTabSpec("group").setIndicator(
				res.getString(R.string.group),
				res.getDrawable(R.drawable.icon_group)).setContent(
				R.id.tabGroup));
		tabHost.addTab(tabHost.newTabSpec("schedule").setIndicator(
				res.getString(R.string.schedule),
				res.getDrawable(R.drawable.icon_schedule)).setContent(
				R.id.tabSchedule));

		eventDao = EventDao.getInstance(this);
		messageDao = new MessageDao(this);
		smsContactDao = new SmsContactDao(this);
		scheduleDao = new ScheduleDao(this);

		this.init();
	}

	private void init() {

		textEvent = (TextView) this.findViewById(R.id.textViewEventName);
		Intent intent = this.getIntent();
		long id = intent.getExtras().getLong(EventHelper.COLUMN_ID);
		event = (Event) eventDao.getById(id);

		if (event != null) {
			textEvent.setText(event.getName());
			toggleEvent.setChecked(event.getEnable() == 1 ? true : false);
		}

		if (intent.getAction() != null
				&& intent.getAction().equalsIgnoreCase(Intent.ACTION_EDIT)) {
			this.mode = MODE_EDIT;
		} else {
			this.mode = MODE_NEW;
		}

		refreshMessageUI();
		refreshContactUI();
		refreshScheduleUI();
	}

	private void refreshScheduleUI() {
		if (event != null && event.getScheduleId() > 0) {
			schedule = (Schedule) this.scheduleDao.getById(event
					.getScheduleId());
			datePicker.updateDate(schedule.getYear(), schedule.getMonth(),
					schedule.getDay());
			timePicker.setCurrentHour(schedule.getHour());
			timePicker.setCurrentMinute(schedule.getMinute());
			textSchedule.setText(schedule.getFormatString());
		} else {
			textSchedule.setText(R.string.stringScheduleNotSet);
		}
	}

	private void refreshContactUI() {
		Cursor cursor = smsContactDao.getCursorByGroupId(event.get_id());
		SmsContactHelper helper = SmsContactHelper.getInstance();
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				R.layout.layout_item_contact, cursor,
				new String[] { helper.COLUMN_PHONE_NUMBER,
						helper.COLUMN_CONTACT_NAME }, new int[] {
						R.id.contactPhone, R.id.contactName });
		this.contactView.setAdapter(adapter);

	}

	private void refreshMessageUI() {
		if (event.getMessageId() > 0) {
			message = (Message) messageDao.getById(event.getMessageId());
			editMessageBox.setText(message.getContext());
		} else {
			message = new Message();
			message.set_id(-1);
		}

		enableSaveButton(false);
	}

	public static final int REQUEST_CODE_MESSAGE = 1;
	public static final int REQUEST_CODE_CONTACT = 2;
	public static final int REQUEST_CODE_PHONE = 3;

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ButtonLoadMessageFromFile: {
			Intent intent = new Intent(this, LoadActivity.class);
			intent.putExtra(LoadActivity.FILE_TYPE, LoadActivity.FILE_TYPE_TXT);
			this.startActivityForResult(intent, REQUEST_CODE_MESSAGE);
			break;
		}
		case R.id.ButtonSaveMessage:
			saveMessage();
			break;
		case R.id.ButtonLoadContactFromFile: {
			Intent intent = new Intent(this, LoadActivity.class);
			intent.putExtra(LoadActivity.FILE_TYPE, LoadActivity.FILE_TYPE_TXT);
			this.startActivityForResult(intent, REQUEST_CODE_CONTACT);
			break;
		}
		case R.id.ButtonLoadContactFromPhone: {
			Intent intent = new Intent(this, UserSelectActivity.class);
			this.startActivityForResult(intent, REQUEST_CODE_PHONE);
			break;
		}
		case R.id.buttonClear: {
			this.showDialog(DIALOG_CONFIRM_CLEAR);
			break;
		}
		case R.id.ButtonSet: {
			if (schedule == null) {
				schedule = new Schedule();
			}
			this.schedule.setDate(datePicker.getYear(), datePicker.getMonth(),
					datePicker.getDayOfMonth());
			this.schedule.setTime(timePicker.getCurrentHour(), timePicker
					.getCurrentMinute(), 0);
			if (schedule.get_id() < 1) {
				long scheduleId = scheduleDao.save(schedule);
				this.event.setScheduleId((int) scheduleId);
				this.eventDao.update(event);
				schedule.set_id((int) scheduleId);
			} else {
				scheduleDao.update(schedule);
			}
			
			bindSendingService(schedule);
			this.refreshScheduleUI();
			break;
		}
		case R.id.ButtonRunNow: {
			Intent intent = getActivityForSending();
			if (intent != null) {
				this.startActivity(intent);
			}
			break;
		}
		}

	}

	private void bindSendingService(Schedule schedule2) {
		AlarmManager alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);
		Intent intent = this .getServiceForSending();
//		PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		PendingIntent pi = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		long time = schedule2.getTimeMillis();
		Long s = time-System.currentTimeMillis();
		Log.i("remain time",s/1000+" second");
		alarmManager.set(AlarmManager.RTC_WAKEUP, time, pi);
		Toast.makeText(this, R.string.toast_message_set_successful,Toast.LENGTH_SHORT).show();
	}

	private Intent getActivityForSending() {
		if (this.message == null || this.message.getContext() == null
				|| this.message.getContext().length() == 0) {
			Toast.makeText(this, R.string.toast_message_null,
					Toast.LENGTH_SHORT).show();
			return null;
		}
		List<SmsContact> contactList = smsContactDao.getContactByGroup(event
				.get_id());
		if (contactList == null || contactList.size() == 0) {
			Toast.makeText(this, R.string.toast_contact_null,
					Toast.LENGTH_SHORT).show();
			return null;
		}
		Intent intent = new Intent(this, SendingActivity.class);
		intent.putExtra(Event.class.getName(), event);
		intent.putExtra(Message.class.getName(), message.getContext());
		SmsContact[] contactArray = new SmsContact[1];
		intent.putExtra(SmsContact.class.getName(), contactList
				.toArray(contactArray));
		return intent;
	}
	
	private Intent getServiceForSending() {
		if (this.message == null || this.message.getContext() == null
				|| this.message.getContext().length() == 0) {
			Toast.makeText(this, R.string.toast_message_null,
					Toast.LENGTH_SHORT).show();
			return null;
		}
		List<SmsContact> contactList = smsContactDao.getContactByGroup(event
				.get_id());
		if (contactList == null || contactList.size() == 0) {
			Toast.makeText(this, R.string.toast_contact_null,
					Toast.LENGTH_SHORT).show();
			return null;
		}
		Intent intent = new Intent(this, SendingService.class);
		intent.putExtra(Event.class.getName(), event);
		intent.putExtra(Message.class.getName(), message.getContext());
		SmsContact[] contactArray = new SmsContact[1];
		intent.putExtra(SmsContact.class.getName(), contactList
				.toArray(contactArray));
		return intent;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		if (v.getId() == R.id.contactList) {
			MenuInflater inflater = this.getMenuInflater();
			inflater.inflate(R.menu.menu_basic, menu);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.menuDelete:
			smsContactDao.delete(info.id);
			this.refreshContactUI();
			Toast.makeText(this, R.string.toast_delete_success,
					Toast.LENGTH_SHORT).show();
			break;
		case R.id.menuEdit:

			currentContact = (SmsContact) smsContactDao.getById(info.id);
			this.showDialog(DIALOG_EDIT_CONTACT);
			break;
		}

		return super.onContextItemSelected(item);
	}

	private static final int DIALOG_EDIT_CONTACT = 1;
	private static final int DIALOG_CONFIRM_CLEAR = 2;

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_EDIT_CONTACT: {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.editContact);
			LayoutInflater inflater = this.getLayoutInflater();
			View v = inflater.inflate(R.layout.layout_edit_2item, null);
			final EditText item1 = (EditText) v.findViewById(R.id.editItem1);
			final EditText item2 = (EditText) v.findViewById(R.id.editItem2);
			if (currentContact != null) {
				item2.setText(currentContact.getPhoneNumber());
				if (currentContact.getContactName() != null) {
					item1.setText(currentContact.getContactName());
				}
			}
			TextView t1 = (TextView) v.findViewById(R.id.viewItem1);
			TextView t2 = (TextView) v.findViewById(R.id.viewItem2);
			t1.setText(this.getResources().getString(R.string.contactName));
			t2.setText(this.getResources().getString(R.string.phoneNumber));
			builder.setView(v);
			builder.setPositiveButton(R.string.button_ok,
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							currentContact.setContactName(item1.getText()
									.toString());
							currentContact.setPhoneNumber(item2.getText()
									.toString());
							smsContactDao.update(currentContact);
							Toast.makeText(EventMain.this,
									R.string.toast_save_success,
									Toast.LENGTH_SHORT).show();
							refreshContactUI();
							EventMain.this.removeDialog(DIALOG_EDIT_CONTACT);
						}
					});
			builder.setNegativeButton(R.string.button_cancel,
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							EventMain.this.removeDialog(DIALOG_EDIT_CONTACT);
						}
					});
			return builder.create();

		}
		case DIALOG_CONFIRM_CLEAR: {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.editContact);
			builder.setMessage(R.string.message_confirm_clear);
			builder.setPositiveButton(R.string.button_ok,
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							clearContact();
						}
					});
			builder.setNegativeButton(R.string.button_cancel, null);
			return builder.create();
		}
		}
		return super.onCreateDialog(id);
	}

	private void clearContact() {
		int count = smsContactDao.deleteAll(event.get_id());
		Toast.makeText(this,
				this.getResources().getString(R.string.deleteCount) + count,
				Toast.LENGTH_SHORT).show();
		this.refreshContactUI();
	}

	private void saveMessage() {
		message.setContext(editMessageBox.getText().toString());
		if (message.get_id() <= 0) {
			long id = messageDao.save(message);
			message.set_id(new Long(id).intValue());
			event.setMessageId(new Long(id).intValue());
			eventDao.update(event);
		} else {
			messageDao.update(message);
		}
		Toast.makeText(this, R.string.toast_save_success, Toast.LENGTH_SHORT)
				.show();
		enableSaveButton(false);
	}

	private void enableSaveButton(boolean enable) {
		findViewById(R.id.ButtonSaveMessage).setEnabled(enable);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_MESSAGE && resultCode == RESULT_OK) {
			String s = FileUtil.getStringFromFile(data.getExtras().getString(
					LoadActivity.PATH));
			if (s != null) {
				this.editMessageBox.setText(s);
			}
		} else if (requestCode == REQUEST_CODE_CONTACT
				&& resultCode == RESULT_OK) {
			this.getContactFromFile(data.getExtras().getString(
					LoadActivity.PATH));
			this.refreshContactUI();
		} else if (requestCode == REQUEST_CODE_PHONE && resultCode == RESULT_OK) {
			this.getcontactFromString(data.getExtras().getString(
					UserSelectActivity.RETURN_LIST));
		}
	}

	private void getcontactFromString(String data) {
		if (data == null || data.length() == 0) {
			return;
		}

		String[] arrayContact = data.split("#");
		for (String singleContact : arrayContact) {
			if (singleContact != null && singleContact.length() > 0) {
				String[] props = singleContact.split(":");
				if (props.length == 2) {
					SmsContact contact = new SmsContact();
					contact.setContactName(props[0]);
					contact.setPhoneNumber(props[1]);
					contact.setGroupId(this.event.get_id());
					this.smsContactDao.save(contact);

				}
			}

		}
		this.refreshContactUI();
	}

	private void getContactFromFile(String filename) {
		List<String> lines = FileUtil.getListFromFile(filename);
		if (lines != null && lines.size() > 0) {
			for (String line : lines) {
				String[] props = line.split(",");
				if (props.length > 0) {
					SmsContact contact = new SmsContact();
					contact.setPhoneNumber(props[0]);
					contact.setGroupId(event.get_id());
					if (props.length > 1) {
						contact.setContactName(props[1]);
					}
					smsContactDao.save(contact);
					Log.i("contact add", contact.getPhoneNumber() + ""
							+ contact.getContactName());
				}
			}

		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		DbHandler.getInstance(this).close();

	}
}
