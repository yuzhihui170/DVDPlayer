package com.apical.dvdplayer.dvdview;

import java.util.ArrayList;
import java.util.HashMap;

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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class DVDCDPlayerActivity extends Activity
{
	public static Activity DVDCDPlayerAct = null;
	
	private HScrollViewGroup hsView;
	private ProgressBar mSeekBar; // 播放进度条
	
	private ListView CDListView; // CD歌曲列表
	private TextView mCurTrackTextView; // 显示当前歌曲曲目
	private TextView mCurTime; // 当前播放时间
	private TextView mTotalTime; // 总播放时间
	private TextView mCurTrackNo;
	private TextView mTotalTrackNo;
	private TextView mMusicPrequick;
	private TextView mMusicNextquick;
	private TextView m_tvMusicSound; //声道
	
	private ImageButton imageBtnPlayPause;
	private RadioButton m_rdBtnRandom;
	private RadioButton m_rdBtnRepetClose;
	private RadioButton m_rdBtnsingle;
	
	private int mTotalTrack = 0; // 总共CD曲目
	FuncListAdapter flistItemAdapter; //自定义Adapter
	
	private RadioButton m_rdBtnRepetList;
	
	private int mMediaLength = 0; // 播放时间转换为进度
	
	private int mCurPlayTrack = 0; // 记录当前播放的CD序号
	
	private ApicalHardwareCtrl mApicalHawreCtrl; 

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		DVDCDPlayerAct = this;
		
		setContentView(R.layout.dvd_cdplayer_activity);
		
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
		
		mSeekBar = (ProgressBar) findViewById(R.id.CDseekBarProgress);
		//mSeekBar.setMax(/*DVDService.SEEKBAR_ZERO*/0);
		mSeekBar.setMax(/*DVDService.SEEK_BAR_MAX*/1000);
		
		CDListView = (ListView) findViewById(R.id.listViewMedia);
		CDListView.setOnItemClickListener(cDlistListener);
		mCurTrackTextView = (TextView) findViewById(R.id.cdplayeract_tv0);
		mCurTrackNo = (TextView) findViewById(R.id.cdplayeract_tv2);
		mTotalTrackNo = (TextView) findViewById(R.id.cdplayeract_tv3);
		mCurTime = (TextView) findViewById(R.id.cdplayeract_tv4);
		mTotalTime = (TextView) findViewById(R.id.cdplayeract_tv5);

		mMusicNextquick = (TextView) findViewById(R.id.datalistmusic_tv6);
		mMusicPrequick = (TextView) findViewById(R.id.datalistmusic_tv7);

		mMusicPrequick.setVisibility(View.GONE);
		mMusicNextquick.setVisibility(View.GONE);
		
		m_tvMusicSound = (TextView) findViewById(R.id.cdplay_tv7);
		
		ImageButton imageBtn = (ImageButton) findViewById(R.id.cdplayeract_sward);
		imageBtn.setOnClickListener(cdplayeract_OnClickListener);
		imageBtn = (ImageButton) findViewById(R.id.cdplayeract_pre);
		imageBtn.setOnClickListener(cdplayeract_OnClickListener);
		imageBtnPlayPause = (ImageButton) findViewById(R.id.cdplayeract_playpause);
		imageBtnPlayPause.setOnClickListener(cdplayeract_OnClickListener);
		imageBtn = (ImageButton) findViewById(R.id.cdplayeract_next);
		imageBtn.setOnClickListener(cdplayeract_OnClickListener);
		imageBtn = (ImageButton) findViewById(R.id.cdplayeract_fward);
		imageBtn.setOnClickListener(cdplayeract_OnClickListener);
		imageBtn = (ImageButton) findViewById(R.id.cdplayeract_circulation);
		imageBtn.setOnClickListener(cdplayeract_OnClickListener);
		imageBtn = (ImageButton) findViewById(R.id.cdplayeract_stop);
		imageBtn.setOnClickListener(cdplayeract_OnClickListener);
		imageBtn = (ImageButton) findViewById(R.id.cdplayeract_audio);
		imageBtn.setOnClickListener(cdplayeract_OnClickListener);
		imageBtn = (ImageButton) findViewById(R.id.cdplayeract_soundchange);
		imageBtn.setOnClickListener(cdplayeract_OnClickListener);
		imageBtn = (ImageButton) findViewById(R.id.cdplayeract_outdisc);
		imageBtn.setOnClickListener(cdplayeract_OnClickListener);
		ImageButton imageBtn_sward = (ImageButton) findViewById(R.id.cdplayeract_fward);
		imageBtn_sward.setOnClickListener(cdplayeract_OnClickListener);
		
		m_rdBtnRepetList = (RadioButton)findViewById(R.id.raidobtn_repetlist);
		m_rdBtnRepetClose = (RadioButton)findViewById(R.id.raidobtn_repetclose);
		m_rdBtnsingle = (RadioButton)findViewById(R.id.raidobtn_single);
		m_rdBtnRandom = (RadioButton)findViewById(R.id.raidobtn_random);
		m_rdBtnRepetList.setChecked(true);
		
		/*****************************************************************/
		IntentFilter filter = new IntentFilter(DVDDealSet.DVDPLAYER_DVD_EVENT);
		filter.addAction(DVDDealSet.DVD_ACTIVITY_EXIT);
		filter.addAction("com.apical.radiobuttonchange");
		filter.addAction(DVDDealSet.DVDPLAYER_CIRCLE_UPDATE);
		registerReceiver(mReceiver, filter);
		/******************************************************************/
		
		mApicalHawreCtrl = new ApicalHardwareCtrl(ApicalHardwareCtrl.APP_DVD);
	}

	@Override
	protected void onDestroy() 
	{
		mApicalHawreCtrl.AudioSourceRelease();
		
		super.onDestroy();
		
		DVDCDPlayerAct = null;
		
		unregisterReceiver(mReceiver);
	}

	@Override
	protected void onPause() 
	{
		// TODO Auto-generated method stub
		super.onPause();
		
		DVDCDPlayerAct = null;
	}

	@Override
	protected void onResume() 
	{
		mApicalHawreCtrl.AudioSourceRequest();
		
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
		Intent serviceIntent = new Intent(DVDCDPlayerActivity.this, 
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
			if(intent.getAction().equals(DVDDealSet.DVD_ACTIVITY_EXIT))
			{
				LogCat.Logd("DVDCDPlayerActivity->onReceive  dvdplayer_exit_app");
				DVDCDPlayerActivity.this.finish();
			}
			else if(intent.getAction().equals(DVDDealSet.DVDPLAYER_DVD_EVENT))
			{
				Bundle data = intent.getExtras();
				int cmd = data.getInt("cmd");
				
				switch (cmd) 
				{
				case DvdControl.DVD_MCU_RET_CUR_TRACK:
					int temp = data.getByte("TotalChapterL");
					if(temp != mTotalTrack)
					{
						mTotalTrack = temp;
						InitListView();
					}
					short parmLen = data.getShort("paramlen");
					byte thour;
					byte tminute;
					byte tsecond;
					if (parmLen == 3) 
					{
						
					}
				    else if (parmLen > 3) 
				    {
						// 设置总时间，总曲目
						thour = data.getByte("TotalHour");
						tminute = data.getByte("TotalMin");
						tsecond = data.getByte("TotalSec");
						mMediaLength = thour * 3600 + tminute * 60 + tsecond;
						mTotalTime.setText(String.format("%02d:%02d:%02d", thour, tminute,
								tsecond));
						mTotalTrackNo.setText("" + mTotalTrack);

						// 设置当前曲目名称
						//mCurPlayTrack = data.getByte("DvdTitle");
						int tempCurTrack = data.getByte("DvdTitle");
						if(mCurPlayTrack != tempCurTrack)  //避免点击play某个track的时候，会选中当前后又选中前一个，再回到当前
						{
							mCurPlayTrack = tempCurTrack;
							mCurTrackTextView.setText("Track"
								+ mCurPlayTrack);
						    mCurTrackNo.setText("" + mCurPlayTrack);
							flistItemAdapter.SetSelectedItem(mCurPlayTrack - 1);
							flistItemAdapter.notifyDataSetChanged();
						}
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
				case DVDDealSet.CIRCLE_RANDOM:
					m_rdBtnRandom.setChecked(true);
					//m_rdBtnRepetClose.setChecked(true);
					break;
				case DVDDealSet.CIRCLE_NULL:
					m_rdBtnRepetClose.setChecked(true);
					//m_rdBtnsingle.setChecked(true);
					break;
				case DVDDealSet.CIRCLE_SINGLE:
					m_rdBtnsingle.setChecked(true);
					//m_rdBtnRandom.setChecked(true);
					break;
				case DVDDealSet.CIRCLE_LIST:
					m_rdBtnRepetList.setChecked(true);
					//m_rdBtnRepetList.setChecked(true);
					 break;
				default:
					break;
				}
			}
		}
		
	};
	
	//列表监听
		OnItemClickListener cDlistListener = new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) 
			{
				LogCat.Logd("DVDCDPlayerActivity cDlistListener  arg2 = " + arg2);
//				HashMap<String, String> map = (HashMap<String, String>) CDListView
//						.getItemAtPosition(arg2);
//				if (flistItemAdapter != null)
//				{
//					flistItemAdapter.SetSelectedItem(arg2);  //这里注释掉是避免没有play成功时显示与正常play的不一致
//				}
//					
//				String music_name = map.get("Media_name");
				
				Intent serviceIntent = new Intent(DVDCDPlayerActivity.this, 
						com.apical.dvdplayer.dvdcontrol.DVDService.class);
				Bundle bundle = new Bundle();
				bundle.putByte("cd_track_num", (byte)(arg2 + 1));
				bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.CD_TRACK);
				serviceIntent.putExtras(bundle);
		        startService(serviceIntent);
			}
		};
		
	//按钮点击响应
	private OnClickListener cdplayeract_OnClickListener = new OnClickListener() 
	{

		@Override
		public void onClick(View v) 
		{
			Intent serviceIntent = new Intent(DVDCDPlayerActivity.this, 
					com.apical.dvdplayer.dvdcontrol.DVDService.class);
			Bundle bundle = new Bundle();  
	        
			switch (v.getId()) 
			{
			case R.id.cdplayeract_sward:
				bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.BACKWARD);
				LogCat.Logd("dvdplayeract_OnClickListener  trgCmd = BACKWARD");
				break;
			case R.id.cdplayeract_pre:
				bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.PRE_ONE);
				LogCat.Logd("dvdplayeract_OnClickListener  trgCmd = PRE_ONE");
				break;
			case R.id.cdplayeract_playpause:
				bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.PLAY_PAUSE);
				LogCat.Logd("dvdplayeract_OnClickListener  trgCmd = PLAY_PAUSE");
				break;
			case R.id.cdplayeract_next:
				bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.NEXT_ONE);
				LogCat.Logd("dvdplayeract_OnClickListener  trgCmd = NEXT_ONE");
				break;
			case R.id.cdplayeract_fward:
				bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.FORWARD);
				LogCat.Logd("dvdplayeract_OnClickListener  trgCmd = FORWARD");
				break;
			case R.id.cdplayeract_stop:
				bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.STOP);
				LogCat.Logd("dvdplayeract_OnClickListener  trgCmd = STOP");
				break;
