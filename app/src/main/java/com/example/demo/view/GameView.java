package com.example.demo.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.example.demo.GamePresenter;
import com.example.demo.GestureDetect;

public class GameView extends RelativeLayout {

    private boolean isInitial = false;
    private android.view.GestureDetector gestureDetector;
    private static final int rowCnt = 4;
    private static final int layoutLength = 215;
    private static final int marginWidth = 16;
    private int initialNum1 = 0;
    private int initialNum2 = 0;
    private GameViewitem[] items;
    private Context context;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        gestureDetector = new GestureDetector(context, new GestureDetect());
    }

    public void initialNumber() {
        initialNum1 = (int) (Math.random() * 16);
        do {
            initialNum2 = (int) (Math.random() * 16);
        } while (initialNum1 == initialNum2);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!isInitial) {
            initialNumber();
            items = new GameViewitem[rowCnt * rowCnt];
            RelativeLayout.LayoutParams layout;
            for (int i = 0; i < rowCnt * rowCnt; ++i) {
                layout = new LayoutParams(layoutLength, layoutLength);
                items[i] = new GameViewitem(getContext());
                items[i].setCode(i);
                items[i].setId(generateViewId());
                items[i].setLayoutParams(layout);
                if (i == initialNum1 || i == initialNum2) {
                    items[i].setNumber(2);
                }
                if (i >= rowCnt) {
                    layout.addRule(RelativeLayout.BELOW, items[i - rowCnt].getId());
                    layout.topMargin = marginWidth;
                }
                if (i % rowCnt == 0) {
                    //每行的第一个view
                    layout.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    layout.leftMargin = 82;
                } else {
                    layout.addRule(RelativeLayout.RIGHT_OF, items[i - 1].getId());
                    layout.leftMargin = marginWidth;
                }
                addView(items[i], layout);
            }
            isInitial = true;
//            items[1].setNumber(64);
//            items[2].setNumber(8);
//            items[3].setNumber(512);
//            items[4].setNumber(1024);
//            items[5].setNumber(4096);
        }
        int length = Math.min(getMeasuredHeight(), getMeasuredWidth());
        setMeasuredDimension(length, length);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }

    //返回viewItem用于制作动画
    public GameViewitem[] getViewItems() {
        return items;
    }

}
