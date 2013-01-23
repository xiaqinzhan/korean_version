package com.jason.app.bean.helper;

public class ScheduleHelper extends BaseBeanHelper {

	private static ScheduleHelper helper;
	
	private ScheduleHelper(){}
	
	public static final ScheduleHelper getInstance(){
		return helper==null?new ScheduleHelper():helper;
	}
	
	@Override
	protected String getColumnCreateSQL() {
		// TODO Auto-generated method stub
		return COLUMN_SCHEDULE+BaseBeanHelper.SENTENCE_TYPE_TEXT;
	}

	public static String COLUMN_SCHEDULE="schedule";
	
	
	@Override
	protected String[] getSpecialColumns() {
		// TODO Auto-generated method stub
		return new String[]{COLUMN_SCHEDULE};
	}

	private static final String TABLE_NAME="schedule";
	
	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return TABLE_NAME;
	}

}
