package com.jason.app.bean.helper;

public class SmsContactHelper extends BaseBeanHelper {

	private SmsContactHelper(){}
	
	private static SmsContactHelper helper; 
	
	public static SmsContactHelper getInstance(){
		if(helper==null){
			helper = new SmsContactHelper();
		}
		return helper;
	}
	
//	private String phoneNumber;
//	private String contactName;
//	private String groupId;
	
	public static String COLUMN_PHONE_NUMBER="phoneNumber";
	public static String COLUMN_CONTACT_NAME="contactName";
	public static String COLUMN_GROUP_ID="groupId";
	
	@Override
	protected String getColumnCreateSQL() {
		// TODO Auto-generated method stub
		String s = COLUMN_PHONE_NUMBER + BaseBeanHelper.SENTENCE_TYPE_TEXT + BaseBeanHelper.SENTENCE_CREATE_COMMA
		+COLUMN_CONTACT_NAME+BaseBeanHelper.SENTENCE_TYPE_TEXT+BaseBeanHelper.SENTENCE_CREATE_COMMA
		+COLUMN_GROUP_ID+BaseBeanHelper.SENTENCE_TYPE_INTEGER;
		return s;
	}

	@Override
	protected String[] getSpecialColumns() {
		// TODO Auto-generated method stub
		return new String[]{COLUMN_PHONE_NUMBER,COLUMN_CONTACT_NAME,COLUMN_GROUP_ID};
	}

	public static String TABLE_NAME="smsContact";
	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return TABLE_NAME;
	}

}
