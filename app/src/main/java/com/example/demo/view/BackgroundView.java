package com.example.demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.demo.view.GameViewitem;

/**
 * Created by 宇成童鞋 on 2017/1/20.
 */

public class BackgroundView extends RelativeLayout {

    private boolean isInitial = false;
    private static final int rowCnt = 4;
    private static final int layoutLength = 215;
    private static final int marginWidth = 16;
    private GameViewitem[] items;

    public BackgroundView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!isInitial) {
            items = new GameViewitem[rowCnt * rowCnt];
            RelativeLayout.LayoutParams layout;
            for (int i = 0; i < rowCnt * rowCnt; ++i) {
                layout = new RelativeLayout.LayoutParams(layoutLength, layoutLength);
                items[i] = new GameViewitem(getContext());
                items[i].setId(generateViewId());
                items[i].setLayoutParams(layout);
                items[i].setNumber(0);
                if (i >= rowCnt) {
                    layout.addRule(RelativeLayout.BELOW, items[i - rowCnt].getId());
                    layout.topMargin = marginWidth;
                }
                if (i % rowCnt == 0) {
                    layout.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    layout.leftMargin = 82;
                } else {
                    layout.addRule(RelativeLayout.RIGHT_OF, items[i - 1].getId());
                    layout.leftMargin = marginWidth;
                }
                addView(items[i], layout);
            }
            isInitial = true;
        }
        int length = Math.min(getMeasuredHeight(), getMeasuredWidth());
        setMeasuredDimension(length, length);
    }

}
