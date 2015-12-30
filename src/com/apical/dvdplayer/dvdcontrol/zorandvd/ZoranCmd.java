package com.apical.dvdplayer.dvdcontrol.zorandvd;

import com.apical.dvdplayer.LogCat;
import com.apical.dvdplayer.dvdcontrol.DVDService;
import com.apical.dvdplayer.dvdmodel.DVDDealSet;

import android.app.DvdControl;
import android.app.DvdControl.OnCmdCompletedListener;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

public class ZoranCmd
{
	/**********************************************************************/
	//锟斤拷始锟斤拷锟斤拷片&锟斤拷锟狡碉拷片状态
    public static final byte INIT_DVD_DISC_PLAY = 0x01;
    public static final byte INIT_DVD_IDLE_MODE = 0x10;
    
	// 锟斤拷片状态
    public static final byte DVD_DISC_POWER_OFF = 0x08;         //锟较碉拷
    public static final byte DVD_DISC_ENTERING = 0x04; 		 //锟斤拷锟�
    public static final byte DVD_DISC_ENTER_NOT_READ = 0x05; //锟斤拷片锟窖撅拷锟斤拷位锟斤拷没锟叫匡拷始锟斤拷锟斤拷
    public static final byte DVD_DISC_BEGIN_READING = 0x06;  //锟斤拷始锟斤拷锟斤拷未锟斤拷锟斤拷锟斤拷
    public static final byte DVD_DISC_ALREADY_READ = 0x07;   //锟斤拷片锟窖撅拷锟斤拷锟斤拷
    public static final byte DVD_DISC_OUTING = 0x08;		 //锟斤拷锟斤拷
    public static final byte DVD_NOT_DISC = 0x09;			 //锟睫碉拷 
    public static final byte DVD_OUT_DISC_FINISH = 0x0B;			 //锟斤拷锟街碉拷位
    
    //遥锟斤拷指锟斤拷
    public static final byte IRKC_PLAY = (byte)0x12;
    public static final byte IRKC_PAUSE = (byte)0x13;
    public static final byte IRKC_STOP = (byte)0x14;
    public static final byte IRKC_PREV = (byte)0x16;
    public static final byte IRKC_NEXT = (byte)0x15;
    public static final byte IRKC_PREV_QUICK = (byte)0x1a;
    public static final byte IRKC_NEXT_QUICK = (byte)0x19;
    public static final byte IRKC_REPT = (byte)0x17;
    public static final byte IRKC_MENU =  0x23;
    public static final byte IRKC_SETUP =  0x52;
    public static final byte IRKC_SOUND =  0x28;
    public static final byte IRKC_SUBTITLE =  0x29;
    public static final byte IRKC_ZOOM =  0x3D;
   
    
    
    public static final byte IRKC_EJECT = 0x5A; 	//锟斤拷锟斤拷
    public static final byte IRKC_POWER_ON = 0x7E; 	//锟较碉拷
    public static final byte IRKC_POWER_OFF = 0x7F; //锟较碉拷
    public static final byte DVD_SOC_SET_BREAKPOINT = 0x73; 
    
    // 锟斤拷片锟斤拷锟斤拷
    public static final byte DISC_TYPE_DVD_VIDEO = 0x01;
    public static final byte DISC_TYPE_CDDA = 0x14;
    public static final byte DISC_TYPE_VCD = 0x13;
    public static final byte DISC_TYPE_DISC_DATA = 0x03;
    public static final byte DISC_TYPE_NULL = -1;
    
    // 锟斤拷锟斤拷状态
    public static final byte DVD_PLAY_STATE_STOP = 0x00;
    public static final byte DVD_PLAY_STATE_PLAY = 0x02;
    public static final byte DVD_PLAY_STATE_PAUSE = 0x08;    
    public static final byte DVD_PLAY_STATE_MENU = 0x18;
    public static final byte DVD_PLAY_STATE_NULL = 0;
    public static final byte DVD_PLAY_STATE_BACKWARD = 0x04;
    public static final byte DVD_PLAY_STATE_FORWARD = 0x06;
    public static final byte DVD_PLAY_STATE_IDLE = 0x0A;
    
