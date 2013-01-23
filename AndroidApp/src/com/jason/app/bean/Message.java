package com.jason.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.jason.app.bean.helper.BaseBeanHelper;
import com.jason.app.bean.helper.MessageHelper;

public class Message extends BaseBean implements Parcelable {

	private String context;

	

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}


	@Override
	public BaseBeanHelper getHelper() {
		// TODO Auto-generated method stub
		return MessageHelper.getInstance();
	}

	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.get_id());
		dest.writeString(this.getCreate_date_time());
		dest.writeString(this.getContext());
		
	}

	public static final Parcelable.Creator<Message> CREATOR = new Parcelable.Creator<Message>() {

		public Message createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			Message message = new Message();
			message.set_id(source.readInt());
			message.setCreate_date_time(source.readString());
			message.setContext(source.readString());
			return message;
		}

		public Message[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Message[size];
		}
	};
}
