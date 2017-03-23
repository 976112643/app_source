package com.widget.imageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageLoadingProgressListener;

/**
 * 有加载进度显示的ImageView
 * @author WQ 下午2:37:02
 */
public class ProcessImageView extends ImageView implements ImageLoadingProgressListener ,ImageLoadingListener{

    private Paint mPaint;// 画笔
    int width = 0;
    int height = 0;
    Context context = null;
    int progress = -1;

    public ProcessImageView(Context context) {
        super(context);
    }

    public ProcessImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProcessImageView(Context context, AttributeSet attrs,
            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        mPaint = new Paint();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setAntiAlias(true); // 消除锯齿
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor("#00000000"));// 全透明

        mPaint.setTextSize(40);
        mPaint.setColor(Color.parseColor("#FFFFFF"));
        mPaint.setStrokeWidth(2);
        Rect rect = new Rect();
        mPaint.getTextBounds("100%", 0, "100%".length(), rect);// 确定文字的宽度
        if(progress<100&& progress>=0){
        canvas.drawText(progress + "%", getWidth() / 2 - rect.width() / 2,
                getHeight() / 2-rect.height()/2, mPaint);
        }else {
        }
    }

    public void setProgress(int progress) {
        this.progress = progress;
        postInvalidate();
    }

	@Override
	public void onProgressUpdate(String arg0, View arg1, int arg2, int arg3) {
		setProgress((int) (arg2*100.0f/arg3));
	}

	@Override
	public void onLoadingCancelled(String arg0, View arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
		setProgress(100);
	}

	@Override
	public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
		setProgress(-1);
	}

	@Override
	public void onLoadingStarted(String arg0, View arg1) {
		
	};

}