package com.apical.dvdplayer.dvdview;

import com.apical.dvdplayer.LogCat;
import com.apical.dvdplayer.dvdmodel.DvdConfigurator;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class DvdView extends FrameLayout
{
    private static final int CAMERA_ID_DVD = 0;
    public static final int MODE_VIP = 0;
    public static final int MODE_TEXT = 1;
    public static final int MODE_NULL = 2;
    private boolean mSurfaceExisted = false;
    private int mVideoMode;
    private TextView mTextView;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;
    private SharedPreferences mPreferences;
    private byte mTVShape = DvdConfigurator.TV_SHAPE_43;
    private byte mViewMode = DvdConfigurator.VIEW_MODE_AUTO_FIT;
    private int mWidthRatio = 4;
    private int mHeightRatio = 3;
    private byte mVedioRatio = DvdConfigurator.TV_SHAPE_43;

    public DvdView(Context context)
    {
        this(context, null);
    }

    public DvdView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public void init(int videoMode, String text)
    {
        LogCat.Logd("DvdView init");
        mTextView = (TextView)getChildAt(0);
        mSurfaceView = (SurfaceView)getChildAt(1);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        setText(text);
        setMode(videoMode);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        //Log.d(TAG, "init(" + videoMode + ", " + text + ") Before addCallback");
        mSurfaceHolder.addCallback(new Callback()
        {
            public void surfaceChanged(SurfaceHolder holder, int format, int width,
                    int height)
            {
//                Log.e(TAG, "surfaceChanged(" + holder + ", " + format + ", " + width
//                        + ", " + height + ")");
            }

            public void surfaceCreated(SurfaceHolder holder)
            {
            	LogCat.Logd("DvdView init surfaceCreated");
                mSurfaceExisted = true;
                openCamera();
            }

            public void surfaceDestroyed(SurfaceHolder holder)
            {
            	LogCat.Logd("DvdView init surfacedestroyed");
                mSurfaceExisted = false;
                closeCamera();
            }
        });     
    }

    private void openCamera()
    {
    	LogCat.Logd("DvdView openCamera");
        if (mCamera != null)
        {
            closeCamera();
        }
        //mCamera = Camera.open(CAMERA_ID_DVD);
        try 
        {
        	mCamera = Camera.open(CAMERA_ID_DVD);
		}
        catch(RuntimeException e)
        {
        	LogCat.Logd("DvdView openCamera catch exception");
		}
        if (mCamera == null)
        {
            return;
        }
        try
        {
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.startPreview();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private void closeCamera()
    {
    	LogCat.Logd("DvdView closeCamera");
        if (mCamera == null)
        {
            return;
        }
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }
    
    public void onPause()
    {
    	LogCat.Logd("DvdView onPause");
        closeCamera();
    }

    public void onResume()
    {
    	LogCat.Logd("DvdView onResume");
        getVideoSettings();
        if (mSurfaceExisted)
        {
            openCamera();
        }
    }
    
    public void setVedioRatio(Byte tv_shape)
    {
    	LogCat.Logd("DvdView setVedioRatio");

        switch (tv_shape)
        {
        case DvdConfigurator.TV_SHAPE_43:
            //Log.d(TAG, "tv_shape = 4:3");
            mWidthRatio = 4;
            mHeightRatio = 3;
            mVedioRatio = DvdConfigurator.TV_SHAPE_43;
            break;
        case DvdConfigurator.TV_SHAPE_169:
            //Log.d(TAG, "tv_shape = 16:9");
            mWidthRatio = 16;
            mHeightRatio = 9;
            mVedioRatio = DvdConfigurator.TV_SHAPE_169;
            break;
        }
        
        Byte view_mode = Byte.parseByte(mPreferences.getString(
                DvdConfigurator.KEY_VIEW_MODE,
                String.valueOf(DvdConfigurator.VIEW_MODE_AUTO_FIT)));
        if ((mTVShape != tv_shape) || (mViewMode != view_mode))
        {
            mTVShape = tv_shape;
            mViewMode = view_mode;
            adjustVideoSize(getWidth(), getHeight());
        }
    }
    
    public Byte getVedioRatio() 
    {
		
    	return mVedioRatio;
	}

    private void getVideoSettings()
    {
    	LogCat.Logd("DvdView getVideoSettings");
        Byte tv_shape = Byte.parseByte(mPreferences.getString(
                DvdConfigurator.KEY_TV_SHAPE, String.valueOf(DvdConfigurator.TV_SHAPE_43)));
        //Ĭ��169
        tv_shape = DvdConfigurator.TV_SHAPE_169;
        switch (tv_shape)
        {
        case DvdConfigurator.TV_SHAPE_43:
            //Log.d(TAG, "tv_shape = 4:3");
            mWidthRatio = 4;
            mHeightRatio = 3;
            mVedioRatio = DvdConfigurator.TV_SHAPE_43;
            break;
        case DvdConfigurator.TV_SHAPE_169:
            //Log.d(TAG, "tv_shape = 16:9");
            mWidthRatio = 16;
            mHeightRatio = 9;
            mVedioRatio = DvdConfigurator.TV_SHAPE_169;
            break;
        }
        
        Byte view_mode = Byte.parseByte(mPreferences.getString(
                DvdConfigurator.KEY_VIEW_MODE,
                String.valueOf(DvdConfigurator.VIEW_MODE_AUTO_FIT)));
        if ((mTVShape != tv_shape) || (mViewMode != view_mode))
        {
            mTVShape = tv_shape;
            mViewMode = view_mode;
            adjustVideoSize(getWidth(), getHeight());
        }
    }
    
    public void adjustVideoSize(int width, int height)
    {
    	LogCat.Logd("DvdView adjustVideoSize");
        int new_width = width;
        int new_height = height;
 
        //Log.d(TAG, "new_width = " + new_width + ", new_height = " + new_height);
        mSurfaceView.setLayoutParams(new LayoutParams(new_width, new_height,
                Gravity.CENTER));

        measureChild(mSurfaceView, new_width, new_height);
    }

 /*   private void adjustVideoSize(int width, int height)
    {
        Log.d(TAG, "adjustVideoSize(" + width + "," + height + ")");
        int new_width = width;
        int new_height = height;
        switch (mViewMode)
        {
        case DvdConfigurator.VIEW_MODE_FILL:
            break;
        case DvdConfigurator.VIEW_MODE_ORIGINAL:
            if (((float)width / height) < 1.5)
            {
                if (width < 720)
                {
                    new_width = width;
                }
                else
                {
                    new_width = 720;
                }
                new_height = new_width * 2 / 3;
            }
            else
            {
                if (height < 480)
                {
                    new_height = height;
                }
                else
                {
                    new_height = 480;
                }
                new_width = height * 3 / 2;
            }
            break;
        case DvdConfigurator.VIEW_MODE_HEIGHT_FIT:
            new_height = height;
            new_width = height * mWidthRatio / mHeightRatio;
            break;
        case DvdConfigurator.VIEW_MODE_WIDTH_FIT:
            new_width = width;
            new_height = new_width * mHeightRatio / mWidthRatio;
            break;
        case DvdConfigurator.VIEW_MODE_AUTO_FIT:
            if ((width * mHeightRatio) < (height * mWidthRatio))
            {
                new_width = width;
                new_height = new_width * mHeightRatio / mWidthRatio;
            }
            else
            {
                new_height = height;
                new_width = height * mWidthRatio / mHeightRatio;
            }
            break;
        }
        Log.d(TAG, "new_width = " + new_width + ", new_height = " + new_height);
        mSurfaceView.setLayoutParams(new LayoutParams(new_width, new_height,
                Gravity.CENTER));
        int widthMeasureSpec = MeasureSpec.makeMeasureSpec(new_width, MeasureSpec.AT_MOST);
        int heightMeasureSpec = MeasureSpec.makeMeasureSpec(new_height,
                MeasureSpec.AT_MOST);
        measureChild(mSurfaceView, widthMeasureSpec, heightMeasureSpec);
    }*/

    @Override
    public void onSizeChanged(int width, int height, int old_width, int old_height)
    {
//        Log.d(TAG, "onSizeChanged(" + width + "," + height + "," + old_width + ","
//                + old_height + ")");
    	LogCat.Logd("DvdView onSizeChanged");
        if (mVideoMode == MODE_VIP)
        {
            adjustVideoSize(width, height);
        }
    }

    public void setMode(int videoMode)
    {
        //Log.d(TAG, "setMode(" + videoMode + ")");
    	LogCat.Logd("DvdView videoMode");
        if (videoMode == mVideoMode)
        {
            return;
        }
        mVideoMode = videoMode;
        switch (mVideoMode)
        {
        case MODE_TEXT:
           // mSurfaceView.setVisibility(View.GONE);
           // mTextView.setVisibility(View.VISIBLE);
            break;
        case MODE_VIP:
            mTextView.setVisibility(View.GONE);
            mSurfaceView.setVisibility(View.VISIBLE);
            adjustVideoSize(getWidth(), getHeight());
            break;
        }
    }

    public int getMode()
    {
    	LogCat.Logd("DvdView getMode");
        return mVideoMode;
    }

    public void setText(String text)
    {
    	LogCat.Logd("DvdView setText");
        mTextView.setText(text);
    }
}
