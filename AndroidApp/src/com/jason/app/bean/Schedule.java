package com.jason.app.bean;

import java.util.Calendar;

import com.jason.app.bean.helper.ScheduleHelper;

public class Schedule extends BaseBean {

	private String schedule;

	private int[] data = new int[]{0,0,0,0,0,0};

	@Override
	public ScheduleHelper getHelper() {
		// TODO Auto-generated method stub
		return ScheduleHelper.getInstance();
	}

	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
		this.initData();
	}

	private void initData() {
		if (schedule == null) {
			return;
		}
		String[] cols = schedule.split("-");
		if (cols.length == 6) {
			for (int i = 0; i < cols.length; i++) {
				data[i] = Integer.parseInt(cols[i]);
			}
		}

	}
	
	public String getFormatString(){
		return String.format("%4d-%02d-%02d %02d:%02d:%02d", data[0],data[1],data[2],data[3],data[4],0);
	}
	
	private void refreshSchedule(){
		schedule=data[0]+"-"+data[1]+"-"+data[2]+"-"+data[3]+"-"+data[4]+"-"+data[5];
	}
	
	public void setDate(int year,int month,int day){
		data[0]=year;
		data[1]=month;
		data[2]=day;
		refreshSchedule();
	}
	
	public void setTime(int hour,int minute,int second){
		data[3]=hour;
		data[4]=minute;
		data[5]=second;
		refreshSchedule();
	}
	
	public long getTimeMillis(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(data[0], data[1], data[2], data[3], data[4]);
		return calendar.getTimeInMillis();
	}

	public boolean isInFuture(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(data[0], data[1], data[2], data[3], data[4]);
		Calendar now = Calendar.getInstance();
		if(calendar.compareTo(now)==1){
			return true;
		}
		return false;
	}
	
	public int getYear() {
		return data[0];
	}

	public int getMonth() {
		return data[1];
	}

	public int getDay() {
		return data[2];
	}

	public int getHour() {
		return data[3];
	}

	public int getMinute() {
		return data[4];
	}

	public int getSecond() {
		return data[5];
	}

}
