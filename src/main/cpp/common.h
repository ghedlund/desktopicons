#include <jni.h>

/**
 * Set pixel color for given buffered image.
 * 
 * @param env 
 * @param img must be of type BufferedImage
 * @param x
 * @param y
 * @argb color in ARGB format
 */
void drawPixel(JNIEnv* env, jobject img, int x, int y, int argb);