    //循锟斤拷锟斤拷锟�
    public static final byte REPET_CLOSE = 0x02;		//循锟斤拷锟截憋拷
    public static final byte REPET_LIST = 0x03;			//锟叫憋拷循锟斤拷
    public static final byte REPET_SINGLE = 0x04;		//锟斤拷锟斤拷循锟斤拷
    public static final byte REPET_RADAM = 0x06;		//锟斤拷锟�
    
    //key 
    public static final byte  IR_UP  =  0x0d;      //鏂瑰悜锟�
    public static final byte  IR_LEFT = 0x0e;      //鏂瑰悜锟�
    public static final byte  IR_RIGHT = 0x0f;      //鏂瑰悜锟�
    public static final byte  IR_DOWN = 0x10;      //鏂瑰悜锟�
    public static final byte  IR_ENTER = 0x11;      //纭锟�
    /**********************************************************************/
    
    public static final byte ZERO = 0x00; 
    
    private DvdControl mDvdControl = null;
	
	/**/
	public void Init(OnCmdCompletedListener listener, Context context)
	{
		LogCat.Logd("DVDZoran->Init");
		mDvdControl = new DvdControl(context, "com.csr.dvd.DvdPlayerActivity");
		mDvdControl.setOnCmdCompletedListener(listener);
	}
	
	/**/
	public void DVDPowerOn()
	{
		LogCat.Logd("DVDZoran->DVDPowerOn");
		Bundle param = new Bundle();
        param.putByte("KeyCode", GetPowerOn());
        mDvdControl.sendCommand(DvdControl.DVD_SOC_KEY_CTRL, param);
	}
	
	/**/
	public void DVDPowerOff()
	{
		LogCat.Logd("DVDZoran->DVDPowerOff");
		Bundle param = new Bundle();
        param.putByte("KeyCode", GetPowerOff());
        mDvdControl.sendCommand(DvdControl.DVD_SOC_KEY_CTRL, param);
	}
	
	/*鑾峰彇涓绘澘绫诲瀷*/
	public void getMainBordType()
	{
		LogCat.Logd("DVDZoran->getMainBordType");
		Bundle param = new Bundle();
        param.putByte("null", ZERO);
    	mDvdControl.sendCommand(DvdControl.SETUP_SOC_GET_MB, param);
	}
	
	/*鑾峰彇纰熺墖绫诲瀷*/
	public void getDiscType()
	{
		LogCat.Logd("DVDZoran->getDiscType");
		Bundle param = new Bundle();
        param.putByte("null", ZERO);
        mDvdControl.sendCommand(DvdControl.DVD_SOC_GET_DISCTYPE, param);
	}
	
	/*鑾峰彇鎾斁鐘舵�*/
	public void getPlayState()
	{
		LogCat.Logd("DVDZoran->getPlayState");
		Bundle param = new Bundle();
        param.putByte("null", ZERO);
        mDvdControl.sendCommand(DvdControl.DVD_SOC_GET_PLAYSTATUS, param);
	}
	
	/*鑾峰彇纰熺墖浣嶇疆*/
	public void getDiscPos()
	{
		LogCat.Logd("DVDZoran->getDiscPos");
		Bundle param = new Bundle();
        param.putByte("null", ZERO);
        mDvdControl.sendCommand(DvdControl.DVD_SOC_GET_DISCPOS, param);
	}
	
	//璁剧疆纰熺墖鐘舵�锛屽垵濮嬪寲纰熺墖鍜岃缃挱鏀句綅缃�
    public void SetDiscState(byte playCtrl)
    {
    	LogCat.Logd("DVDZoran->SetDiscState");
    	Bundle param = new Bundle();
        param.putByte("Media", playCtrl);
        mDvdControl.sendCommand(DvdControl.DVD_SOC_SEL_MEDIA, param);
    }
    
