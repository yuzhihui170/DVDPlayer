package com.apical.dvdplayer.dvdmodel;

import java.util.ArrayList;
import java.util.HashMap;

import com.apical.dvdplayer.LogCat;

public class AppData
{
	private static ArrayList<HashMap<String, Object>> mListVideo = null; //视频数据列表
	private static ArrayList<HashMap<String, Object>> mListMusic = null; //音乐数据列表
	private static ArrayList<HashMap<String, Object>> mListPicture = null; //图片数据列表
	
	private static String mCurMediaName = null;
	private static byte mCurMediaType = 0;
	private static int mCurMediaNum = 0;
	private static int mCurMediaItem = 0;
	
	private static int mMusicCircleType = 0;
	
	//程序没设计好，不然这两个变量相关的条件判断可以不要
	//后续优化
	private static byte mCurAppType = 0;  //dvd/sdhc
	private static byte mCurAppState = 0;  //foreground/background
	
	private static AppData data = null;
	private AppData()  //单例模式，不许实例化
	{
		mListVideo = new ArrayList<HashMap<String, Object>>();
		mListMusic = new ArrayList<HashMap<String, Object>>();
		mListPicture = new ArrayList<HashMap<String, Object>>();
	}
	
	public static AppData Instance()
	{
		if(data == null)
		{
			data = new AppData();
		}
		
		return data;
	}
	
	public void addItem(HashMap<String, Object> listItem, byte itemType)
	{
		LogCat.Logd("Mp5Data addItme " + itemType);
		switch(itemType)
		{
		case DVDDealSet.MP5_MUSIC:
			LogCat.Logd("Mp5Data addItem music");
			if(mListMusic != null)
			{
				mListMusic.add(listItem);
			}
			break;
		case DVDDealSet.MP5_PICTURE:
			LogCat.Logd("Mp5Data addItem picture");
			if(mListPicture != null)
			{
				mListPicture.add(listItem);
			}
			break;
		case DVDDealSet.MP5_VIDEO:
			LogCat.Logd("Mp5Data addItem video");
			if(mListVideo != null)
			{
				mListVideo.add(listItem);
			}
			break;
		default:
			break;
		}
	}
	
	public void clearList()
	{
		LogCat.Logd("Mp5Data clearList");
		
		if(!mListVideo.isEmpty())
		{
			mListVideo.clear();
		}
		if(!mListMusic.isEmpty())
		{
			mListMusic.clear();
		}
		if(!mListPicture.isEmpty())
		{
			mListPicture.clear();
		}
	}
	
	public ArrayList<HashMap<String, Object>> getList(byte listType)
	{
		LogCat.Logd("Mp5Data getList " + listType);
		switch(listType)
		{
		case DVDDealSet.MP5_VIDEO:
			return mListVideo;
		case DVDDealSet.MP5_MUSIC:
			return mListMusic;
		case DVDDealSet.MP5_PICTURE:
			return mListPicture;
		default:
			break;
		}
		
		return null;
	}
	
	public int getCount(byte listType)
	{
		LogCat.Logd("Mp5Data getCount " + listType);
		switch(listType)
		{
		case DVDDealSet.MP5_MUSIC:
			return mListMusic.size();
		case DVDDealSet.MP5_PICTURE:
			return mListPicture.size();
		case DVDDealSet.MP5_VIDEO:
			return mListVideo.size();
		default:
			break;
		}
		
		return -1;
	}
	
    public void setCurMediaName(String strName)
    {
    	mCurMediaName = strName;
    }
    
    public String getCurMediaName()
    {
    	return mCurMediaName;
    }
    
    public void setCurMediaType(byte bType)
    {
    	mCurMediaType = bType;
    }
    
    public byte getCurMediaType()
    {
    	return mCurMediaType;
    }
    
    public void setCurMediaNum(int iNum)
    {
    	mCurMediaNum = iNum;
    }
    
    public int getCurMediaNum()
    {
    	return mCurMediaNum;
    }
    
    public void setCurMediaItem(int iItem)
    {
    	mCurMediaItem = iItem;
    }
    
    public int getCurMediaItem()
    {
    	return mCurMediaItem;
    }
    
    public void setMusicCircleType(int type)
    {
    	mMusicCircleType = type;
    }
    
    public int getMusicCircletype()
    {
    	return mMusicCircleType;
    }
    
    public void setAppType(byte type)
    {
    	LogCat.Logd("AppData setAppType : " + type);
    	mCurAppType = type;
    }
    
    public byte getAppType()
    {
    	LogCat.Logd("AppData getAppType : " + mCurAppType);
    	return mCurAppType;
    }
    
    public void setAppState(byte state)
    {
    	LogCat.Logd("AppData setAppState : " + state);
    	mCurAppState = state;
    }
    
    public byte getAppState()
    {
    	LogCat.Logd("AppData getAppState : " + mCurAppState);
    	return mCurAppState;
    }
}