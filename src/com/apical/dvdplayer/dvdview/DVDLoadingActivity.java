package com.apical.dvdplayer.dvdview;

import com.apical.dvdplayer.LogCat;
import com.apical.dvdplayer.R;
import com.apical.dvdplayer.dvdmodel.DVDDealSet;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

public class DVDLoadingActivity extends Activity
{
	public static Activity DVDLoadingAct = null;
	
	private ImageView mImageLoading;
	private TextView mTextLoading;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		LogCat.Logd("DVDLoadingActivity->onCreate");
		
		super.onCreate(savedInstanceState);
		
		DVDLoadingAct = this;
		
		setContentView(R.layout.dvd_loading_activity);
		
		//¶¯»­
		Animation operatingAnim = AnimationUtils.loadAnimation(this, R.anim.load_dvd);
		mImageLoading = (ImageView) findViewById(R.id.dvdmain_act_iv_dvdanimation);
		if (operatingAnim != null) {
			mImageLoading.startAnimation(operatingAnim);
		}
		mImageLoading.setOnTouchListener(new IVReadDiscListener());
		
		mTextLoading =(TextView) findViewById(R.id.dvdmain_act_tv_readdisc);
		
		
		
		/*****************************************************************/
		IntentFilter filter = new IntentFilter("dvdplayer_dvd_event");
		filter.addAction("dvdplayer_dvddisc_timeout");
		filter.addAction(DVDDealSet.DVD_ACTIVITY_EXIT);
		registerReceiver(mReceiver, filter);
		/******************************************************************/
	}

	@Override
	protected void onDestroy() 
	{
		LogCat.Logd("DVDLoadingActivity->onDestroy");
		
		DVDLoadingAct = null;
		unregisterReceiver(mReceiver);
		super.onDestroy();
	}

	@Override
	protected void onPause() 
	{
		LogCat.Logd("DVDLoadingActivity->onPause");
		
		super.onPause();
	}

	@Override
	protected void onResume() 
	{
		LogCat.Logd("DVDLoadingActivity->onResume");
		
		super.onResume();
	}

	@Override
	protected void onStart() 
	{
		LogCat.Logd("DVDLoadingActivity->onStart");
		
		super.onStart();
	}

	@Override
	protected void onStop() 
	{
		LogCat.Logd("DVDLoadingActivity->onStop");
		
		super.onStop();
	}
	
	class IVReadDiscListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) 
		{
			LogCat.Logd("DVDLoadingActivity{IVReadDiscListener onTouch}");
			ExitApp();
			return false;
		}
	}
	
	public void ExitApp()
	{
		finish();
		Intent serviceIntent = new Intent(DVDLoadingActivity.this, com.apical.dvdplayer.dvdcontrol.DVDService.class);
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
//			Bundle data = intent.getExtras();
//			if(data == null)
//			{
//				LogCat.Logd("DVDLoadingActivity->onReceive  data = null");
//				return;
//			}
			
			if(intent.getAction().equals("dvdplayer_dvddisc_timeout"))
			{
				LogCat.Logd("DVDLoadingActivity->onReceive  dvdplayer_dvddisc_timeout");
				mTextLoading.setText(R.string.load_time_out);
				mImageLoading = (ImageView) findViewById(R.id.dvdmain_act_iv_dvdanimation);
				mImageLoading.clearAnimation();
				
				LogCat.Logd("DVDLoadingActivity->onReceive  dvdplayer_dvddisc_timeout 1");
				Intent serviceIntent = new Intent(DVDLoadingActivity.this, com.apical.dvdplayer.dvdcontrol.DVDService.class);
				Bundle bundle = new Bundle();  
		        bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.TIME_OUT);
		        serviceIntent.putExtras(bundle);
		        startService(serviceIntent);
		        LogCat.Logd("DVDLoadingActivity->onReceive  dvdplayer_dvddisc_timeout 2");
			}
			else if(intent.getAction().equals(DVDDealSet.DVD_ACTIVITY_EXIT))
			{
				LogCat.Logd("DVDLoadingActivity->onReceive  dvdplayer_exit_app");
				DVDLoadingActivity.this.finish();
			}
			else if(intent.getAction().equals("dvdplayer_dvd_event"))
			{
				
			}
			
		}
		
	};
}