   //鎾斁CD纰熸寚瀹氭洸鐩�
    public void PlayCDTrack(byte num)
    {
    	LogCat.Logd("DVDZoran->PlayCDTrack");
    	Bundle param = new Bundle();
        param.putByte("TrackH", (byte)0);
        param.putByte("TrackL", num);
        mDvdControl.sendCommand(DvdControl.DVD_SOC_PLAY_TRACK, param);
    }
    
    //鎾斁鏁版嵁纰熷唴瀹�
    public void PlayDataDiscMedia(int num)
    {
    	LogCat.Logd("DVDZoran->PlayDataDiscMedia");
    	byte idH = (byte)((num>>8)&0x00FF);
		byte idL = (byte)(num&0x00FF);
        Bundle param = new Bundle();
        param.putByte("TrackH", idH);
        param.putByte("TrackL", idL);
        
        mDvdControl.sendCommand(DvdControl.DVD_SOC_PLAY_TRACK, param);
    }
    
    //涓婁竴鏇�
    public void previous()
    {
    	LogCat.Logd("DVDZoran->previous");
    	Bundle param = new Bundle();
        param.putByte("KeyCode", GetDVDIRPrev());
        mDvdControl.sendCommand(DvdControl.DVD_SOC_KEY_CTRL, param);
    }
    
    //涓嬩竴鏇�
    public void next()
    {
    	LogCat.Logd("DVDZoran->next");
    	Bundle param = new Bundle();
        param.putByte("KeyCode", GetDVDIRNext());
        mDvdControl.sendCommand(DvdControl.DVD_SOC_KEY_CTRL, param);
    }
    
    /**/
    public void prevQuick()
    {
    	LogCat.Logd("DVDZoran->prevQuick");
    	Bundle param = new Bundle();
        param.putByte("KeyCode", GetDVDIRPrevQuick());
        mDvdControl.sendCommand(DvdControl.DVD_SOC_KEY_CTRL, param);
    }
    
    //
    public void nextQuick()
    {
    	LogCat.Logd("DVDZoran->nextQuick");
    	Bundle param = new Bundle();
        param.putByte("KeyCode", GetDVDIRNextQuick());
        mDvdControl.sendCommand(DvdControl.DVD_SOC_KEY_CTRL, param);
    }
    
    //寰幆鎺у埗
    public void repet()
    {
    	LogCat.Logd("DVDZoran->repet");
    	Bundle param = new Bundle();
        param.putByte("KeyCode", GetDVDIRRept());
        mDvdControl.sendCommand(DvdControl.DVD_SOC_KEY_CTRL, param);
    }
    
    public void play()
    {
    	LogCat.Logd("DVDZoran->play");
    	Bundle param = new Bundle();
        param.putByte("KeyCode", GetDVDIRPlay());
        mDvdControl.sendCommand(DvdControl.DVD_SOC_KEY_CTRL, param);
    }
    
    public void pause()
    {
    	LogCat.Logd("DVDZoran->pause");
    	Bundle param = new Bundle();
        param.putByte("KeyCode", GetDVDIRPause());
        mDvdControl.sendCommand(DvdControl.DVD_SOC_KEY_CTRL, param);
    }
    
    public void stop()
    {
    	LogCat.Logd("DVDZoran->stop");
    	Bundle param = new Bundle();
        param.putByte("KeyCode", GetDVDIRStop());
        mDvdControl.sendCommand(DvdControl.DVD_SOC_KEY_CTRL, param);
    }
    
    public void menu()
    {
    	LogCat.Logd("DVDZoran->menu");
    	Bundle param = new Bundle();
        param.putByte("KeyCode", GetDVDIRMenu());
        mDvdControl.sendCommand(DvdControl.DVD_SOC_KEY_CTRL, param);
    }
    
    public void eject()
    {
    	LogCat.Logd("DVDZoran->eject");
    	Bundle param = new Bundle();
        param.putByte("KeyCode", GetDVDIREject());
        mDvdControl.sendCommand(DvdControl.DVD_SOC_KEY_CTRL, param);
    }
    
