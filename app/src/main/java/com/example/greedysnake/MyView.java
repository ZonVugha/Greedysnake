package com.example.greedysnake;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

class MyView extends View {
    private Point head = new Point();
    private Point food = new Point();
    private final int W = 100, H = 100;
    private int SnakeBody = 3;
    private float  x1 ,x2;
    private float y1 ,y2;
    private boolean GameOver = false;
    private Bitmap bmp;
    private ArrayList<Point> pts = new ArrayList<Point>();
    private enum DIRT{
        UP,
        DOWN,
        LEFT,
        RIGHT,
        PAUSE
    }
    private DIRT dirt = DIRT.PAUSE;

    public void snakeMove(){
        if (dirt == DIRT.PAUSE)
            return;

        switch (dirt){
            case UP:
                head.y -=H;
                break;
            case DOWN:
                head.y +=H;
                break;
            case LEFT:
                head.x -=W;
                break;
            case RIGHT:
                head.x +=W;
                break;
        }
        if ((food.x == head.x) && (food.y == head.y)){
            pts.add(0,new Point(food.x,food.y));
            newFood();
            invalidate();
            return;
        }
        if (checkSnake()){
            GameOver = true;
        }
        for (int i = pts.size()-1; i>0;i--){
            pts.get(i).set(pts.get(i-1).x,pts.get(i-1).y);
        }
        pts.get(0).set(head.x, head.y);
        invalidate();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_DPAD_DOWN:
                if (dirt != DIRT.UP)
                    dirt = DIRT.DOWN;break;
            case KeyEvent.KEYCODE_DPAD_UP:
                if (dirt != DIRT.DOWN)
                    dirt = DIRT.UP;break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                if (dirt != DIRT.RIGHT)
                    dirt = DIRT.LEFT;break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if (dirt != DIRT.LEFT)
                    dirt = DIRT.RIGHT;break;
            case KeyEvent.KEYCODE_SPACE:
                dirt = DIRT.PAUSE;
                break;
            case KeyEvent.KEYCODE_ENTER:
//                重新开始
                head.set(600,200);
                pts.clear();
                for (int i = 0; i <SnakeBody; i++)
                    pts.add(new Point(head.x + i*W, head.y));
                dirt = DIRT.LEFT;
                GameOver = false;
                newFood();
                break;
        }
        return true;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            x1 = event.getX();
            y1 = event.getY();
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE){
            x2 = event.getX();
            y2 = event.getY();
            if (y1-y2>50){
                Log.d("TAG", "onTouchEvent: 上滑");
                if (dirt != DIRT.DOWN)
                    dirt = DIRT.UP;
            }else if (y2-y1>50){
                Log.d("TAG", "onTouchEvent: 下滑");
                if (dirt != DIRT.UP)
                    dirt = DIRT.DOWN;
            }else if (x1-x2>50){
                Log.d("TAG", "onTouchEvent: 左滑");
                if (dirt != DIRT.RIGHT)
                    dirt = DIRT.LEFT;
            }else if (x2-x1>50){
                Log.d("TAG", "onTouchEvent: 右滑");
                if (dirt != DIRT.LEFT)
                    dirt = DIRT.RIGHT;
            }
        }
        return true;
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        head.set(200,200);
        setFocusable(true);

        for(int i = 0; i<SnakeBody; i++){
            pts.add(new Point(head.x,head.y + i*H));
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    snakeMove();
                }
            }
        }).start();
        newFood();
        bmp = BitmapFactory.decodeResource(getResources(),R.drawable.over);
    }
    public void newFood(){
        Random r = new Random();
//        获得屏幕像素
        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int x = r.nextInt(width)/W*W;
        int y = r.nextInt(height-200)/H*H;
        food.set(x,y);
    }
    public boolean checkSnake(){
        if ((head.x<0)||(head.y<0)){
            return true;
        }
        if ((head.x>=getWidth())||(head.y>=getHeight())){
            return true;
        }
        for (int i = 0; i<pts.size();i++){
            if ((pts.get(i).x == head.x)&&(pts.get(i).y == head.y)){
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        Paint p = new Paint();
        p.setColor(Color.YELLOW);
        Paint snakeHead = new Paint();
        snakeHead.setColor(Color.RED);
        if (GameOver){
            canvas.drawBitmap(bmp,0,0,p);
            return;
        }
        for (int i =0;i<pts.size();i++){
            int x = pts.get(i).x;
            int y = pts.get(i).y;
            canvas.drawRect(x,y,x+W,y+H,p);
        }
        canvas.drawRect(head.x,head.y,head.x+W,head.y+H,snakeHead);
        Paint test = new Paint();
        test.setColor(Color.GREEN);
        canvas.drawRect(food.x,food.y,food.x+W,food.y+H,test);
    }
}
