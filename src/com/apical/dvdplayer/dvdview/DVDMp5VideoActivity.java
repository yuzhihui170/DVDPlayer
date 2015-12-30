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
import android.os.Handler;
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

public class DVDMp5VideoActivity extends Activity
{
	public static Activity DVDMp5VedioAct = null;
	
	private DvdView mDvdView;
	private HScrollViewGroup hsView;
	
	private ProgressBar mSeekBar;
	private ImageButton imageBtnPlayPause;
	
	private TextView mCurTime;
	private TextView mTotalTime;
	
	private TextView textLoading;
	
	private int mMediaLength = 0;
	
	private boolean ctrlsIsShow = true;
	
	private ApicalHardwareCtrl mApicalHawreCtrl; 

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		DVDMp5VedioAct = this;
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);  //全屏
		setContentView(R.layout.dvd_mp5vedio_activity);
		
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
		
		
		ImageButton imageBtn = (ImageButton) findViewById(R.id.datalistvedioact_sward);
		imageBtn.setOnClickListener(datalistvedioact_OnClickListener);
		imageBtn = (ImageButton) findViewById(R.id.datalistvedioact_pre);
		imageBtn.setOnClickListener(datalistvedioact_OnClickListener);
		imageBtnPlayPause = (ImageButton) findViewById(R.id.datalistvedioact_playpause);
		imageBtnPlayPause.setOnClickListener(datalistvedioact_OnClickListener);
		imageBtn = (ImageButton) findViewById(R.id.datalistvedioact_next);
		imageBtn.setOnClickListener(datalistvedioact_OnClickListener);
		imageBtn = (ImageButton) findViewById(R.id.datalistvedioact_fward);
		imageBtn.setOnClickListener(datalistvedioact_OnClickListener);
		imageBtn = (ImageButton) findViewById(R.id.datalistvedioact_stop);
		imageBtn.setOnClickListener(datalistvedioact_OnClickListener);
		imageBtn = (ImageButton) findViewById(R.id.datalistvedioact_audio);
		imageBtn.setOnClickListener(datalistvedioact_OnClickListener);
		imageBtn = (ImageButton) findViewById(R.id.datalistvedioact_setlight);
		imageBtn.setOnClickListener(datalistvedioact_OnClickListener);
		imageBtn = (ImageButton) findViewById(R.id.datalistvedioact_soundchange);
		imageBtn.setOnClickListener(datalistvedioact_OnClickListener);
		imageBtn = (ImageButton) findViewById(R.id.datalistvedioact_outdisc);
		imageBtn.setOnClickListener(datalistvedioact_OnClickListener);
		ImageButton imageBtn_sward = (ImageButton) findViewById(R.id.datalistvedioact_fward);
		imageBtn_sward.setOnClickListener(datalistvedioact_OnClickListener);
		
		mCurTime = (TextView) findViewById(R.id.datadiscvedioact_tv0);
		mTotalTime = (TextView) findViewById(R.id.datadiscvedioact_tv1);

		mSeekBar = (ProgressBar) findViewById(R.id.datadiscvedioact_seekbarprocess);
		mSeekBar.setMax(/*DVDService.SEEKBAR_ZERO*/0);
		
		textLoading = (TextView) findViewById(R.id.text_load);
		
		
		// 返回
		ImageButton mButtonBack = (ImageButton) findViewById(R.id.buttonBack);
		mButtonBack.setOnClickListener(new OnClickListener() 
		{
		    public void onClick(View view) 
		    {
		    	LogCat.Logd("DVDMp5VideoActivity onKeyDown back");
				//ExitApp();
				Intent serviceIntent = new Intent(DVDMp5VideoActivity.this, 
						com.apical.dvdplayer.dvdcontrol.DVDService.class);
				Bundle bundle = new Bundle();  
				bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.KEY_BACK);
				serviceIntent.putExtras(bundle);
		        startService(serviceIntent);
		        
		        finish();
		    }
		});

		ImageButton mButtonHome = (ImageButton) findViewById(R.id.buttonHome);
		mButtonHome.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View view) 
			{
				Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);
				mHomeIntent.addCategory(Intent.CATEGORY_HOME);
				mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				DVDMp5VideoActivity.this.startActivity(mHomeIntent);
			}
		});
	    
		
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
		DVDMp5VedioAct = null;
		
		unregisterReceiver(mReceiver);
		
		super.onDestroy();
		
		mApicalHawreCtrl.AudioSourceRelease();
	}

	@Override
	protected void onPause() 
	{
		mDvdView.onPause();
		
		super.onPause();
	}

	@Override
	protected void onResume() 
	{
		mApicalHawreCtrl.AudioSourceRequest();
		
		showhideCtrls(false);
		ctrlsIsShow = false;
		
		super.onResume();
	}

	@Override
	protected void onStart() 
	{
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() 
	{
		// TODO Auto-generated method stub
		super.onStop();
	}

	public void ExitApp()
	{
		finish();
		Intent serviceIntent = new Intent(DVDMp5VideoActivity.this, 
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
			LogCat.Logd("DVDMp5MusicActivity onKeyDown back");
			//ExitApp();
			Intent serviceIntent = new Intent(DVDMp5VideoActivity.this, 
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
	
	public void showhideCtrls(boolean flag)
	{
		LinearLayout layoutProcess = (LinearLayout) findViewById(R.id.linearLayoutProgress);
		RelativeLayout layoutPanel = (RelativeLayout) findViewById(R.id.RelativeLayoutPanel);
		RelativeLayout layoutRightPanel = (RelativeLayout) findViewById(R.id.RightMenuBar_video);
		if(!flag)
		{
			layoutProcess.setVisibility(View.GONE);
			layoutPanel.setVisibility(View.GONE);
			layoutRightPanel.setVisibility(View.GONE);
			findViewById(R.id.bright_set_video).setVisibility(View.GONE);
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
				LogCat.Logd("DVDMp5VideoActivity->onReceive  dvdplayer_exit_app");
				DVDMp5VideoActivity.this.finish();
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
	
	private OnClickListener datalistvedioact_OnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) 
		{
			
			Intent serviceIntent = new Intent(DVDMp5VideoActivity.this, 
					com.apical.dvdplayer.dvdcontrol.DVDService.class);
			Bundle bundle = new Bundle();  
			
			switch (v.getId()) 
			{
			case R.id.datalistvedioact_sward:
				bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.BACKWARD);
				break;
			case R.id.datalistvedioact_pre:
				bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.PRE_ONE);
				break;
			case R.id.datalistvedioact_playpause:
				bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.PLAY_PAUSE);
				break;
			case R.id.datalistvedioact_next:
				bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.NEXT_ONE);
				break;
			case R.id.datalistvedioact_fward:
				bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.FORWARD);
				break;
			case R.id.datalistvedioact_stop:
				bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.STOP);
				break;
			case R.id.datalistvedioact_setlight:
				View setView = findViewById(R.id.bright_set_video);
				if (setView.getVisibility() == View.VISIBLE) 
				{
					setView.setVisibility(View.INVISIBLE);
				} 
				else 
				{
					setView.setVisibility(View.VISIBLE);
				}
				bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.LIGHT_SET);
				break;
			case R.id.datalistvedioact_audio:
				bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.AUDIO_SET);
				break;
			case R.id.datalistvedioact_soundchange:
				bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.SOUND_CHANGE);
				break;
			case R.id.datalistvedioact_outdisc:
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