package com.zq.app.view;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;







import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

@SuppressLint({ "NewApi", "DrawAllocation" })
public class GIFImageView extends ImageView{
	/**  
     * 播放GIF动画的关键类  
     */ 
    private Movie movie;  
 
    /**  
     * 记录动画开始的时间  
     */ 
    private long startTime;  
 
    /**  
     * 图片是否正在播放  
     */ 
    private boolean isPlaying = true;  
    
    public GIFImageView(Context context) {  
        super(context);  
    }

	public GIFImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		TypedArray a = context.obtainStyledAttributes(attrs,null);
		int resourceId = getResourceId(a, context, attrs);
		if (resourceId != 0) {
			InputStream is = getResources().openRawResource(resourceId);
			movie = Movie.decodeStream(is);
			if (movie != null) {
				Bitmap bitmap = BitmapFactory.decodeStream(is);
				mWidth = bitmap.getWidth();
				mHeight = bitmap.getHeight();
				bitmap.recycle();
			}
		}
	}
	
	int mWidth,mHeight;
	
	public GIFImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, new int[]{});
		int resourceId = getResourceId(a, context, attrs);
		if (resourceId != 0) {
			InputStream is = getResources().openRawResource(resourceId);
			movie = Movie.decodeStream(is);
			if (movie != null) {
				Bitmap bitmap = BitmapFactory.decodeStream(is);
				mWidth = bitmap.getWidth();
				mHeight = bitmap.getHeight();
				bitmap.recycle();
			}
		}
	}

	float w,h;
  	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
  		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  		w = MeasureSpec.getSize(widthMeasureSpec);
  		h = MeasureSpec.getSize(heightMeasureSpec);
  		Drawable dra = getBackground();
  		if(dra==null)dra = getDrawable();
  		if(dra!=null){
  			Bitmap bmp = drawable2Bitmap(dra);
  			float bw = bmp.getWidth();
  			float bh = bmp.getHeight();
  			h = bh / bw * w;
  		}
  		setMeasuredDimension((int)w,(int)h);
  	}
    
    @Override 
    protected void onDraw(Canvas canvas) {
        if (movie == null) {  
            super.onDraw(canvas);  
        } else {  
        	if(isPlaying){
		        playMovie(canvas);
		        invalidate(); 
        	}
        }  
    }  
    
    public void setPause(boolean boo){
    	isPlaying = boo;
    	if(isPlaying){
    		invalidate();
    	}
    }
    
    
    Paint paint = new Paint();
    boolean loadAllPic = false;
    Bitmap temp;
    
    private void playMovie(Canvas canvas) {
    	Bitmap bmp = Bitmap.createBitmap(mWidth,mHeight,Config.ARGB_8888);
    	Canvas ca = new Canvas(bmp);
        long now = SystemClock.uptimeMillis();  
        if (startTime == 0) {  
            startTime = now;  
        }  
        int duration = movie.duration();  
        if (duration == 0) {  
            duration = 1000;
        }  
        int relTime = (int) ((now - startTime) % duration);
        movie.setTime(relTime);
        movie.draw(ca, 0, 0);
        bmp = Bitmap.createScaledBitmap(bmp,getWidth(),getHeight(),true);
        canvas.drawBitmap(bmp,0, 0,paint);
        temp = bmp;
        if ((now - startTime) >= duration) {  
            startTime = 0;
        }
    }  
 
    
    private int getResourceId(TypedArray a, Context context, AttributeSet attrs) {
        try {
            Field field = TypedArray.class.getDeclaredField("mValue");
            field.setAccessible(true);
            TypedValue typedValueObject = (TypedValue) field.get(a);
            return typedValueObject.resourceId;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (a != null) {
                a.recycle();
            }  
        }  
        return 0;
    }  
 
    Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {
            Bitmap bitmap = Bitmap  .createBitmap(drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight(),drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888  : Bitmap.Config.RGB_565);  
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());  
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }
    
    public InputStream Bitmap2InputStream(Bitmap bm, int quality) {  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, quality, baos); 
        InputStream is = new ByteArrayInputStream(baos.toByteArray());  
        return is;  
    }  
}
