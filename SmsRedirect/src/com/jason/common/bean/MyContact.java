package com.jason.common.bean;

import com.jason.common.bean.helper.BaseBeanHelper;
import com.jason.common.bean.helper.MyContactHelper;

public class MyContact extends BaseBean {

	private String shortNumber;
	private String fullNumber;




	@Override
	public BaseBeanHelper getHelper() {
		// TODO Auto-generated method stub
		return MyContactHelper.getInstance();
	}




	public String getShortNumber() {
		return shortNumber;
	}




	public void setShortNumber(String shortNumber) {
		this.shortNumber = shortNumber;
	}




	public String getFullNumber() {
		return fullNumber;
	}




	public void setFullNumber(String fullNumber) {
		this.fullNumber = fullNumber;
	}


}
