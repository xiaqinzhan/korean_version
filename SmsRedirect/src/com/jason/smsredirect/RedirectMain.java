package com.jason.smsredirect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import any4media_27.challenge.KafUtil;

import com.kaf.KafManager;

public class RedirectMain extends ListActivity implements OnClickListener {
	CheckBox cbEnable;
	EditText editTarget, editStart, editEnd;
	SharePreferenceHandler handler;

	Button buttonAdd;
	RadioGroup radioGroup;
	RadioButton buttonAll, buttonPartial;
	ListView listNumber;

	String numbers;

	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		KafUtil.KafInit(this, KafManager.INIT_COPYRIGHT);
		
		cbEnable = (CheckBox) this.findViewById(R.id.CheckEnable);
		editTarget = (EditText) this.findViewById(R.id.EditTarget);
		editStart = (EditText) this.findViewById(R.id.EditStartChar);
		editEnd = (EditText) this.findViewById(R.id.EditEndChar);
		listNumber = (ListView) this.findViewById(android.R.id.list);

		LayoutInflater inflator = this.getLayoutInflater();
		View header = inflator.inflate(R.layout.listheader, null);
		listNumber.addHeaderView(header);

		buttonAdd = (Button) this.findViewById(R.id.ButtonAdd);

		radioGroup = (RadioGroup) this.findViewById(R.id.group1);
		buttonAll = (RadioButton) this.findViewById(R.id.RadioAll);
		buttonPartial = (RadioButton) this.findViewById(R.id.RadioPartial);

