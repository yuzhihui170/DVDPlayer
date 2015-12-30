package com.apical.dvdplayer.dvdcontrol.sunplusdvd;

import android.app.DvdControl.OnCmdCompletedListener;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.apical.dvdplayer.dvdcontrol.AbstractDVD;

public class SunplusDVD extends AbstractDVD
{
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