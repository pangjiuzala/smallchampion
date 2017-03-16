package com.nuc.smallchampion.lovingdraw;

import com.nuc.smallchampion.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class MyAdtper extends BaseAdapter {
	private Context myContext;
	private Integer myImageArry[] = {R.drawable.next3,R.drawable.s1, R.drawable.s2, R.drawable.s3,
			R.drawable.s4, R.drawable.s5, R.drawable.next0, R.drawable.s6,
			R.drawable.s7, R.drawable.s8, R.drawable.s9, R.drawable.s10,
			R.drawable.s11, R.drawable.s12, R.drawable.next0, R.drawable.s13,
			R.drawable.s14, R.drawable.s15, R.drawable.s16, R.drawable.s17,
			R.drawable.s18, R.drawable.s19, R.drawable.next0,
			R.drawable.next0, R.drawable.s33,
			R.drawable.s34, R.drawable.s35, R.drawable.s37, R.drawable.s38,
			R.drawable.s39,  R.drawable.next1,   };

	public MyAdtper(Context context) {
		myContext = context;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return myImageArry.length;
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ImageView im = new ImageView(myContext);
		im.setImageResource(myImageArry[position]);
		im.setId(myImageArry[position]);
		im.setLayoutParams(new Gallery.LayoutParams(200, 200));// im.setLayoutParams(new
		// Gallery.LayoutParams(width,height));
		return im;
	}

}
