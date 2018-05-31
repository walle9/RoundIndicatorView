package com.example.walle9.roundindicatorview;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private RoundView mRoundView;
    private EditText mEditText;
    private Integer mCurrentNum;
    private int mMaxNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRoundView = findViewById(R.id.RoundView);
        mEditText = findViewById(R.id.et);
    }

    public void click(View view) {
        mMaxNum = mRoundView.getMaxNum();//获取当前最大刻度
        String text = mEditText.getText().toString().trim();
        if (!TextUtils.isEmpty(text)) {
            mCurrentNum = Integer.decode(text);
            //设置小球动画Holder
            PropertyValuesHolder currentNumValuesHolder = PropertyValuesHolder.ofInt
                    ("currentNum", 0, mCurrentNum);

            int currentColor = getCurrentRgb(mCurrentNum);//获取当前设置的刻度最终的颜色
            //设置背景动画Holder
            PropertyValuesHolder backgroundColorValuesHolder = PropertyValuesHolder.ofObject
                    ("BackgroundColor", new ArgbEvaluator(), Color.parseColor("#e55e10"),
                            currentColor);
            ValueAnimator animator = ObjectAnimator.ofPropertyValuesHolder(mRoundView,
                    currentNumValuesHolder, backgroundColorValuesHolder);
            animator.setDuration(3000);
            animator.start();
        }
    }

    //获取当前设置的结束颜色
    private int getCurrentRgb(int currentNum) {
        ArgbEvaluator evealuator = new ArgbEvaluator();
        float fraction;
        int color;
        fraction = (float) currentNum / (mMaxNum);
        color = (int) evealuator.evaluate(fraction, Color.parseColor("#e55e10"), Color.BLUE);//由橙到蓝
        return color;
    }

}
