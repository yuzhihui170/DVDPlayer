package com.apical.dvdplayer.dvdcontrol;

import java.util.ArrayList;
import java.util.List;

import com.apical.dvdplayer.LogCat;
import com.apical.dvdplayer.dvdcontrol.zorandvd.dvdunit.DVDCDPlayer;
import com.apical.dvdplayer.dvdcontrol.zorandvd.dvdunit.DVDDVDPlayer;
import com.apical.dvdplayer.dvdcontrol.zorandvd.dvdunit.DVDLoading;
import com.apical.dvdplayer.dvdcontrol.zorandvd.dvdunit.DVDMp5List;
import com.apical.dvdplayer.dvdcontrol.zorandvd.dvdunit.DVDMp5Music;
import com.apical.dvdplayer.dvdcontrol.zorandvd.dvdunit.DVDMp5Picture;
import com.apical.dvdplayer.dvdcontrol.zorandvd.dvdunit.DVDMp5Vedio;
import com.apical.dvdplayer.dvdcontrol.zorandvd.sdhcunit.SDHCLoading;
import com.apical.dvdplayer.dvdcontrol.zorandvd.sdhcunit.SDHCMp5List;
import com.apical.dvdplayer.dvdcontrol.zorandvd.sdhcunit.SDHCMp5Music;
import com.apical.dvdplayer.dvdcontrol.zorandvd.sdhcunit.SDHCMp5Picture;
import com.apical.dvdplayer.dvdcontrol.zorandvd.sdhcunit.SDHCMp5Vedio;

public class UnitContainer 
{
	public List<DVDEngineBase> DVDUnitCon = null; 
	public List<SDHCEngineBase> SDHCUnitCon = null; 
	
	public void InitContainer()
	{
		LogCat.Logd("UnitContainer->InitContainer");
		if(DVDUnitCon == null)
		{
			DVDUnitCon = new ArrayList<DVDEngineBase>();
		}
		else if(!DVDUnitCon.isEmpty())
		{
			DVDUnitCon.clear();
		}
		
		if(SDHCUnitCon == null)
		{
			SDHCUnitCon = new ArrayList<SDHCEngineBase>();
		}
		else if(!SDHCUnitCon.isEmpty())
		{
			SDHCUnitCon.clear();
		}
		

		for(int i = 0; i < DVDUnit.DVDUNIT_CNT.value; i ++)
		{
			LogCat.Logd("UnitContainer->InitContainer DVDUNIT i = " + i);
			//对DVDUnit.DVDCDPLAYER.value不能用switch/case，没研究为什么
			if(i == DVDUnit.DVDCDPLAYER.value)
			{
				DVDUnitCon.add(DVDCDPlayer.Instance());
			}
			else if(i == DVDUnit.DVDDVDPLAYER.value)
			{
				DVDUnitCon.add(DVDDVDPlayer.Instance());
			}
			else if(i == DVDUnit.DVDLOADING.value)
			{
				DVDUnitCon.add(DVDLoading.Inatance());
			}
			else if(i == DVDUnit.DVDMP5LIST.value)
			{
				DVDUnitCon.add(DVDMp5List.Instance());
			}
			else if(i == DVDUnit.DVDMP5MUSIC.value)
			{
				DVDUnitCon.add(DVDMp5Music.Insatnce());
			}
			else if(i == DVDUnit.DVDMP5PICTURE.value)
			{
				DVDUnitCon.add(DVDMp5Picture.Instance());
			}
			else if(i == DVDUnit.DVDMP5VEDIO.value)
			{
				DVDUnitCon.add(DVDMp5Vedio.Instance());
			}
			else
			{
				
			}
		}
		
		for(int i = 0; i < SDHCUnit.SDHCUNIT_CNT.value; i ++)
		{
			LogCat.Logd("UnitContainer->InitContainer SDHCUNIT i = " + i);
			//对SDHCUnit.SDHCLOADING.value不能用switch/case，没研究为什么
			if(i == SDHCUnit.SDHCLOADING.value)
			{
				SDHCUnitCon.add(SDHCLoading.Inatance());
			}
			else if(i == SDHCUnit.SDHCMP5LIST.value)
			{
				SDHCUnitCon.add(SDHCMp5List.Instance());
			}
			else if(i == SDHCUnit.SDHCMP5MUSIC.value)
			{
				SDHCUnitCon.add(SDHCMp5Music.Instance());
			}
			else if(i == SDHCUnit.SDHCMP5PICTURE.value)
			{
				SDHCUnitCon.add(SDHCMp5Picture.Instance());
			}
			else if(i == SDHCUnit.SDHCMP5VEDIO.value)
			{
				SDHCUnitCon.add(SDHCMp5Vedio.Instance());
			}
		}
	}
	
	
//	//dvd 
//	static int DVDLOADING = 0;
//	static int DVDCDPLAYER = 1;
//	static int DVDDVDPLAYER = 2;
//	static int DVDMP5LIST =3;
//	static int DVDMP5MUSIC = 4;
//	static int DVDMP5PICTURE = 5;
//	static int DVDMP5VEDIO = 6;
//	
//	//sdhc
//	static int SDHCLOADING = 0;
//	static int SDHCMP5LIST = 1;
//	static int SDHCMP5MUSIC = 2;
//	static int SDHCMP5PICTURE = 3;
//	static int SDHCMP5VEDIO = 4;
	public enum DVDUnit
	{
		DVDLOADING(0), 
		DVDCDPLAYER(1), 
		DVDDVDPLAYER(2), 
		DVDMP5LIST(3), 
		DVDMP5MUSIC(4), 
		DVDMP5PICTURE(5), 
		DVDMP5VEDIO(6),
		DVDUNIT_CNT(7); 
		public int value = 0;

	    private DVDUnit(int value)  //    必须是private的，否则编译错误
	    {   
	        this.value = value;
	    }
	}
	
	public enum SDHCUnit
	{
		SDHCLOADING(0), 
		SDHCMP5LIST(1), 
		SDHCMP5MUSIC(2), 
		SDHCMP5PICTURE(3), 
		SDHCMP5VEDIO(4),
		SDHCUNIT_CNT(5);
		
		public int value = 0;

	    private SDHCUnit(int value)  //    必须是private的，否则编译错误
	    {   
	        this.value = value;
	    }
	}
}