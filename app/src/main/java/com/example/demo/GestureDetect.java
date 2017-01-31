package com.example.demo;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by 宇成童鞋 on 2017/1/17.
 */

public class GestureDetect extends android.view.GestureDetector.SimpleOnGestureListener {

    private static final int minDistance = 50;
    private String direction = "";
    public static final int GETDIR = 1;

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        float x = e2.getX() - e1.getX();
        float y = e2.getY() - e1.getY();

        Message message = new Message();
        Handler handler = GamePresenter.handler;

        if (x > minDistance && Math.abs(velocityX) > Math.abs(velocityY)) {
            direction = "right";
        } else if (x < -minDistance && Math.abs(velocityX) > Math.abs(velocityY)) {
            direction = "left";
        } else if (y > minDistance && Math.abs(velocityX) < Math.abs(velocityY)) {
            direction = "down";
        } else if (y < -minDistance && Math.abs(velocityX) < Math.abs(velocityY)) {
            direction = "up";
        }
        message.what = GETDIR;
        message.obj = direction;
        handler.sendMessage(message);
        return true;
    }

}
