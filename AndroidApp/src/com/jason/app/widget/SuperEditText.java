package com.jason.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

public class SuperEditText extends EditText {

	MessageListener listener;
	
	public SuperEditText(Context context) {
		super(context);
	}
	
	
	
	public SuperEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}



	public SuperEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}



	public void setListener(MessageListener listener) {
		this.listener = listener;
	}



	public void onTextChanged(CharSequence text,
             int start, int before, int after) {
		 if(listener != null){
			 listener.onMessage(text.toString());
		 }
}
	 
}
