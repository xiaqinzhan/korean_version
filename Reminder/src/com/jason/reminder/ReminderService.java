package com.jason.reminder;

import java.util.Calendar;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.jason.common.util.CallHandler;
import com.jason.common.util.MediaHandler;
import com.jason.common.util.SharePreferenceHandler;
import com.jason.common.util.SmsHandler;
import com.jason.common.util.VibratorHandler;

public class ReminderService extends Service {

	SharePreferenceHandler sphandler;
	public static Handler handler;
	SmsHandler smsHandler;
	CallHandler callHandler;
	Context context;

	VibratorHandler vibratorHandler;
	MediaHandler mediaHandler;

	ReminderCaller.Stub caller = new ReminderCaller.Stub() {
		public void updateConfig() throws RemoteException {
			// TODO Auto-generated method stub
			update();
		}
	};

	public void update() {
		init();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return caller;
	}

	@Override
	public void onCreate() {
		Log.i("ReminderService", "onCreate..........");
		// TODO Auto-generated method stub
		super.onCreate();
		init();
	}

	private void init() {
		Log.i("ReminderSerivce", "reset parameters");
		if (mediaHandler == null) {
			mediaHandler = new MediaHandler(this);
			mediaHandler.addSound(0, R.raw.alarm1);
			mediaHandler.addSound(1, R.raw.notify);
			mediaHandler.addSound(2, R.raw.sound3);
			mediaHandler.addSound(3, R.raw.sound4);
			mediaHandler.addSound(4, R.raw.sound5);
			mediaHandler.addSound(5, R.raw.sound6);
		}
		if (sphandler == null)
			sphandler = new SharePreferenceHandler(this);
		initData();
		run();
	}

	static boolean isEnable, isSleepEnable;
	static int interval, type, method, ringtone;
	static int hour1, minute1, hour2, minute2;
	static String sleepSetting;

	private void initData() {
		isEnable = sphandler.getAsBoolean(ReminderMain.ENABLE);
		interval = sphandler.getAsInt(ReminderMain.INTERVAL);
		if (interval == sphandler.INT_DEFAULT) {
			interval = 5 * 600;
		}
		type = sphandler.getAsInt(ReminderMain.TYPE);
		Log.i("get type value", "type value is " + type);
		if (type == sphandler.INT_DEFAULT) {
			type = 1;
		}
		method = sphandler.getAsInt(ReminderMain.METHOD);
		if (method == sphandler.INT_DEFAULT) {
			method = 1;
		}
		ringtone = sphandler.getAsInt(ReminderMain.RINGTONE);
		if (ringtone == sphandler.INT_DEFAULT) {
			ringtone = 1;
		}
		isSleepEnable = sphandler.getAsBoolean(ReminderMain.SLEEPENABLE);
		sleepSetting = sphandler.getAsString(ReminderMain.SLEEPSETTING);
		int[] settings = ReminderMain.getSleepSetting(sleepSetting);
		if (settings != null) {
			hour1 = settings[0];
			minute1 = settings[1];
			hour2 = settings[2];
			minute2 = settings[3];
		} else {
			hour1 = 22;
			minute1 = 0;
			hour2 = 7;
			minute2 = 0;
		}

	}

	private boolean needSleep() {

		if (isSleepEnable == false) {
			return false;
		}

		Calendar calendar = Calendar.getInstance();

		int ch = calendar.get(Calendar.HOUR_OF_DAY);
		int cm = calendar.get(Calendar.MINUTE);

		Log.i("needSleep", "CH is "+ch+" and CM is "+cm);
		Log.i("needSleep", "hour setting is  "+hour1+":"+minute1+" to "+hour2+":"+minute2);
		
		if (ch > hour1 && ch <= 24) {
			return true;
		}

		if (ch == hour1 && ch <= 24 && cm > minute1) {
			return true;
		}

		if (ch >= 0 && ch < hour2) {
			return true;
		}

		if (ch >= 0 && ch == hour2 && cm < minute2) {
			return true;
		}

		return false;
	}

	private static final int RUN = 1;

	private void run() {

		if (handler == null) {
			Log.i("ReminderService",
					"handler is null, create a new handler.................");
			handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {

					Log.i("ReminderService", "handle message..........");
					Log.i("ReminderService", "isEnable is" + isEnable);
					// TODO Auto-generated method stub
					if (isEnable) {
						Log.i("ReminderService", "is enable");
						if (!needSleep()) {
							if (needRemind()) {
								try {
									remind();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								Log.i("ReminderService", "remind");
							}
							handler.sendMessageDelayed(handler
									.obtainMessage(RUN), interval * 1000);
							Log.i("ReminderService", "post message delay"
									+ interval);
						} else {
							Log.i("ReminderService", "It is sleeping time");
							handler.sendMessageDelayed(handler
									.obtainMessage(RUN), interval * 1000);
							Log.i("ReminderService", "post message delay"
									+ interval);
						}
					}else{
						Log.i("ReminderService", "not enable, so delay long");
						handler.sendMessageDelayed(handler.obtainMessage(RUN),
								30 * 1000 * 60);
						Log
								.i(
										"ReminderService",
										"post message delay long time" + 30 * 1000 * 60);

					}

				}
			};
			handler.sendMessageDelayed(handler.obtainMessage(RUN),
					interval * 1000);
			Log.i("ReminderService", "init message has been post to queue. "
					+ interval);
		} else {
			Log
					.i("ReminderService",
							"handler is not null, just reset the queue.................");
			handler.removeMessages(RUN);
			handler.sendMessageDelayed(handler.obtainMessage(RUN),
					interval * 1000);
			Log.i("ReminderService", "post message delay long time" + interval);
		}
	}

	private boolean needRemind() {
		Log.i("ReminderService", "begin to check if need remind, type is "
				+ type);
		Log.i("ReminderService", "SmsHandler.getUnreadCount(this) is "
				+ SmsHandler.getUnreadCount(this)
				+ " CallHandler.getMissingCount(this) is "
				+ CallHandler.getMissingCount(this));
		if (type == 0) {
			if (SmsHandler.getUnreadCount(this) > 0
					|| CallHandler.getMissingCount(this) > 0) {
				return true;
			}
		} else if (type == 1) {
			if (CallHandler.getMissingCount(this) > 0) {
				return true;
			}
		} else if (type == 2) {
			if (SmsHandler.getUnreadCount(this) > 0) {
				return true;
			}
		}
		return false;
	}

	private void remind() {
		Log.i("remind", "current method is "+method);
		if (method == 0 || method == 1) {
			vibratorHandler
					.vibratorNow(this, new long[] { 500, 500, 500, 500 });
		}
		if (method == 0 || method == 2) {
			mediaHandler.play(ringtone);
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (sphandler == null){
			sphandler = new SharePreferenceHandler(this);
		}
		sphandler.setAsInt(ReminderMain.RUNNINGSTATUS, 0);
		
	}
	
	
}
