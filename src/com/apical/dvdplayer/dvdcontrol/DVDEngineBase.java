package com.apical.dvdplayer.dvdcontrol;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class DVDEngineBase extends EngineBase
{
	/**/
	public void Show(Context context)
	{
		
	}
	
	/**/
	public void Exit(Context context)
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