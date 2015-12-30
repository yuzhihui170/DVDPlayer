package com.apical.dvdplayer.dvdmodel;

public class DVDDealSet 
{
	
	//�û����������ؼ���
	public static final String DVD_TRG_CMD = "dvd_trg_cmd";  //key
	//�û������˳�ָ��
	public static final byte DVD_EXIT_APP = 0x00;            //exit
	//�����˳��㲥�����ڹر�activity
	public static final String DVD_ACTIVITY_EXIT = "dvd_exit_app";
	public static final String SDHC_ACTIVITY_EXIT = "sdhc_exit_app";
	//DVD������������activity��������Ĺ㲥
	public static final String DVDPLAYER_DVD_EVENT = "dvdplayer_dvd_event";
	//
	public static final String DVDPLAYER_CIRCLE_UPDATE = "dvdplayer_circle_update";
	//
	public static final String MEDIA_TITTLE_UPDATE = "media_tittle_update";
	
	/********************************DVD user cmd**************************/
	public static final byte TIME_OUT = 0x01;
	public static final byte BACKWARD = 0x02;
	public static final byte FORWARD = 0x03;
	public static final byte PRE_ONE = 0x04;
	public static final byte NEXT_ONE = 0x05;
	public static final byte PLAY_PAUSE = 0x06;
	public static final byte STOP = 0x07;
	public static final byte AUDIO_SET = 0x08;
	public static final byte LIGHT_SET = 0x09;
	public static final byte SOUND_CHANGE = 0x0a;
	public static final byte OUT_DISC = 0x0b;
	public static final byte TOUCH_SCREEN = 0x0c;
	public static final byte CD_TRACK = 0x0d;
	public static final byte CIRCLE_TYP = 0x0e;
	public static final byte LAUNCH_APP = 0x0f;
	public static final byte MP5_TRACK = 0x10;
	public static final byte KEY_BACK = 0x11;
	public static final byte KEY_HOME = 0x12;
	public static final byte LEFT_ROTATE = 0x13;
	public static final byte RIGHT_ROTATE = 0x14;
	public static final byte MAGNIFY = 0x15;
	
//	public static final byte  test = 1;
//	
//	//byte���Ѿ����ó�final�ˣ���������case�Ƿ��ʣ���֪��Ϊʲô,��һ��Int�Ͳ��Ժõģ���
//  //�Ļ������ֺ��ˣ�ɥ�Ĳ��� �����Ⱑ
//	public void test()
//	{
//		byte test1 = 0x01;
//		switch(test1)
//		{
//		case TIME_OUT/*test*/:
//			break;
//		}
//	}
	
	/************************��������****************************/
	public static final byte MB_UNK0 = 0;
	public static final byte MB_05DVD = 1;
	public static final byte MB_05HDVD = 2;
	public static final byte MB_XUGANG = 3;
			
	public static final byte SDHC = 0x03;
	public static final byte DVD = 0x01;
	public static final byte USB = 0x01;
	public static final byte IDLE = 0x10; //����ģʽ
	public static final byte APKFLAG_SDHC = 0;
	public static final byte APKFLAG_DVD = -89;
	public static final byte APKFLAG_USB = 0;
	
	/***************************ģʽ******************************/
    public static final Byte RADIO_MODE = 0x01;
    public static final Byte DVD_MODE = 0x02;
    public static final Byte USB_MODE = 0x06;
    public static final Byte IPOD_MODE = 0x07;
    public static final Byte SDHC_MODE = 0x0F;
	
    
    /********************************DVD����***********************************/
    public static final byte DVD_ZORAN = 0x01;
    public static final byte DVD_SUNPLUS = 0x02;
    
    /*********************************MP5��������***********************************/
    public static final byte MP5_MUSIC = 0x01;
    public static final byte MP5_VIDEO = 0x02;
    public static final byte MP5_PICTURE = 0x03;
    
    /***********************************ѭ������************************************/
    public static final int CIRCLE_RANDOM = 1;
    public static final int CIRCLE_NULL = 2;
    public static final int CIRCLE_SINGLE = 3;
    public static final int CIRCLE_LIST = 4;
    
    /***********************************DVD/SDHC************************************/
    public static final byte APP_DVD = 0x01;
    public static final byte APP_SDHC = 0x02;
    
    /**********************************ǰ̨/��̨*********************************/
    public static final byte APP_FOREG = 0x01;
    public static final byte APP_BACKG = 0x02;
    
    /******************************activity��Ӧ�Ľ���*****************************/
    public static final byte APP_LOADING = 0x01;
    public static final byte APP_MP5LIST = 0x02;
    public static final byte APP_MP5MUSIC = 0x03;
    public static final byte APP_MP5PICTURE = 0x04;
    public static final byte APP_MP5VIDEO = 0x05;
    public static final byte APP_CDPLAYER = 0x06;
    public static final byte APP_DVDPLAYER = 0x07;
}