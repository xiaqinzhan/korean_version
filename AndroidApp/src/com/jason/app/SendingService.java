package com.jason.app;

import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Log;

import com.jason.app.bean.Event;
import com.jason.app.bean.Message;
import com.jason.app.bean.SmsContact;
import com.jason.app.dao.EventDao;
import com.jason.app.utils.SmsSender;

public class SendingService extends Service {

	List<String> numbers;
	Event event;
	String message;
	SmsContact[] contacts;
	EventDao eventDao;
	
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public void onRebind(Intent intent) {
		// TODO Auto-generated method stub
		super.onRebind(intent);
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		sendMsg(intent);
		return super.onStartCommand(intent, flags, startId);
	}

	public void sendMsg(Intent intent){
		
		event = intent.getExtras().getParcelable(Event.class.getName());
		
		eventDao = EventDao.getInstance(this);
		Event eventNow = (Event) eventDao.getById(event.get_id());
		
		if(eventNow.getEnable()==0){
			Log.i("sendingService", "event is disabled, no need to send");
			return;
		}else{
			Log.i("sendingService", "event is enable, start to send");
		}
		
		message = intent.getExtras().getString(Message.class.getName());
		Parcelable[] p = intent.getExtras().getParcelableArray(SmsContact.class.getName());
		contacts = new SmsContact[p.length];
		numbers = new ArrayList<String>();
		for(int i =0 ;i<contacts.length;i++){
			contacts[i]=(SmsContact)p[i];
			numbers.add(contacts[i].getPhoneNumber());
		}
		
		SmsSender.sendMessge(numbers.toArray(new String[1]), message);
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		return super.onUnbind(intent);
	}

	private Intent intent;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		if(mBinder==null){
			mBinder = new LocalBinder();
		}
		this.intent = intent;
		
		return mBinder;
	}

	private static LocalBinder mBinder;
	
	public class LocalBinder extends Binder{
		public SendingService getService(){
			return SendingService.this;
		}
	};

	
}
