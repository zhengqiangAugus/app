package com.zq.app.view.swipemenu;

import java.util.List;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SwipeMenuView extends LinearLayout implements OnClickListener {

    private SwipeMenuLayout mLayout;
    private SwipeMenu mMenu;
    private OnSwipeItemClickListener onItemClickListener;
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public SwipeMenuView(SwipeMenu menu) {
        super(menu.getContext());
        mMenu = menu;
        init();
    }
    
    private void init(){
    	removeAllViews();
    	List<SwipeMenuItem> items = mMenu.getMenuItems();
    	int id = 0;
    	for (SwipeMenuItem item : items) {
    		addItem(item, id++);
    	}
    }
    
    public void refresh(){
    	removeAllViews();
    	List<SwipeMenuItem> items = mMenu.getMenuItems();
    	int id = 0;
    	for (SwipeMenuItem item : items) {
    		addItem(item, id++);
    	}
    }
    
    public SwipeMenu getMenu() {
		return mMenu;
	}

	public void removeItem(int id){
    	mMenu.getMenuItems().remove(id);
    	removeAllViews();
    	init();
    }
    
    public void updateItem(SwipeMenuItem item, int id){
    	mMenu.getMenuItems().set(id, item);
    	removeAllViews();
    	init();
    }
    
    @SuppressWarnings("deprecation")
	private void addItem(SwipeMenuItem item, int id) {
        LayoutParams params = new LayoutParams(item.getWidth(), LayoutParams.MATCH_PARENT);
        LinearLayout parent = new LinearLayout(getContext());
        parent.setId(id);
        parent.setGravity(Gravity.CENTER);
        parent.setOrientation(LinearLayout.VERTICAL);
        parent.setLayoutParams(params);
        parent.setBackgroundDrawable(item.getBackground());
        parent.setOnClickListener(this);
        addView(parent);

        if (item.getIcon() != null) {
            parent.addView(createIcon(item));
        }
        if (!TextUtils.isEmpty(item.getTitle())) {
            parent.addView(createTitle(item));
        }

    }

    private ImageView createIcon(SwipeMenuItem item) {
        ImageView iv = new ImageView(getContext());
        iv.setImageDrawable(item.getIcon());
        return iv;
    }

    private TextView createTitle(SwipeMenuItem item) {
        TextView tv = new TextView(getContext());
        tv.setText(item.getTitle());
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(item.getTitleSize());
        tv.setTextColor(item.getTitleColor());
        return tv;
    }
    
    @Override
    public void onClick(View v) {
        if (onItemClickListener != null && mLayout.isOpen()) {
            onItemClickListener.onItemClick(this,mMenu, v.getId());
        }
        if(mLayout.isOpen()){
        	mLayout.smoothCloseMenu();
        }
    }

    public OnSwipeItemClickListener getOnSwipeItemClickListener() {
        return onItemClickListener;
    }

    public void setOnSwipeItemClickListener(OnSwipeItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setLayout(SwipeMenuLayout mLayout) {
        this.mLayout = mLayout;
    }

    public static interface OnSwipeItemClickListener {
        void onItemClick(SwipeMenuView view,SwipeMenu menu, int index);
    }
}