    //澹伴亾鍒囨崲
    public void soundChange()
    {
    	LogCat.Logd("DVDZoran->soundChange");
    	Bundle param = new Bundle();
        param.putByte("KeyCode", GetDVDIRSound());
        mDvdControl.sendCommand(DvdControl.DVD_SOC_KEY_CTRL, param);
    }
    
    //瀛楀箷鍒囨崲
    public void TextChange()
    {
    	LogCat.Logd("DVDZoran->TextChange");
    	Bundle param = new Bundle();
        param.putByte("KeyCode", GetDVDIRSubtitles());
        mDvdControl.sendCommand(DvdControl.DVD_SOC_KEY_CTRL, param);
    }
    
    //
    public void rotateLeft()
    {
    	LogCat.Logd("DVDZoran->rotateLeft");
    	Bundle param = new Bundle();
        param.putByte("KeyCode", (byte)0x0e);
        mDvdControl.sendCommand(DvdControl.DVD_SOC_KEY_CTRL, param);
    }
    
    public void rotateRight()
    {
    	LogCat.Logd("DVDZoran->rotateRight");
    	Bundle param = new Bundle();
        param.putByte("KeyCode", (byte)0x0f);
        mDvdControl.sendCommand(DvdControl.DVD_SOC_KEY_CTRL, param);
    }
    
    public void rotate()
    {
    	LogCat.Logd("DVDZoran->rotate");
    	Bundle param = new Bundle();
        param.putByte("KeyCode", (byte)0x25);
        mDvdControl.sendCommand(DvdControl.DVD_SOC_KEY_CTRL, param);
    }
    
    //鏀惧ぇ
    public void zoom()
    {
    	LogCat.Logd("DVDZoran->zoom");
    	Bundle param = new Bundle();
        param.putByte("KeyCode", GetDVDIRZoom());
        mDvdControl.sendCommand(DvdControl.DVD_SOC_KEY_CTRL, param);
    }
    
    //瑙︽懜浣嶇疆
    public void touch(int x, int y)
    {
    	LogCat.Logd("DVDZoran->touch x= " + x + " y= "+ y);
    	byte xH = (byte)((x>>8)&0x00FF);
		byte xL = (byte)(x&0x00FF);
		byte yH = (byte)((y>>8)&0x00FF);
		byte yL = (byte)(y&0x00FF);
        Bundle param = new Bundle();
        param.putByte("XH", xH);
        param.putByte("XL", xL);
        param.putByte("YH", yH);
        param.putByte("YL", yL);
        mDvdControl.sendCommand(DvdControl.DVD_SOC_TOUCH, param);
    }
    
    //璺冲埌鎾斁鎸囧畾鎾斁鏃堕棿
    public void setPlayTime(int time)
    {
    	LogCat.Logd("DVDZoran->setPlayTime time = " + time);
    	byte hour = (byte)(time/3600);
        byte minute = (byte)((time%3600)/60);
        byte second = (byte)(time-hour*3600-minute*60);
        
        Bundle param = new Bundle();
        param.putByte("Hour", hour);
        param.putByte("Min", minute);
        param.putByte("Sec", second);
        mDvdControl.sendCommand(DvdControl.DVD_SOC_SET_PLAYTIME, param);
    }
    
    //鑾峰彇纰熺墖鏇茬洰鏁扮洰
    public void getTOC()
    {
    	LogCat.Logd("DVDZoran->getTOC");
    	Bundle param = new Bundle();
        param.putByte("null", ZERO);
        mDvdControl.sendCommand(DvdControl.DVD_SOC_GET_TOC, param);
    }
    
    //鑾峰彇褰撳墠鏂囦欢ID3淇℃伅
    public void getDataDiscCurID3()
    {
    	LogCat.Logd("DVDZoran->getDataDiscCurID3");
    	Bundle param = new Bundle();
        param.putByte("InfoType", (byte)0x00);
        mDvdControl.sendCommand(DvdControl.DVD_SOC_GET_INFO, param);
    }
    
