#include "common.h"

void drawPixel(JNIEnv* env, jobject img, int x, int y, int argb) {
    const char *szClassName = "java/awt/image/BufferedImage";
    const char *szMethodName = "setRGB";
    const char *szMethodSig = "(III)V";
    
    jclass BufferedImage = env->FindClass(szClassName);
    jmethodID setRGB = env->GetMethodID(BufferedImage, szMethodName, szMethodSig);
    
    env->CallVoidMethod(img, setRGB, x, y, argb);
}

