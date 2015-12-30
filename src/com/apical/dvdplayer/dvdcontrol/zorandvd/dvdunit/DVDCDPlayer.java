package com.apical.dvdplayer.dvdcontrol.zorandvd.dvdunit;

import java.util.List;

import com.apical.dvdplayer.LogCat;
import com.apical.dvdplayer.dvdcontrol.DVDEngineBase;
import com.apical.dvdplayer.dvdcontrol.DVDService;
import com.apical.dvdplayer.dvdcontrol.zorandvd.ZoranDVD;
import com.apical.dvdplayer.dvdmodel.DVDDealSet;
import com.apical.dvdplayer.dvdview.DVDCDPlayerActivity;
import com.apical.dvdplayer.dvdview.DVDDVDPlayerActivity;
import com.apical.dvdplayer.dvdview.DVDMp5Activity;

import android.app.ActivityManager;
import android.app.DvdControl;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class DVDCDPlayer extends DVDEngineBase
{
	Context mContext = null;
	
	byte circleType = 0; //循环播放类型
	
	private byte mPlayState = 0;
	
	private static DVDCDPlayer dvdCDPlayer = null;
	private DVDCDPlayer()
	{
		
	}
	
	public static DVDCDPlayer Instance()
	{
		if(dvdCDPlayer == null)
		{
			dvdCDPlayer = new DVDCDPlayer();
		}
		
		return dvdCDPlayer;
	}
	
	/**/
	public void Show(Context context)
	{
        LogCat.Logd("DVDCDPlayer -- Show");
		
		mContext = context;
		circleType = 0;
		
		if(DVDCDPlayerActivity.DVDCDPlayerAct == null)
		{
			LogCat.Logd("DVDCDPlayer act = null");
			Intent cdplayer = new Intent(context, 
					com.apical.dvdplayer.dvdview.DVDCDPlayerActivity.class);
			cdplayer.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			cdplayer.putExtra("setModeFlag", 1) ;
			context.startActivity(cdplayer);
		}
		else 
		{
			LogCat.Logd("DVDCDPlayer act != null");
//			Intent cdplayer = new Intent(context, 
//					com.apical.dvdplayer.dvdview.DVDCDPlayerActivity.class);
//			cdplayer.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			cdplayer.putExtra("setModeFlag", 1) ;
//			context.startActivity(cdplayer);
			
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
				LogCat.Logd("DVDCDPlayer tasksInfo.size() > 0");
				//DVD 未置顶
				if( !packageName.equals(tasksInfo.get(0).topActivity.getPackageName()) )
				{
					LogCat.Logd("DVDCDPlayer not top");
					final ActivityManager am = (ActivityManager)
					           context.getSystemService(Context.ACTIVITY_SERVICE);
					am.moveTaskToFront(DVDCDPlayerActivity.DVDCDPlayerAct.getTaskId(), ActivityManager.MOVE_TASK_WITH_HOME);		
				}
			}
		}
		
	}
	
	/**/
	public void Exit(Context context)
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
        	
        	LogCat.Logd("DVDCDPlayer  trgCmd = " + trgCmd);
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
        	case DVDDealSet.CD_TRACK:
        		byte trackNum = bundle.getByte("cd_track_num");
        		ZoranDVD.dvdInstance.DVD_CMD.PlayCDTrack(trackNum);
        		LogCat.Logd("DVDCDPlayer  CD_TRACK  trackNum = " + trackNum);
        		break;
        	case DVDDealSet.CIRCLE_TYP:
        		ZoranDVD.dvdInstance.DVD_CMD.repet();
//        		Intent update_ct = new Intent();
//        		update_ct.setAction(DVDDealSet.DVDPLAYER_CIRCLE_UPDATE);
//        		Bundle cbundle = new Bundle();  
//        		cbundle.putInt("circle_type", circleType);
//        		update_ct.putExtras(cbundle);
//    			mContext.sendBroadcast(update_ct);
        		break;
        	default:
        		break;
        	}
        }
        else
        {
        	LogCat.Logd("DVDCDPlayer  bundle = null");
        }
	}
	
	/**/
	public boolean CmdCompleted(int cmd, Bundle data)
	{
		LogCat.Logd("DVDCDPlayer CmdCompleted cmd = " + cmd);
		switch(cmd)
		{
		case DvdControl.SETUP_MCU_RET_MB:
			break;
		case DvdControl.DVD_MCU_RET_DISCTYPE:
			//每次断电后，循环模式都会变成3
			LogCat.Logd("DVDCDPlayer DISCTYPE = " + data.getByte("RepetMode"));
			circleType = (byte) ((data.getByte("RepetMode"))&0x00ff);
			
    		Intent update_ct = new Intent(DVDDealSet.DVDPLAYER_CIRCLE_UPDATE);
    		Bundle cbundle = new Bundle();  
    		if(circleType == ZoranDVD.dvdInstance.DVD_CMD.GetRepetRadom())
    		{
    			cbundle.putInt("circle_type", 1);
    		}
    		else if(circleType == ZoranDVD.dvdInstance.DVD_CMD.GetRepetClose())
    		{
    			cbundle.putInt("circle_type", 2);
    		}
    		else if(circleType == ZoranDVD.dvdInstance.DVD_CMD.GetRepetSingle())
    		{
    			cbundle.putInt("circle_type", 3);
    		}
    		else if(circleType == ZoranDVD.dvdInstance.DVD_CMD.GetRepetList())
    		{
    			cbundle.putInt("circle_type", 4);
    		}
    		update_ct.putExtras(cbundle);
			mContext.sendBroadcast(update_ct);
			break;
		case DvdControl.DVD_MCU_RET_CUR_TRACK:
			LogCat.Logd("num = " + data.getByte("TotalChapterL"));
			Intent cintent = new Intent(DVDDealSet.DVDPLAYER_DVD_EVENT);
	        cintent.putExtras(data);
	        mContext.sendBroadcast(cintent);
			
			break;
		case DvdControl.DVD_MCU_RET_PLAYSTATUS:
			mPlayState = data.getByte("PlayStatus");
			if (mPlayState == ZoranDVD.dvdInstance.DVD_CMD.GetDVDPlayStatePlay()) 
			{
				data.putInt("dvd_state", 1);
			} 
			else 
			{
				data.putInt("dvd_state", 2);
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