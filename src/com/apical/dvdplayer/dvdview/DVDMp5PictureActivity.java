package com.apical.dvdplayer.dvdview;

import com.apical.dvdplayer.LogCat;
import com.apical.dvdplayer.R;
import com.apical.dvdplayer.dvdmodel.DVDDealSet;

import android.app.Activity;
import android.app.DvdControl;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DVDMp5PictureActivity extends Activity
{
	public static Activity DVDMp5PictureAct = null;
	
	private DvdView mDvdView;
	private HScrollViewGroup hsView;
	private boolean ctrlsIsShow = false;
	
	private ImageView imageBtnPlayPause;
	
	private TextView textLoading;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		LogCat.Logd("DVDMp5PictureActivity  onCreate");
		super.onCreate(savedInstanceState);
		
		DVDMp5PictureAct = this;
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);  //全屏
		setContentView(R.layout.dvd_mp5picture_activity);
		
		mDvdView = (DvdView) findViewById(R.id.dvdViewPictrue);
		mDvdView.init(DvdView.MODE_VIP, null);
		mDvdView.adjustVideoSize(800, 480);
		
		mDvdView.setOnTouchListener(new OnTouchListener() 
		{
			public boolean onTouch(View view, MotionEvent event) 
			{
				if ((mDvdView.getMode() == DvdView.MODE_VIP)) 
				{
					int x = (int) event.getX();
					int y = (int) event.getY();
                    
				}
				// 点击上部分屏幕，才会切换控制菜单
				if (event.getY() < 350) 
				{
					showhideCtrls(!ctrlsIsShow);
					ctrlsIsShow = !ctrlsIsShow;
				}

				return false;
			}
		});
		
		hsView = (HScrollViewGroup) findViewById(R.id.hsView);
		hsView.setSnapSpeed(1000); 
		
		final ImageButton btn = (ImageButton) findViewById(R.id.move);
		btn.setOnClickListener(new OnClickListener() 
		{

			@Override
			public void onClick(View v) 
			{
				if (hsView.getCurScreen() > 0) 
				{
					//resetAutoHide();
					btn.setBackgroundResource(R.drawable.nextpagebtn);
					hsView.setDirection(HScrollViewGroup.Direction.LEFT);
					hsView.snapToScreen(0);
				} else 
				{
					//resetAutoHide();
					btn.setBackgroundResource(R.drawable.prepagebtn);
					hsView.setDirection(HScrollViewGroup.Direction.RIGHT);
					hsView.snapToScreen(1);
				}
			}
		});
		
		ImageButton imageBtn = (ImageButton) findViewById(R.id.datalistpictrueact_leftrotate);
		imageBtn.setOnClickListener(datalistpictureact_OnClickListener);
		imageBtn = (ImageButton) findViewById(R.id.datalistpictrueact_pre);
		imageBtn.setOnClickListener(datalistpictureact_OnClickListener);
		imageBtnPlayPause = (ImageButton) findViewById(R.id.datalistpictrueact_playpause);
		imageBtnPlayPause.setOnClickListener(datalistpictureact_OnClickListener);
		imageBtn = (ImageButton) findViewById(R.id.datalistpictrueact_next);
		imageBtn.setOnClickListener(datalistpictureact_OnClickListener);
		imageBtn = (ImageButton) findViewById(R.id.datalistpictrueact_rightrotate);
		imageBtn.setOnClickListener(datalistpictureact_OnClickListener);
		imageBtn = (ImageButton) findViewById(R.id.datalistpictrueact_magnify);
		imageBtn.setOnClickListener(datalistpictureact_OnClickListener);
		imageBtn = (ImageButton) findViewById(R.id.datalistpictrueact_setlight);
		imageBtn.setOnClickListener(datalistpictureact_OnClickListener);
		imageBtn = (ImageButton) findViewById(R.id.datalistpictrueact_leftrotate);
		imageBtn.setOnClickListener(datalistpictureact_OnClickListener);
		imageBtn = (ImageButton) findViewById(R.id.datalistpictrueact_outdisc);
		imageBtn.setOnClickListener(datalistpictureact_OnClickListener);
		
		textLoading = (TextView) findViewById(R.id.text_load);
		
		//back
		ImageButton mButtonBack = (ImageButton) findViewById(R.id.buttonBack);
		mButtonBack.setOnClickListener(new OnClickListener() 
		{
		    public void onClick(View view) 
		    {
		    	LogCat.Logd("DVDMp5PictureActivity onKeyDown back");
				//ExitApp();
				Intent serviceIntent = new Intent(DVDMp5PictureActivity.this, 
						com.apical.dvdplayer.dvdcontrol.DVDService.class);
				Bundle bundle = new Bundle();  
				bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.KEY_BACK);
				serviceIntent.putExtras(bundle);
		        startService(serviceIntent);
		        
		        finish();
		    }
		});

		//home
		ImageButton mButtonHome = (ImageButton) findViewById(R.id.buttonHome);
		mButtonHome.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View view) 
			{
				Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);
				mHomeIntent.addCategory(Intent.CATEGORY_HOME);
				mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				DVDMp5PictureActivity.this.startActivity(mHomeIntent);
			}
		});
		
