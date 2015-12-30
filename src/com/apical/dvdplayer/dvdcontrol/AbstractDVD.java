package com.apical.dvdplayer.dvdcontrol;

import com.apical.dvdplayer.dvdcontrol.zorandvd.ZoranCmd;

import android.app.DvdControl.OnCmdCompletedListener;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class AbstractDVD
{
	protected EngineBase DP_ENGINEER = null;
	public Context mContext = null;
	//public UnitContainer UNIT = null;
	
	
	public void Init(OnCmdCompletedListener listener, Context context)
	{
		
	}
	
	/*用户动作*/
	public void TrgCompleted(Intent intent, int startId)
	{
		
	}
	
	/**/
	public boolean CmdCompleted(int cmd, Bundle data)
	{
        return true;
	}
}