    //鑾峰彇褰撳墠鏁版嵁纰熸枃浠跺悕
    public void getDataDiscCurFile()
    {
    	LogCat.Logd("DVDZoran->getDataDiscCurFile");
    	Bundle param = new Bundle();
        param.putByte("InfoType", (byte)0x01);
        mDvdControl.sendCommand(DvdControl.DVD_SOC_GET_INFO, param);
    }
    
    //鑾峰彇褰撳墠鏁版嵁纰熺洰褰�
    public void getDataDiscCurIndex()
    {
    	LogCat.Logd("DVDZoran->getDataDiscCurIndex");
    	Bundle param = new Bundle();
        param.putByte("InfoType", (byte)0x02);
        mDvdControl.sendCommand(DvdControl.DVD_SOC_GET_INFO, param);
    }
    
    //鑾峰彇鐩綍鍒楄〃
    public void getDataDiscIndexList()
    {
    	LogCat.Logd("DVDZoran->getDataDiscIndexList");
    	Bundle param = new Bundle();
        param.putByte("InfoType", (byte)0x04);
        mDvdControl.sendCommand(DvdControl.DVD_SOC_GET_INFO, param);
    }
    
    //鑾峰彇鏂囦欢鍒楄〃
    public void getDataDiscFileList()
    {
    	LogCat.Logd("DVDZoran->getDataDiscFileList");
    	Bundle param = new Bundle();
        param.putByte("InfoType", (byte)0x03);
        mDvdControl.sendCommand(DvdControl.DVD_SOC_GET_INFO, param);
    }
    
    //鑾峰彇纰熺墖淇℃伅
    public void getDiscInfo()
    {
    	LogCat.Logd("DVDZoran->getDiscInfo");
    	Bundle param = new Bundle();
        param.putByte("InfoType", (byte)0x05);
        mDvdControl.sendCommand(DvdControl.DVD_SOC_GET_INFO, param);
    }
    
    //閫夋嫨鏁版嵁纰熸寚瀹氭枃浠�
    public void setDataDiscPageMedia(byte cmd)
    {
    	LogCat.Logd("DVDZoran->setDataDiscPageMedia cmd = " + (byte)cmd);
    	Bundle param = new Bundle();
        param.putByte("Media", cmd);
        mDvdControl.sendCommand(DvdControl.DVD_SOC_SEL_MEDIA, param);
    }
    
    public void SetDVDMode()
    {
    	LogCat.Logd("DVDZoran->SetDVDMode");
    	Bundle param = new Bundle();
        param.putByte("WorkMode", DVDDealSet.DVD_MODE);
        mDvdControl.sendCommand(DvdControl.SETUP_SOC_SET_WORKMODE, param);
    }
    
    public void SetWorkMode(byte workMode, byte apkFlag)
    {
    	LogCat.Logd("DVDZoran->SetWorkMode");
    	Bundle param = new Bundle();
        param.putByte("WorkMode", workMode);
        param.putByte("ApkFlag", apkFlag);
        mDvdControl.sendCommand(DvdControl.SETUP_SOC_SET_WORKMODE, param);	
    }
    
    public void ReleaseWorkMode(byte workMode) 
    {
    	LogCat.Logd("DVDZoran->ReleaseWorkMode");
    	Bundle param = new Bundle();
        param.putByte("WorkMode", workMode);
		param.putByte("ApkFlag", DVDDealSet.APKFLAG_DVD);
        mDvdControl.sendCommand(DvdControl.SETUP_SOC_REL_WORKMODE, param);	
    }
    
    public void BackWorkMode(byte workMode)
    {
    	LogCat.Logd("DVDZoran->BackWorkMode");
    	Bundle param = new Bundle();
        param.putByte("WorkMode", workMode);
		param.putByte("IsBack", (byte) 1);
        mDvdControl.sendCommand(DvdControl.SETUP_MODE_BACKGROUND, param);
    }
    
    public void FrontWorkMode(byte workMode) 
    {
    	LogCat.Logd("DVDZoran->FrontWorkMode");
    	Bundle param = new Bundle();
        param.putByte("WorkMode", workMode);
		param.putByte("IsBack", (byte) 0);
        mDvdControl.sendCommand(DvdControl.SETUP_MODE_BACKGROUND, param);	
    }
    
