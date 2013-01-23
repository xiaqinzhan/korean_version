package com.jason.common.util;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class CallHandler {
	private Context context;

	public CallHandler(Context context) {
		this.context = context;
	}

	public static int getMissingCount(Context context){
		int count;
		Cursor c = context.getContentResolver().query(
		android.provider.CallLog.Calls.CONTENT_URI, null,  android.provider.CallLog.Calls.TYPE+"="+android.provider.CallLog.Calls.MISSED_TYPE+" and "+android.provider.CallLog.Calls.NEW+"=1",null,
		android.provider.CallLog.Calls.DATE + " DESC");

		
		
		// Retrieve the column-indixes of phoneNumber, date and calltype
		count = c.getCount();
		c.close();
		return count;
		/*
		int numberColumn = c.getColumnIndex(
		android.provider.CallLog.Calls.NUMBER);
		int dateColumn = c.getColumnIndex(android.provider.CallLog.Calls.DATE);

		// type can be: Incoming, Outgoing or Missed

		int typeColumn = c.getColumnIndex(android.provider.CallLog.Calls.TYPE);

		int newColumn =  c.getColumnIndex(android.provider.CallLog.Calls.NEW);
		// Will hold the calls, available to the cursor

		// Loop through all entries the cursor provides to us.

		if (c.moveToFirst()) {
			do {
				String callerPhoneNumber = c.getString(numberColumn);
				int callDate = c.getInt(dateColumn);
				int callType = c.getInt(typeColumn);
				int isNew = c.getInt(newColumn);
				Log.i("new column", ""+isNew);
				
				// Drawable currentIcon = null;
				switch (callType) {
				case android.provider.CallLog.Calls.INCOMING_TYPE:
					break;
				case android.provider.CallLog.Calls.MISSED_TYPE:
					break;
				case android.provider.CallLog.Calls.OUTGOING_TYPE:
					break;
				}

				// Convert the unix-timestamp to a readable datestring

			} while (c.moveToNext());

		}

		c.close();
		
		return 0;
		// Create an ListAdapter that manages to display out 'callList'

		// Done =)
*/
	}
}
