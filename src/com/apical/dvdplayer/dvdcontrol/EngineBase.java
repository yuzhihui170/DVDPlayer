package com.apical.dvdplayer.dvdcontrol;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class EngineBase
{
	/**/
	public void Show(Context context)
	{
		
	}
	
	/**/
	public void Exit(Context context)
	{
		
	}
	
	/*�û�����*/
	public void TrgCompleted(Intent intent, int startId)
	{
		
	}
	
	/*DVD����*/
	public boolean CmdCompleted(int cmd, Bundle data)
	{
		return true;
	}
}