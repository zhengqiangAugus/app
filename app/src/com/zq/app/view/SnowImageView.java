package com.zq.app.view;

import java.util.Random;

import com.zq.app.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ImageView;

@SuppressLint("NewApi")
public class SnowImageView extends ImageView {
	Bitmap snow;
	float density;
	public SnowImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	public SnowImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}
	
	public SnowImageView(Context context) {
		super(context);
	}

  	Snow snows[] = new Snow[25];
  	void init(Context context,AttributeSet attrs){
		DisplayMetrics dm = new DisplayMetrics();
		dm = getResources().getDisplayMetrics();
		density = dm.density;
		snow = BitmapFactory.decodeResource(getResources(), R.drawable.snow);
  	}
  	
  	int width , height;
  	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
  		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  		width = MeasureSpec.getSize(widthMeasureSpec);
  		height = MeasureSpec.getSize(heightMeasureSpec);
  		initSnows();
  	}
  	
  	void initSnows(){
  		for (int i = 0; i < snows.length; i++) {
			snows[i] = new Snow();
		}
  	}
  	
  	Matrix m = new Matrix();
  	Paint paint = new Paint();
	@SuppressLint("DrawAllocation")
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		for (int i = 0; i < snows.length; i++) {
			Snow snowFlower = snows[i];
			
			snowFlower.y += snowFlower.g;
			
			paint.setAlpha(snowFlower.alpha);
			canvas.drawBitmap(snowFlower.flower, snowFlower.x, snowFlower.y, paint);
			
			if (snowFlower.y >= height) {
				snowFlower.y = 0;
				snowFlower.x = r.nextInt(width);
			}
		}
		SystemClock.sleep(30);
		invalidate();
	}
  	
	Random r = new Random();
	class Snow{
		int x;
		int y;
		float size;
		int alpha;
		int g;
		Bitmap flower;
		public void init(){
			float aa = r.nextFloat();			
			this.x = r.nextInt(width);
			this.y = r.nextInt(height);
			if (aa >= 1) {
				this.size = 0.9f;
			}else if (aa <= 0.2) {
				this.size = 0.4f;
			}else{
				this.size = aa;
			}
			this.alpha = r.nextInt(155) + 100;
			this.g = (int) ((r.nextInt(3) + 2) * density);
			m.reset();
			m.setScale(size,size);
			this.flower = Bitmap.createBitmap(snow,0,0,snow.getWidth(),snow.getHeight(),m,true);
		}
		
		public Snow(){
			super();
			init();
		}		
		
	}
}
