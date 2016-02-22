//引入头文件
#include "com_mjj_gaussianblur_GaussianBlur.h"
#include <jni.h>
#include <math.h>
#include <stdio.h>
#include <stdlib.h>


jint*  boxesForGauss(jint sigma,jint n)
{
    jint wIdeal = sqrt((12*sigma*sigma/n)+1);
    jint wl = floor(wIdeal);if(wl%2==0) {wl--;}
    jint wu = wl+2;

    jint mIdeal = (12*sigma*sigma - n*wl*wl - 4*n*wl - 3*n)/(-4*wl - 4);
    jlong m = round(mIdeal);

    jint sizes[n];
    for(int i=0; i<n; i++)
    {
        sizes[i] = (jint) (i<m?wl:wu);
    }
    return sizes;
}

void boxBlurH (jint* scl,jint* tcl,jint w,jint h,jint r)
{
    jfloat iarr = 1.0 / (r+r+1);
    for(int i=0; i<h; i++)
    {
        jint ti = i*w, li = ti, ri = ti+r;
        jint fv = scl[ti],lv = scl[ti+w-1];

        int val_r = (r+1)*((fv >> 16) & 0xFF);
        int val_g = (r+1)*((fv >> 8) & 0xFF);
        int val_b = (r+1)*(fv & 0xFF);

        for(int j=0; j<r; j++)
        {
            val_r += (scl[ti+j] >> 16) & 0xFF;
            val_g += (scl[ti+j] >> 8) & 0xFF;
            val_b += scl[ti+j] & 0xFF;
        }

        for(int j=0  ; j<=r ; j++)
        {
            jint index = ri++;
            val_r += ((scl[index] >> 16) & 0xFF) - ((fv >> 16) & 0xFF);
            val_g += ((scl[index] >> 8) & 0xFF) - ((fv >> 8) & 0xFF);
            val_b += (scl[index] & 0xFF) -  (fv & 0xFF);

            tcl[ti++] = (0xFF << 24) | (((jint)round(val_r*iarr)) << 16) | (((jint)round(val_g*iarr)) << 8) | ((jint)round(val_b*iarr));
        }

        for(int j=r+1; j<w-r; j++)
        {
            jint index = ri++;
            jint index2 = li++;
            val_r += ((scl[index] >> 16) & 0xFF) - ((scl[index2] >> 16) & 0xFF);
            val_g += ((scl[index] >> 8) & 0xFF) - ((scl[index2] >> 8) & 0xFF);
            val_b += (scl[index] & 0xFF) -  (scl[index2] & 0xFF);

            tcl[ti++] = (0xFF << 24) | (((jint)round(val_r*iarr)) << 16) | (((jint)round(val_g*iarr)) << 8) | ((jint)round(val_b*iarr));
        }

        for(int j=w-r; j<w  ; j++)
        {
            jint index = li++;
            val_r += ((lv >> 16) & 0xFF) - ((scl[index] >> 16) & 0xFF);
            val_g += ((lv >> 8) & 0xFF) - ((scl[index] >> 8) & 0xFF);
            val_b += (lv & 0xFF) -  (scl[index] & 0xFF);

            tcl[ti++] = (0xFF << 24) | (((jint)round(val_r*iarr)) << 16) | (((jint)round(val_g*iarr)) << 8) | ((jint)round(val_b*iarr));
        }
    }
}

void boxBlurT (jint* scl,jint* tcl,jint w,jint h,jint r) {
    float iarr = 1.0 / (r+r+1);
    for(int i=0; i<w; i++) {
        jint ti = i, li = ti, ri = ti+r*w;
        jint fv = scl[ti], lv = scl[ti+w*(h-1)];

        jint val_r = (r+1)*((fv >> 16) & 0xFF);
        jint val_g = (r+1)*((fv >> 8) & 0xFF);
        jint val_b = (r+1)*(fv & 0xFF);

        for(int j=0; j<r; j++)
        {
            val_r += (scl[ti+j*w] >> 16) & 0xFF;
            val_g += (scl[ti+j*w] >> 8) & 0xFF;
            val_b += scl[ti+j*w] & 0xFF;
        }

        for(int j=0  ; j<=r ; j++)
        {
            val_r += ((scl[ri] >> 16) & 0xFF) - ((fv >> 16) & 0xFF);
            val_g += ((scl[ri] >> 8) & 0xFF) - ((fv >> 8) & 0xFF);
            val_b += (scl[ri] & 0xFF) -  (fv & 0xFF);

            tcl[ti] = (0xFF << 24) | (((jint)round(val_r*iarr)) << 16) | (((jint)round(val_g*iarr)) << 8) | ((jint)round(val_b*iarr));

            ri+=w;ti+=w;
        }

        for(int j=r+1; j<h-r; j++)
        {
            val_r += ((scl[ri] >> 16) & 0xFF) - ((scl[li] >> 16) & 0xFF);
            val_g += ((scl[ri] >> 8) & 0xFF) - ((scl[li] >> 8) & 0xFF);
            val_b += (scl[ri] & 0xFF) -  (scl[li] & 0xFF);
            tcl[ti] = (0xFF << 24) | (((jint)round(val_r*iarr)) << 16) | (((jint)round(val_g*iarr)) << 8) | ((jint)round(val_b*iarr));
            li+=w; ri+=w; ti+=w;
        }

        for(jint j=h-r; j<h  ; j++)
        {
            val_r += ((lv >> 16) & 0xFF) - ((scl[li] >> 16) & 0xFF);
            val_g += ((lv >> 8) & 0xFF) - ((scl[li] >> 8) & 0xFF);
            val_b += (lv & 0xFF) -  (scl[li] & 0xFF);

            tcl[ti] = (0xFF << 24) | (((jint)round(val_r*iarr)) << 16) | (((jint)round(val_g*iarr)) << 8) | ((jint)round(val_b*iarr));
            li+=w; ti+=w;
        }
    }
}

void boxBlur (jint* scl,jint* tcl,jint w,jint h,jint r) {
    for(jint i=0; i<w*h; i++)
    {
        tcl[i] = scl[i];
    }
    boxBlurH(tcl,scl, w, h, r);
    boxBlurT(scl, tcl, w, h, r);
}

JNIEXPORT void JNICALL
Java_com_mjj_gaussianblur_GaussianBlur_toBoxBlur(JNIEnv *env, jclass type, jintArray pix_,jintArray newPix_,
                                                 jint radius, jint width, jint height) {
    jint *pix = env->GetIntArrayElements(pix_, NULL);
    jint newPix[width*height];

    jint* bxs = boxesForGauss(radius, 3);

    jint radiusA = (bxs[0] - 1) / 2;
    jint radiusB = (bxs[1] - 1) / 2;
    jint radiusC = (bxs[2] - 1) / 2;

    free(bxs);

    boxBlur(pix, newPix, width, height, radiusA);
    boxBlur(newPix, pix, width, height, radiusB);
    boxBlur(pix, newPix, width, height, radiusC);

    env->SetIntArrayRegion(newPix_,0,width*height,newPix);
    env->ReleaseIntArrayElements(pix_, pix, 0);
    env->ReleaseIntArrayElements(pix_, pix, 0);
}