//			case R.id.dvdplayeract_setlight:
//				View setView = findViewById(R.id.bright_set_dvd);
//				if (setView.getVisibility() == View.VISIBLE) {
//					setView.setVisibility(View.INVISIBLE);
//				} else {
//					setView.setVisibility(View.VISIBLE);
//				}
//				bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.LIGHT_SET);
//				LogCat.Logd("dvdplayeract_OnClickListener  trgCmd = LIGHT_SET");
//				break;
			case R.id.cdplayeract_audio:
//				Intent i = new Intent();
//        		i.setClassName("com.apical.audiosettings",
//        				"com.apical.audiosettings.AudioActivity");
//        		startActivity(i);
				bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.AUDIO_SET);
				LogCat.Logd("dvdplayeract_OnClickListener  trgCmd = AUDIO_SET");
				break;
			case R.id.cdplayeract_soundchange:
				bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.SOUND_CHANGE);
				LogCat.Logd("dvdplayeract_OnClickListener  trgCmd = SOUND_CHANGE");
				break;
			case R.id.cdplayeract_circulation:
				bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.CIRCLE_TYP);
				LogCat.Logd("dvdplayeract_OnClickListener  trgCmd = SOUND_CHANGE");
				break;
			case R.id.cdplayeract_outdisc:
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
	
	public void InitListView()
	{
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();

		String cDNameString = "Track";
		for (int i = 0; i < mTotalTrack; i++) 
		{
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("Media_name", cDNameString + (i + 1));  //加1  从1开始显示
			listItem.add(map);
		}

		// 初始化列表
		flistItemAdapter = new FuncListAdapter(DVDCDPlayerActivity.this, listItem,
				R.drawable.cdlistview_itemcontent,
				new String[] { "Media_name" }, new int[] { R.id.Media_name });
		flistItemAdapter.SetItemSelPic(getResources().getDrawable(
				R.drawable.cdlistview_sel));
		CDListView.setOnItemClickListener(cDlistListener);
		CDListView.setAdapter(flistItemAdapter);
	}
}