    public void setBrightness(byte value)
    {
    	LogCat.Logd("DVDZoran->setBrightness");
    	Bundle bundle = new Bundle();
    	bundle.putByte("SetMode", (byte)0x02);
    	bundle.putByte("Lev", value);
    	mDvdControl.sendCommand(mDvdControl.SETUP_SOC_SET_BRIGHTNESS, bundle);
    }
    
    public void setContrast(byte value)
    {
    	LogCat.Logd("DVDZoran->setContrast");
    	Bundle bundle = new Bundle();
    	bundle.putByte("SetMode", (byte)0x02);
    	bundle.putByte("Lev", value);
    	mDvdControl.sendCommand(mDvdControl.SETUP_SOC_SET_CONTRAST, bundle);
    }
    
    public void setHue(byte value)
    {
    	LogCat.Logd("DVDZoran->setHue");
    	Bundle bundle = new Bundle();
    	bundle.putByte("SetMode", (byte)0x02);
    	bundle.putByte("Lev", value);
    	mDvdControl.sendCommand(mDvdControl.SETUP_SOC_SET_HUE, bundle);
    }
    
    public void updateRead()
    {
    	LogCat.Logd("DVDZoran->updateRead");
    	Bundle bundle = new Bundle();
    	bundle.putByte("Media", (byte)0x01);
        mDvdControl.sendCommand(DvdControl.DVD_SOC_SEL_MEDIA, bundle);
    }
    
    public void updateKey(byte value)
    {
    	LogCat.Logd("DVDZoran->updateKey");
    	Bundle bundle = new Bundle();
    	bundle.putByte("KeyCode", value);
        mDvdControl.sendCommand(DvdControl.DVD_SOC_KEY_CTRL, bundle);
    }
    
    public byte SetRemeber()
    {
    	LogCat.Logd("DVDZoran->SetRemeber");
    	Bundle bundle = new Bundle();
    	bundle.putByte("null", ZERO);
        mDvdControl.sendCommand(DvdControl.DVD_SOC_SET_BREAKPOINT, bundle);
		
		return ZERO;
    }
    
    //鑾峰彇浼烘湇瑙ｇ爜鏂规鍟嗕俊鎭�
  	public byte GetDVDInfo()
  	{
  		LogCat.Logd("DVDZoran->GetDVDInfo");
  		Bundle bundle = new Bundle();
    	bundle.putByte("null", ZERO);
        mDvdControl.sendCommand(DvdControl.DVD_SOC_GET_SERVOINFO, bundle);
		
		return ZERO;
  	}
  	
  	/*******************************IR鏁版嵁**********************************/
  	public byte GetPowerOn()
  	{
  		return IRKC_POWER_ON;
  	}
  	
  	public byte GetPowerOff()
  	{
  		return IRKC_POWER_OFF;
  	}
  	
  	public byte GetDVDIRPlay()
  	{
  		return IRKC_PLAY;
  	}
  	
  	public byte GetDVDIRPause()
  	{
  		return IRKC_PAUSE;
  	}
  	
  	public byte GetDVDIRStop()
  	{
  		return IRKC_STOP;
  	}
  	
  	public byte GetDVDIRPrevQuick()
  	{
  		return IRKC_PREV_QUICK;
  	}
  	
  	public byte GetDVDIRNextQuick()
  	{
  		return IRKC_NEXT_QUICK;
  	}
  	
  	public byte GetDVDIRPrev()
  	{
  		return IRKC_PREV;
  	}
  	
  	public byte GetDVDIRNext()
  	{
  		return IRKC_NEXT;
  	}
  	
  	public byte GetDVDIRRept()
  	{
  		return IRKC_REPT;
  	}
  	
  	public byte GetDVDIRMenu()
  	{
  		return IRKC_MENU;
  	}
  	
  	public byte GetDVDIREject()
  	{
  		return IRKC_EJECT;
  	}
  	
  	public byte GetDVDIRSetup()
  	{
  		return IRKC_SETUP;
  	}
  	
