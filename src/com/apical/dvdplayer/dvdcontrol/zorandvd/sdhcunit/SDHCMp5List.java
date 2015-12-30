package com.apical.dvdplayer.dvdcontrol.zorandvd.sdhcunit;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import com.apical.dvdplayer.LogCat;
import com.apical.dvdplayer.R;
import com.apical.dvdplayer.dvdcontrol.SDHCEngineBase;
import com.apical.dvdplayer.dvdcontrol.zorandvd.ZoranDVD;
import com.apical.dvdplayer.dvdmodel.AppData;
import com.apical.dvdplayer.dvdmodel.DVDDealSet;
import com.apical.dvdplayer.dvdview.SDHCMp5Activity;

import android.app.DvdControl;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class SDHCMp5List extends SDHCEngineBase
{
	Context mContext = null;
	
	public int nTrackSelAck = 0;
	public int nTrackSelNum = 0;
	public byte nTrackSelType = 0;
	
	AppData mdata = null;
	
	private static SDHCMp5List sdhcMp5List = null;
	private SDHCMp5List()
	{
		mdata = AppData.Instance();
	}
	public static SDHCMp5List Instance()
	{
		if(sdhcMp5List == null)
		{
			sdhcMp5List = new SDHCMp5List();
		}
		
		return sdhcMp5List;
	}
	/**/
	public void Show(Context context)
	{
		mContext = context;
		
		if(SDHCMp5Activity.SDHCMp5Act == null)
		{
			LogCat.Logd("SDHCMp5List act = null");
			Intent mp5list = new Intent(context, 
					com.apical.dvdplayer.dvdview.SDHCMp5Activity.class);
			mp5list.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mp5list.putExtra("setModeFlag", 1) ;
			context.startActivity(mp5list);
		}
		else 
		{
			LogCat.Logd("SDHCMp5List act != null");
			Intent mp5list = new Intent(context, 
					com.apical.dvdplayer.dvdview.SDHCMp5Activity.class);
			mp5list.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mp5list.putExtra("setModeFlag", 1) ;
			context.startActivity(mp5list);
		}
	}
	
	/**/
	public void Exit(Context context)
	{
		
	}
	
	/*�û�����*/
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
        		
        		ZoranDVD.dvdInstance.DVD_CMD.DVDPowerOff();
        		Intent exit = new Intent();
    			exit.setAction(DVDDealSet.DVD_ACTIVITY_EXIT);
    			mContext.sendBroadcast(exit);
        		break;
            case DVDDealSet.MP5_TRACK:
        		
        		int trackNum = bundle.getInt("mp5_track_num");
        		nTrackSelType = bundle.getByte("mp5_track_type");
        		
        		HashMap<String,Object> map;
        		map = mdata.getList(nTrackSelType).get(trackNum);
    	        String media_name = (String)map.get("Media_name");
    	        String media_id = (String)map.get("Media_ID"); 
    	        int num_media_id = 0;
    	        num_media_id = Integer.parseInt(media_id);
    	        
    	        mdata.setCurMediaName(media_name);
    	        mdata.setCurMediaType(nTrackSelType);
    	        mdata.setCurMediaNum(num_media_id);
    	        mdata.setCurMediaItem(trackNum);
        		
        		LogCat.Logd("trackNum , nTrackSelType : " + num_media_id + " , " + nTrackSelType);
        		
        		//PlayTrack(trackNum);
        		if (nTrackSelType == DVDDealSet.MP5_MUSIC/*x02*/)
    			{
    	        	//ZoranDVD.dvdInstance.ChangeUnit(ZoranDVD.dvdInstance.UNIT.DVDUnitCon.get(DVDUnit.DVDMP5MUSIC.value));
        			ZoranDVD.dvdInstance.ChangeUnit(SDHCMp5Music.Instance());
    			}
    	        else if (nTrackSelType == DVDDealSet.MP5_PICTURE/*0x03*/)
    			{
    	        	//ZoranDVD.dvdInstance.ChangeUnit(ZoranDVD.dvdInstance.UNIT.DVDUnitCon.get(DVDUnit.DVDMP5PICTURE.value));
    	        	ZoranDVD.dvdInstance.ChangeUnit(SDHCMp5Picture.Instance());
    			}
    	        else if (nTrackSelType == DVDDealSet.MP5_VIDEO/*0x01*/)
    			{
    	        	//ZoranDVD.dvdInstance.ChangeUnit(ZoranDVD.dvdInstance.UNIT.DVDUnitCon.get(DVDUnit.DVDMP5VEDIO.value));
    	        	ZoranDVD.dvdInstance.ChangeUnit(SDHCMp5Vedio.Instance());
    			}
        		
        		ZoranDVD.dvdInstance.DVD_CMD.PlayDataDiscMedia(num_media_id);
        		break;
        	}
        }
	}
	
	/**/
	public boolean CmdCompleted(int cmd, Bundle data)
	{
		LogCat.Logd("DVDMp5List CmdCompleted cmd = " + cmd);
		switch(cmd)
		{
		case DvdControl.SETUP_MCU_RET_MB:
			break;
		case DvdControl.DVD_MCU_RET_DISCTYPE:
			break;
		case DvdControl.DVD_MCU_RET_CUR_TRACK:
//			Intent cintent = new Intent(DVDDealSet.DVDPLAYER_DVD_EVENT);
//	        cintent.putExtras(data);
//	        mContext.sendBroadcast(cintent);
			break;
		case DvdControl.DVD_MCU_RET_PLAYSTATUS:
//			Intent sintent = new Intent(DVDDealSet.DVDPLAYER_DVD_EVENT);
//	        sintent.putExtras(data);
//	        mContext.sendBroadcast(sintent);
			break;
		case DvdControl.DVD_MCU_RET_INFO:
			Mp5Info(data);
			Intent cintent = new Intent(DVDDealSet.DVDPLAYER_DVD_EVENT);
	        cintent.putExtras(data);
	        mContext.sendBroadcast(cintent);
			break;
		case DvdControl.DVD_MCU_RET_PLAYTIME: // ����ʱ��
//			Intent tintent = new Intent(DVDDealSet.DVDPLAYER_DVD_EVENT);
//	        tintent.putExtras(data);
//	        mContext.sendBroadcast(tintent);
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
		case 0xF1: //������� 
			LogCat.Logd("DVDMp5List Mp5Info E1");
			break;
		case 0xF2: //�ļ�����
			int a = data.getShort("VideoNums");
			int b = data.getShort("AudioNums");
			int c = data.getShort("PhotoNums");
			
			LogCat.Logd("DVDMp5List Mp5Info F2 " + a + " " + b + " " + c);
			break;
		case 0xF3:
			nTrackSelAck = data.getShort("TrackIndex");
			LogCat.Logd("DVDMp5List Mp5Info F3 nTrackSelAck = " + nTrackSelAck);
			break;
		case 0xE1: //�ļ���Ϣ
			short fileLen = data.getShort("paramlen"); //��������
			short MediaID = data.getShort("ContextID");//ý����
			byte  mediaType = data.getByte("InfoType"); //ý���ļ�����
			
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
			LogCat.Logd("DVDMp5List Mp5Info E1 len = " + fileLen + "MediaID = " + MediaID + "mediaType = " + mediaType + FileName);
			break;  
		case 0xE3: //������
			LogCat.Logd("DVDMp5List Mp5Info E3");
//			Intent intent_artist = new Intent(DVDDealSet.DVDPLAYER_DVD_EVENT);
//			intent_artist.putExtras(data);
//	        mContext.sendBroadcast(intent_artist);
			break;
		case 0xE4: //��Ƭ
			LogCat.Logd("DVDMp5List Mp5Info E4");
//			Intent intent_album = new Intent(DVDDealSet.DVDPLAYER_DVD_EVENT);
//			intent_album.putExtras(data);
//	        mContext.sendBroadcast(intent_album);
			break;
		case 0xE6: //��Ŀ
			LogCat.Logd("DVDMp5List Mp5Info E6");
//			Intent intent_name = new Intent(DVDDealSet.DVDPLAYER_DVD_EVENT);
//			intent_name.putExtras(data);
//	        mContext.sendBroadcast(intent_name);
			break;
		case 0xE7: //����
			LogCat.Logd("DVDMp5List Mp5Info E7");
			break;
		case 0xE8: //��ע
			LogCat.Logd("DVDMp5List Mp5Info E8");
			break;
		case 0xE9: //����
			LogCat.Logd("DVDMp5List Mp5Info E9");
			break;
		case 0xEa: //����
			LogCat.Logd("DVDMp5List Mp5Info Ea");
			break;
		case 0xEb: //��Ȩ
			LogCat.Logd("DVDMp5List Mp5Info Eb");
			break;
		case 0xEc: //����
			LogCat.Logd("DVDMp5List Mp5Info Ec");
			break;
		case 0xEE: //��ǰ��������
			byte type = data.getByte("InfoType");
			
			LogCat.Logd("DVDMp5List Mp5Info EE " + type);
			break;
		}
	}
}