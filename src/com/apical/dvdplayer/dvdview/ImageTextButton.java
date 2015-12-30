package com.apical.dvdplayer.dvdview;

import com.apical.dvdplayer.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageButton;

public class ImageTextButton extends ImageButton{
	private String mstrtext = "";
    private int mcolor = 0;
    private float mtextsize = 0f;
	public ImageTextButton(Context context, AttributeSet attrs)
	{
        super(context, attrs);
        TypedArray t = getContext().obtainStyledAttributes(attrs,
				R.styleable.ImageTextButton);

        mstrtext = t.getString(
				R.styleable.ImageTextButton_text);
        
        mtextsize = t.getDimension(R.styleable.ImageTextButton_textSize, 20);
        
        mcolor = t.getInt(R.styleable.ImageTextButton_textColor, Color.WHITE);
        
        Log.d("ImageTextButton", "ImageTextButton -1" + mstrtext);
        
    }
	
	public ImageTextButton(Context context)
	{
        super(context);
    }
    
    public void setText(String text)
    {
        this.mstrtext = text;
    }
    
    public void setColor(int color)
    {
        this.mcolor = color;
    }
    
    public void setTextSize(float textsize)
    {
        this.mtextsize = textsize;
    }
    
    @Override
    protected void onDraw(Canvas canvas) 
    {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setTextAlign(Align.CENTER);
        paint.setColor(mcolor);
        paint.setTextSize(mtextsize);
        
        Rect textBounds = new Rect();
        paint.getTextBounds(mstrtext, 0, mstrtext.length(), textBounds);//get text bounds, that can get the text width and height   
        int textHeight = textBounds.bottom - textBounds.top;
        
        FontMetrics fontMetrics = paint.getFontMetrics();  
        float fontOff= (fontMetrics.descent - fontMetrics.top)/2;
        canvas.drawText(mstrtext, getWidth()/2, getHeight()/2 + textHeight/2 -5, paint);
    }

}
