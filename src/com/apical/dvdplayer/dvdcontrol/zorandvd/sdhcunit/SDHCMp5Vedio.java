package com.apical.dvdplayer.dvdcontrol.zorandvd.sdhcunit;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import com.apical.dvdplayer.LogCat;
import com.apical.dvdplayer.R;
import com.apical.dvdplayer.dvdcontrol.DVDService;
import com.apical.dvdplayer.dvdcontrol.SDHCEngineBase;
import com.apical.dvdplayer.dvdcontrol.zorandvd.ZoranDVD;
import com.apical.dvdplayer.dvdcontrol.zorandvd.dvdunit.DVDMp5List;
import com.apical.dvdplayer.dvdmodel.AppData;
import com.apical.dvdplayer.dvdmodel.DVDDealSet;
import com.apical.dvdplayer.dvdview.DVDMp5VideoActivity;
import com.apical.dvdplayer.dvdview.SDHCMp5VideoActivity;

import android.app.DvdControl;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class SDHCMp5Vedio extends SDHCEngineBase
{
	Context mContext = null;
	
	public byte mPlayState = 0;
	public byte mPlayType = 0;
	
	public byte mDetectCount = 0;
	public boolean mIsDetecting = false;
	
	AppData mdata = null;
	
	private static SDHCMp5Vedio sdhcMp5Vedio = null;
	private SDHCMp5Vedio()
	{
		mdata = AppData.Instance();
	}
	public static SDHCMp5Vedio Instance()
	{
		if(sdhcMp5Vedio == null)
		{
			sdhcMp5Vedio = new SDHCMp5Vedio();
		}
		
		return sdhcMp5Vedio;
	}
	/**/
	public void Show(Context context)
	{
		mContext = context;
		
		if(SDHCMp5VideoActivity.SDHCMp5VedioAct == null)
		{
			LogCat.Logd("SDHCMp5Video act = null");
			Intent mp5vedio = new Intent(context, 
					com.apical.dvdplayer.dvdview.SDHCMp5VideoActivity.class);
			mp5vedio.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mp5vedio.putExtra("setModeFlag", 1) ;
			context.startActivity(mp5vedio);
			
			startDetect(); //开始检测是否播放成功
		}
		else
		{
			LogCat.Logd("SDHCMp5Video act != null");
			Intent mp5vedio = new Intent(context, 
					com.apical.dvdplayer.dvdview.SDHCMp5VideoActivity.class);
			mp5vedio.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mp5vedio.putExtra("setModeFlag", 1) ;
			context.startActivity(mp5vedio);
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
        	switch(trgCmd)
        	{
        	case DVDDealSet.DVD_EXIT_APP:
        		ZoranDVD.dvdInstance.ChangeUnit(null);
        		Intent exit = new Intent();
    			exit.setAction(DVDDealSet.SDHC_ACTIVITY_EXIT);
    			mContext.sendBroadcast(exit);
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
        	case DVDDealSet.LIGHT_SET:
        		//activity里做了
        		break;
        	case DVDDealSet.NEXT_ONE:
        		ZoranDVD.dvdInstance.DVD_CMD.next();
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
        		ZoranDVD.dvdInstance.DVD_CMD.previous();
        		break;
        	case DVDDealSet.SOUND_CHANGE:
        		ZoranDVD.dvdInstance.DVD_CMD.soundChange();
        		break;
        	case DVDDealSet.STOP:
        		ZoranDVD.dvdInstance.DVD_CMD.stop();
        		break;
        	case DVDDealSet.KEY_BACK:
        		
        		ZoranDVD.dvdInstance.DVD_CMD.stop();
        		
        		//ZoranDVD.dvdInstance.ChangeUnit(ZoranDVD.dvdInstance.UNIT.DVDUnitCon.get(DVDUnit.DVDMP5LIST.value));
        		ZoranDVD.dvdInstance.ChangeUnit(DVDMp5List.Instance());
        		break;
        	}
        }
	}
	
	/**/
	public boolean CmdCompleted(int cmd, Bundle data)
	{
		LogCat.Logd("SDHCMp5Vedio CmdCompleted cmd = " + cmd);
		switch(cmd)
		{
		case DvdControl.SETUP_MCU_RET_MB:
			break;
		case DvdControl.DVD_MCU_RET_DISCTYPE:
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
			LogCat.Logd("DVDMp5List Mp5Info E1");
			break;
		case 0xF2: //文件总数
			int a = data.getShort("VideoNums");
			int b = data.getShort("AudioNums");
			int c = data.getShort("PhotoNums");
			
			LogCat.Logd("DVDMp5Music Mp5Info F2 " + a + " " + b + " " + c);
			break;
		case 0xF3:
			int nTrackSelAck = data.getShort("TrackIndex");
			LogCat.Logd("DVDMp5Video Mp5Info F3 nTrackSelAck = " + nTrackSelAck);
			if(nTrackSelAck == mdata.getCurMediaNum())
			{
				mPlayType = DVDDealSet.MP5_VIDEO;
			}
			break;
		case 0xF4:
			byte byteJump = data.getByte("JumpTo");
			LogCat.Logd("DVDMp5Video Mp5Info F4 byteJump = " + byteJump);
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
			LogCat.Logd("DVDMp5Vedio Mp5Info E1 len = " + fileLen + "MediaID = " + MediaID + "mediaType = " + mediaType + FileName);
			break;  
		case 0xE3: //艺术家
			LogCat.Logd("DVDMp5List Mp5Info E3");
			break;
		case 0xE4: //唱片
			LogCat.Logd("DVDMp5List Mp5Info E4");
			break;
		case 0xE6: //曲目
			LogCat.Logd("DVDMp5List Mp5Info E6");
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
			LogCat.Logd("DVDMp5Video Mp5Info EE " + type);
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
		mPlayType = 0;
		mDetectCount = 0;
		
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
			
			if(!mIsDetecting)
			{
				ZoranDVD.dvdInstance.DVD_CMD.stop();
				break;
			}
			
			if(mPlayType == DVDDealSet.MP5_VIDEO)
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
}