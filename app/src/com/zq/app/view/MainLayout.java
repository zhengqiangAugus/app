package com.zq.app.view;


import com.zq.app.R;
import com.zq.app.view.swipemenu.SwipeMenuLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.percent.PercentRelativeLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

@SuppressLint({ "ClickableViewAccessibility", "Recycle" })
public class MainLayout extends PercentRelativeLayout{
	
	Scroller scroller;
	View left , main;
	View shade;
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
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		init();
	}
	
	public void init(){
		left = findViewById(R.id.left_frame);
		main = findViewById(R.id.center_frame);
		shade =  findViewById(R.id.shade);
		main.bringToFront();
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
							shade.scrollTo(x + 10, y);
						else
							shade.scrollTo(x - 10, y);
					}
				}
				invalidate();
			}
		} 
	}
	
	float lastX,lastY;
	float touchSlop;
	int VELOCITY = 50;
	static final int LAYOUT = 0,MENU = 1;
	int type = -1;
	public boolean onTouchEvent(MotionEvent ev) {
		if (vt == null) {
			vt = VelocityTracker.obtain();
		}
		vt.addMovement(ev);
		final int action = ev.getAction();
		final float x = ev.getX();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			if (!scroller.isFinished()) {
				scroller.abortAnimation();
			}
			lastX = x;
			if(closedMenu){
				closedMenu = false;
				return false;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			switch (type) {
			case LAYOUT:
				final float dx = lastX - x;
				lastX = x;
				float oldScrollX = main.getScrollX();
				float scrollX = oldScrollX + dx;
				if (scrollX > 0)
					scrollX = 0;
				if (dx < 0 && oldScrollX < 0) { // left view
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
						shade.scrollTo((int) scrollX + 10,main.getScrollY());
					else
						shade.scrollTo((int) scrollX - 10,main.getScrollY());
				}
				break;
			case MENU:
				if(menu!=null){
					menu.onSwipe(ev);
				}
				break;
			}
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			switch (type) {
			case LAYOUT:
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
				if (main.getScrollX() == -left.getWidth()&& lastX > left.getWidth()) {
					showLeft();
				}
				break;
			case MENU:
				if(menu!=null){
					menu.onSwipe(ev);
				}
				menu = null;
				break;
			}
			break;
		}
		return true;
	}
	boolean closedMenu = false;
	boolean isFlingMenu = false;
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		final int action = ev.getAction();
		final float x = ev.getX();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			lastX = x;
			if (main.getScrollX() == -left.getWidth()&& lastX > left.getWidth()){
				return true;
			}
			if(oldMenu!=null&&oldMenu.isOpen()){
				boolean noInside = oldMenu.checkMenu(ev);
				isFlingMenu = !noInside;
				closedMenu = noInside;
				return noInside;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			final float dx = x - lastX;
			if (dx > touchSlop) {
				type = LAYOUT;
				if(menu!=null&&menu.isOpen()){
					type = MENU;
					oldMenu = menu;
				}
				return true;
			}else if (main.getScrollX() == -left.getWidth()&&Math.abs(dx) > touchSlop) {
				type = LAYOUT;
				return true;
			}else if(menu!=null && -dx > touchSlop){
				type = MENU;
				oldMenu = menu;
				return  true;
			}
			if(oldMenu!=null&&oldMenu.isOpen() && isFlingMenu){
				type = MENU;
				menu = oldMenu;
				return  true;
			}
			break;
		case MotionEvent.ACTION_UP:
			if (main.getScrollX() == -left.getWidth()&& lastX > left.getWidth()) {
				return true;
			}
			menu = null;
			break;
		}
		return false;
	}
	
	private SwipeMenuLayout menu,oldMenu;
	public void selectSwipeMenu(SwipeMenuLayout menu){
		this.menu = menu;
	}
	
	void smoothScrollTo(int dx) {
		int duration = 500;
		int oldScrollX = main.getScrollX();
		scroller.startScroll(oldScrollX, main.getScrollY(), dx,main.getScrollY(), duration);
		invalidate();
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
