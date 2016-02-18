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
import android.util.Log;
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

    /**
     * 通过jni进行图片模糊
     * @param bkg       需要模糊的bitmap
     * @param radius    模糊半径
     * @param view      显示模糊图片的ImageView
     */
    public static void blurByJni(Bitmap bkg, int radius, ImageView view) {
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
        int[] newPixels = toGaussianBlur(pixels,radius,width,height);
        overlay.setPixels(newPixels, 0, width, 0, 0, width, height);

        view.setImageBitmap(overlay);
        Log.i("asd","cost " + (System.currentTimeMillis() - startMs) + "ms");
    }


    /**
     * 通过RenderScript进行图片模糊
     * @param bkg       需要模糊的bitmap
     * @param radius    模糊半径，RenderScript规定范围为[1,25]
     * @param view      显示模糊图片的ImageView
     * @param context   上下文
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void blurByRenderScript(Bitmap bkg,int radius, ImageView view,Context context)
    {
        long startMs = System.currentTimeMillis();
        //[1,25] 不能超出这个范围
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
        Log.i("asd", "cost " + (System.currentTimeMillis() - startMs) + "ms");
    }

    /**
     * 高斯模糊
     * @param buf       需要模糊的颜色数组
     * @param radius    模糊半径
     * @param w         图像宽度
     * @param h         图像高度
     * @return 模糊后的一维数组
     */
    private static int[] doGaussianBlur(int[] buf,int radius, int w, int h)
    {
        //总的像素
        int size=w * h;
        //新图像指针
        int[] newBuf = new int[size];
        int[] temBuf = new int[size];

        if (buf == null) {
            return null;
        }

        int alpha = buf[0] & 0xFF000000;

        //权重
        double[]  weight = oneDimensionalWeight(radius);

        for (int i = 0; i < size; ++i)
        {
            int hcolor = oneDimensionalGaussian( i, size, buf, radius, weight, 1);
            temBuf[i] = alpha | hcolor;
        }
        for (int i = 0; i < size; ++i)
        {
            int color = oneDimensionalGaussian( i, size, temBuf, radius, weight, w);
            newBuf[i] = alpha | color;
        }

        return newBuf;
    }

    /**
     * 计算权重（一维）
     * @param radius	模糊半径
     */
    private static double[] oneDimensionalWeight(int radius)
    {
        double sum = 0.0;
        int size = radius*2+1;
        double[] weightArray = new double[size];

        double v = 64;

        for (int i = 0; i <= radius; ++i)
        {
            weightArray[radius-i] = 1.0/(v*2*Math.PI)*Math.pow(Math.E, -(i * i / (2 * v)));
            sum+=weightArray[radius-i];
        }

        for (int j = 0; j < radius; ++j)
        {
            weightArray[size-1-j] = weightArray[j];
            sum+=weightArray[j];
        }

        //归一
        if(sum!=1.0)
        {
            for (int i = 0; i < size; ++i)
            {
                weightArray[size-1-i] /= sum;
            }
        }
        return weightArray;
    }


    /**
     * 一维高斯模糊
     * @param n             计算的像素中心点
     * @param size          总像素大小
     * @param cbuf          像素指针
     * @param radius        模糊半径
     * @param weight        权重指针
     * @param coefficient   系数（为了计算方便）
     */
    private static int oneDimensionalGaussian(int n,int size,int[] cbuf,int radius,double[] weight,int coefficient)
    {
        //临时存放需要计算的像素点
        int[] tem = new int[radius*2+1];
        //获取需要计算的像素点
        for (int j = 0; j < radius*2+1; ++j)
        {
            int index = n - (radius - j)*coefficient;
            tem[j] = cbuf[n];
            if (0 <= index && index < size)
            {
                tem[j] = cbuf[index];
            }
        }

        //置零
        double red_sum = 0;
        double green_sum = 0;
        double blue_sum = 0;
        //RGB分离
        for (int j = 0; j < radius*2+1; ++j)
        {
            int red = ((tem[j] & 0x00FF0000) >> 16);
            int green = ((tem[j] & 0x0000FF00) >> 8);
            int blue = tem[j] & 0x000000FF;

            red_sum += red * weight[j];
            green_sum += green * weight[j];
            blue_sum += blue * weight[j];
        }
        return ((int)(red_sum))<<16 | ((int)(green_sum))<<8 |((int)(blue_sum));
    }



}
