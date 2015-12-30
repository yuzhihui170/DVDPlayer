package com.apical.dvdplayer.dvdview;

import java.util.List;
import java.util.Map;

import com.apical.dvdplayer.R;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class FuncListAdapter extends SimpleAdapter{

	Drawable drawable = null;
	int mSelectedItem = -1;
	private LayoutInflater minflater = null;
	
	public FuncListAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		minflater = LayoutInflater.from(context);
		// TODO Auto-generated constructor stub
	}
	
	public void SetItemSelPic(Drawable selPic){
		drawable = selPic;
	}
	
	public void SetSelectedItem(int item){
		mSelectedItem = item;
	}
	
	public int GetSelectedItem(){
		return mSelectedItem;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
		}
		if (convertView != null) {
			if (mSelectedItem == position) {
				if (drawable != null)
				{
					convertView.setBackgroundDrawable(drawable);
					TextView title = (TextView) convertView.findViewById(R.id.Media_name);
					title.setTextColor(convertView.getResources().getColor(R.color.color_currenttrack));
				}
			} else {
				TextView title = (TextView) convertView.findViewById(R.id.Media_name);
				title.setTextColor(convertView.getResources().getColor(R.color.color_white));
				convertView.setBackgroundColor(Color.TRANSPARENT);
			}
		}
		return super.getView(position, convertView, parent);
	}
}