package com.example.demo;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.example.demo.view.GameView;
import com.example.demo.view.GameViewitem;

import java.util.ArrayList;
import java.util.List;

public class GamePresenter {

    private GameView gameView;
    static Handler handler;
    private static final float squareWidth = 232;
    private static final int OVER = -1;  //表示动画结束
    private TranslateAnimation anim;
    private GameViewitem[] viewitems;
    private static Context context;
    private static TextView scoreText;
    private static ItemBean[] itemBeen = new ItemBean[16];
    private List<GameViewitem> items = new ArrayList<>();

    private void initialItem() {
        items.clear();
        viewitems = gameView.getViewItems();
        int n = 0;
        for (GameViewitem v : viewitems) {
            itemBeen[n++] = new ItemBean(v.getNumber());
            if (v.getNumber() != 0) {
                items.add(v);
            }
        }
    }

    GamePresenter(GameView gameView, TextView scoreText, Context context) {
        this.gameView = gameView;
        GamePresenter.scoreText = scoreText;
        GamePresenter.context = context;
        getDirection();
    }

    private void getDirection() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case GestureDetect.GETDIR:
                        initialItem();
                        makeCartoon(msg.obj.toString());
                        break;
                    case OVER:
                        initialItem();
                        if (!isPanelFull())
                            addRandomNumber();
                        else
                            showDialog();
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private void makeCartoon(String direction) {
        switch (direction) {
            case "up":
                makeUpPath();
                break;
            case "down":
                makeDownPath();
                break;
            case "left":
                makeLeftPath();
                break;
            case "right":
                makeRightPath();
                break;
            default:
                break;
        }
    }

    private void setAnim(TranslateAnimation anim) {
        anim.setDuration(75);
        anim.setInterpolator(new LinearInterpolator());
        anim.setFillAfter(true);
    }

    private void sendOverMessage() {
        Message message = new Message();
        message.what = OVER;
        handler.sendMessage(message);
    }

    private void makeUpPath() {
        boolean isIntoif = false;
        for (GameViewitem v : items) {
            final boolean isLast = (v == items.get(items.size() - 1));
            if (v.getCode() >= 4) {
                isIntoif = true;
                final int code = v.getCode();
                final int number = v.getNumber();
                final GameViewitem fv = v;
                int tempCode = code;
                float distance = 0;
                boolean merge = false;
                for (int i = code - 4; i >= 0; i -= 4) {
                    int itemNumber = itemBeen[i].getNumber();
                    if (!itemBeen[i].isMerge() && itemNumber == number
                            || itemNumber == 0) {
                        tempCode = i;
                        distance += squareWidth;
                        merge = itemNumber != 0;
                        if (itemNumber != 0)
                            break;
                    } else
                        break;
                }
                final int newCode = tempCode;
                final boolean isMerge = merge;
                final boolean noMove = (distance == 0);
                anim = new TranslateAnimation(0, 0, 0, -distance);
                v.bringToFront();
                setAnim(anim);
                itemBeen[code].setNumber(noMove ? v.getNumber() : 0);
                itemBeen[newCode].setNumber(isMerge ? number * 2 : number);
                addScore(isMerge ? number * 2 : 0);
                itemBeen[newCode].setMerge(isMerge);
                v.startAnimation(anim);
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        fv.clearAnimation();
                        fv.setNumber(itemBeen[fv.getCode()].getNumber());
                        GameViewitem viewitem = viewitems[newCode];
                        viewitem.setNumber(itemBeen[newCode].getNumber());
                        if (isLast)
                            sendOverMessage();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
            }
        }
        if (!isIntoif)
            sendOverMessage();
    }

    private void makeDownPath() {
        boolean isIntoif = false;
        for (int i = items.size() - 1; i >= 0; --i) {
            final boolean isLast = (i == 0);
            GameViewitem v = items.get(i);
            if (v.getCode() <= 11) {
                isIntoif = true;
                final int code = v.getCode();
                final int number = v.getNumber();
                final GameViewitem fv = v;
                int tempCode = code;
                float distance = 0;
                boolean merge = false;
                for (int j = code + 4; j < 16; j += 4) {
                    int itemNumber = itemBeen[j].getNumber();
                    if (!itemBeen[j].isMerge() && itemNumber == number
                            || itemNumber == 0) {
                        tempCode = j;
                        distance += squareWidth;
                        merge = itemNumber != 0;
                        if (itemNumber != 0)
                            break;
                    } else
                        break;
                }
                final boolean noMove = (distance == 0);
                final int newCode = tempCode;
                final boolean isMerge = merge;
                itemBeen[code].setNumber(noMove ? v.getNumber() : 0);
                itemBeen[newCode].setNumber(isMerge ? number * 2 : number);
                itemBeen[newCode].setMerge(isMerge);
                addScore(isMerge ? number * 2 : 0);
                anim = new TranslateAnimation(0, 0, 0, +distance);
                setAnim(anim);
                v.bringToFront();
                v.startAnimation(anim);
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        fv.clearAnimation();
                        fv.setNumber(itemBeen[code].getNumber());
                        GameViewitem viewitem = viewitems[newCode];
                        viewitem.setNumber(itemBeen[newCode].getNumber());
                        if (isLast)
                            sendOverMessage();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
            }
        }
        if (!isIntoif)
            sendOverMessage();
    }

