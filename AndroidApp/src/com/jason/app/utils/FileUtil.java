package com.jason.app.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.jason.app.bean.SmsContact;

public class FileUtil {

	public static List<String> getListFromFile(String filepath){
		if(filepath==null){
			Log.e(FileUtil.class.getName(),"filepath is null");
			return null;
		}
		
		File f = new File(filepath);
		if(!f.isFile()){
			Log.e(FileUtil.class.getName(),"filepath is not a file");
			return null;
		}
		
		List<String> allString = new ArrayList<String>();
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filepath));
			String s;
			while((s=reader.readLine())!=null){
				allString.add(s);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
	
		return allString;
	}
	
	public static String getStringFromFile(String filepath){
		List<String> all = getListFromFile(filepath);
		if(all != null){
			StringBuffer buffer = new StringBuffer();
			for(String s:all){
				buffer.append(s);
			}
			return buffer.toString();
		}
		return null;
	}
	
	
	
	
}
