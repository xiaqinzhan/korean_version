package com.jason.app.utils;

import java.util.ArrayList;

import android.telephony.SmsManager;
import android.util.Log;

public class SmsSender {
	public static void sendMessge(String[] dest, String message){
		new SendThread(dest,message).start();
	}
	
	public static void sendSingleMessage(String dest, String message){
		SmsManager manager = SmsManager.getDefault();
		Log.i("send msg",dest+":"+message);
		ArrayList<String> list = manager.divideMessage(message);
		manager.sendMultipartTextMessage(dest, null, list, null,null);
	}
	
	private static class SendThread extends Thread{
		
		private String[] dest;
		private String message;
		
		public SendThread(String[] dest, String message){
			this.dest = dest;
			this.message = message;
		}
		
		public void run(){
			SmsManager manager = SmsManager.getDefault();
			ArrayList<String> list = manager.divideMessage(message);
			
			for(String address:dest){
				manager.sendMultipartTextMessage(address, null, list, null,null);
				Log.i("sending",address+":"+list.get(0));
			}
		}
	}
}
