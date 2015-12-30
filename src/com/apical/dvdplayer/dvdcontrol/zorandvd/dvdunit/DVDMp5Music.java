package com.apical.dvdplayer.dvdcontrol.zorandvd.dvdunit;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

import com.apical.dvdplayer.LogCat;
import com.apical.dvdplayer.R;
import com.apical.dvdplayer.dvdcontrol.DVDEngineBase;
import com.apical.dvdplayer.dvdcontrol.DVDService;
import com.apical.dvdplayer.dvdcontrol.UnitContainer.DVDUnit;
import com.apical.dvdplayer.dvdcontrol.zorandvd.ZoranDVD;
import com.apical.dvdplayer.dvdmodel.AppData;
import com.apical.dvdplayer.dvdmodel.DVDDealSet;
import com.apical.dvdplayer.dvdview.DVDLoadingActivity;
import com.apical.dvdplayer.dvdview.DVDMp5MusicActivity;

import android.app.ActivityManager;
import android.app.DvdControl;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class DVDMp5Music extends DVDEngineBase
{
	
	Context mContext = null;
	
	public byte mPlayState = 0;
	public byte mPlayType = 0;
	
	//以下变量用来，防止连续点击下一曲之类的动作，出现多个线程一起检测
	public byte mDetectCount = 0;
	public boolean mIsDetecting = false;
	
	AppData mdata = null;
	
	private static DVDMp5Music dvdMp5Music = null;
	private DVDMp5Music()
	{
		mdata = AppData.Instance();
	}
	
	public static DVDMp5Music Insatnce()
	{
		if(dvdMp5Music == null)
		{
			dvdMp5Music = new DVDMp5Music();
		}
		
		return dvdMp5Music;
	}
	
	/**/
	public void Show(Context context)
	{
		LogCat.Logd("DVDMp5Music -- Show");
		
		mContext = context;
		
		if(DVDMp5MusicActivity.DVDMp5MusicAct == null)
		{
			LogCat.Logd("DVDMp5Music act = null");
			Intent mp5music = new Intent(context, 
					com.apical.dvdplayer.dvdview.DVDMp5MusicActivity.class);
			mp5music.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mp5music.putExtra("setModeFlag", 1) ;
			context.startActivity(mp5music);
			
			startDetect(); //开始检测是否播放成功
			mdata.setMusicCircleType(DVDDealSet.CIRCLE_LIST);
		}
		else
		{
			LogCat.Logd("DVDMp5Music act != null");
//			Intent mp5music = new Intent(context, 
//					com.apical.dvdplayer.dvdview.DVDMp5MusicActivity.class);
//			mp5music.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			mp5music.putExtra("setModeFlag", 1) ;
//			context.startActivity(mp5music);
			
			ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE); 
			List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
//			String TouchpackageName = TOUCH_PACKAGE_NAME;
//			if(tasksInfo.size() > 0)
//			{
//				if(TouchpackageName.equals(tasksInfo.get(0).topActivity.getPackageName())) //触摸校准界面
//				{
//					Log.d("DVD_Service", "code_follow222 : enterDVDPlayer -- tasksInfo.size() > 0");
//					return false;
//				}
//			}
			
			String packageName = "com.apical.dvdplayer";
			if(tasksInfo.size() > 0)
			{
				LogCat.Logd("DVDMp5Music tasksInfo.size() > 0");
				//DVD 未置顶
				if( !packageName.equals(tasksInfo.get(0).topActivity.getPackageName()) )
				{
					LogCat.Logd("DVDMp5Music not top");
					final ActivityManager am = (ActivityManager)
					           context.getSystemService(Context.ACTIVITY_SERVICE);
					am.moveTaskToFront(DVDMp5MusicActivity.DVDMp5MusicAct.getTaskId(), ActivityManager.MOVE_TASK_WITH_HOME);		
				}
			}
		}
		
	}
	
	/**/
	public void Exit(Context context)
	{
		if(mIsDetecting)
		{
			mIsDetecting = false;
		}
	}
	
	/*用户动作*/
	public void TrgCompleted(Intent intent, int startId)
	{
		Bundle bundle = new Bundle();
        bundle = intent.getExtras();
        if(bundle != null) 
        {
        	byte trgCmd = bundle.getByte(DVDDealSet.DVD_TRG_CMD);
        	
        	LogCat.Logd("DVDMp5Music  trgCmd = " + trgCmd);
        	switch(trgCmd)
        	{
        	case DVDDealSet.DVD_EXIT_APP:
        		ZoranDVD.dvdInstance.ChangeUnit(null);
        		ZoranDVD.dvdInstance.DVD_CMD.DVDPowerOff();
        		Intent exit = new Intent();
    			exit.setAction(DVDDealSet.DVD_ACTIVITY_EXIT);
    			mContext.sendBroadcast(exit);
        		break;
        	case DVDDealSet.KEY_BACK:
        		ZoranDVD.dvdInstance.DVD_CMD.stop();
        		//ZoranDVD.dvdInstance.ChangeUnit(ZoranDVD.dvdInstance.UNIT.DVDUnitCon.get(DVDUnit.DVDMP5LIST.value));
        		ZoranDVD.dvdInstance.ChangeUnit(DVDMp5List.Instance());
        		break;
        	case DVDDealSet.AUDIO_SET:
        		Intent i = new Intent();
        		i.setClassName("com.apical.audiosettings",
        				"com.apical.audiosettings.AudioActivity");
        		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  //activity外面启动activity，需要添加这个标志，不然会报错。
        		mContext.startActivity(i);
        		break;
        	case DVDDealSet.BACKWARD:
        		ZoranDVD.dvdInstance.DVD_CMD.prevQuick();
        		break;
        	case DVDDealSet.FORWARD:
        		ZoranDVD.dvdInstance.DVD_CMD.nextQuick();
        		break;
        	case DVDDealSet.NEXT_ONE:
        		//ZoranDVD.dvdInstance.DVD_CMD.next();
        		playMusic(true, true);
        		break;
        	case DVDDealSet.OUT_DISC:
        		//程序退出
        		break;
        	case DVDDealSet.PLAY_PAUSE:
        		ZoranDVD.dvdInstance.DVD_CMD.getPlayState();
        		
        		if (mPlayState == ZoranDVD.dvdInstance.DVD_CMD.GetDVDPlayStatePlay()) 
        		{
        			ZoranDVD.dvdInstance.DVD_CMD.pause();
				}
        		else
        		{
        			ZoranDVD.dvdInstance.DVD_CMD.play();
        		}
        		break;
        	case DVDDealSet.PRE_ONE:
        		//ZoranDVD.dvdInstance.DVD_CMD.previous();
        		playMusic(false, true);
        		break;
        	case DVDDealSet.SOUND_CHANGE:
        		ZoranDVD.dvdInstance.DVD_CMD.soundChange();
        		break;
        	case DVDDealSet.STOP:
        		ZoranDVD.dvdInstance.DVD_CMD.stop();
        		break;
        	case DVDDealSet.CD_TRACK:
        		byte trackNum = bundle.getByte("cd_track_num");
        		ZoranDVD.dvdInstance.DVD_CMD.PlayCDTrack(trackNum);
        		LogCat.Logd("DVDCDPlayer  CD_TRACK  trackNum = " + trackNum);
        		break;
        	case DVDDealSet.CIRCLE_TYP:
        		//ZoranDVD.dvdInstance.DVD_CMD.repet();
        		int type = mdata.getMusicCircletype();
//        		if(type == 0)  //
//        		{
//        			type = 4;
//        		}
        		type ++;
        		if(type > 4)
        		{
        			type = 1;
        		}
        		mdata.setMusicCircleType(type);
        		
        		Intent update_ct = new Intent(DVDDealSet.DVDPLAYER_CIRCLE_UPDATE);
        		Bundle cbundle = new Bundle();  
        		cbundle.putInt("circle_type", type);       
        		
        		update_ct.putExtras(cbundle);
    			mContext.sendBroadcast(update_ct);
        		break;
        	default:
        		break;
        	}
        }
        else
        {
        	LogCat.Logd("DVDMp5Music  bundle = null");
        }
	}
	
	/**/
	public boolean CmdCompleted(int cmd, Bundle data)
	{
		LogCat.Logd("DVDMp5Music CmdCompleted cmd = " + cmd);
		switch(cmd)
		{
		case DvdControl.SETUP_MCU_RET_MB:
			break;
		case DvdControl.DVD_MCU_RET_DISCTYPE:
			LogCat.Logd("DVDCDPlayer DISCTYPE = " + data.getByte("RepetMode"));
			break;
		case DvdControl.DVD_MCU_RET_CUR_TRACK:
			Intent cintent = new Intent(DVDDealSet.DVDPLAYER_DVD_EVENT);
	        cintent.putExtras(data);
	        mContext.sendBroadcast(cintent);
			break;
		case DvdControl.DVD_MCU_RET_PLAYSTATUS:
			mPlayState = data.getByte("PlayStatus");
			if (mPlayState == ZoranDVD.dvdInstance.DVD_CMD.GetDVDPlayStatePlay()) 
			{
				data.putInt("dvd_state", 1);
			} 
			else 
			{
				data.putInt("dvd_state", 2);
			}
			Intent sintent = new Intent(DVDDealSet.DVDPLAYER_DVD_EVENT);
	        sintent.putExtras(data);
	        mContext.sendBroadcast(sintent);
			break;
		case DvdControl.DVD_MCU_RET_INFO:
			Mp5Info(data);
			Intent iintent = new Intent(DVDDealSet.DVDPLAYER_DVD_EVENT);
			iintent.putExtras(data);
	        mContext.sendBroadcast(iintent);
			break;
		case DvdControl.DVD_MCU_RET_PLAYTIME: // 播放时间
			Intent tintent = new Intent(DVDDealSet.DVDPLAYER_DVD_EVENT);
	        tintent.putExtras(data);
	        mContext.sendBroadcast(tintent);
			break;
		case DvdControl.DVD_MCU_RET_DISCERR:
			break;
		}
		return true;
	}
	
	public void Mp5Info(Bundle data)
	{
		int dataInfoType = data.getByte("InfoID");
		dataInfoType &= 0x00FF;
		
		switch (dataInfoType)
		{
		case 0xF1: //播放完毕 
			LogCat.Logd("DVDMp5Music Mp5Info F1");
			playMusic(true, false);
			break;
		case 0xF2: //文件总数
			int a = data.getShort("VideoNums");
			int b = data.getShort("AudioNums");
			int c = data.getShort("PhotoNums");
			
			LogCat.Logd("DVDMp5Music Mp5Info F2 " + a + " " + b + " " + c);
			break;
		case 0xF3:
			int nTrackSelAck = data.getShort("TrackIndex");
			LogCat.Logd("DVDMp5Music Mp5Info F3 nTrackSelAck = " + nTrackSelAck);
			if(nTrackSelAck == mdata.getCurMediaNum())
			{
				mPlayType = DVDDealSet.MP5_MUSIC;
			}
			
			Intent tintent = new Intent(DVDDealSet.MEDIA_TITTLE_UPDATE);
	        mContext.sendBroadcast(tintent);
			break;
		case 0xE1: //文件信息
			short fileLen = data.getShort("paramlen"); //参数长度
			short MediaID = data.getShort("ContextID");//媒体编号
			byte  mediaType = data.getByte("InfoType"); //媒体文件类型
			
			byte [] fileInfo = new byte[fileLen-4];
			fileInfo = data.getByteArray("Info");
			String FileName="";
			try
			{
				FileName = new String(fileInfo, "unicode");
			} 
			catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
			
			if(mediaType == 0)
			{
				HashMap<String, Object> listItem = new HashMap<String, Object>();
		        listItem.put("item_icon", R.drawable.data_music_icon);
		        listItem.put("Media_ID", String.valueOf(MediaID));	        
		        listItem.put("Media_name", ". "+FileName);
		        listItem.put("Index", String.valueOf(mdata.getCount(DVDDealSet.MP5_MUSIC)+1));	
				mdata.addItem(listItem, DVDDealSet.MP5_MUSIC);
				
			}
			else if(mediaType == 1)
			{
				HashMap<String, Object> listItem = new HashMap<String, Object>();
		        listItem.put("item_icon", R.drawable.data_vedio_icon);
		        listItem.put("Media_ID", String.valueOf(MediaID));	        
		        listItem.put("Media_name", ". "+FileName);
		        listItem.put("Index", String.valueOf(mdata.getCount(DVDDealSet.MP5_VIDEO)+1));	
				mdata.addItem(listItem, DVDDealSet.MP5_VIDEO);
			}
			else if(mediaType ==2)
			{
				HashMap<String, Object> listItem = new HashMap<String, Object>();
		        listItem.put("item_icon", R.drawable.data_picture_icon);
		        listItem.put("Media_ID", String.valueOf(MediaID));	        
		        listItem.put("Media_name", ". "+FileName);
		        listItem.put("Index", String.valueOf(mdata.getCount(DVDDealSet.MP5_PICTURE)+1));	
				mdata.addItem(listItem, DVDDealSet.MP5_PICTURE);
			}
			LogCat.Logd("DVDMp5Music Mp5Info E1 len = " + fileLen + "MediaID = " + MediaID + "mediaType = " + mediaType + FileName);
			break;  
		case 0xE3: //艺术家
			LogCat.Logd("DVDMp5List Mp5Info E3");
//			Intent intent_artist = new Intent(DVDDealSet.DVDPLAYER_DVD_EVENT);
//			intent_artist.putExtras(data);
//	        mContext.sendBroadcast(intent_artist);
			break;
		case 0xE4: //唱片
			LogCat.Logd("DVDMp5List Mp5Info E4");
//			Intent intent_album = new Intent(DVDDealSet.DVDPLAYER_DVD_EVENT);
//			intent_album.putExtras(data);
//	        mContext.sendBroadcast(intent_album);
			break;
		case 0xE6: //曲目
			LogCat.Logd("DVDMp5List Mp5Info E6");
//			Intent intent_name = new Intent(DVDDealSet.DVDPLAYER_DVD_EVENT);
//			intent_name.putExtras(data);
//	        mContext.sendBroadcast(intent_name);
			break;
		case 0xE7: //流派
			LogCat.Logd("DVDMp5List Mp5Info E7");
			break;
		case 0xE8: //备注
			LogCat.Logd("DVDMp5List Mp5Info E8");
			break;
		case 0xE9: //标题
			LogCat.Logd("DVDMp5List Mp5Info E9");
			break;
		case 0xEa: //作者
			LogCat.Logd("DVDMp5List Mp5Info Ea");
			break;
		case 0xEb: //版权
			LogCat.Logd("DVDMp5List Mp5Info Eb");
			break;
		case 0xEc: //描述
			LogCat.Logd("DVDMp5List Mp5Info Ec");
			break;
		case 0xEE: //当前播放类型
			byte type = data.getByte("InfoType");
			LogCat.Logd("DVDMp5Music Mp5Info EE " + type);
//			if(type == 0x01) // video
//			{
//				mPlayType = DVDDealSet.MP5_VIDEO;
//			}
//			else if(type == 0x02) //music
//			{
//				mPlayType = DVDDealSet.MP5_MUSIC;
//			}
//			else if(type == 0x03) //picture
//			{
//				mPlayType = DVDDealSet.MP5_PICTURE;
//			}
		}
	}
	
	public void startDetect()
	{
		//开始检测时，先清空相关变量
		mPlayType = 0;
		mDetectCount = 0;
		
		//判断是否有线程在检测，有则返回，因为此时通过mdata访问的当前歌曲，是最后选择的歌曲
		//此时只需要保持一个线程在检测，更换一个目标重新检测就行了。
		//
		if(mIsDetecting)
		{
			return;
		}
		else
		{
			mIsDetecting = true;
		}
		new Thread(new Runnable()		
		{			
			@Override			
			public void run()			
			{			
				Detect();
			}		
		}).start();	
	}
	
	public synchronized void Detect()  //添加synckronized关键字修饰，不然会有object not locked by thread before wait()之类的报错
	{
		while(true)
		{
			try 
			{
				this.wait(1000);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
			
			//Music界面退出,这里加判断，防止Music界面退出后还在播放
			if(!mIsDetecting)
			{
				ZoranDVD.dvdInstance.DVD_CMD.stop();
				break;
			}
			
			if(mPlayType == DVDDealSet.MP5_MUSIC)
			{
				LogCat.Logd("DVDMp5Music Play success！！！");
				//播放成功
				break;
			}
			else
			{
				if(mDetectCount < 10)
				{
					ZoranDVD.dvdInstance.DVD_CMD.PlayDataDiscMedia(mdata.getCurMediaNum());
					LogCat.Logd("DVDMp5Music play nCount , MediaNum : " + mDetectCount + mdata.getCurMediaNum());
				}
				else
				{
					//提示播放失败
					break;
				}
			}
			
			mDetectCount ++;
		}
		
		mIsDetecting = false;
	}
	
	public void playMusic(boolean bNext, boolean bManual)
	{
		int item = mdata.getCurMediaItem();
		int count = mdata.getCount(DVDDealSet.MP5_MUSIC);
		
		switch(mdata.getMusicCircletype())
		{
		case DVDDealSet.CIRCLE_RANDOM:
			item = (int) (Math.random()*count);
			break;
		case DVDDealSet.CIRCLE_NULL:
			if(bNext)
			{
				item ++;
			}
			else
			{
				item --;
			}
			break;
		case DVDDealSet.CIRCLE_SINGLE:
			if(bManual)
			{
				if(bNext)
				{
					item ++;
				}
				else
				{
					item --;
				}
			}
			else 
			{
				//不变
			}
			break;
		case DVDDealSet.CIRCLE_LIST:
			if(bNext)
			{
				item ++;
			}
			else
			{
				item --;
			}
			break;
		default:
			if(bNext)
			{
				item ++;
			}
			else
			{
				item --;
			}
			break;
		}
		
		if(item < 0)
		{
			item = 0;
		}
		else if(item >= count)
		{
			item = count - 1;
		}
		
		HashMap<String,Object> map;
		map = mdata.getList(DVDDealSet.MP5_MUSIC).get(item);
        String media_name = (String)map.get("Media_name");
        String media_id = (String)map.get("Media_ID"); 
        int num = 0;
        num = Integer.parseInt(media_id);
        
        mdata.setCurMediaName(media_name);
        mdata.setCurMediaType(DVDDealSet.MP5_MUSIC);
        mdata.setCurMediaNum(num);
        mdata.setCurMediaItem(item);
        
        //放在这里更新的话，容易在更新后再收到前面歌曲的id3信息造成显示错误，
        //需要在收到trackack应答后再更新一下
        //因为trackack应答总是在前面id3信息后，在当前id3信息前发送过来
        //保留这里的，是为了让人觉得反应快
        Intent tintent = new Intent(DVDDealSet.MEDIA_TITTLE_UPDATE);
        mContext.sendBroadcast(tintent);
        
        ZoranDVD.dvdInstance.DVD_CMD.PlayDataDiscMedia(num);
        
        startDetect();
	}
}