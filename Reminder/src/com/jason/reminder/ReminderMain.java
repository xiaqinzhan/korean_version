package com.jason.reminder;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import any4media_27.challenge.KafUtil;

import com.jason.common.util.MediaHandler;
import com.jason.common.util.SharePreferenceHandler;
import com.jason.common.util.VibratorHandler;
import com.kaf.KafManager;

public class ReminderMain extends Activity implements OnClickListener {

	EditText textInterval,textHour1,textHour2,textMinute1,textMinute2;
	CheckBox cbEnable,cbSleepEnable;
	Spinner spinnerType, spinnerMethod, spinnerRingtone;
	View layoutSleepSetting;

	boolean isEnable,sleepEnable;
	int type, method, ringtone, interval,runningstatus;
	String sleepSetting;
	int[] settings;

	SharePreferenceHandler handler;
	VibratorHandler vibratorHandler;
	MediaHandler mediaHandler;

	public static String ENABLE = "enable";
	public static String TYPE = "type";
	public static String METHOD = "method";
	public static String RINGTONE = "ringtone";
	public static String INTERVAL = "interval";
	public static String SLEEPENABLE = "sleepEnable";
	public static String SLEEPSETTING = "sleepSetting";
	public static String RUNNINGSTATUS = "runingstatus";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		KafUtil.KafInit(this, KafManager.INIT_COPYRIGHT);
		
		textInterval = (EditText) this.findViewById(R.id.remindInterval);
		textHour1 = (EditText) this.findViewById(R.id.hour1);
		textHour2 = (EditText) this.findViewById(R.id.hour2);
		textMinute1 = (EditText) this.findViewById(R.id.minute1);
		textMinute2 = (EditText) this.findViewById(R.id.minute2);
		
		cbEnable = (CheckBox) this.findViewById(R.id.CheckBoxEnable);
		cbSleepEnable = (CheckBox) this.findViewById(R.id.CheckBoxSleepEnable);
		spinnerType = (Spinner) this.findViewById(R.id.SpinnerType);
		spinnerMethod = (Spinner) this.findViewById(R.id.SpinnerMethod);
		spinnerRingtone = (Spinner) this.findViewById(R.id.SpinnerRingtone);
		layoutSleepSetting = this.findViewById(R.id.layoutSleepSetting);

		mediaHandler = new MediaHandler(this);
		mediaHandler.addSound(1, R.raw.alarm1);
		mediaHandler.addSound(2, R.raw.notify);
		mediaHandler.addSound(3, R.raw.sound3);
		mediaHandler.addSound(4, R.raw.sound4);
		mediaHandler.addSound(5, R.raw.sound5);
		mediaHandler.addSound(6, R.raw.sound6);
		ArrayAdapter<CharSequence> adapterType = ArrayAdapter
				.createFromResource(this, R.array.array_type,
						android.R.layout.simple_spinner_item);
		adapterType
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerType.setAdapter(adapterType);

		ArrayAdapter<CharSequence> adapterMethod = ArrayAdapter
				.createFromResource(this, R.array.array_mehod,
						android.R.layout.simple_spinner_item);
		adapterMethod
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerMethod.setAdapter(adapterMethod);

		ArrayAdapter<CharSequence> adapterRingtone = ArrayAdapter
				.createFromResource(this, R.array.array_ringtone,
						android.R.layout.simple_spinner_item);
		adapterRingtone
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerRingtone.setAdapter(adapterRingtone);

		handler = new SharePreferenceHandler(this);

