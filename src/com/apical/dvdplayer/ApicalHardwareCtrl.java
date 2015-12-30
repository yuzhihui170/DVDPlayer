package com.apical.dvdplayer;

import android.content.Intent;
import android.hardware.ApicalCtrlsManager;
import android.util.Log;
import android.view.KeyEvent;

//Apical纭欢鎺у埗鐩稿叧鎺ュ彛灏佽
public class ApicalHardwareCtrl {
	
	private static final String TAG="qulinglingDVD ApicalHardwareCtrl";
	
	//apical椹卞姩鎺ュ彛
	private ApicalCtrlsManager mApicalCtrlsManager;
	//闊虫簮绫诲瀷
    public static final byte APP_DVD = 0x01;
    public static final byte APP_RADIO = 0x02;
    public static final byte APP_IPOD = 0x03;
    
    private static final String APP_DVD_NAME = "ApicalDVD";
    private static final String APP_RADIO_NAME = "ApicalRadio";
    private static final String APP_IPOD_NAME = "ApicalIpod";
    
    private static final String AUDIO_TYPE_DVD = "DVD";
    private static final String AUDIO_TYPE_RADIO = "RADIO";
    private static final String AUDIO_TYPE_IPOD = "ApicalIpod";
    
    private String mAppNameString = "";
    private String mVedioTypeString = "";
    
    public ApicalHardwareCtrl(byte AppType) 
    {
    	//椹卞姩鎺ュ彛鎺у埗
    	mApicalCtrlsManager = new ApicalCtrlsManager();
        switch (AppType)
		{
		case APP_DVD:
		    mAppNameString = APP_DVD_NAME;
		    mVedioTypeString = AUDIO_TYPE_DVD;
			break;
		case APP_RADIO:
		    mAppNameString = APP_RADIO_NAME;
		    mVedioTypeString = AUDIO_TYPE_RADIO;
			break;
		case APP_IPOD:
		    mAppNameString = APP_IPOD_NAME;
		    mVedioTypeString = AUDIO_TYPE_IPOD;
			break;
		default:
			break;
		}
	}
	
  //瑙嗛婧愮敵璇�
    public int VideoSourceRequest() 
    {
    	try 
    	{	
    		mApicalCtrlsManager.VideoSourceRequest(mAppNameString, mVedioTypeString, 2);
		} 
    	catch (Exception e) 
    	{
			Log.e(TAG,"VideoSourceRequest()--->" + e);
		}
		return 0;
	}
    
  //瑙嗛婧愰噴鏀�
    public int VideoSourceRelease() {
    	
    	try 
    	{	
    		mApicalCtrlsManager.VideoSourceRelease(mAppNameString);
		} 
    	catch (Exception e) 
    	{
			Log.e(TAG,"VideoSourceRelease()--->" + e);
		}
		return 0;
	}
    
  //闊抽婧愮敵璇�
    public int AudioSourceRequest() {
    	
    	try 
    	{	
    		Log.d(TAG,"AudioSourceRequest()--->Request");
    		mApicalCtrlsManager.AudioSourceRequst(mAppNameString, mVedioTypeString, 2);
		} 
    	catch (Exception e) 
    	{
			Log.e(TAG,"AudioSourceRequest()--->" + e);
		}
    	
    	return 0;
	}
   

  //闊抽婧愰噴鏀�
    public int AudioSourceRelease() {
    	
    	try 
    	{	
    		Log.d(TAG,"AudioSourceRequest()--->Release");
    		mApicalCtrlsManager.AudioSourceDelayRelease(mAppNameString, 0);
		} 
    	catch (Exception e) 
    	{
			Log.e(TAG,"AudioSourceRequest()--->" + e);
		}
    	
    	return 0;
	}
    
  //闈欓煶鎺ュ彛
    public int MuteRequest() {
    	try 
    	{	
    		mApicalCtrlsManager.PaMuteRequest(mAppNameString, 1);
		} 
    	catch (Exception e) 
    	{
			Log.e(TAG,"MuteRequest()--->" + e);
		}
    	
    	return 0;
	}

    
}
