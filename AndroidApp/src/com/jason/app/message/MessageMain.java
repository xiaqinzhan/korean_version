package com.jason.app.message;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SimpleCursorAdapter;

import com.jason.app.BaseActivity;
import com.jason.app.R;
import com.jason.app.bean.Message;
import com.jason.app.bean.helper.MessageHelper;
import com.jason.app.dao.MessageDao;

public class MessageMain extends BaseActivity implements OnClickListener{

	MessageDao dao;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.layout_message);
		dao = new MessageDao(this);
		fillData(); 
		
	}
	
	private void fillData(){
		Cursor cursor = dao.getAllAsCursor(null);
//		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,R.layout.layout_item_message,
//				cursor, new String[]{MessageHelper.CONTEXT_COLUMN,MessageHelper.CREATE_DATE_TIME_COLUMN},
//				new int[]{R.id.TextViewItemMessageContext,R.id.TextViewItemMessageDate});
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,R.layout.layout_item_message,
				cursor, new String[]{MessageHelper.COLUMN_CONTEXT,MessageHelper.COLUMN_CREATE_DATE_TIME},
				new int[]{R.id.MessageName,R.id.CreateDate});
		this.setListAdapter(adapter);
		LayoutInflater d;
//		 SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.noteslist_item, cursor,
//	                new String[] { Notes.TITLE }, new int[] { android.R.id.text1 });
	}
	
	public void createMessage(){
		
		Message message = new Message();
		message.setContext("This is is ");
		dao.save(message);
	
	}

	public void onClick(View v) {
				
	}
}
