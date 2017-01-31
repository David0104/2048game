package com.example.demo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

public class GameViewitem extends View {

    private int number;  //方块上的数字
    private int code;   //方块代码
    private String numVal;
    private Paint mPaint; //画笔
    private Rect mBound;  //绘制区域

    public GameViewitem(Context context) {
        super(context);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initial();
        String bgColor = " ";
        //根据数字设置方块颜色和字体大小
        switch (number) {
            case 0:
                bgColor = "#CCC0B3";
                break;
            case 2:
                bgColor = "#EEE4DA";
                mPaint.setTextSize(130f);
                break;
            case 4:
                bgColor = "#EDE0C8";
                mPaint.setTextSize(130f);
                break;
            case 8:
                bgColor = "#F2B179";
                mPaint.setTextSize(130f);
                break;
            case 16:
                bgColor = "#F49563";
                mPaint.setTextSize(120f);
                break;
            case 32:
                bgColor = "#F5794D";
                mPaint.setTextSize(120f);
                break;
            case 64:
                bgColor = "#F55D37";
                mPaint.setTextSize(120f);
                break;
            case 128:
                bgColor = "#EEE863";
                mPaint.setTextSize(105f);
                break;
            case 256:
                bgColor = "#EDB04D";
                mPaint.setTextSize(105f);
                break;
            case 512:
                bgColor = "#ECB04D";
                mPaint.setTextSize(105f);
                break;
            case 1024:
                bgColor = "#EB9437";
                mPaint.setTextSize(85f);
                break;
            default:
                bgColor = "#EA7821";
                mPaint.setTextSize(85f);
                break;
        }
        mPaint.setColor(Color.parseColor(bgColor));
        mPaint.setStyle(Paint.Style.FILL);
        //width和height相等时可绘制正方形
        canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
        if (!"0".equals(numVal)) {
            drawNumber(canvas);
        }
    }

    private void drawNumber(Canvas canvas) {
        mPaint.setColor(Color.parseColor("#666666"));
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mPaint.getTextBounds(numVal, 0, numVal.length(), mBound);
        //使数字居中
        float x = (getWidth() - mBound.width()) / 2;
        float y = (getHeight() + mBound.height()) / 2;
        canvas.drawText(numVal, x, y, mPaint);
    }

    public void initial() {
        this.numVal = "" + number;
        mPaint = new Paint();
        mBound = new Rect();
        invalidate();
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
