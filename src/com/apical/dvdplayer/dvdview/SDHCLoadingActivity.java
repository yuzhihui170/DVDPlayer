package com.apical.dvdplayer.dvdview;

import com.apical.dvdplayer.LogCat;
import com.apical.dvdplayer.R;
import com.apical.dvdplayer.dvdmodel.DVDDealSet;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;

public class SDHCLoadingActivity extends Activity
{
	public static Activity SDHCLoadingAct = null;
	
	private ImageView m_ivLoad;
	private TextView m_tvLoad;
	private AnimationDrawable animationDrawable; // 动画控制对象

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		SDHCLoadingAct = this;
		setContentView(R.layout.sdhc_loading_activity);
		
		m_ivLoad = (ImageView) findViewById(R.id.sdhcmainact_iv_loadsdhc);
		m_tvLoad = (TextView) findViewById(R.id.sdhcmainact_tv_loadsdhc);
		
		m_ivLoad.setImageResource(R.anim.load_sdhc);
		animationDrawable = (AnimationDrawable) m_ivLoad.getDrawable();
		animationDrawable.start();
		
		/*****************************************************************/
		IntentFilter filter = new IntentFilter("dvdplayer_dvd_event");
		filter.addAction("dvdplayer_dvddisc_timeout");
		filter.addAction(DVDDealSet.SDHC_ACTIVITY_EXIT);
		registerReceiver(mReceiver, filter);
		/******************************************************************/
		
		LogCat.Logd("SDHCLoadingActivity onCreate");
	}

	@Override
	protected void onDestroy() 
	{
		SDHCLoadingAct = null;
		
		unregisterReceiver(mReceiver);
		
		super.onDestroy();
		
		LogCat.Logd("SDHCLoadingActivity onDestroy");
	}

	@Override
	protected void onPause() 
	{
		LogCat.Logd("SDHCLoadingActivity onPause");
		
		super.onPause();
	}

	@Override
	protected void onResume() 
	{
		LogCat.Logd("SDHCLoadingActivity onResume");
		
		super.onResume();
	}

	@Override
	protected void onStart() 
	{
		LogCat.Logd("SDHCLoadingActivity onStart");
		
		super.onStart();
	}

	@Override
	protected void onStop() 
	{
		LogCat.Logd("SDHCLoadingActivity onStop");
		
		super.onStop();
	}
	
	public void ExitApp()
	{
		finish();
		Intent serviceIntent = new Intent(SDHCLoadingActivity.this, 
				com.apical.dvdplayer.dvdcontrol.DVDService.class);
		Bundle bundle = new Bundle();  
        bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.DVD_EXIT_APP);
        serviceIntent.putExtras(bundle);
        startService(serviceIntent);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		if (keyCode == KeyEvent.KEYCODE_BACK) 
		{
			ExitApp();
			return true;
		} 
		return super.onKeyDown(keyCode, event);
	}
	
	private BroadcastReceiver mReceiver = new BroadcastReceiver() 
	{

		@Override
		public void onReceive(Context context, Intent intent) 
		{
			if(intent.getAction().equals(DVDDealSet.SDHC_ACTIVITY_EXIT))
			{
				LogCat.Logd("SDHCLoadingActivity->onReceive  dvdplayer_exit_app");
				SDHCLoadingActivity.this.finish();
			}
			else if(intent.getAction().equals("dvdplayer_dvd_event"))
			{
				
			}
			else if(intent.getAction().equals("dvdplayer_dvddisc_timeout"))
			{
				m_tvLoad.setText(R.string.load_time_out);
				animationDrawable.stop();
				m_ivLoad.setImageResource(R.drawable.sdhcmainact_animation0);
				
				Intent serviceIntent = new Intent(SDHCLoadingActivity.this, 
						com.apical.dvdplayer.dvdcontrol.DVDService.class);
				Bundle bundle = new Bundle();  
		        bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.TIME_OUT);
		        serviceIntent.putExtras(bundle);
		        startService(serviceIntent);
			}
		}
		
	};
}