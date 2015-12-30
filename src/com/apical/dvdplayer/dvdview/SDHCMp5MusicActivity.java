package com.apical.dvdplayer.dvdview;

import java.io.UnsupportedEncodingException;

import com.apical.dvdplayer.ApicalHardwareCtrl;
import com.apical.dvdplayer.LogCat;
import com.apical.dvdplayer.R;
import com.apical.dvdplayer.dvdmodel.AppData;
import com.apical.dvdplayer.dvdmodel.DVDDealSet;

import android.app.Activity;
import android.app.DvdControl;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

public class SDHCMp5MusicActivity extends Activity
{
	public static Activity SDHCMp5MusicAct = null;
	
private HScrollViewGroup hsView;
	
	private ImageButton imageBtnPlayPause; // 播放暂停按钮
	private ProgressBar mSeekBar; // 播放进度条
	private TextView mCurTime; // 当前播放时间
	private TextView mTotalTime; // 总播放时间
	private TextView mMusicNameTextView; // 音乐标题
	private TextView mMusicAutorTextView; // 歌手
	private TextView mMusicSpecialTextView; // 专辑
	private TextView mMusicPrequick; // 快退
	private TextView mMusicNextquick; // 快进
	private TextView m_tvMusicSound; // 声道
	private RadioButton m_rdBtnRandom;
	private RadioButton m_rdBtnRepetClose;
	private RadioButton m_rdBtnsingle;
	private RadioButton m_rdBtnRepetList;
	
	private int mTotalTrack = 0; // 总共CD曲目
	private int mMediaLength = 0; // 播放时间转换为进度
	private int mCurPlayTrack = 0; // 记录当前播放的CD序号
	
	String strArtist = null;
	String strAlbum = null;
	String strName = null;
	
	AppData mdata = null;
	
	private ApicalHardwareCtrl mApicalHawreCtrl; 

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		SDHCMp5MusicAct = this;
		setContentView(R.layout.sdhc_mp5music_activity);
		
		mdata = AppData.Instance();
		
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
		
		/****************************************************************/
		mMusicNameTextView = (TextView) findViewById(R.id.datalistmusic_tv0);
		mMusicAutorTextView = (TextView) findViewById(R.id.datalistmusic_tv1);
		mMusicSpecialTextView = (TextView) findViewById(R.id.datalistmusic_tv2);
		mCurTime = (TextView) findViewById(R.id.datalistmusic_tv3);
		mTotalTime = (TextView) findViewById(R.id.datalistmusic_tv4);
		mMusicNextquick = (TextView) findViewById(R.id.datalistmusic_tv5);
		mMusicPrequick = (TextView) findViewById(R.id.datalistmusic_tv6);
		m_tvMusicSound = (TextView) findViewById(R.id.datalistmusic_tv7);
		mMusicPrequick.setVisibility(View.GONE);
		mMusicNextquick.setVisibility(View.GONE);
		m_rdBtnRepetList = (RadioButton) findViewById(R.id.datalistmusic_raidobtn_repetlist);
		m_rdBtnRepetClose = (RadioButton) findViewById(R.id.datalistmusic_raidobtn_repetclose);
		m_rdBtnsingle = (RadioButton) findViewById(R.id.datalistmusic_raidobtn_single);
		m_rdBtnRandom = (RadioButton) findViewById(R.id.datalistmusic_raidobtn_random);
		/******************************************************************/
		
		mSeekBar = (ProgressBar) findViewById(R.id.datalistmusic_seekBarProgress0);
		mSeekBar.setMax(/*DVDService.SEEKBAR_ZERO*/0);
		
