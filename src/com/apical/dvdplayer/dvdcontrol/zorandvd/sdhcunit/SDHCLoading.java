package com.apical.dvdplayer.dvdcontrol.zorandvd.sdhcunit;

import com.apical.dvdplayer.LogCat;
import com.apical.dvdplayer.dvdcontrol.DVDService;
import com.apical.dvdplayer.dvdcontrol.SDHCEngineBase;
import com.apical.dvdplayer.dvdcontrol.zorandvd.ZoranDVD;
import com.apical.dvdplayer.dvdcontrol.zorandvd.dvdunit.DVDCDPlayer;
import com.apical.dvdplayer.dvdcontrol.zorandvd.dvdunit.DVDLoading;
import com.apical.dvdplayer.dvdcontrol.zorandvd.dvdunit.DVDLoading.MyThread;
import com.apical.dvdplayer.dvdmodel.DVDDealSet;
import com.apical.dvdplayer.dvdview.DVDLoadingActivity;
import com.apical.dvdplayer.dvdview.SDHCLoadingActivity;

import android.app.DvdControl;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class SDHCLoading extends SDHCEngineBase
{
	private int discTimerout = 0;
	private int defaultTime = 10;
	
	Context mContext = null;
	
	private boolean stopThread = false;
	
	private static SDHCLoading sdhcLoading = null;
	private SDHCLoading()
	{
		
	}
	
	public static SDHCLoading Inatance()
	{
		if(sdhcLoading == null)
		{
			sdhcLoading = new SDHCLoading();
		}
		
		return sdhcLoading;
	}
	/**/
	public void Show(Context context)
	{
		mContext = context;
		
		if(SDHCLoadingActivity.SDHCLoadingAct == null)
		{
			LogCat.Logd("SDHCLoading act = null");
			Intent loading = new Intent(context, 
					com.apical.dvdplayer.dvdview.SDHCLoadingActivity.class);
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
			LogCat.Logd("SDHCLoading act != null");
			Intent loading = new Intent(context, 
					com.apical.dvdplayer.dvdview.SDHCLoadingActivity.class);
			loading.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			loading.putExtra("setModeFlag", 1) ;
			context.startActivity(loading);
		}
	}
	
	/**/
	public void Exit(Context context)
	{
		if(SDHCLoadingActivity.SDHCLoadingAct != null)
		{
			SDHCLoadingActivity.SDHCLoadingAct.finish();
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
        	switch(trgCmd)
        	{
        	case DVDDealSet.DVD_EXIT_APP:
        		ZoranDVD.dvdInstance.ChangeUnit(null);
        		
        		ZoranDVD.dvdInstance.DVD_CMD.DVDPowerOff();
        		Intent exit = new Intent();
    			exit.setAction(DVDDealSet.DVD_ACTIVITY_EXIT);
    			mContext.sendBroadcast(exit);
        		break;
        	case DVDDealSet.TIME_OUT:
        		try 
        		{
					Thread.sleep(1000);
				} catch (InterruptedException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		
        		Intent timeOut = new Intent();
				timeOut.setAction(DVDDealSet.SDHC_ACTIVITY_EXIT);
				mContext.sendBroadcast(timeOut);
        		break;
        	}
        }
	}
	
	/**/
	public boolean CmdCompleted(int cmd, Bundle data)
	{
		LogCat.Logd("SDHCLoading CmdCompleted cmd = " + cmd);
		switch(cmd)
		{
		case DvdControl.SETUP_MCU_RET_MB:
			break;
		case DvdControl.DVD_MCU_RET_DISCTYPE:
			int mDiscType = data.getByte("DiscType");
			//LogCat.Logd("DVDLoading CmdCompleted cmd = " + cmd + "mDiscType = " + mDiscType);
			//playDisc();
			LogCat.Logd("SDHCLoading DVD_MCU_RET_DISCTYPE : " + mDiscType);
			stopThread = true;
			
			ZoranDVD.dvdInstance.ChangeUnit(SDHCMp5List.Instance());
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
					LogCat.Logd("SDHCLoading -- Runnable -- TIME_OUT");
					
					Intent timeOut = new Intent();
					timeOut.setAction("dvdplayer_dvddisc_timeout");
					mContext.sendBroadcast(timeOut);
					
					//断电
					//ZoranDVD.dvdInstance.DVD_CMD.eject();
					break;
				}
				else 
				{
					//不断读碟
					ZoranDVD.dvdInstance.DVD_CMD.SetDiscState(DVDDealSet.SDHC);
				}
				discTimerout ++;
				
				try 
				{
					Thread.sleep(2000);
				} 
				catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				LogCat.Logd("SDHCLoading -- Runnable -- " + discTimerout);
			}
			
		}
		
	}
}