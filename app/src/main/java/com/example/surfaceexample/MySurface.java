package com.example.surfaceexample;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ResourceBundle;

public class MySurface extends SurfaceView implements SurfaceHolder.Callback {

    //Переменные для рисования
    float x, y;//текущее положение картинки
    float tx, ty;//коорденаты точки касания
    float dx, dy;//смещение координат
    float koeff;//коэффициент скорость

    //переменные для картинки
    Bitmap image;
    Resources res;
    Paint paint;

    //объект потока
    DrawThread drawThread;

    public MySurface(Context context) {
        super(context);
        x = 100;
        y = 100;
        koeff = 5;
        res = getResources();
        image = BitmapFactory.decodeResource(res, R.drawable.snowboarding);
        paint = new Paint();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            tx = event.getX();
            ty = event.getY();
        }
        return true;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawBitmap(image, x, y, paint);
        //расчет смещения
        if (tx != 0) calculate();
        x += dx;
        y += dy;
    }

    private void calculate(){
        double g = Math.sqrt((tx-x)*(tx-x)+(ty-y)*(ty-y));
        dx = (float) (koeff * (tx-x)/g);
        dy = (float) (koeff * (ty-y)/g);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        drawThread = new DrawThread(this, getHolder());
        drawThread.setRun(true);
        drawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean stop = true;
        drawThread.setRun(false);
        while (stop) {
            try {
                drawThread.join();
                stop = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
