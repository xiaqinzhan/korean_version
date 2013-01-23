package com.jason.app;

import java.io.File;
import java.io.FilenameFilter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class LoadActivity extends BaseActivity implements OnClickListener {

	public String fileType;

	public static String FILE_TYPE = "fileType";

	public static String FILE_TYPE_TXT = ".txt";
	public static String FILE_TYPE_CSV = ".csv";
	public static String FILE_TYPE_ALL = "all";

	private String filePath;
	private EditText editPath;
	String files[];

	private FilenameFilter filter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.layout_load_file);
		filePath = "/";
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		if(bundle!=null){
			fileType = bundle.getString(FILE_TYPE);
		}
		if (fileType == null) {
			fileType = FILE_TYPE_ALL;
		}

		filter = new FilenameFilter() {

			public boolean accept(File arg0, String arg1) {
				if(arg0.isDirectory()){
					Log.i("directory", arg1);
					return true;
				}
				
				if (arg1.toLowerCase().endsWith(fileType)){
					Log.i("true", arg1);
					return true;
				}else{	
					Log.i("false", arg1);
					return false;
				}
			}
		};

		this.editPath = (EditText) findViewById(R.id.EditCurrentPath);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		String fileName = files[position];
		String newFile;
		
		if (!filePath.endsWith("/")) {
			newFile = filePath + "/" + fileName;
		} else {
			newFile = filePath + fileName;
		}
		
		File f = new File(newFile);
		if (f.isDirectory()) {
			
			filePath = newFile;
			initList();
		} else {
			editPath.setText(newFile);
		}

	}

	private void initList() {
		File parent = new File(filePath);
		editPath.setText(filePath);
		if (fileType != FILE_TYPE_ALL) {
			files = parent.list(filter);
		} else {
			files = parent.list();
		}

		ArrayAdapter adapter = new ArrayAdapter(this,R.layout.layout_item_1,
				R.id.fileName,files);
		this.setListAdapter(adapter);

	}

	protected void onResume() {
		super.onResume();
		initList();
	}

	public static String PATH = "path";

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ButtonLoad:
			File f = new File(editPath.getText().toString());
			if (f.exists() && !f.isDirectory()) {
				Intent intent = new Intent();
				intent.putExtra(PATH, editPath.getText().toString());
				this.setResult(RESULT_OK, intent);
				this.finish();
			} else {
				Toast.makeText(this, getResourceString(R.string.notValidFile), Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.ButtonCancel:
			this.setResult(Activity.RESULT_CANCELED);
			this.finish();
		case R.id.ButtonUp:
			this.upper();
		}

	}
	
	private void upper(){
		if(this.filePath.equals("/")){
			return;
		}else{
			int i = filePath.lastIndexOf("/");
			Log.i("before",filePath);
			if(i==0)i=1;
			filePath = filePath.substring(0,i);
			Log.i("after",filePath);
			this.initList();
		}
	}
}
