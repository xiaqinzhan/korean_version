package com.jason.common.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.SmsMessage;

public class SmsHandler {
	private Context context;

	public static final Uri SMS_CONTENT_URI = Uri.parse("content://sms");
	public static final Uri SMS_INBOX_CONTENT_URI = Uri.withAppendedPath(
			SMS_CONTENT_URI, "inbox");

	public SmsHandler(Context context) {
		super();
		this.context = context;
	}

	public static int getUnreadCount(Context context) {

		String WHERE_CONDITION = "read = 0";
		Cursor cursor = context.getContentResolver().query(
				SMS_INBOX_CONTENT_URI, new String[] { "_id" }, WHERE_CONDITION,
				null, null);
		int count = cursor.getCount();
		cursor.close();
		return count;
	}
/*
	public static int getSmsDetails(Context context, boolean unreadOnly) {
		String SMS_READ_COLUMN = "read";
		String WHERE_CONDITION = unreadOnly ? SMS_READ_COLUMN + " = 0" : null;
		String SORT_ORDER = "date DESC";
		int count = 0;

		// Log.v(WHERE_CONDITION);

		Cursor cursor = context.getContentResolver().query(
				SMS_INBOX_CONTENT_URI,
				new String[] { "_id", "thread_id", "address", "person", "date",
						"body" }, WHERE_CONDITION, null, SORT_ORDER);

		if (cursor != null) {
			try {
				count = cursor.getCount();
				if (count > 0) {
					cursor.moveToFirst();

					// String[] columns = cursor.getColumnNames();
					// for (int i=0; i<columns.length; i++) {
					// Log.v("columns " + i + ": " + columns[i] + ": "
					// + cursor.getString(i));
					// }

					long messageId = cursor.getLong(0);
					long threadId = cursor.getLong(1);
					String address = cursor.getString(2);
					long contactId = cursor.getLong(3);
					String contactId_string = String.valueOf(contactId);
					long timestamp = cursor.getLong(4);

					String body = cursor.getString(5);

					if (!unreadOnly) {
						count = 0;
					}

				}
			} finally {
				cursor.close();
			}
		}
		return count;
	}
*/
}
