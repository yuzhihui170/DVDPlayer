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
	
	/*��ȡ��������*/
	public void getMainBordType()
	{
		
	}
	
	/*��ȡ��Ƭ����*/
	public void getDiscType()
	{
		
	}
	
	/*��ȡ����״̬*/
	public void getPlayState()
	{
		
	}
	
	/*��ȡ��Ƭλ��*/
	public void getDiscPos()
	{
		
	}
	
	//���õ�Ƭ״̬����ʼ����Ƭ�����ò���λ��
    public void SetDiscState(byte playCtrl)
    {
    	
    }
    
   //����CD��ָ����Ŀ
    public void PlayCDTrack(byte num)
    {
    	
    }
    
    //�������ݵ�����
    public void PlayDataDiscMedia(int num)
    {
    	
    }
    
    //��һ��
    public void previous()
    {
    	
    }
    
    //��һ��
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
    
    //ѭ������
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
    
    //�����л�
    public void soundChange()
    {
    	
    }
    
    //��Ļ�л�
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
    
    //�Ŵ�
    public void zoom()
    {
    	
    }
    
    //����λ��
    public void touch(int x, int y)
    {
    	
    }
    
    //��������ָ������ʱ��
    public void setPlayTime(int time)
    {
    	
    }
    
    //��ȡ��Ƭ��Ŀ��Ŀ
    public void getTOC()
    {
    	
    }
    
    //��ȡ��ǰ�ļ�ID3��Ϣ
    public void getDataDiscCurID3()
    {
    	
    }
    
    //��ȡ��ǰ���ݵ��ļ���
    public void getDataDiscCurFile()
    {
    	
    }
    
    //��ȡ��ǰ���ݵ�Ŀ¼
    public void getDataDiscCurIndex()
    {
    	
    }
    
    //��ȡĿ¼�б�
    public void getDataDiscIndexList()
    {
    	
    }
    
    //��ȡ�ļ��б�
    public void getDataDiscFileList()
    {
    	
    }
    
    //��ȡ��Ƭ��Ϣ
    public void getDiscInfo()
    {
    	
    }
    
    //ѡ�����ݵ�ָ���ļ�
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
    
    //��ȡ�ŷ����뷽������Ϣ
  	public byte GetDVDInfo()
  	{
  		return 0;
  	}
  	
  	/*******************************IR����**********************************/
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
  	/*******************************IR����**********************************/
  	
  	
  	
  	/******************************��ȡ����״̬����******************************/
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
  	/******************************��ȡ����״̬����******************************/
  	
  	
  	
  	/********************************��ȡ��Ƭ��������******************************/
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
  	/********************************��ȡ��Ƭ��������******************************/
  	
  	
  	/*****************************��ȡ��Ƭ����״̬����****************************/
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
  	/*****************************��ȡ��Ƭ����״̬����****************************/
  	
  	
  	
  	/********************************ѭ�����******************************/
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
  	/********************************ѭ�����******************************/
}