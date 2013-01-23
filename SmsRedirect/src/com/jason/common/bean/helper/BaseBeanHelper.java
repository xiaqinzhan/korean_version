package com.jason.common.bean.helper;

import java.util.ArrayList;
import java.util.Arrays;


public abstract class BaseBeanHelper {
	
	public static String COLUMN_ID = "_id"; 
	public static DbType COLUMN_ID_TYPE=DbType.INTEGER;
	
	public static DbType COLUMN_CREATE_DATE_TIME_TYPE = DbType.TEXT;
	public static String COLUMN_CREATE_DATE_TIME = "create_date_time";
	
	public static String SENTENCE_CREATE_TABLE = "create table ";
	public static String SENTENCE_DROP_TABLE = "drop table ";
	
	private String[] PARENT_COLUMNS = new String[]{COLUMN_ID,COLUMN_CREATE_DATE_TIME};
	
	public static String SENTENCE_CREATE_ID_LINE = COLUMN_ID + " INTEGER primary key autoincrement,";
	public static String SENTENCE_CREATE_COMMA =" ,";
	public static String SENTENCE_TYPE_TEXT =" TEXT";
	public static String SENTENCE_TYPE_INTEGER =" INTEGER";
	public static String SENTENCE_CREATE_START_BRACKET=" ( ";
	public static String SENTENCE_CREATE_END_BRACKET=" );";
	
	public abstract String getTableName();
	
	public String[] getColumns(){
		ArrayList<String> parentList = new ArrayList<String>(Arrays.asList(PARENT_COLUMNS));
		parentList.addAll(Arrays.asList(getSpecialColumns()));
		String[] strArr = new String[parentList.size()];
		System.arraycopy(parentList.toArray(), 0, strArr, 0, parentList.size());
		return strArr;
	}
	
	public String getCreateSQL(){
		return  BaseBeanHelper.SENTENCE_CREATE_TABLE+getTableName()
		+BaseBeanHelper.SENTENCE_CREATE_START_BRACKET
		+BaseBeanHelper.SENTENCE_CREATE_ID_LINE
		+BaseBeanHelper.COLUMN_CREATE_DATE_TIME+BaseBeanHelper.SENTENCE_TYPE_TEXT+BaseBeanHelper.SENTENCE_CREATE_COMMA
		+getColumnCreateSQL()
		+SENTENCE_CREATE_END_BRACKET;
	}
	
	protected abstract String getColumnCreateSQL();
	
	protected abstract String[] getSpecialColumns();
	
	public enum DbType{
		INTEGER,TEXT,BLOC,
	}

	public String getDropSQL(){
		return "DROP TABLE IF EXISTS "+getTableName()+";";
	}
	
	public static String getIdColumnExpress(){
		return "_id=?";
	}
	
	public int getColumnIndexByName(String name){
		for(int i=0;i<this.getColumns().length;i++){
			if(name.equalsIgnoreCase(this.getColumns()[i])){
				return i;
			}
		}
		return -1;
	}
}
