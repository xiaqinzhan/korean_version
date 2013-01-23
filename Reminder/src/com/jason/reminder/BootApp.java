package com.jason.reminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.jason.common.util.SharePreferenceHandler;

public class BootApp extends BroadcastReceiver {

	SharePreferenceHandler sphandler;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		Log.i("startService", "**************start service*************************************");
		
		// TODO Auto-generated method stub
		Intent intentService = new Intent(context,ReminderService.class);
		context.startService(intentService);
		
		if (sphandler == null){
			sphandler = new SharePreferenceHandler(context);
		}
		sphandler.setAsInt(ReminderMain.RUNNINGSTATUS, 1);
		Log.i("startService", "**************start service************");
	}

}