		initData();
	}

	private void initData() {

		this.isEnable = handler.getAsBoolean(ENABLE);
		this.type = handler.getAsInt(TYPE);
		if (type == handler.INT_DEFAULT) {
			type = 0;
		}

		this.method = handler.getAsInt(METHOD);
		if (method == handler.INT_DEFAULT) {
			method = 0;
		}

		this.ringtone = handler.getAsInt(RINGTONE);
		if (ringtone == handler.INT_DEFAULT) {
			ringtone = 0;
		}

		this.interval = handler.getAsInt(INTERVAL);
		if (interval == handler.INT_DEFAULT) {
			interval = 300;
		}
		
		this.sleepEnable = handler.getAsBoolean(SLEEPENABLE);		
		this.sleepSetting = handler.getAsString(SLEEPSETTING);
		
		this.cbSleepEnable.setChecked(sleepEnable);
		if(sleepEnable){
			this.layoutSleepSetting.setVisibility(View.VISIBLE);
		}else{
			this.layoutSleepSetting.setVisibility(View.GONE);
		}
		
		settings = getSleepSetting(this.sleepSetting);
		if(settings!=null){
			textHour1.setText(String.format("%02d", settings[0]));
			textMinute1.setText(String.format("%02d", settings[1]));
			textHour2.setText(String.format("%02d", settings[2]));
			textMinute2.setText(String.format("%02d", settings[3]));
		}else{
			textHour1.setText("22");
			textMinute1.setText("00");
			textHour2.setText("07");
			textMinute2.setText("00");
		}
		

		this.textInterval.setText(this.interval + "");
		this.cbEnable.setChecked(isEnable);
		this.spinnerType.setSelection(type);
		this.spinnerMethod.setSelection(method);
		this.spinnerRingtone.setSelection(ringtone);
		this.spinnerRingtone
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						Log.i("onItemSelected", "position is " + position);
						mediaHandler.play(position + 1);

					}

					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub

					}
				});

	}

	public static int[] getSleepSetting(String s){
		if(s==null || s.split(":").length!=4){
			return null;
		}
		String[] charArray = s.split(":");
		int [] returnArray = new int[4];
		for(int i=0;i<4;i++){
			returnArray[i] = Integer.parseInt(charArray[i]);
		}
		return returnArray;
	}
	
	private boolean validateData(){
		boolean returnValue = true;
		try {
			int hour1 = Integer.parseInt(this.textHour1.getText().toString().trim());
			int hour2 = Integer.parseInt(this.textHour2.getText().toString().trim());
			int minute1 = Integer.parseInt(this.textMinute1.getText().toString().trim());
			int minute2 = Integer.parseInt(this.textMinute2.getText().toString().trim());
			int interval = Integer.parseInt(this.textInterval.getText().toString().trim());
			
			if(hour1<0 || hour1>24){
				Toast.makeText(this, R.string.stringHourRestrict, Toast.LENGTH_SHORT).show();
				textHour1.requestFocus();
				return false;
			}
			
			if(hour2<0 || hour2>24){
				Toast.makeText(this, R.string.stringHourRestrict, Toast.LENGTH_SHORT).show();
				textHour2.requestFocus();
				return false;
			}
			
			if(minute1<0 || minute1>60){
				Toast.makeText(this, R.string.stringMinuteRestrict, Toast.LENGTH_SHORT).show();
				textMinute1.requestFocus();
				return false;
			}
			
			if(minute2<0 || minute2>60){
				Toast.makeText(this, R.string.stringMinuteRestrict, Toast.LENGTH_SHORT).show();
				textMinute2.requestFocus();
				return false;
			}
			
			if(hour1==hour2 && minute1==minute2){
				Toast.makeText(this, R.string.stringShouldNotSame, Toast.LENGTH_SHORT).show();
				textMinute2.requestFocus();
				return false;
			}
			
			
			
		} catch (NumberFormatException e) {
			Toast.makeText(this, R.string.stringNumberNeeded, Toast.LENGTH_SHORT).show();
			return false;
		}
		
		
		try {
			Integer.parseInt(this.textInterval.getText().toString());
		} catch (NumberFormatException e) {
			Toast.makeText(this, R.string.stringIntervalNumberNeeded, Toast.LENGTH_SHORT).show();
			this.textInterval.requestFocus();
			return false;
		}
		
		return true;
	}
	
	private void saveData() {
		handler.setAsBoolean(ENABLE, this.cbEnable.isChecked());
		handler.setAsInt(TYPE, this.spinnerType.getSelectedItemPosition());
		handler.setAsInt(METHOD, this.spinnerMethod.getSelectedItemPosition());
		handler.setAsInt(RINGTONE, this.spinnerRingtone
				.getSelectedItemPosition());
		handler.setAsInt(INTERVAL, Integer.parseInt(this.textInterval.getText()
				.toString().trim()));

		int hour1 = Integer.parseInt(this.textHour1.getText().toString().trim());
		int hour2 = Integer.parseInt(this.textHour2.getText().toString().trim());
		int minute1 = Integer.parseInt(this.textMinute1.getText().toString().trim());
		int minute2 = Integer.parseInt(this.textMinute2.getText().toString().trim());
		handler.setAsBoolean(SLEEPENABLE, this.cbSleepEnable.isChecked());
		handler.setAsString(SLEEPSETTING, hour1+":"+minute1+":"+hour2+":"+minute2);
		
		synchronizedWithService();

	}

	boolean isConnected = false;
	Intent serviceIntent;
	public ReminderCaller caller;
	ServiceConnection connection = new ServiceConnection() {

		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			Log.i("Connection", "Service connection open");
			isConnected = true;
			caller = ReminderCaller.Stub.asInterface(service);
		}

		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			Log.i("Connection", "Service connection closed");
		}
	};

	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		bindService();
	}

	void bindService() {
		
		serviceIntent = new Intent(this, ReminderService.class);
		
		runningstatus = this.handler.getAsInt(RUNNINGSTATUS);
		if(runningstatus == handler.INT_DEFAULT || runningstatus == 0){
			this.startService(serviceIntent);
			this.handler.setAsInt(RUNNINGSTATUS, 1);
			Log.i("onBind", "This is the first time to run this app, start service");
		}
		
		boolean isBind;
	
		if (ReminderService.handler == null) {
			isBind = this.bindService(serviceIntent, connection,0);
			Log.i("onBind", "handler is null and we still connect,result is "+isBind);
		} else {
			isBind = this.bindService(serviceIntent, connection, 0);
			Log.i("onBind", "handler is not null and we just connect to it,result is "+isBind);
		}

	}

	void synchronizedWithService() {

		try {
			caller.updateConfig();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		if (this.connection != null) {
			this.unbindService(connection);
			isConnected = false;
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layoutEnable:
			cbEnable.setChecked(!cbEnable.isChecked());
			break;
		case R.id.ButtonOK:
			if(this.validateData()){
				saveData();
				this.finish();
			}
			break;
		case R.id.ButtonCancel:
			this.finish();
			break;
		case R.id.ButtonAbout:
			this.showDialog(DIALOG_ABOUT);
			break;
		case R.id.layoutSleep:
			this.cbSleepEnable.setChecked(!cbSleepEnable.isChecked());
			if(cbSleepEnable.isChecked()){
				this.layoutSleepSetting.setVisibility(View.VISIBLE);
			}else{
				this.layoutSleepSetting.setVisibility(View.GONE);
			}
			break;
		}

	}

	private static final int DIALOG_ABOUT = 1;
	private static final int DIALOG_CONFIRM_EXIT = 2;

	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		switch (id) {
		case DIALOG_ABOUT:

			builder.setTitle(R.string.aboutSoft);
			builder.setMessage(getAboutString());
			builder.setPositiveButton(R.string.ok, null);
			break;
		case DIALOG_CONFIRM_EXIT:
			builder.setTitle(R.string.confirmExit);
			builder.setMessage(R.string.confirmMessage);
			builder.setPositiveButton(R.string.saveData,
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							if(validateData()){
							saveData();
							finish();
							}
						}
					});
			builder.setNegativeButton(R.string.abortSave,
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							finish();
						}
					});
			break;
		}

		return builder.create();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.showDialog(DIALOG_CONFIRM_EXIT);
		}

		return super.onKeyDown(keyCode, event);
	}

	public String getAboutString(){
		try {
            InputStream is = getAssets().open("about.txt");
            
            // We guarantee that the available method returns the total
            // size of the asset...  of course, this does mean that a single
            // asset can't be more than 2 gigs.
            int size = is.available();
            
            // Read the entire asset into a local byte buffer.
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            
            // Convert the buffer into a string.
            String text = new String(buffer);
            
           return text;
        } catch (IOException e) {
            // Should never happen!
            throw new RuntimeException(e);
        }
	}


}