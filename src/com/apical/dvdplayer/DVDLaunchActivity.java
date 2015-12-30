package com.apical.dvdplayer;

import com.apical.dvdplayer.dvdcontrol.zorandvd.ZoranDVD;
import com.apical.dvdplayer.dvdmodel.AppData;
import com.apical.dvdplayer.dvdmodel.DVDDealSet;
import com.apical.dvdplayer.dvdview.DVDDVDPlayerActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class DVDLaunchActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
//		Intent serviceIntent = new Intent(DVDLaunchActivity.this, 
//				com.apical.dvdplayer.dvdcontrol.DVDService.class);
//		Bundle bundle = new Bundle();  
//        bundle.putString(DVDDealSet.DVD_TRG_CMD, "DVD");
//        
//        serviceIntent.putExtras(bundle);
//        startService(serviceIntent);
		
		//如果之前运行的SDHC，SDHC相关activity要退出，DP_ENGINEER要置为Null
//		if(AppData.Instance().getAppType() == DVDDealSet.APP_SDHC)  
//		{
//			LogCat.Logd("AppData.Instance().getAppType() == DVDDealSet.APP_SDHC");
//			ZoranDVD.dvdInstance.ChangeUnit(null);
//    		Intent exit = new Intent();
//			exit.setAction(DVDDealSet.SDHC_ACTIVITY_EXIT);
//			sendBroadcast(exit);
//		}
		
		Intent serviceIntent = new Intent(DVDLaunchActivity.this, 
				com.apical.dvdplayer.dvdcontrol.DVDService.class);
		Bundle bundle = new Bundle();  
		bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.LAUNCH_APP);
		bundle.putByte("launch_type", (byte)0x01);
		serviceIntent.putExtras(bundle);
        startService(serviceIntent);
        
        LogCat.Logd("DVDLaunchActivity -- onCreate");
        
        finish();
        
        super.onCreate(savedInstanceState);
	}
}
