package com.jason.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.jason.app.bean.helper.BaseBeanHelper;
import com.jason.app.bean.helper.EventHelper;

public class Event extends BaseBean implements Parcelable{

	private int messageId;
	private int groupId;
	private int scheduleId;
	private int enable; // 1 for enable and 0 for disable
	
	public int getEnable() {
		return enable;
	}

	public void setEnable(int enable) {
		this.enable = enable;
	}

	private String name;
	
	@Override
	public BaseBeanHelper getHelper() {
		// TODO Auto-generated method stub
		return EventHelper.getInstance();
	}

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public int getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(int scheduleId) {
		this.scheduleId = scheduleId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.get_id());
		dest.writeString(this.getCreate_date_time());
		dest.writeInt(this.getGroupId());
		dest.writeInt(this.getScheduleId());
		dest.writeInt(this.getEnable());
		dest.writeInt(this.getMessageId());
		dest.writeString(this.getName());

	}
	
	public static final Parcelable.Creator<Event> CREATOR = 
		new Parcelable.Creator<Event>(){

			public Event createFromParcel(Parcel source) {
				// TODO Auto-generated method stub
				Event event = new Event();
				event.set_id(source.readInt());
				event.setCreate_date_time(source.readString());
				event.setGroupId(source.readInt());
				event.setScheduleId(source.readInt());
				event.setEnable(source.readInt());
				event.setMessageId(source.readInt());
				event.setName(source.readString());
				return event;
			}

			public Event[] newArray(int size) {
				// TODO Auto-generated method stub
				return new Event[size];
			}};
}