		ImageButton imageBtn = (ImageButton) findViewById(R.id.datalistmusicact_sward);
		imageBtn.setOnClickListener(datalistmusicact_OnClickListener);
		imageBtn = (ImageButton) findViewById(R.id.datalistmusicact_pre);
		imageBtn.setOnClickListener(datalistmusicact_OnClickListener);
		imageBtnPlayPause = (ImageButton) findViewById(R.id.datalistmusicact_playpause);
		imageBtnPlayPause.setOnClickListener(datalistmusicact_OnClickListener);
		imageBtn = (ImageButton) findViewById(R.id.datalistmusicact_next);
		imageBtn.setOnClickListener(datalistmusicact_OnClickListener);
		imageBtn = (ImageButton) findViewById(R.id.datalistmusicact_fward);
		imageBtn.setOnClickListener(datalistmusicact_OnClickListener);
		imageBtn = (ImageButton) findViewById(R.id.datalistmusicact_circulation);
		imageBtn.setOnClickListener(datalistmusicact_OnClickListener);
		imageBtn = (ImageButton) findViewById(R.id.datalistmusicact_stop);
		imageBtn.setOnClickListener(datalistmusicact_OnClickListener);
		imageBtn = (ImageButton) findViewById(R.id.datalistmusicact_audio);
		imageBtn.setOnClickListener(datalistmusicact_OnClickListener);
		imageBtn = (ImageButton) findViewById(R.id.datalistmusicact_soundchange);
		imageBtn.setOnClickListener(datalistmusicact_OnClickListener);
		ImageButton imageBtn_sward = (ImageButton) findViewById(R.id.datalistmusicact_fward);
		imageBtn_sward.setOnClickListener(datalistmusicact_OnClickListener);
//		imageBtn = (ImageButton) findViewById(R.id.datalistmusicact_outdisc);
//		imageBtn.setOnClickListener(datalistmusicact_OnClickListener);	
		
		/*****************************************************************/
		IntentFilter filter = new IntentFilter(DVDDealSet.DVDPLAYER_DVD_EVENT);
		filter.addAction(DVDDealSet.SDHC_ACTIVITY_EXIT);
		filter.addAction(DVDDealSet.DVDPLAYER_CIRCLE_UPDATE);
		filter.addAction(DVDDealSet.MEDIA_TITTLE_UPDATE);
		filter.addAction("com.apical.radiobuttonchange");
		registerReceiver(mReceiver, filter);
		/******************************************************************/
		
		mApicalHawreCtrl = new ApicalHardwareCtrl(ApicalHardwareCtrl.APP_DVD);
		
