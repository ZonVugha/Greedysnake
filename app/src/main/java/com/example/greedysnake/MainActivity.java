package com.example.greedysnake;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

public class MainActivity extends AppCompatActivity {
    private float  x1 ,x2;
    private float y1 ,y2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        setContentView(new MyView(this,null));

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
            }else if (y2-y1>50){
                Log.d("TAG", "onTouchEvent: 下滑");
            }else if (x1-x2>50){
                Log.d("TAG", "onTouchEvent: 左滑");
            }else if (x2-x1>50){
                Log.d("TAG", "onTouchEvent: 右滑");
            }
        }
        return false;
    }

}