package com.jason.common.bean.helper;

public class MyContactHelper extends BaseBeanHelper {
	

	public static String COLUMN_SHORT_NUMBER="shortNumber";
	public static String COLUMN_FULL_NUMBER="fullNumber";
	
	private static MyContactHelper _helper;
	
	private MyContactHelper(){}
	
	public static MyContactHelper getInstance(){
		if(_helper == null){
			_helper = new MyContactHelper();
		}
		
		return _helper;
	}
	
	
	@Override
	protected String getColumnCreateSQL() {
		// TODO Auto-generated method stub
		return "shortNumber text,fullNumber text";
	}

	@Override
	protected String[] getSpecialColumns() {
		// TODO Auto-generated method stub
		return new String[]{COLUMN_SHORT_NUMBER,COLUMN_FULL_NUMBER};
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "mycontact";
	}

}