		LogCat.Logd("SDHCMp5MusicActivity onCreate");
	}

	@Override
	protected void onDestroy() 
	{
		SDHCMp5MusicAct = null;
		
		unregisterReceiver(mReceiver);
		
		super.onDestroy();
		
		mApicalHawreCtrl.AudioSourceRelease();
		
		LogCat.Logd("SDHCMp5MusicActivity onDestroy");
	}

	@Override
	protected void onPause() 
	{
		LogCat.Logd("SDHCMp5MusicActivity onPause");
		
		super.onPause();
	}

	@Override
	protected void onResume() 
	{
        mApicalHawreCtrl.AudioSourceRequest();
		
		mMusicNameTextView.setText(mdata.getCurMediaNum() + mdata.getCurMediaName());
		
		super.onResume();
		
		LogCat.Logd("SDHCMp5MusicActivity onResume");
	}

	@Override
	protected void onStart() 
	{
		LogCat.Logd("SDHCMp5MusicActivity onStart");
		super.onStart();
	}

	@Override
	protected void onStop() 
	{
		LogCat.Logd("SDHCMp5MusicActivity onStop");
		
		super.onStop();
	}
	
	public void ExitApp()
	{
		finish();
		Intent serviceIntent = new Intent(SDHCMp5MusicActivity.this, 
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
			Intent serviceIntent = new Intent(SDHCMp5MusicActivity.this, 
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
				LogCat.Logd("DVDMp5MusicActivity->onReceive  dvdplayer_exit_app");
				SDHCMp5MusicActivity.this.finish();
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
						//
					} 
					else if (parmLen > 3) 
					{
						thour = data.getByte("TotalHour");
						tminute = data.getByte("TotalMin");
						tsecond = data.getByte("TotalSec");
						mTotalTrack = data.getByte("TotalChapterL");
						mMediaLength = thour * 3600 + tminute * 60 + tsecond;
						mTotalTime.setText(String.format("%02d:%02d:%02d", thour, tminute,
								tsecond));

						mCurPlayTrack = data.getByte("DvdTitle");
						
						mSeekBar.setMax(/*DVDService.SEEK_BAR_MAX*/1000);
					}
					break;
				case DvdControl.DVD_MCU_RET_PLAYTIME:
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
					if(dataInfoType == 0xE3)
					{
						short fileLen = data.getShort("paramlen");
						byte [] fileInfo = new byte[fileLen-1];
						fileInfo = data.getByteArray("Info");
						String singerString="";
						try 
						{
							singerString = new String(fileInfo, "unicode");
						} 
						catch (UnsupportedEncodingException e) 
						{
							e.printStackTrace();
						}
						strArtist = singerString;
						
						mMusicAutorTextView.setText(strArtist);
						
						LogCat.Logd("DVDMp5MusicActivity onReceiver strArtist = " + strArtist);
						
					}
					else if(dataInfoType == 0xE4)
					{
						short fileLen = data.getShort("paramlen");
						byte [] fileInfo = new byte[fileLen-1];
						fileInfo = data.getByteArray("Info");
						String singerString="";
						try 
						{
							singerString = new String(fileInfo, "unicode");
						} 
						catch (UnsupportedEncodingException e) 
						{
							e.printStackTrace();
						}
						strAlbum = singerString;
						
						mMusicSpecialTextView.setText(strAlbum);
						
						LogCat.Logd("DVDMp5MusicActivity onReceiver strAlbum = " + strAlbum);
					}
//					else if(dataInfoType == 0xE5)  //tittle一直收不到机芯的，只有去掉自己传过来了
//					{
//						short fileLen = data.getShort("paramlen");
//						byte [] fileInfo = new byte[fileLen-1];
//						fileInfo = data.getByteArray("Info");
//						String singerString="";
//						try 
//						{
//							singerString = new String(fileInfo, "unicode");
//						} 
//						catch (UnsupportedEncodingException e) 
//						{
//							e.printStackTrace();
//						}
//						strName = singerString;
//						
//						mMusicNameTextView.setText(strName);
//						
//						LogCat.Logd("DVDMp5MusicActivity onReceiver strName = " + strName);
//					}
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
			else if(intent.getAction().equals(DVDDealSet.DVDPLAYER_CIRCLE_UPDATE))
			{
				Bundle data = intent.getExtras();
				int circleType = data.getInt("circle_type");
				LogCat.Logd("DVDCDPlayerActivity->onReceive  DVDPLAYER_CIRCLE_UPDATE circleType = " + circleType);
				switch(circleType)
				{
				case 1:
					m_rdBtnRandom.setChecked(true);
					break;
				case 2:
					m_rdBtnRepetClose.setChecked(true);
					break;
				case 3:
					m_rdBtnsingle.setChecked(true);
					break;
				case 4:
					m_rdBtnRepetList.setChecked(true);
					 break;
				default:
					break;
				}
			}
			else if(intent.getAction().equals(DVDDealSet.MEDIA_TITTLE_UPDATE))
			{
				strArtist = " ";
				strAlbum = " ";
				mMusicAutorTextView.setText(strArtist);
				mMusicSpecialTextView.setText(strAlbum);
				mMusicNameTextView.setText(mdata.getCurMediaNum() + mdata.getCurMediaName());
				
				
				LogCat.Logd("DVDMp5MusicActivity onReceiver media_tittle_update");
			}
		}
	};
	
	private OnClickListener datalistmusicact_OnClickListener = new OnClickListener() 
	{
		@Override
		public void onClick(View v) 
		{
			
			Intent serviceIntent = new Intent(SDHCMp5MusicActivity.this, 
					com.apical.dvdplayer.dvdcontrol.DVDService.class);
			Bundle bundle = new Bundle();  
			
			switch (v.getId()) 
			{
			case R.id.datalistmusicact_sward:
				bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.BACKWARD);
				break;
			case R.id.datalistmusicact_pre:
				bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.PRE_ONE);
				break;
			case R.id.datalistmusicact_playpause:
				bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.PLAY_PAUSE);
				break;
			case R.id.datalistmusicact_next:
				bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.NEXT_ONE);
				break;
			case R.id.datalistmusicact_fward:
				bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.FORWARD);
				break;
			case R.id.datalistmusicact_circulation:
				bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.CIRCLE_TYP);
				break;
			case R.id.datalistmusicact_stop:
				bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.STOP);
				break;
			case R.id.datalistmusicact_audio:
				bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.AUDIO_SET);
				break;
			case R.id.datalistmusicact_soundchange:
				bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.SOUND_CHANGE);
				break;
			case R.id.datalistmusicact_outdisc:
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
}