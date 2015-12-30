package com.apical.dvdplayer.dvdcontrol;

import com.apical.dvdplayer.dvdcontrol.sunplusdvd.SunplusDVD;
import com.apical.dvdplayer.dvdcontrol.zorandvd.ZoranDVD;
import com.apical.dvdplayer.dvdmodel.DVDDealSet;

public class DVDFactory
{
//	public enum DVDType
//	{
//		DVD_ZORAN,
//		DVD_SUNPLUS;
//	}
	
	public static AbstractDVD CreateObject(byte type)
	{
		switch(type)
		{
		case DVDDealSet.DVD_ZORAN:
			return new ZoranDVD();
		case DVDDealSet.DVD_SUNPLUS:
			return new SunplusDVD();
		default:
			break;
		}
		return null;
	}
}