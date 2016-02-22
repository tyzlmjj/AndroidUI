//引入头文件
#include "com_mjj_gaussianblur_GaussianBlur.h"
#include <jni.h>
#include <math.h>
#include <stdio.h>
#include <stdlib.h>
//导入日志头文件
#include <android/log.h>
//修改日志tag中的值
#define LOG_TAG "asd"
//日志显示的等级
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

/**
 * 计算权重（一维）
 * @param *env
 * @param radius	模糊半径
 */
jdoubleArray oneDimensionalWeight(JNIEnv *env,jint radius)
{
     double sum = 0.0;
     int size = radius*2+1;
     double weightArray[size];

     double v = 64;

     for (int i = 0; i <= radius; ++i)
     {
         weightArray[radius-i] = 1.0/(v*2*M_PI)*pow(M_E,-(i*i/(2*v)));
         sum+=weightArray[radius-i];
     }

     for (int j = 0; j < radius; ++j)
     {
         weightArray[size-1-j] = weightArray[j];
         sum+=weightArray[j];
     }

     if(sum!=1.0)
     {
         for (int i = 0; i < size; ++i)
         {
             weightArray[size-1-i] /= sum;
         }
     }

     jdoubleArray  array = env->NewDoubleArray(size);
     env->SetDoubleArrayRegion(array,0,size,weightArray);
     free(weightArray);
     return array;
}

/**
 * 一维高斯模糊
 * @param n             计算的像素中心点
 * @param size          总像素大小
 * @param cbuf	        像素指针
 * @param radius	    模糊半径
 * @param weight       权重指针
 * @param coefficient	系数（为了计算方便）
 */
jint oneDimensionalGaussian(jint n,jint size,jint *cbuf,jint radius,jdouble *weight,jint coefficient)
{

    //临时存放需要计算的像素点
    int tem[radius*2+1];
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

/**
 * 一维高斯模糊
 * @param buf           像素数组
 * @param radius        模糊半径
 * @param w     	    宽度
 * @param h             高度
 */
JNIEXPORT jintArray JNICALL Java_com_mjj_gaussianblur_GaussianBlur_toGaussianBlur
        (JNIEnv *env, jclass c, jintArray buf,jint radius, jint w, jint h)
{
    //总的像素
    int size=w * h;
    //新图像指针
    int newBuf[size];
    int temBuf[size];
    //原图像指针
    int *oldBuf;
    oldBuf = env->GetIntArrayElements(buf, false);

    if (oldBuf == NULL) {
        return 0;
    }

    int alpha = oldBuf[0] & 0xFF000000;

    //权重
    jdoubleArray  temArray = oneDimensionalWeight(env,radius);
    double *weight = env->GetDoubleArrayElements(temArray,false);

    for (int i = 0; i < size; ++i)
    {
        int hcolor = oneDimensionalGaussian( i, size, oldBuf, radius, weight, 1);
        temBuf[i] = alpha | hcolor;
    }
    for (int i = 0; i < size; ++i)
    {
        int color = oneDimensionalGaussian( i, size, temBuf, radius, weight, w);
        newBuf[i] = alpha | color;
    }

    jintArray result = env->NewIntArray(size);
    env->SetIntArrayRegion(result, 0, size, newBuf);
    env->ReleaseIntArrayElements(buf, oldBuf, 0);
    env->ReleaseDoubleArrayElements(temArray, weight, 0);
    free(newBuf);
    free(temBuf);

    return result;
}