//		textLoading.setVisibility(View.VISIBLE);
//		mDvdView.adjustVideoSize(0, 0);
		
		/*****************************************************************/
		IntentFilter filter = new IntentFilter(DVDDealSet.DVDPLAYER_DVD_EVENT);
		filter.addAction(DVDDealSet.DVD_ACTIVITY_EXIT);
		filter.addAction("com.apical.radiobuttonchange");
		registerReceiver(mReceiver, filter);
		/******************************************************************/
	}

	@Override
	protected void onDestroy() 
	{
		LogCat.Logd("DVDMp5PictureActivity  onDestroy");
		DVDMp5PictureAct = null;
		
		unregisterReceiver(mReceiver);
		
		super.onDestroy();
	}

	@Override
	protected void onPause() 
	{
		LogCat.Logd("DVDMp5PictureActivity  onPause");
		
		super.onPause();
		
		mDvdView.onPause();
	}

	@Override
	protected void onResume() 
	{
		LogCat.Logd("DVDMp5PictureActivity  onResume");
		super.onResume();
		
		showhideCtrls(false);
		ctrlsIsShow = false;
		
		mDvdView.onResume();
	}

	@Override
	protected void onStart() 
	{
		LogCat.Logd("DVDMp5PictureActivity  onStart");
		super.onStart();
	}

	@Override
	protected void onStop() 
	{
		LogCat.Logd("DVDMp5PictureActivity  onStop");
		super.onStop();
	}	
	
	public void ExitApp()
	{
		finish();
		Intent serviceIntent = new Intent(DVDMp5PictureActivity.this, 
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
			LogCat.Logd("DVDMp5PictureActivity onKeyDown back");
			//ExitApp();
			Intent serviceIntent = new Intent(DVDMp5PictureActivity.this, 
					com.apical.dvdplayer.dvdcontrol.DVDService.class);
			Bundle bundle = new Bundle();  
			bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.KEY_BACK);
			serviceIntent.putExtras(bundle);
	        startService(serviceIntent);
		} 
		else if(keyCode == KeyEvent.KEYCODE_HOME)
		{
			LogCat.Logd("DVDMp5MusicActivity onKeyDown home");
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private BroadcastReceiver mReceiver = new BroadcastReceiver() 
	{

		@Override
		public void onReceive(Context context, Intent intent) 
		{
			if(intent.getAction().equals(DVDDealSet.DVD_ACTIVITY_EXIT))
			{
				LogCat.Logd("DVDMp5PictureActivity->onReceive  dvdplayer_exit_app");
				DVDMp5PictureActivity.this.finish();
			}
			else if(intent.getAction().equals(DVDDealSet.DVDPLAYER_DVD_EVENT))
			{
				Bundle data = intent.getExtras();
				int cmd = data.getInt("cmd");
				
				switch (cmd) 
				{
				case DvdControl.DVD_MCU_RET_CUR_TRACK:
					break;
				case DvdControl.DVD_MCU_RET_PLAYTIME:
					break;
				case DvdControl.DVD_MCU_RET_DISCPOS:
					break;
				case DvdControl.DVD_MCU_RET_INFO:
					int dataInfoType = data.getByte("InfoID");
					dataInfoType &= 0x00FF;
					if(dataInfoType == 0xF4)
					{
						byte byteJump = data.getByte("JumpTo");
						if (byteJump == 0x01) 
						{
							textLoading.setVisibility(View.VISIBLE);
							mDvdView.adjustVideoSize(0, 0);
						} 
						else if (byteJump == 0x02) 
						{
							jumpDVDView.postDelayed(DispDVDView, 800);
							textLoading.setVisibility(View.INVISIBLE);
						}
					}
					break;
				case DvdControl.DVD_MCU_RET_PLAYSTATUS:
					int state = data.getInt("dvd_state");
					if(state == 1)
					{
						imageBtnPlayPause.setBackgroundResource(R.drawable.btnplaypause);
					}
					else if(state == 2)
					{
						imageBtnPlayPause.setBackgroundResource(R.drawable.btnpause);
					}
					break;
				case DvdControl.DVD_MCU_RET_SERVOVERSION:
					break;
				case DvdControl.DVD_MCU_RET_DISCERR:
					break;
				}
			}
			else if (intent.getAction().equals("com.apical.radiobuttonchange")) 
			{
				final ImageButton moveImageButton = (ImageButton) findViewById(R.id.move);
				//resetAutoHide();
				if (hsView.getCurScreen() > 0) 
				{
					moveImageButton.setBackgroundResource(R.drawable.prepagebtn);
				} 
				else 
				{
					moveImageButton.setBackgroundResource(R.drawable.nextpagebtn);
				}
			}
		}
		
	};
	
	public void showhideCtrls(boolean flag)
	{
		RelativeLayout layoutPanel = (RelativeLayout)findViewById(R.id.RelativeLayoutPanel);
        RelativeLayout layoutRightPanel = (RelativeLayout) findViewById(R.id.RightMenuBar);
		if(!flag)
		{
			layoutPanel.setVisibility(View.GONE);
			layoutRightPanel.setVisibility(View.GONE);
		}
		else
		{
			layoutRightPanel.setVisibility(View.VISIBLE);
			layoutPanel.setVisibility(View.VISIBLE);
		}
	}
	
	private OnClickListener datalistpictureact_OnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) 
		{
			Intent serviceIntent = new Intent(DVDMp5PictureActivity.this, 
					com.apical.dvdplayer.dvdcontrol.DVDService.class);
			Bundle bundle = new Bundle();  
			
			switch (v.getId()) 
			{
			case R.id.datalistpictrueact_leftrotate:
				bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.LEFT_ROTATE);
				break;
			case R.id.datalistpictrueact_pre:
				bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.PRE_ONE);
				break;
			case R.id.datalistpictrueact_playpause:
				bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.PLAY_PAUSE);
				break;
			case R.id.datalistpictrueact_next:
				bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.NEXT_ONE);
				break;
			case R.id.datalistpictrueact_rightrotate:
				bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.RIGHT_ROTATE);
				break;
			case R.id.datalistpictrueact_magnify:
				bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.MAGNIFY);
				break;
			case R.id.datalistpictrueact_setlight:	
				View setView = findViewById(R.id.bright_set_picture);
				if (setView.getVisibility() == View.VISIBLE) {
					setView.setVisibility(View.INVISIBLE);
				} else {
					setView.setVisibility(View.VISIBLE);
				}
				bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.LIGHT_SET);
				break;
			case R.id.datalistpictrueact_outdisc:
				ExitApp();
				bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.OUT_DISC);
				break;
			default:
				break;

			}
			
			serviceIntent.putExtras(bundle);
	        startService(serviceIntent);
		}

	};
	
	Handler jumpDVDView = new Handler();
	Runnable DispDVDView = new Runnable() {
		@Override
		public void run() {
			mDvdView.adjustVideoSize(800, 480);
		}
	};
}