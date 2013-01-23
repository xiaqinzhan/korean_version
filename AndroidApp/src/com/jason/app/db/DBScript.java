package com.jason.app.db;

public class DBScript {
	public static String CREATE_MESSAGE="CREATE TABLE MESSAGE ("
		+"  _id        INTEGER primary key autoincrement,"
		+"  CONTEXT      TEXT,"
		+"  CREATE_DATE_TIME  TEXT"
		+");";
	
	public static String DROP_MESSAGE="DROP TABLE IF EXISTS MESSAGE";
	
	public static String CREATE_CONTACT="CREATE TABLE CONTACT ("
			+"  _id        INTEGER primary key autoincrement,"
			+"  NAME        TEXT,"
			+"  PHONE       TEXT"
			+");";

	public static String DROP_CONTACT="DROP TABLE IF EXISTS CONTACT";
	
	public static String CREATE_SMSGROUP="CREATE TABLE SMSGROUP ("
		+"  _id        INTEGER primary key autoincrement,"
		+"  GROUP_NAME     TEXT,"
		+"  CREATE_DATE_TIME  TEXT"
		+");";

	public static String DROP_SMSGROUP="DROP TABLE IF EXISTS SMSGROUP";
	
	public static String CREATE_SCHEDULE="CREATE TABLE SCHEDULE ("
		+"  _id        INTEGER primary key autoincrement,"
		+"  DATE        TEXT,"
		+"  TIME        TEXT"
		+");";
	
	public static String DROP_SCHEDULE="DROP TABLE IF EXISTS SCHEDULE";
	
	public static String CREATE_EVENT="CREATE TABLE EVENT ("
		+"  _id        INTEGER primary key autoincrement,"
		+"  MESSAGE_ID     INTEGER,"
		+"  GROUP_ID      INTEGER,"
		+"  SCHEDULE_ID    INTEGER,"
		+"  NAME        TEXT"
		+");";
	
	public static String DROP_EVENT="DROP TABLE IF EXISTS EVENT";
}