    private void makeLeftPath() {
        boolean isIntoif = false;
        for (GameViewitem v : items) {
            final boolean isLast = (v == items.get(items.size() - 1));
            if (v.getCode() % 4 != 0) {
                isIntoif = true;
                final int code = v.getCode();
                final int number = v.getNumber();
                final GameViewitem fv = v;
                int tempCode = code;
                float distance = 0;
                boolean merge = false;
                for (int i = 1; (code - i) % 4 != 3 && code >= i; i++) {
                    int itemNumber = itemBeen[code - i].getNumber();
                    if (!itemBeen[code - i].isMerge() && itemNumber == number
                            || itemNumber == 0) {
                        tempCode = code - i;
                        distance += squareWidth;
                        merge = itemNumber != 0;
                        if (itemNumber != 0)
                            break;
                    } else
                        break;
                }
                final int newCode = tempCode;
                final boolean isMerge = merge;
                final boolean noMove = (distance == 0);
                anim = new TranslateAnimation(0, -distance, 0, 0);
                setAnim(anim);
                v.bringToFront();
                itemBeen[code].setNumber(noMove ? v.getNumber() : 0);
                itemBeen[newCode].setNumber(isMerge ? number * 2 : number);
                itemBeen[newCode].setMerge(isMerge);
                addScore(isMerge ? number * 2 : 0);
                v.startAnimation(anim);
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        fv.clearAnimation();
                        fv.setNumber(itemBeen[fv.getCode()].getNumber());
                        GameViewitem viewitem = viewitems[newCode];
                        viewitem.setNumber(itemBeen[newCode].getNumber());
                        if (isLast)
                            sendOverMessage();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
            }
        }
        if (!isIntoif)
            sendOverMessage();
    }

    private void makeRightPath() {
        boolean isIntoif = false;
        for (int i = items.size() - 1; i >= 0; --i) {
            final boolean isLast = (i == 0);
            GameViewitem v = items.get(i);
            if (v.getCode() % 4 != 3) {
                isIntoif = true;
                final int code = v.getCode();
                final int number = v.getNumber();
                final GameViewitem fv = v;
                int tempCode = code;
                float distance = 0;
                boolean merge = false;
                for (int j = code + 1; j % 4 != 0; ++j) {
                    int itemNumber = itemBeen[j].getNumber();
                    if (!itemBeen[j].isMerge() && itemNumber == number
                            || itemNumber == 0) {
                        tempCode = j;
                        distance += squareWidth;
                        merge = itemNumber != 0;
                        if (itemNumber != 0)
                            break;
                    } else
                        break;
                }
                final boolean noMove = (distance == 0);
                final int newCode = tempCode;
                final boolean isMerge = merge;
                itemBeen[code].setNumber(noMove ? v.getNumber() : 0);
                itemBeen[newCode].setNumber(isMerge ? number * 2 : number);
                itemBeen[newCode].setMerge(isMerge);
                addScore(isMerge ? number * 2 : 0);
                anim = new TranslateAnimation(0, +distance, 0, 0);
                setAnim(anim);
                v.bringToFront();
                v.startAnimation(anim);
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        fv.clearAnimation();
                        fv.setNumber(itemBeen[code].getNumber());
                        GameViewitem viewitem = viewitems[newCode];
                        viewitem.setNumber(itemBeen[newCode].getNumber());
                        if (isLast)
                            sendOverMessage();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
            }
        }
        if (!isIntoif)
            sendOverMessage();
    }

    private void addRandomNumber() {
        int ram;
        while (true) {
            ram = (int) (Math.random() * 16);
            if (itemBeen[ram].getNumber() == 0)
                break;
        }
        itemBeen[ram].setNumber(2);
        viewitems[ram].setNumber(2);
        Animation animation = new ScaleAnimation(0f, 1f, 0f, 1f
                , Animation.RELATIVE_TO_SELF, 0.5f
                , Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(75);
        viewitems[ram].startAnimation(animation);
    }

    private boolean isPanelFull() {
        int zeroCnt = 0;
        for (int i = 0; i < itemBeen.length; ++i) {
            if (itemBeen[i].getNumber() == 0)
                ++zeroCnt;
        }
        return zeroCnt == 0;
    }

    private void showDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("游戏结束");
        dialog.setMessage("最终得分：" + scoreText.getText());
        dialog.setCancelable(false);
        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                restartGame();
            }
        });
        dialog.show();
    }

    private void restartGame() {
        for (int i = 0; i < itemBeen.length; ++i) {
            itemBeen[i].setNumber(0);
            viewitems[i].setNumber(0);
            int nowScore = Integer.parseInt(scoreText.getText().toString());
            addScore(-nowScore);
        }
        addRandomNumber();
        addRandomNumber();
    }

    private void addScore(int score) {
        int bfScore = Integer.parseInt(scoreText.getText().toString());
        final int newScore = bfScore + score;
        Activity activity = (Activity) scoreText.getContext();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                scoreText.setText(String.valueOf(newScore));
            }
        });
    }

    protected static void saveItems() {
        int score = Integer.parseInt(scoreText.getText().toString());
        SharedPreference.setSharedPreference(context, itemBeen, score);
    }

}
