package com.jason.app.bean.helper;



public class MessageHelper extends BaseBeanHelper {
	public static DbType COLUMN_CONTEXT_TYPE = DbType.TEXT;
	public static String COLUMN_CONTEXT = "context";


	public static String TABLE_NAME = "message";

	private static String[] columns = new String[] { COLUMN_ID,
			COLUMN_CONTEXT, COLUMN_CREATE_DATE_TIME };

	private static String CREATE_TABLE = BaseBeanHelper.SENTENCE_CREATE_TABLE+TABLE_NAME
			+BaseBeanHelper.SENTENCE_CREATE_START_BRACKET
			+BaseBeanHelper.SENTENCE_CREATE_ID_LINE
			+COLUMN_CONTEXT+BaseBeanHelper.SENTENCE_TYPE_TEXT+BaseBeanHelper.SENTENCE_CREATE_COMMA
			+COLUMN_CREATE_DATE_TIME + BaseBeanHelper.SENTENCE_TYPE_TEXT
			+BaseBeanHelper.SENTENCE_CREATE_END_BRACKET;

	private static MessageHelper _helper;
	
	private MessageHelper(){}
	
	public static MessageHelper getInstance(){
		if(_helper==null){
			_helper = new MessageHelper();
		}
		return _helper;
	}
	
	
	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return TABLE_NAME;
	}


	@Override
	protected String getColumnCreateSQL() {
		// TODO Auto-generated method stub
		return COLUMN_CONTEXT+BaseBeanHelper.SENTENCE_TYPE_TEXT;
	}


	@Override
	protected String[] getSpecialColumns() {
		// TODO Auto-generated method stub
		return  new String[] { COLUMN_CONTEXT };
	}
	
	
	


	
}
