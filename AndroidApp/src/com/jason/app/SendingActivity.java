package com.jason.app;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jason.app.bean.Event;
import com.jason.app.bean.Message;
import com.jason.app.bean.SmsContact;
import com.jason.app.utils.SmsSender;

public class SendingActivity extends Activity {

	List<String> numbers;
	Event event;
	String message;
	SmsContact[] contacts;
	TextView view;
	boolean isFinished=false;
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
			Log.i("onTouchEvent","ACTION_DOWN");
			if(this.isFinished){
				this.finish();
			}
			break;
		case MotionEvent.ACTION_UP:
			Log.i("onTouchEvent","ACTION_UP");
			break;
		}
		
		return super.onTouchEvent(event);
	}



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		ScrollView scroller = new ScrollView(this);
		scroller.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
		
		view = new TextView(this);
		view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
		view.setTextColor(Color.RED);
		view.setText(this.getResources().getString(R.string.stringBeginSend));
		
		scroller.addView(view);
		this.setContentView(scroller);
		
		Intent intent = this.getIntent();
		event = intent.getExtras().getParcelable(Event.class.getName());
		Log.i("begin","message");
		message = intent.getExtras().getString(Message.class.getName());
		Log.i("end","message");
		Parcelable[] p = intent.getExtras().getParcelableArray(SmsContact.class.getName());
		contacts = new SmsContact[p.length];
		numbers = new ArrayList<String>();
		for(int i =0 ;i<contacts.length;i++){
			contacts[i]=(SmsContact)p[i];
			numbers.add(contacts[i].getPhoneNumber());
		}
		
		new SendingTask().execute(numbers.toArray(new String[1]));
	}
	
	
	
	public class SendingTask extends AsyncTask<String, String, Long>{

		@Override
		protected void onCancelled() {
			Log.i("onCancelled","onCancelled");
			// TODO Auto-generated method stub
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(Long result) {
			// TODO Auto-generated method stub
			Log.i("onPostExecute","onPostExecute");
			isFinished = true;
			view.setText(view.getText().toString()+"\n"+SendingActivity.this.getResources().getString(R.string.stringEndSend));
//			SendingActivity.this.finish();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			Log.i("onPreExecute","onPreExecute");
		}

		@Override
		protected void onProgressUpdate(String... values) {
			view.setText(view.getText().toString()+"\n"
					+SendingActivity.this.getResources().getString(R.string.stringBegin)
					+values[0]
					+SendingActivity.this.getResources().getString(R.string.stringSuccess)
					+"!");
			
			
		}

		@Override
		protected Long doInBackground(String... params) {


			for(String s:params){
				SmsSender.sendSingleMessage(s, message);
				this.publishProgress(s);
			}
			
			return 1l;
		}

		
	}
}
