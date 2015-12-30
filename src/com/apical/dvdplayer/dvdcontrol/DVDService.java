package com.apical.dvdplayer.dvdcontrol;

import com.apical.dvdplayer.LogCat;
import com.apical.dvdplayer.dvdmodel.DVDDealSet;

import android.app.Service;
import android.app.DvdControl.OnCmdCompletedListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

public class DVDService extends Service implements OnCmdCompletedListener
{
//	private EngineBase DP_ENGINEER = null;
//	private EngineBase PRE_DP = null;
//	public DVDOperation DVDOPER = null;  
//	public static DVDService serviceInstance = null;
//	public UnitContainer UNIT = null;
	
	public AbstractDVD DVD_BRIDGE = null;

	@Override
	public IBinder onBind(Intent intent) 
	{
		LogCat.Logd("DVDService onBind");
		return null;
	}
	
	@Override
	public void onCreate() 
	{
		super.onCreate();
		
		LogCat.Logd("DVDService->onCreate");
		
		DVD_BRIDGE = DVDFactory.CreateObject(DVDDealSet.DVD_ZORAN);
		if(DVD_BRIDGE != null)
		{
			DVD_BRIDGE.Init(this, this);
		}
		
//		//选择DVD :DVD_ZORAN/ DVD_SUNPLUS
//		DVDOPER = DVDFactory.CreateObject(DVDFactory.DVDType.DVD_ZORAN);
//		if(DVDOPER == null)
//		{
//			LogCat.Logd("DVDService->onCreate DVDOPER = null");
//		}
//		DVDOPER.Init(this, this);  //DVD初始化动作
//		DVDOPER.getMainBordType();
//		
//		/*初始化每个activity对应的unit*/
//		if(UNIT == null)
//		{
//			UNIT = new UnitContainer();
//		}
//		UNIT.InitContainer();
//		
//		//
//		serviceInstance = this;
	}
	
	

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) 
	{
		
		LogCat.Logd("DVDService->onStartCommand");
		
		return super.onStartCommand(intent, flags, startId);
	}

	/*每个activity触发的用户动作，都会通过startactivity来交给service处理
	 * 任务在onStart中分发到当前所在的Unit中执行
	 * 0710,调整*/
	@Override
	public void onStart(Intent intent, int startId) 
	{
		LogCat.Logd("DVDService->onStart");
		
		if(DVD_BRIDGE != null)
		{
			DVD_BRIDGE.TrgCompleted(intent, startId);
		}
		
//		super.onStart(intent, startId);
//		
//		Bundle bundle = new Bundle();
//        bundle = intent.getExtras();
//        if(bundle != null) //如果startservice的时候没有传bundle进来，这里会是null
//        {
//        	String trgCmd = bundle.getString(DVDDealSet.DVD_TRG_CMD);
//        	LogCat.Logd("DVDService->onStart  " + trgCmd);
//        	if(trgCmd != null)
//        	{
//        		if(trgCmd.equals("DVD"))
//	            {
//	            	LaunchDVDProgram(DVDService.LaunchType.DVD_PLAYER);
//	            	return;
//	            }
//	            else if(trgCmd.equals("SDHC"))
//	            {
//	            	LaunchDVDProgram(DVDService.LaunchType.SDHC_PLAYER);
//	            	return;
//	            }
//        	}
//
//        	//分发处理用户动作
//            if(DP_ENGINEER != null)
//            {
//            	LogCat.Logd("DVDService->onStart  DP_ENGINEER");
//            	DP_ENGINEER.TrgCompleted(intent, startId);
//            }
//        }
//        else
//        {
//        	LogCat.Logd("DVDService->onStart  bundle = null");
//        }
	}
	

	/*
	 * 启动*/
	public void LaunchDVDProgram(LaunchType type)
	{
//		LogCat.Logd("DVDService->ProgramLaunched");
//		switch(type)
//		{
//		case DVD_PLAYER:
//			if(DP_ENGINEER == null)  
//			{
//				//等于Null，表示程序未启动，赋Loading值打开loading界面
//				if(UNIT != null)
//				{
//					DP_ENGINEER = UNIT.DVDUnitCon.get(DVDUnit.DVDLOADING.value);
//					DP_ENGINEER.Show(this);
//				}
//				else
//				{
//					LogCat.Logd("DVDService->ProgramLaunched->DVDP_LAYER->UNIT == null");
//				}
//			}
//			else
//			{
//				//不为null的时候，直接显示
//				DP_ENGINEER.Show(this);
//			}
//			break;
//		case SDHC_PLAYER:
//			if(DP_ENGINEER == null)
//			{
//				if(UNIT != null)
//				{
//					DP_ENGINEER = UNIT.SDHCUnitCon.get(SDHCUnit.SDHCLOADING.value);
//					DP_ENGINEER.Show(this);
//				}
//				else 
//				{
//					LogCat.Logd("DVDService->ProgramLaunched->SDHC_PLAYER->UNIT == null");
//				}
//			}
//			else
//			{
//				DP_ENGINEER.Show(this);
//			}
//			break;
//		default:
//			break;
//		}
	}

	/**/
	public enum LaunchType
	{
		DVD_PLAYER, SDHC_PLAYER, UPDATER;
	}
	
	
	/*切换Unit状态*/
	public void ChangeUnit(EngineBase dp)
	{
//		LogCat.Logd("DVDService ChangeUnit");
//		if(DP_ENGINEER != null)
//		{
//			LogCat.Logd("DVDService ChangeUnit exit");
//			DP_ENGINEER.Exit(this);
//		}
//		PRE_DP = DP_ENGINEER;
//		DP_ENGINEER = dp;
//		
//		if(dp == null)
//		{
//			LogCat.Logd("DVDService ChangeUnit dp = null");
//			DP_ENGINEER = null;
//			DVDOPER.eject();
//			return;
//		}
//		DP_ENGINEER.Show(this);
	}

	@Override
	public boolean onCmdCompleted(int cmd, Bundle data) 
	{
		//处理DVD动作
		if(DVD_BRIDGE != null)
		{
			DVD_BRIDGE.CmdCompleted(cmd, data);
		}
		
		return false;
	}
}