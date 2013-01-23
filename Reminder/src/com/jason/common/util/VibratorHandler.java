package com.jason.common.util;

import android.content.Context;
import android.os.Vibrator;
import android.util.Log;

public class VibratorHandler {
	
	public static void vibratorNow(Context context,long vibratorTime){
		Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(vibratorTime);
		
		Log.i("VibratorHandler", vibratorTime+"");
	}
	
	public static void vibratorNow(Context context,long[] pattern){
		Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(pattern, -1);
		Log.i("VibratorHandler", "begin to vibrator");
	}
}
