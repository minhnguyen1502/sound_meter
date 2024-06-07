package com.example.soundmeter.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.soundmeter.R;

public class SoundView extends View {

    private float scaleWidth, scaleHeight;
    private int newWidth, newHeight;
    private final Matrix matrix = new Matrix();
    private Bitmap indiBitmap;
    private Paint paint = new Paint();
    static final long ANIMATION_INTERVAL = 20;
    public SoundView(Context context) {
        super(context);
    }

    public SoundView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        init(w, h);
    }

    private void init(int width, int height) {
        Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_needle);
        int bitmapHeight = mBitmap.getHeight();
        int bitmapWidth = mBitmap.getWidth();

        newHeight = height;
        newWidth = width;

        scaleWidth = ((float) newWidth) / ((float) bitmapWidth);
        scaleHeight = ((float) newHeight) / ((float) bitmapHeight);
        matrix.postScale(scaleWidth, scaleHeight);
        indiBitmap = Bitmap.createBitmap(mBitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, true);

        paint = new Paint();
        paint.setTextSize(40);
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.WHITE);
    }

    public void refresh() {
        postInvalidateDelayed(ANIMATION_INTERVAL);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (indiBitmap != null) {
            matrix.setRotate(getAngle(World.dbCount), newWidth / 2, newHeight * 229f / 460);
            canvas.drawBitmap(indiBitmap, matrix, paint);
        }
    }

    private float getAngle(float dbCount) {
        float minDb = 0;
        float maxDb = 140;

        return (dbCount - minDb) * 225.5f / (maxDb - minDb);
    }
}
