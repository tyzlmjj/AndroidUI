package com.mjj.gaussianblur;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.widget.ImageView;

/**
* 高斯模糊
*/
public class GaussianBlur
{
    static {
        System.loadLibrary("jni-Demo");
    }

    public static native int[] toGaussianBlur(int[] pix,int radius,int width,int height);
    public static native void toBoxBlur(int[] pix,int[] newPix,int radius,int width,int height);

    /**
     * 通过jni进行图片模糊，均值模糊算法
     * @param bkg       需要模糊的bitmap
     * @param radius    模糊半径
     * @param view      显示模糊图片的ImageView
     * @return          消耗时间,单位毫秒（ms）
     */
    public static long blurByJni_box(Bitmap bkg, int radius, ImageView view) {
        long startMs = System.currentTimeMillis();
        float scaleFactor = 8;
        int width = (int)(view.getMeasuredWidth()/scaleFactor);
        int height = (int)(view.getMeasuredHeight()/scaleFactor);


        Bitmap overlay = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);

        int[] pixels = new int[width*height];
        overlay.getPixels(pixels, 0, width, 0, 0, width, height);

        int[] newPixels = new int[width*height];
        toBoxBlur(pixels, newPixels, radius, width, height);

        overlay.setPixels(newPixels, 0, width, 0, 0, width, height);

        view.setImageBitmap(overlay);

        return System.currentTimeMillis() - startMs;
    }


    /**
     * 通过jni进行图片模糊,一维高斯算法
     * @param bkg       需要模糊的bitmap
     * @param radius    模糊半径
     * @param view      显示模糊图片的ImageView
     * @return          消耗时间,单位毫秒（ms）
     */
    public static long blurByJni_Gaussian(Bitmap bkg, int radius, ImageView view) {
        long startMs = System.currentTimeMillis();
        float scaleFactor = 8;
        int width = (int)(view.getMeasuredWidth()/scaleFactor);
        int height = (int)(view.getMeasuredHeight()/scaleFactor);


        Bitmap overlay = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);

        int[] pixels = new int[width * height];
        overlay.getPixels(pixels, 0, width, 0, 0, width, height);
        int[] newPixels = toGaussianBlur(pixels,radius,width,height);
        overlay.setPixels(newPixels, 0, width, 0, 0, width, height);

        view.setImageBitmap(overlay);

        return System.currentTimeMillis() - startMs;
    }


    /**
     * 通过RenderScript进行图片模糊
     * @param bkg       需要模糊的bitmap
     * @param radius    模糊半径，RenderScript规定范围为[1,25]
     * @param view      显示模糊图片的ImageView
     * @param context   上下文
     * @return          消耗时间,单位毫秒（ms）
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static long blurByRenderScript(Bitmap bkg,int radius, ImageView view,Context context)
    {
        long startMs = System.currentTimeMillis();
        float scaleFactor = 8;

        int width = (int)(view.getMeasuredWidth()/scaleFactor);
        int height = (int)(view.getMeasuredHeight()/scaleFactor);

        Bitmap overlay = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);

        RenderScript rs = RenderScript.create(context);

        Allocation overlayAlloc = Allocation.createFromBitmap(rs, overlay);
        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rs, overlayAlloc.getElement());
        blur.setInput(overlayAlloc);
        blur.setRadius(radius);
        blur.forEach(overlayAlloc);
        overlayAlloc.copyTo(overlay);

        view.setImageBitmap(overlay);

        rs.destroy();

        return System.currentTimeMillis() - startMs;
    }

}
