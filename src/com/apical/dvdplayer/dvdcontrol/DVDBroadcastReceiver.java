package com.apical.dvdplayer.dvdcontrol;

import com.apical.dvdplayer.LogCat;
import com.apical.dvdplayer.dvdcontrol.zorandvd.ZoranDVD;
import com.apical.dvdplayer.dvdmodel.AppData;
import com.apical.dvdplayer.dvdmodel.DVDDealSet;
import android.app.DvdControl;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

public class DVDBroadcastReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) // 开机广播
		{
			LogCat.Logd("DVDBroadcastReceiver -- BOOT_COMPLETED");
			intent.setClass(context, DVDService.class);
			context.startService(intent);
		}
		else if(intent.getAction().equals("Apical_Key_Broadcast"))
		{
			Bundle keyBundle = new Bundle();
			keyBundle = intent.getExtras();
			boolean downState = keyBundle.getBoolean("Action"); // 按钮状态
			int keyCode = keyBundle.getInt("Code"); // 按钮标识
			
			LogCat.Logd("DVDBroadcastReceiver  onReceiver Apical_Key_Broadcast action , code : " + downState + keyCode);

			if (downState == true) 
			{
				if (keyCode == KeyEvent.KEYCODE_MEDIA_EJECT/* 129 */) 
				{
					
				} 
				else if (keyCode == KeyEvent.KEYCODE_PROG_YELLOW/* 164 */) // 启动DVD
				{
					Intent serviceIntent = new Intent(context, 
							com.apical.dvdplayer.dvdcontrol.DVDService.class);
					Bundle bundle = new Bundle();  
					bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.LAUNCH_APP);
					bundle.putByte("launch_type", (byte)0x01);
					serviceIntent.putExtras(bundle);
					context.startService(serviceIntent);
					
					LogCat.Logd("DVDBroadcastReceiver -- startService  LAUNCH_APP");
				}
			}
		}
		else if(intent.getAction().equals("apk_bc_setup"))
		{
			Bundle data = intent.getExtras();
	        int cmd = data.getInt("cmd");
	        byte mode = data.getByte("WorkMode");
	        
	        LogCat.Logd("DVDBroadcastReceiver  onReceiver apk_bc_setup cmd , mode : " + cmd + mode);
	        if (cmd == DvdControl.SETUP_MCU_RET_WORKMODE)
	        {
	        	if (mode == DVDDealSet.DVD_MODE)
	        	{
	        		Intent serviceIntent = new Intent(context, 
							com.apical.dvdplayer.dvdcontrol.DVDService.class);
					Bundle bundle = new Bundle();  
					bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.LAUNCH_APP);
					bundle.putByte("launch_type", (byte)0x01);
					serviceIntent.putExtras(bundle);
					context.startService(serviceIntent);
					
					LogCat.Logd("DVDBroadcastReceiver -DVD_MODE- startService  LAUNCH_APP");
	        	}
	        	else
	        	{
	        		Intent serviceIntent = new Intent(context, 
	        				com.apical.dvdplayer.dvdcontrol.DVDService.class);
	        		Bundle bundle = new Bundle();  
	                bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.DVD_EXIT_APP);
	                serviceIntent.putExtras(bundle);
	                context.startService(serviceIntent);
	                
	                LogCat.Logd("DVDBroadcastReceiver -else- startService  DVD_EXIT_APP");
	        	}
	        }
		}
		else if(intent.getAction().equals("apk_bc_key"))
		{
			Bundle data = intent.getExtras();

			int cmd = data.getInt("cmd");
			if (cmd == DvdControl.KEY_MCU_RET_KEY) 
			{
				byte keyCode = data.getByte("KeyCode");
				byte KeyStatus = data.getByte("KeyStatus");
				
				if (keyCode == 0x53) 
				{    	    	        
					//enterDVDPlayer(context, ContactApp.DVDAppStatus.DVDAppStatus_DVD);
				} 
				else if (((KeyStatus == 0x01)||((KeyStatus%10) == 0))  && (keyCode == 0x07)) // 下一曲
				{
					Intent serviceIntent = new Intent(context, 
							com.apical.dvdplayer.dvdcontrol.DVDService.class);
					Bundle bundle = new Bundle(); 
					
					bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.NEXT_ONE);
					
					serviceIntent.putExtras(bundle);
			        context.startService(serviceIntent);
				} 
				else if (((KeyStatus == 0x01)||((KeyStatus%10) == 0))  && (keyCode == 0x08)) // 上一曲
				{
					Intent serviceIntent = new Intent(context, 
							com.apical.dvdplayer.dvdcontrol.DVDService.class);
					Bundle bundle = new Bundle(); 
					
					bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.PRE_ONE);
					
					serviceIntent.putExtras(bundle);
			        context.startService(serviceIntent);
				} 
				else if ((KeyStatus == 0x01) && (keyCode == 0x35)) // 播放/暂停
				{
					Intent serviceIntent = new Intent(context, 
							com.apical.dvdplayer.dvdcontrol.DVDService.class);
					Bundle bundle = new Bundle(); 
					
					bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.PLAY_PAUSE);
					
					serviceIntent.putExtras(bundle);
			        context.startService(serviceIntent);
				} 
				else if (keyCode == 0x3B) // 停止0x3B
				{
					Intent serviceIntent = new Intent(context, 
							com.apical.dvdplayer.dvdcontrol.DVDService.class);
					Bundle bundle = new Bundle(); 
					
					bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.STOP);
					
					serviceIntent.putExtras(bundle);
			        context.startService(serviceIntent);
				}
			}
		}
	}
}