		buttonAdd.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				RedirectMain.this.showDialog(DIALOG_NEW);
			}

		});

		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.RadioAll) {
					listNumber.setVisibility(View.GONE);
				} else {
					listNumber.setVisibility(View.VISIBLE);
				}
			}
		});

		handler = new SharePreferenceHandler(this);

		cbEnable.setChecked(handler.getEnable());
		editTarget.setText(handler.getTarget());
		editStart.setText(handler.getStartChar());
		editEnd.setText(handler.getEndChar());
		if (handler.getFilter()) {
			buttonPartial.setChecked(true);
			listNumber.setVisibility(View.VISIBLE);
		} else {
			buttonAll.setChecked(true);
			listNumber.setVisibility(View.GONE);
		}

	}

	private void refreshList() {

		
		
		SimpleAdapter adapter = new SimpleAdapter(this, list,
				R.layout.layout_item_contact, new String[] { NUMBER, CONTACT },
				new int[] { R.id.contactPhone, R.id.contactName });
		listNumber.setAdapter(adapter);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		this.initListData();
		refreshList();
	}

	private static final String CONTACT = "contact";
	private static final String NUMBER = "number";

	List<Map<String, String>> list;
	
	private void initListData() {

		list = new ArrayList<Map<String, String>>();
		
		numbers = handler.getNumbers();
		if(numbers==null || numbers.trim().length()==0){
			return;
		}
		
		String[] NP = numbers.split(";");

		for (int i = 0; i < NP.length; i++) {
			String parts[] = NP[i].split(":");
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(NUMBER, parts[0]);
			if(parts.length==2)
				map.put(CONTACT, parts[1]);
			else
				map.put(CONTACT, "");
			list.add(map);
		}

	}

	public void onClick(View v) {
		if (v.getId() == R.id.ButtonOK) {
			String startChar = editStart.getText().toString();
			String endChar = editEnd.getText().toString();

			if (startChar == null || startChar.length() == 0) {
				Toast.makeText(this, this.getResources().getString(
						R.string.startCharNotNull), Toast.LENGTH_SHORT);
				editStart.requestFocus();
				return;
			}

			if (endChar == null || endChar.length() == 0) {
				Toast.makeText(this, this.getResources().getString(
						R.string.endCharNotNull), Toast.LENGTH_SHORT);
				editEnd.requestFocus();
				return;
			}

			if (startChar.equals(endChar)) {
				Toast.makeText(this, this.getResources().getString(
						R.string.startEndShouldNotEqual), Toast.LENGTH_SHORT);
				editEnd.requestFocus();
				return;
			}

			handler.putEnable(cbEnable.isChecked());
			handler.putTarget(editTarget.getText().toString());
			handler.putStartChar(startChar);
			handler.putEndChar(endChar);

			// begin to save isFilter
			if (buttonAll.isChecked()) {
				handler.putFilter(false);
			} else {
				handler.putFilter(true);
			}
			
			this.saveNumber();
			this.finish();
		}else if(v.getId() == R.id.ButtonAbout){
			this.showDialog(DIALOG_ABOUT);
		}else{
			this.finish();
		}
		
	}
	
	private void saveNumber(){
		if(this.list==null || this.list.size()==0){
			return;
		}
		
		StringBuffer buffer = new StringBuffer();
		
		for(Map<String,String> map:list){
			if(buffer.length()>0){
				buffer.append(";");
			}
			buffer.append(map.get(NUMBER));
			buffer.append(":");
			buffer.append(map.get(CONTACT));
		}
		
		this.handler.putNumbers(buffer.toString());
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, 1, 0, R.string.about);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		this.showDialog(DIALOG_ABOUT);
		return super.onOptionsItemSelected(item);
	}

	private static final int DIALOG_ABOUT = 1;
	private static final int DIALOG_NEW = 2;
	private static final int DIALOG_EDIT = 3;

	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = this.getLayoutInflater();
		final View view = inflater.inflate(R.layout.layout_edit_2item, null);
		switch (id) {
		case DIALOG_ABOUT:
			builder.setTitle(R.string.aboutSoft);
			builder.setMessage(R.string.aboutDetail);
			builder.setPositiveButton(R.string.buttonOK, null);
			break;
		case DIALOG_NEW:
			builder.setTitle(R.string.stringNewContact);
			builder.setView(view);
			builder.setNegativeButton(R.string.buttonCancel, new  DialogInterface.OnClickListener(){

				public void onClick(DialogInterface dialog, int which) {
					removeDialog(DIALOG_NEW);
				}});
			builder.setPositiveButton(R.string.buttonOK, new  DialogInterface.OnClickListener(){

				public void onClick(DialogInterface dialog, int which) {
					String contact = ((EditText)view.findViewById(R.id.editItem1)).getText().toString();
					String number = ((EditText)view.findViewById(R.id.editItem2)).getText().toString();
					if(number.trim().length()<1){
						Toast.makeText(RedirectMain.this, R.string.WarningNumberNotNull, Toast.LENGTH_SHORT).show();
					}else{
						addNewContact(contact,number);
					}
					
				}});
			
			break;
		case DIALOG_EDIT:
			builder.setTitle(R.string.stringEditContact);
			builder.setView(view);
			((EditText)view.findViewById(R.id.editItem1)).setText(list.get(position).get(CONTACT));
			((EditText)view.findViewById(R.id.editItem2)).setText(list.get(position).get(NUMBER));
			builder.setNegativeButton(R.string.buttonCancel, new  DialogInterface.OnClickListener(){

				public void onClick(DialogInterface dialog, int which) {
					removeDialog(DIALOG_EDIT);
				}});
			builder.setNeutralButton(R.string.buttonOK, new  DialogInterface.OnClickListener(){
			
				public void onClick(DialogInterface dialog, int which) {
					String contact = ((EditText)view.findViewById(R.id.editItem1)).getText().toString();
					String number = ((EditText)view.findViewById(R.id.editItem2)).getText().toString();
					if(number.trim().length()<1){
						Toast.makeText(RedirectMain.this, R.string.WarningNumberNotNull, Toast.LENGTH_SHORT).show();
					}else{
						editContact(contact,number);
					}
					
				}});
			
			builder.setPositiveButton(R.string.buttonDelete, new  DialogInterface.OnClickListener(){
			
				public void onClick(DialogInterface dialog, int which) {
					
						deleteContact();
					
				}});
			break;

		}

		return builder.create();
	}
	
	public void addNewContact(String contact,String number){
		Toast.makeText(this, R.string.addSuccessful, Toast.LENGTH_SHORT).show();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(NUMBER, number);
		map.put(CONTACT, contact);
		list.add(map);
		this.refreshList();
		this.removeDialog(DIALOG_NEW);
		
	}
	
	public void editContact(String contact,String number){
		Toast.makeText(this, R.string.editSuccessful, Toast.LENGTH_SHORT).show();
		HashMap<String, String> map = (HashMap<String, String>) list.get(position);
		map.put(NUMBER, number);
		map.put(CONTACT, contact);
		this.refreshList();
	
		
	}
	
	public void deleteContact(){
		this.list.remove(position);	
		
		this.refreshList();
		Toast.makeText(this, R.string.deleteSuccessful, Toast.LENGTH_SHORT).show();
		
	}

	private int position = -2;
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		this.removeDialog(DIALOG_EDIT);
		super.onListItemClick(l, v, position, id);
		this.position = position-1;
		this.showDialog(DIALOG_EDIT);
	}

	
}