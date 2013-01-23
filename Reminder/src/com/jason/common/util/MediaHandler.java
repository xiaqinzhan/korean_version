package com.jason.common.util;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

public class MediaHandler {
	SoundPool pool;
	Context context;
	AudioManager andioManager;
	HashMap<Integer,Integer> map = new HashMap<Integer, Integer>();
	
	
	public MediaHandler(Context context){
		this.context = context;
		pool = new SoundPool(4, AudioManager.STREAM_RING, 0);
		andioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
	}
	
	public void addSound(int id, int resId){
		map.put(id, pool.load(context, resId, 1));
	}
	
	public void play(int id){
		if(map.containsKey(id)){
			float streamVolume = andioManager.getStreamVolume(AudioManager.STREAM_RING);
			streamVolume = streamVolume / andioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
			Log.i("MediaHandler", "play id is "+id)   ;
			pool.play(map.get(id), streamVolume, streamVolume, 1, 0, 1f);

		}
	}
	
}
