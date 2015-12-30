package com.apical.dvdplayer;

import com.apical.dvdplayer.dvdcontrol.zorandvd.ZoranDVD;
import com.apical.dvdplayer.dvdmodel.AppData;
import com.apical.dvdplayer.dvdmodel.DVDDealSet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SDHCLaunchActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
//		Intent serviceIntent = new Intent(SDHCLaunchActivity.this, 
//				com.apical.dvdplayer.dvdcontrol.DVDService.class);
//		Bundle bundle = new Bundle();  
//        bundle.putString(DVDDealSet.DVD_TRG_CMD, "SDHC");
//        serviceIntent.putExtras(bundle);
//        startService(serviceIntent);
		
		//如果之前运行的DVD，DVD相关activity要退出，DP_ENGINEER要置为Null
//		if(AppData.Instance().getAppType() == DVDDealSet.APP_DVD)
//		{
//			LogCat.Logd("AppData.Instance().getAppType() == DVDDealSet.APP_DVD");
//			ZoranDVD.dvdInstance.ChangeUnit(null);
//    		Intent exit = new Intent();
//			exit.setAction(DVDDealSet.DVD_ACTIVITY_EXIT);
//			sendBroadcast(exit);
//		}
		
		Intent serviceIntent = new Intent(SDHCLaunchActivity.this, 
				com.apical.dvdplayer.dvdcontrol.DVDService.class);
		Bundle bundle = new Bundle();  
		bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.LAUNCH_APP);
		bundle.putByte("launch_type", (byte)0x02);
		serviceIntent.putExtras(bundle);
        startService(serviceIntent);
        
        LogCat.Logd("SDHCLaunchActivity -- onCreate");
        
        finish();
        
        super.onCreate(savedInstanceState);
	}
	
}