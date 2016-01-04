package com.apical.dvdplayer;

import android.util.Log;

public class LogCat
{
	public static final boolean LOGCAT = true; 	
	private final static String TAG = "yzh";
    public static void Logd(String strlog)
    {
    	if(LOGCAT)
    	{
    		Log.d(TAG, strlog);
    	}
    }
}