package com.apical.dvdplayer.dvdview;

import com.apical.dvdplayer.ApicalHardwareCtrl;
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
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DVDDVDPlayerActivity extends Activity
{
	public static DVDDVDPlayerActivity DVDDVDPlayerAct = null; 
	
	private DvdView mDvdView;
	private HScrollViewGroup hsView;
	private boolean ctrlsIsShow = false;
	
	private ProgressBar mSeekBar;
	private ImageButton imageBtnPlayPause;
	
	private TextView mCurTime;
	private TextView mTotalTime;
	
	private int mMediaLength = 0;
	
	private ApicalHardwareCtrl mApicalHawreCtrl; 

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		DVDDVDPlayerAct = this;
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);  //全屏
		setContentView(R.layout.dvd_dvdplayer_activity);
		
		mDvdView = (DvdView) findViewById(R.id.dvdViewVideo);
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
                    
					Intent serviceIntent = new Intent(DVDDVDPlayerActivity.this, 
							com.apical.dvdplayer.dvdcontrol.DVDService.class);
					Bundle bundle = new Bundle(); 
					bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.TOUCH_SCREEN);
					bundle.putInt("touch_screen_x", x);
					bundle.putInt("touch_screen_y", y);
					serviceIntent.putExtras(bundle);
			        startService(serviceIntent);
				}

