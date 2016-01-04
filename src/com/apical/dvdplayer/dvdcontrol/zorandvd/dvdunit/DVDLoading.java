package com.apical.dvdplayer.dvdcontrol.zorandvd.dvdunit;

import java.util.List;

import com.apical.dvdplayer.LogCat;
import com.apical.dvdplayer.dvdcontrol.DVDEngineBase;
import com.apical.dvdplayer.dvdcontrol.DVDService;
import com.apical.dvdplayer.dvdcontrol.UnitContainer.DVDUnit;
import com.apical.dvdplayer.dvdcontrol.zorandvd.ZoranDVD;
import com.apical.dvdplayer.dvdmodel.DVDDealSet;
import com.apical.dvdplayer.dvdview.DVDDVDPlayerActivity;
import com.apical.dvdplayer.dvdview.DVDLoadingActivity;

import android.app.ActivityManager;
import android.app.DvdControl;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class DVDLoading extends DVDEngineBase
{
	private int discTimerout = 0;
	private int defaultTime = 40;

	Context mContext = null;
	
	private Byte mDiscStatus = 0; // 碟片状态
	private byte mDiscType = 0; // 碟片类型
	private byte mDVDMBType = 0; // DVD硬件类型
	
	private boolean stopThread = false;
	
	private static DVDLoading dvdLoading = null;
	private DVDLoading()
	{
		
	}
	
	public static DVDLoading Inatance()
	{
		if(dvdLoading == null)
		{
			dvdLoading = new DVDLoading();
		}
		
		return dvdLoading;
	}
	
	/**/
	public void Show(Context context)
	{
		LogCat.Logd("DVDLoading -- Show");
		
		mContext = context;
		
		if(DVDLoadingActivity.DVDLoadingAct == null)
		{
			LogCat.Logd("DVDLoading act = null");
			Intent loading = new Intent(context, 
					com.apical.dvdplayer.dvdview.DVDLoadingActivity.class);
			loading.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			loading.putExtra("setModeFlag", 1) ;
			context.startActivity(loading);
			
			discTimerout = 0;
			new Thread(new MyThread()).start();
			
			//DVD初始化设置
			stopThread = false;
			ZoranDVD.dvdInstance.DVD_CMD.DVDPowerOn();
			ZoranDVD.dvdInstance.DVD_CMD.getDiscPos();
			ZoranDVD.dvdInstance.DVD_CMD.GetDVDInfo();
		}
		else
		{
			LogCat.Logd("DVDLoading act != null");
//			Intent loading = new Intent(context, 
//					com.apical.dvdplayer.dvdview.DVDLoadingActivity.class);
//			loading.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			loading.putExtra("setModeFlag", 1) ;
//			context.startActivity(loading);
			
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
				LogCat.Logd("DVDLoading tasksInfo.size() > 0");
				//DVD 未置顶
				if( !packageName.equals(tasksInfo.get(0).topActivity.getPackageName()) )
				{
					LogCat.Logd("DVDLoading not top");
					final ActivityManager am = (ActivityManager)
					           context.getSystemService(Context.ACTIVITY_SERVICE);
					am.moveTaskToFront(DVDLoadingActivity.DVDLoadingAct.getTaskId(), ActivityManager.MOVE_TASK_WITH_HOME);		
				}
			}
		}
		
	}
	
	/**/
	public void Exit(Context context)
	{
		if(DVDLoadingActivity.DVDLoadingAct != null)
		{
			DVDLoadingActivity.DVDLoadingAct.finish();
		}
	}
	
	/*用户动作*/
	public void TrgCompleted(Intent intent, int startId)
	{
		Bundle bundle = new Bundle();
        bundle = intent.getExtras();
        if(bundle != null) 
        {
        	byte trgCmd = bundle.getByte(DVDDealSet.DVD_TRG_CMD);
        	
        	LogCat.Logd("DVDLoading  trgCmd = " + trgCmd);
        	switch(trgCmd)
        	{
        	case DVDDealSet.DVD_EXIT_APP:
        		stopThread = true;
        		ZoranDVD.dvdInstance.ChangeUnit(null);
        		
        		ZoranDVD.dvdInstance.DVD_CMD.eject();
        		
        		Intent exit = new Intent();
    			exit.setAction(DVDDealSet.DVD_ACTIVITY_EXIT);
    			mContext.sendBroadcast(exit);
        		break;
        	case DVDDealSet.TIME_OUT:
        		LogCat.Logd("DVDLoading -- TrgCompleted -- TIME_OUT");
        		try 
        		{
					Thread.sleep(1000);
				} catch (InterruptedException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		
        		Intent timeOut = new Intent();
				timeOut.setAction(DVDDealSet.DVD_ACTIVITY_EXIT);
				mContext.sendBroadcast(timeOut);
        		break;
        	}
        }
        else
        {
        	LogCat.Logd("DVDLoading  bundle = null");
        }
	}
	
	/*DVD动作*/
	public boolean CmdCompleted(int cmd, Bundle data)
	{
		LogCat.Logd("DVDLoading CmdCompleted cmd = " + cmd);
		ZoranDVD.dvdInstance.ChangeUnit(DVDDVDPlayer.Instance()); //yzh 可以启动DVD播放界面
		switch (cmd) {
		case DvdControl.SETUP_MCU_RET_MB:
			break;
		case DvdControl.DVD_MCU_RET_DISCTYPE:
			mDiscType = data.getByte("DiscType");
			LogCat.Logd("DVDLoading CmdCompleted cmd = " + cmd + "mDiscType = " + mDiscType);
			playDisc();
			break;
		case DvdControl.DVD_MCU_RET_PLAYSTATUS:
			break;
		case DvdControl.DVD_MCU_RET_INFO:
			break;
		case DvdControl.DVD_MCU_RET_PLAYTIME: // 播放时间
			break;
		case DvdControl.DVD_MCU_RET_DISCERR:
			break;
		}
		return true;
	}
	
	public boolean playDisc()
	{
		stopThread = true;
		
		if(mDiscType == ZoranDVD.dvdInstance.DVD_CMD.GetDiscTypeCD())
		{
			LogCat.Logd("DVDLoading playDisc  cd");
			//ZoranDVD.dvdInstance.ChangeUnit(ZoranDVD.dvdInstance.UNIT.DVDUnitCon.get(DVDUnit.DVDCDPLAYER.value));
			ZoranDVD.dvdInstance.ChangeUnit(DVDCDPlayer.Instance());
		}
		else if(mDiscType == ZoranDVD.dvdInstance.DVD_CMD.GetDiscTypeDVD()
				|| mDiscType == ZoranDVD.dvdInstance.DVD_CMD.GetDiscTypeVCD())
		{
			LogCat.Logd("DVDLoading playDisc  dvd");
			//ZoranDVD.dvdInstance.ChangeUnit(ZoranDVD.dvdInstance.UNIT.DVDUnitCon.get(DVDUnit.DVDDVDPLAYER.value));
			ZoranDVD.dvdInstance.ChangeUnit(DVDDVDPlayer.Instance());
		}
		else if(mDiscType == ZoranDVD.dvdInstance.DVD_CMD.GetDiscTypeDataDisc())
		{
			LogCat.Logd("DVDLoading playDisc  mp5");
			//ZoranDVD.dvdInstance.ChangeUnit(ZoranDVD.dvdInstance.UNIT.DVDUnitCon.get(DVDUnit.DVDMP5LIST.value));
			ZoranDVD.dvdInstance.ChangeUnit(DVDMp5List.Instance());
		}
		return true;
	}
	
	public class MyThread implements Runnable
	{
		@Override
		public void run() 
		{
			while(true)
			{
				//如果收到碟片类型的信息，退出
				if(stopThread)
				{
					break;
				}
				
				if(discTimerout >= defaultTime)
				{
					LogCat.Logd("DVDLoading -- Runnable -- TIME_OUT");
					
					Intent timeOut = new Intent();
					timeOut.setAction("dvdplayer_dvddisc_timeout");
					mContext.sendBroadcast(timeOut);
					
					//断电
					ZoranDVD.dvdInstance.DVD_CMD.eject();
					break;
				}
				else 
				{
					//不断读碟
					ZoranDVD.dvdInstance.DVD_CMD.SetDiscState(DVDDealSet.DVD);
				}
				discTimerout ++;
				stopThread = true; // yzh for test
				
				try 
				{
					Thread.sleep(2000);
				} 
				catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				LogCat.Logd("DVDLoading -- Runnable -- " + discTimerout);
			}
			
		}
		
	}
}