package com.apical.dvdplayer.dvdcontrol.zorandvd;

import android.app.DvdControl.OnCmdCompletedListener;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.apical.dvdplayer.LogCat;
import com.apical.dvdplayer.dvdcontrol.AbstractDVD;
import com.apical.dvdplayer.dvdcontrol.EngineBase;
import com.apical.dvdplayer.dvdcontrol.UnitContainer;
import com.apical.dvdplayer.dvdcontrol.UnitContainer.DVDUnit;
import com.apical.dvdplayer.dvdcontrol.UnitContainer.SDHCUnit;
import com.apical.dvdplayer.dvdcontrol.zorandvd.dvdunit.DVDLoading;
import com.apical.dvdplayer.dvdcontrol.zorandvd.sdhcunit.SDHCLoading;
import com.apical.dvdplayer.dvdmodel.AppData;
import com.apical.dvdplayer.dvdmodel.DVDDealSet;

public class ZoranDVD extends AbstractDVD
{
	public static ZoranDVD dvdInstance = null;
	
	public ZoranCmd DVD_CMD = null;
	
	public void Init(OnCmdCompletedListener listener, Context context)
	{
		if(dvdInstance == null)
		{
			dvdInstance = this;
		}
		mContext = context;
		
		//放在这里存放感觉会丢失，不用了（其实是服务被关闭了，和container没关系，改了就改了）
//		if(UNIT == null)
//		{
//			UNIT = new UnitContainer();
//		}
//		UNIT.InitContainer();
		
		if(DVD_CMD == null)
		{
			DVD_CMD = new ZoranCmd();
		}
		
		DVD_CMD.Init(listener, context);
		DVD_CMD.getMainBordType();
	}
	
	/*用户动作*/
	public void TrgCompleted(Intent intent, int startId)
	{
		Bundle bundle = new Bundle();
        bundle = intent.getExtras();
        if(bundle != null) //如果startservice的时候没有传bundle进来，这里会是null
        {
	      	byte trgCmd = bundle.getByte(DVDDealSet.DVD_TRG_CMD);
	      	LogCat.Logd("ZoranDVD->TrgCompleted  " + trgCmd);
	      	if(trgCmd == DVDDealSet.LAUNCH_APP)
	      	{
	      		AppData.Instance().clearList();
	      		byte lt = bundle.getByte("launch_type");
	      		switch(lt)
	      		{
	      		case 0x01:  //主界面启动
	      			LogCat.Logd("");
	      			DVD_CMD.SetWorkMode(DVDDealSet.DVD_MODE, DVDDealSet.APKFLAG_DVD);
	      			if(DP_ENGINEER == null)  
      				{
      						DP_ENGINEER = DVDLoading.Inatance();
      						DP_ENGINEER.Show(mContext);
      				}
      				else
      				{
      					//不为null的时候，直接显示
      					DP_ENGINEER.Show(mContext);
      				}
	      			break;
	      		case 0x02:
                    break;
	      		default:
	      			break;
	      		}
	      		return;
	      	}

        	//分发处理用户动作
            if(DP_ENGINEER != null)
            {
          	    LogCat.Logd("ZoranDVD->trgCompleted  DP_ENGINEER");
          	    DP_ENGINEER.TrgCompleted(intent, startId);
            }
            else  
            {
            	/*
            	 * 如果跑到这里，有用户动作说明activity可能存在，而对应的服务给关闭了，这里需要处理一下这种情况
            	 * 方法：在每个单元的show方法里设置当前单元，在这里给DP_ENGINEER赋值
            	 * */
            	LogCat.Logd("ZoranDVD->trgCompleted  DP_ENGINEER = null");
            }
        }
	}
	
	/**/
	public boolean CmdCompleted(int cmd, Bundle data)
	{
		//处理DVD动作
		if(DP_ENGINEER != null)
		{
			DP_ENGINEER.CmdCompleted(cmd, data);
		}
        return true;
	}
	
	/*切换Unit状态*/
	public void ChangeUnit(EngineBase dp)
	{
		LogCat.Logd("ZoranDVD ChangeUnit");
		if(DP_ENGINEER != null)
		{
			LogCat.Logd("ZoranDVD ChangeUnit exit");
			DP_ENGINEER.Exit(mContext);
		}
		
		DP_ENGINEER = dp;
		
		if(dp == null)
		{
			LogCat.Logd("ZoranDVD ChangeUnit dp = null");
			DP_ENGINEER = null;
			//DVD_CMD.eject();
			return;
		}
		DP_ENGINEER.Show(mContext);
	}
}