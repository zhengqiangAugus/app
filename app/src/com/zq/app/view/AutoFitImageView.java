package com.zq.app.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

@SuppressLint("NewApi")
public class AutoFitImageView extends ImageView {

	public AutoFitImageView(Context context) {
		super(context);
	}

	public AutoFitImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public AutoFitImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	//重新定义控件的大小
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		float w = MeasureSpec.getSize(widthMeasureSpec);
		float h = MeasureSpec.getSize(heightMeasureSpec);
		Drawable dra = getBackground();
		if(dra==null)dra = getDrawable();
		if(dra!=null){
			Bitmap bmp = drawable2Bitmap(dra);
			float bw = bmp.getWidth();
			float bh = bmp.getHeight();
			h = bw / bh * w;
		}
		setMeasuredDimension((int)w,(int)h);
	}
	
	/*public void setLayoutParams(LayoutParams params) {
		Drawable dra = getBackground();
		if(dra==null)dra = getDrawable();
		if(dra!=null){
			Bitmap bmp = drawable2Bitmap(dra);
			int bw = bmp.getWidth();
			int bh = bmp.getHeight();
			int width = params.width;
			if(width<0){
				DisplayMetrics dm = new DisplayMetrics();
				dm = getResources().getDisplayMetrics();
				float density = dm.density; // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
				int densityDPI = dm.densityDpi; // 屏幕密度（每寸像素：120/160/240/320）
				float scaledDensity=dm.scaledDensity;
				float xdpi = dm.xdpi;
				float ydpi = dm.ydpi;
				width = dm.widthPixels; // 屏幕宽（像素，如：480px）  
				//int screenHeight = dm.heightPixels; // 屏幕高（像素，如：800px）
			}
			params.height = width * (bw/bh);
		}
		super.setLayoutParams(params);
	}*/
	
	Bitmap drawable2Bitmap(Drawable drawable) {  
        if (drawable instanceof BitmapDrawable) {  
            return ((BitmapDrawable) drawable).getBitmap();  
        } else if (drawable instanceof NinePatchDrawable) {  
            Bitmap bitmap = Bitmap  
                    .createBitmap(  
                            drawable.getIntrinsicWidth(),  
                            drawable.getIntrinsicHeight(),  
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888  
                                    : Bitmap.Config.RGB_565);  
            Canvas canvas = new Canvas(bitmap);  
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),  
                    drawable.getIntrinsicHeight());  
            drawable.draw(canvas);  
            return bitmap;  
        } else {  
            return null;  
        }  
    }  
}
