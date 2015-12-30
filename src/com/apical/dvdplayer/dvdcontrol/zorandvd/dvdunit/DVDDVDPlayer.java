package com.apical.dvdplayer.dvdcontrol.zorandvd.dvdunit;

import java.util.List;

import com.apical.dvdplayer.LogCat;
import com.apical.dvdplayer.dvdcontrol.DVDEngineBase;
import com.apical.dvdplayer.dvdcontrol.DVDService;
import com.apical.dvdplayer.dvdcontrol.zorandvd.ZoranDVD;
import com.apical.dvdplayer.dvdmodel.DVDDealSet;
import com.apical.dvdplayer.dvdview.DVDDVDPlayerActivity;
import com.apical.dvdplayer.dvdview.DVDMp5Activity;

import android.app.ActivityManager;
import android.app.DvdControl;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class DVDDVDPlayer extends DVDEngineBase
{
	
	Context mContext = null;
	private byte mPlayState = 0;
	
	private static DVDDVDPlayer dvdDVDPlayer = null;
	private DVDDVDPlayer()
	{
		
	}
	
	public static DVDDVDPlayer Instance()
	{
		if(dvdDVDPlayer == null)
		{
			dvdDVDPlayer = new DVDDVDPlayer();
		}
		
		return dvdDVDPlayer;
	}
	
	/**/
	public void Show(Context context)
	{
        LogCat.Logd("DVDDVDPlayer -- Show");
		
		mContext = context;
		
		if(DVDDVDPlayerActivity.DVDDVDPlayerAct == null)
		{
			LogCat.Logd("DVDDVDPlayer act != null");
			Intent dvdplayer = new Intent(context, 
					com.apical.dvdplayer.dvdview.DVDDVDPlayerActivity.class);
			dvdplayer.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			dvdplayer.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			dvdplayer.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			dvdplayer.putExtra("setModeFlag", 1) ;
			context.startActivity(dvdplayer);
			
		}
		else
		{
			LogCat.Logd("DVDDVDPlayer act != null");
//			Intent dvdplayer = new Intent(context, 
//					com.apical.dvdplayer.dvdview.DVDDVDPlayerActivity.class);
//			dvdplayer.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////			dvdplayer.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////			dvdplayer.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//			dvdplayer.putExtra("setModeFlag", 1) ;
//			context.startActivity(dvdplayer);
			
			ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE); 
			List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
//			String TouchpackageName = TOUCH_PACKAGE_NAME;
//			if(tasksInfo.size() > 0)
//			{
//				if(TouchpackageName.equals(tasksInfo.get(0).topActivity.getPackageName())) //触摸校准界面
//				{
//					Log.d("DVD_Service", "code_follow222 : enterDVDPlayer -- tasksInfo.size() > 0");
//					return false;
//				}
//			}
			
			String packageName = "com.apical.dvdplayer";
			if(tasksInfo.size() > 0)
			{
				LogCat.Logd("DVDDVDPlayer tasksInfo.size() > 0");
				//DVD 未置顶
				if( !packageName.equals(tasksInfo.get(0).topActivity.getPackageName()) )
				{
					LogCat.Logd("DVDDVDPlayer not top");
					final ActivityManager am = (ActivityManager)
					           context.getSystemService(Context.ACTIVITY_SERVICE);
					am.moveTaskToFront(DVDDVDPlayerActivity.DVDDVDPlayerAct.getTaskId(), ActivityManager.MOVE_TASK_WITH_HOME);		
				}
			}
			
		}
		
	}
	
	/**/
	public void Exit()
	{
		
	}
	
	/*用户动作*/
	public void TrgCompleted(Intent intent, int startId)
	{
		Bundle bundle = new Bundle();
        bundle = intent.getExtras();
        if(bundle != null) 
        {
        	byte trgCmd = bundle.getByte(DVDDealSet.DVD_TRG_CMD);
        	
        	LogCat.Logd("DVDDVDPlayer  trgCmd = " + trgCmd);
        	switch(trgCmd)
        	{
        	case DVDDealSet.DVD_EXIT_APP:
        		ZoranDVD.dvdInstance.ChangeUnit(null);
        		
        		ZoranDVD.dvdInstance.DVD_CMD.DVDPowerOff();
        		Intent exit = new Intent();
    			exit.setAction(DVDDealSet.DVD_ACTIVITY_EXIT);
    			mContext.sendBroadcast(exit);
        		break;
        	case DVDDealSet.AUDIO_SET:
        		Intent i = new Intent();
        		i.setClassName("com.apical.audiosettings",
        				"com.apical.audiosettings.AudioActivity");
        		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  //activity外面启动activity，需要添加这个标志，不然会报错。
        		mContext.startActivity(i);
        		break;
        	case DVDDealSet.BACKWARD:
        		ZoranDVD.dvdInstance.DVD_CMD.prevQuick();
        		break;
        	case DVDDealSet.FORWARD:
        		ZoranDVD.dvdInstance.DVD_CMD.nextQuick();
        		break;
        	case DVDDealSet.LIGHT_SET:
        		//activity里做了
        		break;
        	case DVDDealSet.NEXT_ONE:
        		ZoranDVD.dvdInstance.DVD_CMD.next();
        		break;
        	case DVDDealSet.OUT_DISC:
        		//程序退出
        		break;
        	case DVDDealSet.PLAY_PAUSE:
        		ZoranDVD.dvdInstance.DVD_CMD.getPlayState();
        		
        		if (mPlayState == ZoranDVD.dvdInstance.DVD_CMD.GetDVDPlayStatePlay()) 
        		{
        			ZoranDVD.dvdInstance.DVD_CMD.pause();
				}
        		else
        		{
        			ZoranDVD.dvdInstance.DVD_CMD.play();
        		}
        		break;
        	case DVDDealSet.PRE_ONE:
        		ZoranDVD.dvdInstance.DVD_CMD.previous();
        		break;
        	case DVDDealSet.SOUND_CHANGE:
        		ZoranDVD.dvdInstance.DVD_CMD.soundChange();
        		break;
        	case DVDDealSet.STOP:
        		ZoranDVD.dvdInstance.DVD_CMD.stop();
        		break;
        	case DVDDealSet.TOUCH_SCREEN:
        		int x = bundle.getInt("touch_screen_x");
        		int y = bundle.getInt("touch_screen_y");
        		ZoranDVD.dvdInstance.DVD_CMD.touch(x, y);
        		break;
        	default:
        		break;
        	}
        }
        else
        {
        	LogCat.Logd("DVDDVDPlayer  bundle = null");
        }
	}
	
	/**/
	public boolean CmdCompleted(int cmd, Bundle data)
	{
		LogCat.Logd("DVDDVDPlayer CmdCompleted cmd = " + cmd);
		switch(cmd)
		{
		case DvdControl.SETUP_MCU_RET_MB:
			break;
		case DvdControl.DVD_MCU_RET_DISCTYPE:
			break;
		case DvdControl.DVD_MCU_RET_CUR_TRACK:
			Intent cintent = new Intent(DVDDealSet.DVDPLAYER_DVD_EVENT);
	        cintent.putExtras(data);
	        mContext.sendBroadcast(cintent);
			break;
		case DvdControl.DVD_MCU_RET_PLAYSTATUS:
			mPlayState = data.getByte("PlayStatus");
//			Intent update = new Intent();
//			update.setAction("dvdplayer_dvddisc_timeout");
//			mContext.sendBroadcast(update);
			if (mPlayState == ZoranDVD.dvdInstance.DVD_CMD.GetDVDPlayStatePlay()) 
			{
				data.putInt("dvd_state", 1);
				//imageBtnPlayPause.setBackgroundResource(R.drawable.btnplaypause);
//				if(DVDDVDPlayerActivity.DVDDVDPlayerAct != null)
//				{
//					DVDDVDPlayerActivity.DVDDVDPlayerAct.UpdatePlayPause(true);
//				}
			} 
			else 
			{
				data.putInt("dvd_state", 2);
				//imageBtnPlayPause.setBackgroundResource(R.drawable.btnpause);
//				if(DVDDVDPlayerActivity.DVDDVDPlayerAct != null)
//				{
//					DVDDVDPlayerActivity.DVDDVDPlayerAct.UpdatePlayPause(false);
//				}
				
			}
			Intent sintent = new Intent(DVDDealSet.DVDPLAYER_DVD_EVENT);
	        sintent.putExtras(data);
	        mContext.sendBroadcast(sintent);
			break;
		case DvdControl.DVD_MCU_RET_INFO:
			break;
		case DvdControl.DVD_MCU_RET_PLAYTIME: // 播放时间
			Intent tintent = new Intent(DVDDealSet.DVDPLAYER_DVD_EVENT);
	        tintent.putExtras(data);
	        mContext.sendBroadcast(tintent);
			break;
		case DvdControl.DVD_MCU_RET_DISCERR:
			break;
		}
		return true;
	}
}