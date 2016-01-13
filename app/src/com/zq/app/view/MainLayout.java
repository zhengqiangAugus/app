package com.zq.app.view;


import com.zq.app.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.percent.PercentRelativeLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.RelativeLayout;
import android.widget.Scroller;

@SuppressLint({ "ClickableViewAccessibility", "Recycle" })
public class MainLayout extends PercentRelativeLayout{
	
	Scroller scroller;
	View left , main;
	RelativeLayout shade;
	VelocityTracker vt;
	
	public MainLayout(Context context) {
		super(context);
		scroller = new Scroller(getContext());
		touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
	}
	public MainLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		scroller = new Scroller(getContext());
		touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
	}
	public MainLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		scroller = new Scroller(getContext());
		touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
	}
	
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		init();
	}
	
	public void init(){
		left = findViewById(R.id.left_frame);
		main = findViewById(R.id.center_frame);
		shade = (RelativeLayout) findViewById(R.id.shade);
		main.bringToFront();
	}
	
	public void scrollTo(int x, int y) {
		super.scrollTo(x, y);
		postInvalidate();
	}
	
	@Override
	public void computeScroll() {
		if (!scroller.isFinished()) {
			if (scroller.computeScrollOffset()) {
				int oldX = main.getScrollX();
				int oldY = main.getScrollY();
				int x = scroller.getCurrX();
				int y = scroller.getCurrY();
				if (oldX != x || oldY != y) {
					if (main != null) {
						main.scrollTo(x, y);
						if (x < 0)
							shade.scrollTo(x + 20, y);
						else
							shade.scrollTo(x - 20, y);
					}
				}
				invalidate();
			}
		} 
	}
	
	boolean draged = true;
	float lastX,lastY;
	float touchSlop;
	int VELOCITY = 50;
	public boolean onTouchEvent(MotionEvent ev) {
		if (vt == null) {
			vt = VelocityTracker.obtain();
		}
		vt.addMovement(ev);
		final int action = ev.getAction();
		final float x = ev.getX();
		final float y = ev.getY();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			if (!scroller.isFinished()) {
				scroller.abortAnimation();
			}
			lastX = x;
			lastY = y;
			if (main.getScrollX() == -left.getWidth()&& lastX < left.getWidth()) {
				return false;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (draged) {
				final float deltaX = lastX - x;
				lastX = x;
				float oldScrollX = main.getScrollX();
				float scrollX = oldScrollX + deltaX;
				if (scrollX > 0)
					scrollX = 0;
				if (deltaX < 0 && oldScrollX < 0) { // left view
					final float leftBound = 0;
					final float rightBound = -left.getWidth();
					if (scrollX > leftBound) {
						scrollX = leftBound;
					} else if (scrollX < rightBound) {
						scrollX = rightBound;
					}
				} 
				if (main != null) {
					main.scrollTo((int) scrollX,main.getScrollY());
					if (scrollX < 0)
						shade.scrollTo((int) scrollX + 20,main.getScrollY());
					else
						shade.scrollTo((int) scrollX - 20,main.getScrollY());
				}

			}
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			if (draged) {
				final VelocityTracker velocityTracker = vt;
				velocityTracker.computeCurrentVelocity(100);
				float xVelocity = velocityTracker.getXVelocity();			
				int oldScrollX = main.getScrollX();
				int dx = 0;
				if (oldScrollX <= 0) {
					if (xVelocity > VELOCITY) {
						dx = -left.getWidth() - oldScrollX;
					} else if (xVelocity < -VELOCITY) {
						dx = -oldScrollX;
					} else if (oldScrollX < -left.getWidth() / 2) {
						dx = -left.getWidth() - oldScrollX;
					} else if (oldScrollX >= -left.getWidth() / 2) {
						dx = -oldScrollX;
					}

				}
				smoothScrollTo(dx);
			}
			break;
		}
		return true;
	}
	boolean intered = true;
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		final int action = ev.getAction();
		final float x = ev.getX();
		final float y = ev.getY();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			lastX = x;
			lastY = y;
			intered = false;
			break;
		case MotionEvent.ACTION_MOVE:
			final float dx = x - lastX;
			final float xDiff = Math.abs(dx);
			final float yDiff = Math.abs(y - lastY);
			if (xDiff > touchSlop && xDiff > yDiff) {
				if (draged) {
					float oldScrollX = main.getScrollX();
					if (oldScrollX < 0) {
						intered = true;
						lastX = x;
					} else {
						if (dx > 0) {
							intered = true;
							lastX = x;
						}
					}
				} 
			}
			break;
		}
		return intered;
	}
	
	void smoothScrollTo(int dx) {
		int duration = 500;
		int oldScrollX = main.getScrollX();
		scroller.startScroll(oldScrollX, main.getScrollY(), dx,main.getScrollY(), duration);
		invalidate();
	}
	
	public boolean isDraged() {
		return draged;
	}
	public void setDraged(boolean draged) {
		this.draged = draged;
	}
	
	public void closeLeft(){
		int menuWidth = left.getWidth();
		smoothScrollTo(menuWidth);
	}
	
	public void showLeft(){
		int menuWidth = left.getWidth();
		int oldScrollX = main.getScrollX();
		if (oldScrollX == 0) {
			left.setVisibility(View.VISIBLE);
			smoothScrollTo(-menuWidth);
		} else if (oldScrollX == -menuWidth) {
			smoothScrollTo(menuWidth);
		}
	}
}
