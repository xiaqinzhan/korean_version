package com.jason.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.jason.app.bean.helper.BaseBeanHelper;
import com.jason.app.bean.helper.SmsContactHelper;

public class SmsContact  extends BaseBean implements Parcelable{

	private String phoneNumber;
	private String contactName;
	private int groupId;
	
	@Override
	public BaseBeanHelper getHelper() {
		// TODO Auto-generated method stub
		return SmsContactHelper.getInstance();
	}

	

	public String getPhoneNumber() {
		return phoneNumber;
	}



	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}



	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}



	public int getGroupId() {
		return groupId;
	}



	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}



	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}



	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.get_id());
		dest.writeString(this.getCreate_date_time());
		dest.writeString(this.getContactName());
		dest.writeString(this.getPhoneNumber());
		dest.writeInt(this.getGroupId());
		
	}

	public static final Parcelable.Creator<SmsContact> CREATOR = new Parcelable.Creator<SmsContact>() {

		public SmsContact createFromParcel(Parcel source) {
			SmsContact contact = new SmsContact();
			contact.set_id(source.readInt());
			contact.setCreate_date_time(source.readString());
			contact.setContactName(source.readString());
			contact.setPhoneNumber(source.readString());
			contact.setGroupId(source.readInt());
			return contact;
		}

		public SmsContact[] newArray(int size) {
			// TODO Auto-generated method stub
			return new SmsContact[size];
		}
	};

}
