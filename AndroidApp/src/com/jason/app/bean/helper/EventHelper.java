package com.jason.app.bean.helper;

public class EventHelper extends BaseBeanHelper {

	public static String TABLE_NAME = "event";

	public static String COLUMN_MESSAGE_ID = "message_id";
	public static DbType COLUMN_MESSAGE_ID_TYPE = DbType.INTEGER;

	public static String COLUMN_GROUP_ID = "group_id";
	public static DbType COLUMN_GROUP_ID_TYPE = DbType.INTEGER;
	public static String COLUMN_SCHEDULE_ID = "schedule_id";
	public static DbType COLUMN_SCHEDULE_ID_TYPE = DbType.INTEGER;
	
	public static String COLUMN_ENABLE = "enable"; //
	public static DbType COLUMN_COLUMN_ENABLE_TYPE = DbType.INTEGER;
	
	public static String COLUMN_NAME = "name";
	public static DbType COLUMN_NAME_TYPE = DbType.INTEGER;


	private static EventHelper _helper;
	
	private EventHelper(){}
	
	public static EventHelper getInstance(){
		if(_helper==null){
			_helper = new EventHelper();
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
		return COLUMN_NAME + BaseBeanHelper.SENTENCE_TYPE_TEXT + SENTENCE_CREATE_COMMA 
		+ COLUMN_MESSAGE_ID + BaseBeanHelper.SENTENCE_TYPE_INTEGER + SENTENCE_CREATE_COMMA
		 + COLUMN_GROUP_ID + BaseBeanHelper.SENTENCE_TYPE_INTEGER+ SENTENCE_CREATE_COMMA
		 + COLUMN_SCHEDULE_ID + BaseBeanHelper.SENTENCE_TYPE_INTEGER + SENTENCE_CREATE_COMMA 
		+ COLUMN_ENABLE + BaseBeanHelper.SENTENCE_TYPE_INTEGER;
	}

	@Override
	protected String[] getSpecialColumns() {
		// TODO Auto-generated method stub
		return new String[] { COLUMN_MESSAGE_ID, COLUMN_GROUP_ID,
				COLUMN_SCHEDULE_ID, COLUMN_NAME,COLUMN_ENABLE };
	}

}
