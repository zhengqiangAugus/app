package com.zq.app.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.percent.PercentRelativeLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class FixGridLayout extends PercentRelativeLayout{

	public FixGridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public FixGridLayout(Context context) {
		super(context);
	}

	public FixGridLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }
	
	protected void onLayout(boolean arg0, int l, int t, int r, int b) {
		int count = getChildCount();
		int width = r - l;
		int height = b - t;
		int tt = 0,ll = 0 , rowMaxt = 0;
		for(int i = 0;i<count;i++){
			View child = getChildAt(i);
			LayoutParams childlp = (LayoutParams) child.getLayoutParams();
			int cw = childlp.width;
			if(cw<0)cw = width;
			int ch = childlp.height;
			if(ch<0)ch = height;
			int cl = childlp.leftMargin;
			int cr = childlp.rightMargin;
			int ct = childlp.topMargin;
			int cb = childlp.bottomMargin;
			l = cl + ll;
			r = l + cw;
			t = ct + tt;
			b = t + ch;
			
			if(b + cb> rowMaxt){
				rowMaxt = b +cb;
			}
			child.layout(l,t,r,b);
			
			ll = r + cr;
			boolean wrap = false;
			if(i<count-1){
				View next = getChildAt(i+1);
				childlp = (LayoutParams) next.getLayoutParams();
				cw = childlp.width;
				if(cw<0)cw = width;
				cl = childlp.leftMargin;
				cr = childlp.rightMargin;
				if(ll + cl + cw + cr>width){
					wrap = true;
				}
			}
			if(ll >= width || wrap){
				tt = rowMaxt;
				ll = 0;
			}
		}
	}
	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int count = getChildCount();
		int width =	MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		int tt = 0,ll = 0 , rowMaxt = 0;
		int l ,r ,t ,b = 0 ,cl ,ct = 0 ,cr,cb = 0,cw ,ch = 0;
		for(int i = 0;i<count;i++){
			View child = getChildAt(i);
			LayoutParams childlp = (LayoutParams) child.getLayoutParams();
			cw = childlp.width;
			if(cw<0)cw = width;
			ch = childlp.height;
			if(ch<0)ch = height;
			cl = childlp.leftMargin;
			cr = childlp.rightMargin;
			ct = childlp.topMargin;
			cb = childlp.bottomMargin;
			l = cl + ll;
			r = l + cw;
			t = ct + tt;
			b = t + ch;
			ll = r + cr;
			if(b + cb> rowMaxt){
				rowMaxt = b +cb;
			}
			boolean wrap = false;
			if(i<count-1){
				View next = getChildAt(i+1);
				childlp = (LayoutParams) next.getLayoutParams();
				cw = childlp.width;
				if(cw<0)cw = width;
				cl = childlp.leftMargin;
				cr = childlp.rightMargin;
				if(ll + cl + cw + cr>width){
					wrap = true;
				}
			}
			if(ll >= width||wrap){
				tt = rowMaxt;
				ll = 0;
			}
		}
		height = b;
		setMeasuredDimension(width, height);
	}
	
	public static class LayoutParams extends PercentRelativeLayout.LayoutParams{

		public LayoutParams(Context c, AttributeSet attrs) {
			super(c, attrs);
		}
		public LayoutParams(int w, int h) {
			super(w, h);
		}
		public LayoutParams(ViewGroup.LayoutParams source) {
			super(source);
		}
		public LayoutParams(MarginLayoutParams source) {
			super(source);
		}
		@Override
		protected void setBaseAttributes(TypedArray a, int widthAttr,int heightAttr) {
			this.width = a.getLayoutDimension(widthAttr, 0);
			this.height = a.getLayoutDimension(heightAttr, 0);
		}
	}
	
}
