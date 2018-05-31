package com.example.walle9.roundindicatorview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by walle9 on 2018/5/26.
 * 描述:
 */
public class RoundView extends View {

    private int mMaxNum;
    private int mStartAngle;
    private int mSweepAngle;
    private int mSweepInWidth;
    private int mSweepOutWidth;
    private Paint mPaint;
    private Paint mPaint_2;
    private Paint mPaint_3;
    private Paint mPaint_4;
    private int mWidth;
    private int mHeight;
    private int mRadius;
    private int mPx;
    private int currentNum;

    public void setCurrentNum(int currentNum) {
        this.currentNum = currentNum;
        this.invalidate();
    }

    public int getCurrentNum() {
        return currentNum;
    }

    public int getMaxNum() {
        return mMaxNum;
    }


    public RoundView(Context context) {
        this(context, null);
    }

    public RoundView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        init();
    }

    private void initAttrs(Context context, @Nullable AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundView);
        mMaxNum = typedArray.getInteger(R.styleable.RoundView_maxNum, 500);
        mStartAngle = typedArray.getInteger(R.styleable.RoundView_startAngle, 160);
        mSweepAngle = typedArray.getInteger(R.styleable.RoundView_sweepAngle, 220);
        typedArray.recycle();
    }

    private void init() {
        //内外圆弧的宽度
        mSweepInWidth = UIUtils.dip2Px(15);
        mSweepOutWidth = UIUtils.dip2Px(5);
        //抗锯齿
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setDither(true);  //防抖动
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(0xffffffff);
        mPaint_2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint_3 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint_4 = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    //对于不是确定值的直接给定320*480的大小
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        } else {
            mWidth = 320;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        } else {
            mHeight = 480;
        }

        setMeasuredDimension(mWidth, mHeight);//设置最终宽高
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mRadius = getMeasuredWidth() / 3;
        canvas.save();
        canvas.translate(mWidth / 2, mHeight / 2);
        drawRound(canvas);//画圆弧
        drawScale(canvas);//画刻度
        drawIndicator(canvas);//画进度
        drawCenterText(canvas);//画信用值
        canvas.restore();
    }

    private void drawCenterText(Canvas canvas) {
        canvas.save();
        mPaint_4.setStyle(Paint.Style.FILL);
        mPaint_4.setTextSize(mRadius / 2);
        mPaint_4.setColor(0x99ffffff);
        canvas.drawText(currentNum + "", -mPaint_4.measureText(currentNum + "") / 2, 0, mPaint_4);
        //x值传入测量宽度除以2(这种测量只能测量宽度)或者像绘制度数的时候传入0,然后setTextAlign(Paint.Align.CENTER)
        mPaint_4.setTextSize(mRadius / 4);
        String content = "信用";
        if (currentNum < mMaxNum * 1 / 5) {
            content += text[0];
        } else if (currentNum >= mMaxNum * 1 / 5 && currentNum < mMaxNum * 2 / 5) {
            content += text[1];
        } else if (currentNum >= mMaxNum * 2 / 5 && currentNum < mMaxNum * 3 / 5) {
            content += text[2];
        } else if (currentNum >= mMaxNum * 3 / 5 && currentNum < mMaxNum * 4 / 5) {
            content += text[3];
        } else if (currentNum >= mMaxNum * 4 / 5) {
            content += text[4];
        }

        //使用这种测量可以测量出左上右下
        Rect r = new Rect();
        mPaint_4.getTextBounds(content, 0, content.length(), r);
        canvas.drawText(content, -r.width() / 2, r.height() + 20, mPaint_4);
        canvas.restore();
    }

    private void drawIndicator(Canvas canvas) {
        int sweep;//当前扫过的弧度
        if (currentNum <= mMaxNum) {
            sweep = (int) (currentNum * mSweepAngle / (float) mMaxNum);
        } else {
            sweep = mSweepAngle;
        }
        canvas.save();

        if (mStartAngle + sweep > 360) {//当我们角度>360度的时候,我们不能继续渐变,因为渐变就是从0度开始的
            //开始旋转
            canvas.rotate(mStartAngle + sweep - 360);//当前的角度超过360度几度我们就旋转几度
            //那开始角度又透明了,处理不处理随便了.
        }
        mPaint_2.setStyle(Paint.Style.STROKE);
        mPaint_2.setStrokeWidth(mSweepOutWidth);
        int[] colors = {0x00ffffff, Color.GREEN, 0x00ffffff};

        float[] positions = {mStartAngle / 360.f, (mStartAngle + sweep) / 360.f, (mStartAngle +
                sweep) / 360.f};
        Shader shader = new SweepGradient(0, 0, colors, positions);
        mPaint_2.setShader(shader);
        RectF rectFO = new RectF(-mRadius - mPx, -mRadius - mPx, mRadius + mPx, mRadius + mPx);
        canvas.drawArc(rectFO, mStartAngle, mSweepAngle, false, mPaint_2);
        mPaint_2.setStyle(Paint.Style.FILL);
        //canvas.drawCircle(0,0,mRadius+mPx,mPaint_2);//我们这里下半部分是透明的,实际上也可以直接画圆环
        canvas.restore();


        canvas.save();
        //画一个亮闪闪的小球把!
        mPaint_3.setStyle(Paint.Style.FILL);
        mPaint_3.setColor(0xffffffff);
        //当前的弧度
        int radians = mStartAngle + sweep;
        float y = (float) ((mRadius + mPx) * Math.sin(Math.toRadians(radians)));
        float x = (float) ((mRadius + mPx) * Math.cos(Math.toRadians(radians)));
        //设置模糊遮罩滤镜,记得要关闭硬件加速
        mPaint_3.setMaskFilter(new BlurMaskFilter(UIUtils.dip2Px(5), BlurMaskFilter.Blur.SOLID));
        //关闭此VIEW的硬件加速,也可以在清单文件中使用android:hardwareAccelerated="false"关闭activity的硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        canvas.drawCircle(x, y, UIUtils.dip2Px(4), mPaint_3);
        canvas.restore();
    }

    private String[] text = {"较差", "中等", "良好", "优秀", "极好"};

    private void drawScale(Canvas canvas) {
        float angle = mSweepAngle / 30.0f;//刻度间隔
        canvas.save();
        canvas.rotate(-90 - (180 - mStartAngle));//-270+mStartAngle
        //逆时针旋转90度,再逆时针旋转没旋转的度数,注意,此时的旋转和已经绘制好的图形无关了.
        //(可以在不旋转的时候在0,0绘制一个文字,然后旋转90,和-90各看一下文字方向)
        //其实你可以这样理解(-270+mStartAngle),把它看成(-90-(180 - mStartAngle)),
        // 先旋转-90度,文字方向是不是头在左边脚在右边了,再把剩下的没旋转过去的继续旋转过去即可.
        //还有不要想着之前的那个画好的外圈和内圈会怎么怎么转,我们之前save了一把,所以跟他那一层没有半毛钱的关系
        for (int i = 0; i <= 30; i++) {
            if (i % 6 == 0) {//画粗刻度和刻度值
                mPaint.setStrokeWidth(UIUtils.dip2Px(2));
                mPaint.setAlpha(0x70);
                canvas.drawLine(0, -mRadius - mSweepInWidth / 2, 0, -mRadius + mSweepInWidth / 2
                        + UIUtils.dip2Px(2), mPaint);
                drawText(canvas, i * mMaxNum / 30 + "", mPaint);
            } else {//画小刻度
                mPaint.setStrokeWidth(UIUtils.dip2Px(1));
                mPaint.setAlpha(0x50);
                canvas.drawLine(0, -mRadius - mSweepInWidth / 2, 0, -mRadius + mSweepInWidth / 2,
                        mPaint);

                if ((i - 3) % 6 == 0) {  //画刻度区间文字
                    mPaint.setStrokeWidth(UIUtils.dip2Px(2));
                    drawText(canvas, text[(i - 3) / 6], mPaint);
                }
            }

            canvas.rotate(angle);
        }

        canvas.restore();
    }

    private void drawText(Canvas canvas, String text, Paint paint) {
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(UIUtils.dip2Px(15));
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(text, 0, -mRadius + UIUtils.dip2Px(25), mPaint);
        paint.setStyle(Paint.Style.STROKE);
    }

    private void drawRound(Canvas canvas) {
        //画内圆
        mPaint.setAlpha(0x44);//设置透明度,范围是00~ff
        mPaint.setStrokeWidth(mSweepInWidth);
        RectF rectF = new RectF(-mRadius, -mRadius, mRadius, mRadius);
        canvas.drawArc(rectF, mStartAngle, mSweepAngle, false, mPaint);
        //画外圆
        mPx = UIUtils.dip2Px(15);
        mPaint.setStrokeWidth(mSweepOutWidth);
        RectF rectFO = new RectF(-mRadius - mPx, -mRadius - mPx, mRadius + mPx, mRadius + mPx);
        canvas.drawArc(rectFO, mStartAngle, mSweepAngle, false, mPaint);
    }
}
