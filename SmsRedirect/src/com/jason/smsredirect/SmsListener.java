package com.jason.smsredirect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.jason.common.bean.MyContact;
import com.jason.common.db.dao.MyContactDao;

public class SmsListener extends BroadcastReceiver {

	String ACTION = "android.provider.Telephony.SMS_RECEIVED";

	SharePreferenceHandler handler;
	MyContactDao contactDao;
	String startChar;
	String endChar;
	boolean isPartial;
	String numbers;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		handler = new SharePreferenceHandler(context);
		boolean isEnable = handler.getEnable();
		String target = handler.getTarget();
		startChar = handler.getStartChar();
		endChar = handler.getEndChar();
		isPartial = handler.getFilter();
		numbers = handler.getNumbers();
		
		if(!isEnable || target==null || target.trim().length()==0){
			Log.i("onReceive", "no need to handler");
			return;
		}
		
		
		contactDao = new MyContactDao(context);
		
		if (intent.getAction().equals(ACTION)) {
			
			/* The SMS-Messages are 'hiding' within the extras of the Intent. */
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				/* Get all messages contained in the Intent */
				SmsMessage[] messages = getMessagesFromIntent(intent);

				/* Feed the StringBuilder with all Messages found. */
				for (SmsMessage currentMessage : messages) {
					/* Sender-Number */
					String source = currentMessage.getDisplayOriginatingAddress();
					
					if(isPartial){
						if(!needToRedirect(source,numbers)){
							Log.i("onReceive", "no need to redirect");
							return;
						}else{
							Log.i("onReceive", "redirect this message");
						}
					}
					
					if(!isEqual(source,target)){
						StringBuilder sb = new StringBuilder();
						int shortNumber = getShortNumberByFullNumber(currentMessage.getDisplayOriginatingAddress());
						sb.append(context.getResources().getString(R.string.from)+"{"+getContactNameFromNumber(context,source)+"}");
//						sb.append(currentMessage.getDisplayOriginatingAddress());
						
						/* Actual Message-Content */
						sb.append(currentMessage.getDisplayMessageBody());
						sb.append(context.getResources().getString(R.string.reply)+startChar+shortNumber+endChar+context.getResources().getString(R.string.toReply));
						Log.i("onReceive","source not equal to target, send to target");
						sendTextMessage(target,sb.toString());
					}else{
						Log.i("onReceive","equal, start to handle this message");
						handleResponse(currentMessage.getDisplayMessageBody());
							
						}
					}
				}
			
			}

	}

	private boolean needToRedirect(String source, String numbers2) {
		
	
			numbers = handler.getNumbers();
			if(numbers==null || numbers.trim().length()==0){
				return false;
			}
			
			String[] NP = numbers.split(";");

			for (int i = 0; i < NP.length; i++) {
				String parts[] = NP[i].split(":");
				if(this.isEqual(source, parts[0])){
					return true;
				}
			}

		return false;
	}

	private int getShortNumberByFullNumber(String fullNumber) {
		// TODO Auto-generated method stub
		MyContact contact = contactDao.getByFullNumber(fullNumber);
		if(contact==null){
			contact = new MyContact();
			contact.setFullNumber(fullNumber);
			contact.setShortNumber("1");
			int id = (int) contactDao.save(contact);
			return id;
		}
		return contact.get_id();
	}

	private void handleResponse(String message){
		Log.i("handleResponse", "begin to process");
		Log.i("responseInfo", "startChar: "+startChar+" endChar: "+endChar+" message:"+message);
		if(message.startsWith(startChar)){
			int spitter = message.indexOf(endChar);
			if(spitter!=-1){
				String target = message.substring(1,spitter);
				String actualMessage = message.substring(spitter+1);
				String fullNumber = contactDao.getFullNumberByShort(target);
				if(fullNumber!=null){
					sendTextMessage(fullNumber,actualMessage);
				}
			}else{
				Log.i("handleResponse", "not match endChar");
			}
		}else{
			Log.i("handleResponse", "not match startChar");
		}
	}
	
	private SmsMessage[] getMessagesFromIntent(Intent intent) {
		SmsMessage retMsgs[] = null;
		Bundle bdl = intent.getExtras();
		try {
			Object pdus[] = (Object[]) bdl.get("pdus");
			retMsgs = new SmsMessage[pdus.length];
			for (int n = 0; n < pdus.length; n++) {
				byte[] byteData = (byte[]) pdus[n];
				retMsgs[n] = SmsMessage.createFromPdu(byteData);
			}
		} catch (Exception e) {
			Log.e("GetMessages", "fail", e);
		}
		return retMsgs;
	}

	private String getContactNameFromNumber(Context context, String number) {
		// define the columns I want the query to return
		String[] projection = new String[] {
				Contacts.Phones.DISPLAY_NAME,
				Contacts.Phones.NUMBER };
 
		// encode the phone number and build the filter URI
		Uri contactUri = Uri.withAppendedPath(Contacts.Phones.CONTENT_FILTER_URL, Uri.encode(number));
 
		// query time
		Cursor c = context.getContentResolver().query(contactUri, projection, null,
				null, null);
 
		// if the query returns 1 or more results
		// return the first result
		if (c.moveToFirst()) {
			String name = c.getString(c
					.getColumnIndex(Contacts.Phones.DISPLAY_NAME));
			return name;
		}
 
		// return the original number if no match was found
		return number;
	}
	
	private void sendTextMessage(String target,String content){
		SmsManager sm = SmsManager.getDefault();
        sm.sendTextMessage(target, null, content, null, null);
        Log.i("sending message", "target is :"+ target + " and content is: " + content);
	}
	
	public boolean isEqual(String number1,String number2){
		if(number1==null || number2==null || number1.trim().length()==0 || number2.trim().length()==0){
			return false;
		}
			
		int length = number1.length();
		if(length > number2.length()){
			length=number2.length();
		}
		
		number1 = (String) number1.subSequence(number1.length()-length, number1.length());
		number2 = (String) number2.subSequence(number2.length()-length, number2.length());
		if(number1.equalsIgnoreCase(number2)){
			return true;
		}
		
		return false;
	}
	
	
	
	
}