  	public byte GetDVDIRZoom()
  	{
  		return IRKC_ZOOM;
  	}
  	
  	public byte GetDVDIRSound()
  	{
  		return IRKC_SOUND;
  	}
  	
  	public byte GetDVDIRSubtitles()
  	{
  		return IRKC_SUBTITLE;
  	}
  	/*******************************IR鏁版嵁**********************************/
  	
  	
  	
  	/******************************鑾峰彇鎾斁鐘舵�鏁版嵁******************************/
  	public byte GetDVDPlayStatePlay()
  	{
  		return DVD_PLAY_STATE_PLAY;
  	}
  	
  	public byte GetDVDPlayStatePause()
  	{
  		return DVD_PLAY_STATE_PAUSE;
  	}
  	
  	public int GetDVDPlayStateStop()
  	{
  		return DVD_PLAY_STATE_STOP;
  	}
  	
  	public byte GetDVDPlayStateMenu()
  	{
  		return DVD_PLAY_STATE_MENU;
  	}
  	
  	public byte GetDVDPlayStateBackward()
  	{
  		return DVD_PLAY_STATE_BACKWARD;
  	}
  	
  	public byte GetDVDPlayStateForward()
  	{
  		return DVD_PLAY_STATE_FORWARD;
  	}
  	
  	public byte GetDVDPlayStateIdle()
  	{
  		return DVD_PLAY_STATE_IDLE;
  	}
  	/******************************鑾峰彇鎾斁鐘舵�鏁版嵁******************************/
  	
  	
  	
  	/********************************鑾峰彇纰熺墖绫诲瀷鏁版嵁******************************/
  	public byte GetDiscTypeCD()
  	{
  		return DISC_TYPE_CDDA;
  	}
  	
  	public byte GetDiscTypeDVD()
  	{
  		return DISC_TYPE_DVD_VIDEO;
  	}
  	
  	public byte GetDiscTypeVCD()
  	{
  		return DISC_TYPE_VCD;
  	}
  	
  	public byte GetDiscTypeDataDisc()
  	{
  		return DISC_TYPE_DISC_DATA;
  	}
  	
  	public byte GetDiscTypeNULL()
  	{
  		return DISC_TYPE_NULL;
  	}
  	/********************************鑾峰彇纰熺墖绫诲瀷鏁版嵁******************************/
  	
  	
  	/*****************************鑾峰彇纰熺墖杩涘叆鐘舵�鏁版嵁****************************/
  	public byte GetDiscPosPowerOff()
  	{
  		return DVD_DISC_POWER_OFF;
  	}
  	
  	public byte GetDiscPosEntering()
  	{
  		return DVD_DISC_ENTERING;
  	}
  	
  	public byte GetDiscPosNotRead()
  	{
  		return DVD_DISC_ENTER_NOT_READ;
  	}
  	
  	public byte GetDiscPosBeginRead()
  	{
  		return DVD_DISC_BEGIN_READING;
  	}
  	
  	public byte GetDiscPosBeginPlay()
  	{
  		return DVD_DISC_ALREADY_READ;
  	}
  	
  	public byte GetDiscPosDiscOut()
  	{
  		return DVD_DISC_OUTING;
  	}
  	
  	public byte GetDiscPosNotDisc()
  	{
  		return DVD_NOT_DISC;
  	}
  	
  	public byte GetDiscPosOutFinish()
  	{
  		return DVD_OUT_DISC_FINISH;
  	}
  	/*****************************鑾峰彇纰熺墖杩涘叆鐘舵�鏁版嵁****************************/
  	
  	
  	
  	/********************************寰幆鏍囪******************************/
  	public byte GetRepetClose()
  	{
  		return REPET_CLOSE;
  	}
  	
  	public byte GetRepetList()
  	{
  		return REPET_LIST;
  	}
  	
  	public byte GetRepetSingle()
  	{
  		return REPET_SINGLE;
  	}
  	
  	public byte GetRepetRadom()
  	{
  		return REPET_RADAM;
  	}
  	/********************************寰幆鏍囪******************************/
}