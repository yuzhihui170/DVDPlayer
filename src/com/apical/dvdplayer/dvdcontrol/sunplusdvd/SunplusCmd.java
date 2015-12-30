package com.apical.dvdplayer.dvdcontrol.sunplusdvd;

public class SunplusCmd
{
//	/**/
//	public void Init()
//	{
//		
//	}
	
	/**/
	public void DVDPowerOn()
	{
		
	}
	
	/**/
	public void DVDPowerOff()
	{
		
	}
	
	/*获取主板类型*/
	public void getMainBordType()
	{
		
	}
	
	/*获取碟片类型*/
	public void getDiscType()
	{
		
	}
	
	/*获取播放状态*/
	public void getPlayState()
	{
		
	}
	
	/*获取碟片位置*/
	public void getDiscPos()
	{
		
	}
	
	//设置碟片状态，初始化碟片和设置播放位置
    public void SetDiscState(byte playCtrl)
    {
    	
    }
    
   //播放CD碟指定曲目
    public void PlayCDTrack(byte num)
    {
    	
    }
    
    //播放数据碟内容
    public void PlayDataDiscMedia(int num)
    {
    	
    }
    
    //上一曲
    public void previous()
    {
    	
    }
    
    //下一曲
    public void next()
    {
    	
    }
    
    /**/
    public void prevQuick()
    {
    	
    }
    
    //
    public void nextQuick()
    {
    	
    }
    
    //循环控制
    public void repet()
    {
    	
    }
    
    public void play()
    {
    	
    }
    
    public void pause()
    {
    	
    }
    
    public void stop()
    {
    	
    }
    
    public void menu()
    {
    	
    }
    
    public void eject()
    {
    	
    }
    
    //声道切换
    public void soundChange()
    {
    	
    }
    
    //字幕切换
    public void TextChange()
    {
    	
    }
    
    //
    public void rotateLeft()
    {
    	
    }
    
    public void rotateRight()
    {
    	
    }
    
    public void rotate()
    {
    	
    }
    
    //放大
    public void zoom()
    {
    	
    }
    
    //触摸位置
    public void touch(int x, int y)
    {
    	
    }
    
    //跳到播放指定播放时间
    public void setPlayTime(int time)
    {
    	
    }
    
    //获取碟片曲目数目
    public void getTOC()
    {
    	
    }
    
    //获取当前文件ID3信息
    public void getDataDiscCurID3()
    {
    	
    }
    
    //获取当前数据碟文件名
    public void getDataDiscCurFile()
    {
    	
    }
    
    //获取当前数据碟目录
    public void getDataDiscCurIndex()
    {
    	
    }
    
    //获取目录列表
    public void getDataDiscIndexList()
    {
    	
    }
    
    //获取文件列表
    public void getDataDiscFileList()
    {
    	
    }
    
    //获取碟片信息
    public void getDiscInfo()
    {
    	
    }
    
    //选择数据碟指定文件
    public void setDataDiscPageMedia(byte cmd)
    {
    	
    }
    
    public void SetDVDMode()
    {
    	
    }
    
    public void SetWorkMode(byte workMode, byte apkFlag)
    {
    	
    }
    
    public void ReleaseWorkMode(byte workMode) 
    {
    	
    }
    
    public void BackWorkMode(byte workMode)
    {
    	
    }
    
    public void FrontWorkMode(byte workMode) 
    {
    	
    }
    
    public void setBrightness(byte value)
    {
    	
    }
    
    public void setContrast(byte value)
    {
    	
    }
    
    public void setHue(byte value)
    {
    	
    }
    
    public void updateRead()
    {
    	
    }
    
    public void updateKey(byte value)
    {
    	
    }
    
    public byte SetRemeber()
    {
    	return 0;
    }
    
    //获取伺服解码方案商信息
  	public byte GetDVDInfo()
  	{
  		return 0;
  	}
  	
  	/*******************************IR数据**********************************/
  	public byte GetPowerOn()
  	{
  		return 0;
  	}
  	
