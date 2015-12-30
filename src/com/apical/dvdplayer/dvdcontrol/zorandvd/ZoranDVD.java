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
		
		//���������Ÿо��ᶪʧ�������ˣ���ʵ�Ƿ��񱻹ر��ˣ���containerû��ϵ�����˾͸��ˣ�
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
	
	/*�û�����*/
	public void TrgCompleted(Intent intent, int startId)
	{
		Bundle bundle = new Bundle();
        bundle = intent.getExtras();
        if(bundle != null) //���startservice��ʱ��û�д�bundle�������������null
        {
	      	byte trgCmd = bundle.getByte(DVDDealSet.DVD_TRG_CMD);
	      	LogCat.Logd("ZoranDVD->TrgCompleted  " + trgCmd);
	      	if(trgCmd == DVDDealSet.LAUNCH_APP)
	      	{
	      		AppData.Instance().clearList();
	      		byte lt = bundle.getByte("launch_type");
	      		switch(lt)
	      		{
	      		case 0x01:  //����������
	      			LogCat.Logd("");
	      			DVD_CMD.SetWorkMode(DVDDealSet.DVD_MODE, DVDDealSet.APKFLAG_DVD);
	      			if(DP_ENGINEER == null)  
      				{
      						DP_ENGINEER = DVDLoading.Inatance();
      						DP_ENGINEER.Show(mContext);
      				}
      				else
      				{
      					//��Ϊnull��ʱ��ֱ����ʾ
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

        	//�ַ������û�����
            if(DP_ENGINEER != null)
            {
          	    LogCat.Logd("ZoranDVD->trgCompleted  DP_ENGINEER");
          	    DP_ENGINEER.TrgCompleted(intent, startId);
            }
            else  
            {
            	/*
            	 * ����ܵ�������û�����˵��activity���ܴ��ڣ�����Ӧ�ķ�����ر��ˣ�������Ҫ����һ���������
            	 * ��������ÿ����Ԫ��show���������õ�ǰ��Ԫ���������DP_ENGINEER��ֵ
            	 * */
            	LogCat.Logd("ZoranDVD->trgCompleted  DP_ENGINEER = null");
            }
        }
	}
	
	/**/
	public boolean CmdCompleted(int cmd, Bundle data)
	{
		//����DVD����
		if(DP_ENGINEER != null)
		{
			DP_ENGINEER.CmdCompleted(cmd, data);
		}
        return true;
	}
	
	/*�л�Unit״̬*/
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