//				resetAutoHide();
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
		
		//back
		ImageButton mButtonBack = (ImageButton) findViewById(R.id.buttonBack);
		mButtonBack.setOnClickListener(new OnClickListener() 
		{
		    public void onClick(View view) 
		    {
		    	//DVDDVDPlayerActivity.this.moveTaskToBack(true);
		    	ExitApp();
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
				DVDDVDPlayerActivity.this.startActivity(mHomeIntent);
			}
		});
		
		/*********************************************************/
		ImageButton imageBtn = (ImageButton) findViewById(R.id.dvdplayeract_sward);
		imageBtn.setOnClickListener(dvdplayeract_OnClickListener);
		
		imageBtn = (ImageButton) findViewById(R.id.dvdplayeract_pre);
		imageBtn.setOnClickListener(dvdplayeract_OnClickListener);
		
		imageBtnPlayPause = (ImageButton) findViewById(R.id.dvdplayeract_playpause);
		imageBtnPlayPause.setOnClickListener(dvdplayeract_OnClickListener);
		
		imageBtn = (ImageButton) findViewById(R.id.dvdplayeract_next);
		imageBtn.setOnClickListener(dvdplayeract_OnClickListener);
		
		imageBtn = (ImageButton) findViewById(R.id.dvdplayeract_setlight);
		imageBtn.setOnClickListener(dvdplayeract_OnClickListener);
		
		imageBtn = (ImageButton) findViewById(R.id.dvdplayeract_fward);
		imageBtn.setOnClickListener(dvdplayeract_OnClickListener);
		
		imageBtn = (ImageButton) findViewById(R.id.dvdplayeract_stop);
		imageBtn.setOnClickListener(dvdplayeract_OnClickListener);
		
		imageBtn = (ImageButton) findViewById(R.id.dvdplayeract_audio);
		imageBtn.setOnClickListener(dvdplayeract_OnClickListener);
		
		imageBtn = (ImageButton) findViewById(R.id.dvdplayeract_soundchange);
		imageBtn.setOnClickListener(dvdplayeract_OnClickListener);
		
		imageBtn = (ImageButton) findViewById(R.id.dvdplayeract_outdisc);
		imageBtn.setOnClickListener(dvdplayeract_OnClickListener);
		
		ImageButton imageBtn_sward = (ImageButton) findViewById(R.id.dvdplayeract_fward);
		imageBtn_sward.setOnClickListener(dvdplayeract_OnClickListener);
		/***********************************************************/
		
		mCurTime = (TextView) findViewById(R.id.textViewProgress);
		mTotalTime = (TextView) findViewById(R.id.textViewLength);

		mSeekBar = (ProgressBar) findViewById(R.id.dvd_seekBarProgress);
		mSeekBar.setMax(0);
		
		/*****************************************************************/
		IntentFilter filter = new IntentFilter(DVDDealSet.DVDPLAYER_DVD_EVENT);
		filter.addAction(DVDDealSet.DVD_ACTIVITY_EXIT);
		filter.addAction("com.apical.radiobuttonchange");
		registerReceiver(mReceiver, filter);
		/******************************************************************/
		
		mApicalHawreCtrl = new ApicalHardwareCtrl(ApicalHardwareCtrl.APP_DVD);
	}

	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
		
		DVDDVDPlayerAct = null;
		
		unregisterReceiver(mReceiver);
		
		mApicalHawreCtrl.AudioSourceRelease();
	}

	@Override
	protected void onPause() 
	{
		super.onPause();
		
		mDvdView.onPause();
	}

	@Override
	protected void onResume() 
	{
		mApicalHawreCtrl.AudioSourceRequest();
		
		super.onResume();
		
		showhideCtrls(false);
		ctrlsIsShow = false;
		
		mDvdView.onResume();
	}

	@Override
	protected void onStart() 
	{
		super.onStart();
	}

	@Override
	protected void onStop() 
	{
		super.onStop();
	}
	
	public void ExitApp()
	{
		finish();
		Intent serviceIntent = new Intent(DVDDVDPlayerActivity.this, 
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
			LogCat.Logd("DVDDVDPlayerActivity onKeyDown back");
			ExitApp();
			return true;
		} 
		else if(keyCode == KeyEvent.KEYCODE_HOME)
		{
			LogCat.Logd("DVDDVDPlayerActivity onKeyDown home");
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public void showhideCtrls(boolean flag)
	{
		LinearLayout layoutProcess = (LinearLayout) findViewById(R.id.linearLayoutProgress);
		RelativeLayout layoutPanel = (RelativeLayout) findViewById(R.id.relativeLayoutBottomBtn);
		RelativeLayout layoutRightPanel = (RelativeLayout) findViewById(R.id.RightMenuBar);
		if(!flag)
		{
			layoutProcess.setVisibility(View.GONE);
			layoutPanel.setVisibility(View.GONE);
			layoutRightPanel.setVisibility(View.GONE);
			findViewById(R.id.bright_set_dvd).setVisibility(View.GONE);
		}
		else
		{
			layoutRightPanel.setVisibility(View.VISIBLE);
			layoutProcess.setVisibility(View.VISIBLE);
			layoutPanel.setVisibility(View.VISIBLE);
		}
	}
	
	private BroadcastReceiver mReceiver = new BroadcastReceiver() 
	{

		@Override
		public void onReceive(Context context, Intent intent) 
		{
			if(intent.getAction().equals(DVDDealSet.DVD_ACTIVITY_EXIT))
			{
				LogCat.Logd("DVDDVDPlayerActivity->onReceive  dvdplayer_exit_app");
				DVDDVDPlayerActivity.this.finish();
			}
			else if(intent.getAction().equals(DVDDealSet.DVDPLAYER_DVD_EVENT))
			{
				Bundle data = intent.getExtras();
				int cmd = data.getInt("cmd");
				
				switch (cmd) 
				{
				case DvdControl.DVD_MCU_RET_CUR_TRACK:
					short parmLen = data.getShort("paramlen");
					byte thour;
					byte tminute;
					byte tsecond;
					if (parmLen == 3) 
					{
						
					} 
					else if (parmLen > 3) 
					{
						thour = data.getByte("TotalHour");
						tminute = data.getByte("TotalMin");
						tsecond = data.getByte("TotalSec");
						mMediaLength = thour * 3600 + tminute * 60 + tsecond;
						mTotalTime.setText(String.format("%02d:%02d:%02d", thour, tminute,
								tsecond));
						mSeekBar.setMax(/*DVDService.SEEK_BAR_MAX*/1000);
					}
					break;
				case DvdControl.DVD_MCU_RET_PLAYTIME:
					//加了时间刷新后，底部滑动有点卡卡的，待解决
					byte phour;
					byte pminute;
					byte psecond;
					int curTime;
					phour = data.getByte("PlayTimeHour");
					pminute = data.getByte("PlayTimeMin");
					psecond = data.getByte("PlayTimeSec");
					
					curTime = phour * 3600 + pminute * 60 + psecond;
					mCurTime.setText(String.format("%02d:%02d:%02d", phour, pminute, psecond));
					
					if (mMediaLength > 0) 
					{
						int max = mSeekBar.getMax();
						int progress = max * curTime / mMediaLength;
						mSeekBar.setProgress(progress);
					}
					break;
				case DvdControl.DVD_MCU_RET_DISCPOS:
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
			else if (intent.getAction().equals("com.apical.radiobuttonchange")) {
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
	
	private OnClickListener dvdplayeract_OnClickListener = new OnClickListener() 
	{
		@Override
		public void onClick(View v) 
		{
			Intent serviceIntent = new Intent(DVDDVDPlayerActivity.this, 
					com.apical.dvdplayer.dvdcontrol.DVDService.class);
			Bundle bundle = new Bundle();  
	        
			switch (v.getId()) 
			{
			case R.id.dvdplayeract_sward:
				bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.BACKWARD);
				LogCat.Logd("dvdplayeract_OnClickListener  trgCmd = BACKWARD");
				break;
			case R.id.dvdplayeract_pre:
				bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.PRE_ONE);
				LogCat.Logd("dvdplayeract_OnClickListener  trgCmd = PRE_ONE");
				break;
			case R.id.dvdplayeract_playpause:
				bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.PLAY_PAUSE);
				LogCat.Logd("dvdplayeract_OnClickListener  trgCmd = PLAY_PAUSE");
				break;
			case R.id.dvdplayeract_next:
				bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.NEXT_ONE);
				LogCat.Logd("dvdplayeract_OnClickListener  trgCmd = NEXT_ONE");
				break;
			case R.id.dvdplayeract_fward:
				bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.FORWARD);
				LogCat.Logd("dvdplayeract_OnClickListener  trgCmd = FORWARD");
				break;
			case R.id.dvdplayeract_stop:
				bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.STOP);
				LogCat.Logd("dvdplayeract_OnClickListener  trgCmd = STOP");
				break;
			case R.id.dvdplayeract_setlight:
				View setView = findViewById(R.id.bright_set_dvd);
				if (setView.getVisibility() == View.VISIBLE) {
					setView.setVisibility(View.INVISIBLE);
				} else {
					setView.setVisibility(View.VISIBLE);
				}
				bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.LIGHT_SET);
				LogCat.Logd("dvdplayeract_OnClickListener  trgCmd = LIGHT_SET");
				break;
			case R.id.dvdplayeract_audio:
//				Intent i = new Intent();
//        		i.setClassName("com.apical.audiosettings",
//        				"com.apical.audiosettings.AudioActivity");
//        		startActivity(i);
				bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.AUDIO_SET);
				LogCat.Logd("dvdplayeract_OnClickListener  trgCmd = AUDIO_SET");
				break;
			case R.id.dvdplayeract_soundchange:
				bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.SOUND_CHANGE);
				LogCat.Logd("dvdplayeract_OnClickListener  trgCmd = SOUND_CHANGE");
				break;
			case R.id.dvdplayeract_outdisc:
				ExitApp();
				bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.OUT_DISC);
				LogCat.Logd("dvdplayeract_OnClickListener  trgCmd = OUT_DISC");
				break;
			default:
				break;
			}
			serviceIntent.putExtras(bundle);
	        startService(serviceIntent);
		}
	};
	
	public void UpdatePlayPause(boolean state)
	{
		
	}
}