  	public byte GetPowerOff()
  	{
  		return 0;
  	}
  	
  	public byte GetDVDIRPlay()
  	{
  		return 0;
  	}
  	
  	public byte GetDVDIRPause()
  	{
  		return 0;
  	}
  	
  	public byte GetDVDIRStop()
  	{
  		return 0;
  	}
  	
  	public byte GetDVDIRPrevQuick()
  	{
  		return 0;
  	}
  	
  	public byte GetDVDIRNextQuick()
  	{
  		return 0;
  	}
  	
  	public byte GetDVDIRPrev()
  	{
  		return 0;
  	}
  	
  	public byte GetDVDIRNext()
  	{
  		return 0;
  	}
  	
  	public byte GetDVDIRRept()
  	{
  		return 0;
  	}
  	
  	public byte GetDVDIRMenu()
  	{
  		return 0;
  	}
  	
  	public byte GetDVDIREject()
  	{
  		return 0;
  	}
  	
  	public byte GetDVDIRSetup()
  	{
  		return 0;
  	}
  	
  	public byte GetDVDIRZoom()
  	{
  		return 0;
  	}
  	
  	public byte GetDVDIRSound()
  	{
  		return 0;
  	}
  	
  	public byte GetDVDIRSubtitles()
  	{
  		return 0;
  	}
  	/*******************************IR数据**********************************/
  	
  	
  	
  	/******************************获取播放状态数据******************************/
  	public byte GetDVDPlayStatePlay()
  	{
  		return 0;
  	}
  	
  	public byte GetDVDPlayStatePause()
  	{
  		return 0;
  	}
  	
  	public int GetDVDPlayStateStop()
  	{
  		return 0;
  	}
  	
  	public byte GetDVDPlayStateMenu()
  	{
  		return 0;
  	}
  	
  	public byte GetDVDPlayStateBackward()
  	{
  		return 0;
  	}
  	
  	public byte GetDVDPlayStateForward()
  	{
  		return 0;
  	}
  	
  	public byte GetDVDPlayStateIdle()
  	{
  		return 0;
  	}
  	/******************************获取播放状态数据******************************/
  	
  	
  	
  	/********************************获取碟片类型数据******************************/
  	public byte GetDiscTypeCD()
  	{
  		return 0;
  	}
  	
  	public byte GetDiscTypeDVD()
  	{
  		return 0;
  	}
  	
  	public byte GetDiscTypeVCD()
  	{
  		return 0;
  	}
  	
  	public byte GetDiscTypeDataDisc()
  	{
  		return 0;
  	}
  	
  	public byte GetDiscTypeNULL()
  	{
  		return 0;
  	}
  	/********************************获取碟片类型数据******************************/
  	
  	
  	/*****************************获取碟片进入状态数据****************************/
  	public byte GetDiscPosPowerOff()
  	{
  		return 0;
  	}
  	
  	public byte GetDiscPosEntering()
  	{
  		return 0;
  	}
  	
  	public byte GetDiscPosNotRead()
  	{
  		return 0;
  	}
  	
  	public byte GetDiscPosBeginRead()
  	{
  		return 0;
  	}
  	
  	public byte GetDiscPosBeginPlay()
  	{
  		return 0;
  	}
  	
  	public byte GetDiscPosDiscOut()
  	{
  		return 0;
  	}
  	
  	public byte GetDiscPosNotDisc()
  	{
  		return 0;
  	}
  	
  	public byte GetDiscPosOutFinish()
  	{
  		return 0;
  	}
  	/*****************************获取碟片进入状态数据****************************/
  	
  	
  	
  	/********************************循环标记******************************/
  	public byte GetRepetClose()
  	{
  		return 0;
  	}
  	
  	public byte GetRepetList()
  	{
  		return 0;
  	}
  	
  	public byte GetRepetSingle()
  	{
  		return 0;
  	}
  	
  	public byte GetRepetRadom()
  	{
  		return 0;
  	}
  	/********************************循环标记******************************/
}