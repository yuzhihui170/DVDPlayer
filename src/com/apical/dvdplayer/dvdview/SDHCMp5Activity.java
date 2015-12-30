package com.apical.dvdplayer.dvdview;

import java.util.ArrayList;
import java.util.HashMap;

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
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class SDHCMp5Activity extends Activity
{
	public static Activity SDHCMp5Act = null;
	
	public static ListView DataListView;
	private ImageButton vedioButton;
	private ImageButton musicButton;
	private ImageButton pictureButton;
	private ImageButton outButton;
	
	private static ArrayList<HashMap<String, Object>> mListVideoItem; //视频数据列表
	private static ArrayList<HashMap<String, Object>> mListMusicItem; //音乐数据列表
	private static ArrayList<HashMap<String, Object>> mListPictureItem; //图片数据列表
	private SimpleAdapter mVedioListItemAdapter; //列表数据适配器
	private SimpleAdapter mMusicListItemAdapter; //列表数据适配器
	private SimpleAdapter mPictureListItemAdapter; //列表数据适配器
	
	public static int mTotalVedioCount=0; //视频文件总数
	public static int mTotalMusicCount=0; //音频文件总数
	public static int mTotalPictureCount=0; //图片文件总数
	
	AppData mdata = null;
	byte mCurMediaListType = DVDDealSet.MP5_MUSIC;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		SDHCMp5Act = this;
		setContentView(R.layout.sdhc_mp5list_activity);
		
		mdata = AppData.Instance();
		
		/*************************************************************/
		DataListView = (ListView)findViewById(R.id.DataDiscList);
		
		musicButton = (ImageButton)findViewById(R.id.datalist_btn_music);
        musicButton.setOnClickListener(new OnClickListener()
        {
            public void onClick(View view)
            {
                //Log.d(TAG, "musicButton.onClick(" + view + ")");
                if(mCurMediaListType != DVDDealSet.MP5_MUSIC)
                {
                	mCurMediaListType = DVDDealSet.MP5_MUSIC;
                	UpdateList(DVDDealSet.MP5_MUSIC);
                }
            }
        });
        
        //��Ƶ�б�
        vedioButton = (ImageButton)findViewById(R.id.datalist_btn_vedio);
        vedioButton.setOnClickListener(new OnClickListener()
        {
            public void onClick(View view)
            {
                //Log.d(TAG, "vedioButton.onClick(" + view + ")");
                if(mCurMediaListType != DVDDealSet.MP5_VIDEO)
                {
                	mCurMediaListType = DVDDealSet.MP5_VIDEO;
                	UpdateList(DVDDealSet.MP5_VIDEO);
                }
            }
        });      
        
        //ͼƬ�б�      
        pictureButton = (ImageButton)findViewById(R.id.datalist_btn_pictrue);
        pictureButton.setOnClickListener(new OnClickListener()
        {
            public void onClick(View view)
            {
                //Log.d(TAG, "pictureButton.onClick(" + view + ")");
            	if(mCurMediaListType != DVDDealSet.MP5_PICTURE)
                {
                	mCurMediaListType = DVDDealSet.MP5_PICTURE;
                	UpdateList(DVDDealSet.MP5_PICTURE);
                }
            }
        });  
        
//        //出仓按钮
//        outButton = (ImageButton)findViewById(R.id.datalist_btn_outdisc);
//	    outButton.setOnClickListener(new OnClickListener()
//	    {
//	        public void onClick(View view)
//	        {
//	        	ExitApp();
//	        }
//	    });  
		/****************************************************************/
		
		/***************************************************************/
		mListVideoItem = new ArrayList<HashMap<String, Object>>();  
		mListMusicItem = new ArrayList<HashMap<String, Object>>();  
		mListPictureItem = new ArrayList<HashMap<String, Object>>();  
		
		mVedioListItemAdapter= new SimpleAdapter(SDHCMp5Activity.this, mListVideoItem,  
				 R.layout.datalistview_item,
				 new String[] { "item_icon", "Index", "Media_name" },
				 new int[] { R.id.item_icon, R.id.Media_ID, R.id.Media_name });
				
		mMusicListItemAdapter= new SimpleAdapter(SDHCMp5Activity.this, mListMusicItem,  
				 R.layout.datalistview_item,
				 new String[] { "item_icon", "Index", "Media_name" },
				 new int[] { R.id.item_icon, R.id.Media_ID, R.id.Media_name });
		
		mPictureListItemAdapter= new SimpleAdapter(SDHCMp5Activity.this, mListPictureItem,  
				 R.layout.datalistview_item,
				 new String[] { "item_icon", "Index", "Media_name" },
				 new int[] { R.id.item_icon, R.id.Media_ID, R.id.Media_name });
		
		DataListView.setAdapter(mMusicListItemAdapter);
		
		DataListView.setOnItemClickListener(datalistListener);	 
		/************************************************************************/
		
		/*****************************************************************/
		IntentFilter filter = new IntentFilter("dvdplayer_dvd_event");
		filter.addAction(DVDDealSet.SDHC_ACTIVITY_EXIT);
		registerReceiver(mReceiver, filter);
		/******************************************************************/
		
		LogCat.Logd("SDHCMp5Activity onCreate");
	}

	@Override
	protected void onDestroy() 
	{
		SDHCMp5Act = null;
		
		unregisterReceiver(mReceiver);
		
		super.onDestroy();
		
		LogCat.Logd("SDHCMp5Activity onDestroy");
	}

	@Override
	protected void onPause() 
	{
		LogCat.Logd("SDHCMp5Activity onPause");
		
		super.onPause();
	}

	@Override
	protected void onResume() 
	{
		LogCat.Logd("SDHCMp5Activity onResume");
		
		super.onResume();
	}

	@Override
	protected void onStart() 
	{
		LogCat.Logd("SDHCMp5Activity onStart");
		
		super.onStart();
	}

	@Override
	protected void onStop() 
	{
		LogCat.Logd("SDHCMp5Activity onStop");
		
		super.onStop();
	}
	
	OnItemClickListener datalistListener = new OnItemClickListener()
	{
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3)
		{
			LogCat.Logd("SDHCMp5Activity OnItemClickListener arg2 = " + arg2);
			Intent serviceIntent = new Intent(SDHCMp5Activity.this, 
					com.apical.dvdplayer.dvdcontrol.DVDService.class);
			Bundle bundle = new Bundle();

//			HashMap<String,String> map=(HashMap<String,String>)DataListView.getItemAtPosition(arg2); 
//	        String media_name=map.get("Media_name");
//	        String media_id=map.get("Media_ID"); 
//
//	        int num_media_id = 0;
//	        num_media_id = Integer.parseInt(media_id);
//	        bundle.putInt("mp5_track_num", num_media_id);
			bundle.putInt("mp5_track_num", arg2);
	        bundle.putByte("mp5_track_type", mCurMediaListType);
			bundle.putByte(DVDDealSet.DVD_TRG_CMD, DVDDealSet.MP5_TRACK);
			serviceIntent.putExtras(bundle);
	        startService(serviceIntent);		            
		}
	};
	
	public void ExitApp()
	{
		finish();
		Intent serviceIntent = new Intent(SDHCMp5Activity.this, 
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
				LogCat.Logd("DVDMp5Activity->onReceive  dvdplayer_exit_app");
				SDHCMp5Activity.this.finish();
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
				case DvdControl.DVD_MCU_RET_PLAYSTATUS:
					break;
				case DvdControl.DVD_MCU_RET_SERVOVERSION:
					break;
				case DvdControl.DVD_MCU_RET_INFO:
					int dataInfoType = data.getByte("InfoID");
					dataInfoType &= 0x00FF;
					
					switch (dataInfoType)
					{
					case 0xF1: //播放结束
						break;
					case 0xF2: //获取数据碟文件总数
						mTotalVedioCount = data.getShort("VideoNums");
						mTotalMusicCount = data.getShort("AudioNums");
						mTotalPictureCount = data.getShort("PhotoNums");
						UpdateBtnState();
						break;
					case 0xE1: 
						byte  mediaType = data.getByte("InfoType");
						if(mediaType == 0)
						{
							UpdateMusicList();
						}
						else if(mediaType == 1)
						{
							UpdateVideoList();
						}
						else if(mediaType ==2)
						{
							UpdatePictureList();
						}
						break;
					case 0xF7:
						break;
					case 0xF3: //选曲命令的反馈
						break;
					}
					break;
				case DvdControl.DVD_MCU_RET_DISCERR:
					break;
				}
			}
			
		}
	};
	
	public void UpdateMusicList()
	{
		ArrayList<HashMap<String, Object>> tempListData;
		tempListData = mdata.getList(DVDDealSet.MP5_MUSIC);
		
		mListMusicItem.clear();
		LogCat.Logd("DVDMp5Acitivity UpdateMusicList tempListData.size() = " + tempListData.size());
		for(int i = 0; i < tempListData.size(); i ++)
		{
			mListMusicItem.add(tempListData.get(i));
		}
		mMusicListItemAdapter.notifyDataSetChanged();
	}
	public void UpdatePictureList()
	{
		ArrayList<HashMap<String, Object>> tempListData;
		tempListData = mdata.getList(DVDDealSet.MP5_PICTURE);
		
		mListPictureItem.clear();
		LogCat.Logd("DVDMp5Acitivity UpdatePictureList tempListData.size() = " + tempListData.size());
		for(int i = 0; i < tempListData.size(); i ++)
		{
			mListPictureItem.add(tempListData.get(i));
		}
		mPictureListItemAdapter.notifyDataSetChanged();
	}
	public void UpdateVideoList()
	{
		ArrayList<HashMap<String, Object>> tempListData;
		tempListData = mdata.getList(DVDDealSet.MP5_VIDEO);
		
		mListVideoItem.clear();
		LogCat.Logd("DVDMp5Acitivity UpdateVideoList tempListData.size() = " + tempListData.size());
		for(int i = 0; i < tempListData.size(); i ++)
		{
			mListVideoItem.add(tempListData.get(i));
		}
		mVedioListItemAdapter.notifyDataSetChanged();
	}
	
	public void UpdateList(byte listType)
	{
		switch(listType)
		{
		case DVDDealSet.MP5_MUSIC:
			DataListView.setAdapter(mMusicListItemAdapter);
			UpdateMusicList();
			break;
		case DVDDealSet.MP5_PICTURE:
			DataListView.setAdapter(mPictureListItemAdapter);
			UpdatePictureList();
			break;
		case DVDDealSet.MP5_VIDEO:
			DataListView.setAdapter(mVedioListItemAdapter);
			UpdateVideoList();
			break;
		default:
			break;
		}
	}
	
	public void UpdateBtnState()
	{
		if (mTotalVedioCount > 0)
		{
			vedioButton.setEnabled(true);
		}
		else
		{
			vedioButton.setEnabled(false);
		}
		
		if (mTotalMusicCount > 0)
		{
			musicButton.setEnabled(true);
		}
		else
		{
			musicButton.setEnabled(false);
		}		
	
		if (mTotalPictureCount > 0)
		{
			pictureButton.setEnabled(true);
		}
		else
		{
			pictureButton.setEnabled(false);
		}
	}
}