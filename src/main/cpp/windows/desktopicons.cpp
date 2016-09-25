#include <windows.h>
#include <shellapi.h>
#include <jni.h>

#include "desktopicons.h"
#include "../jniload.h"
#include "../common.h"

HBITMAP getResizedBitmap(HICON hIcon, int w, int h) {
	HDC hDc = GetDC(NULL);
	HDC hMemDc = CreateCompatibleDC(hDc);
	HBITMAP hMemBmp = CreateCompatibleBitmap(hDc, w, h);
	HGDIOBJ hGdi = SelectObject(hMemDc, hMemBmp);

	DrawIconEx(hMemDc, 0, 0, hIcon, w, h, 0, NULL, DI_NORMAL);

	SelectObject(hMemDc, hGdi);
	DeleteDC(hMemDc);
	ReleaseDC(NULL, hDc);

	return hMemBmp;
}

void drawImage(JNIEnv* env, jobject bufferedImage, HICON hIcon, 
	int x, int y, int w, int h) {
	HDC hDc = GetDC(NULL);
	HBITMAP hBmp = getResizedBitmap(hIcon, w, h);
	
	SelectObject(hDc, hBmp);
	
	for(int col = 0; col < w; col++) {
		for(int row = 0; row < h; row++) {
			COLORREF cRef = GetPixel(hDc, col, row);
			drawPixel(env, bufferedImage, x+col, y+row, (int)cRef);
		}
	}
	SelectObject(hDc, NULL);
	ReleaseDC(NULL, hDc);
	DeleteObject(hBmp);
}

/*
 * Class:     ca_hedlund_desktopicons_DesktopIcons
 * Method:    _drawIconForPath
 * Signature: (Ljava/lang/String;Ljava/awt/image/BufferedImage;IIII)I
 */
JNIEXPORT jint JNICALL Java_ca_hedlund_desktopicons_DesktopIcons__1drawIconForPath
	(JNIEnv *env, jclass DesktopIcons, jstring path, jobject bufferedImage, jint x, jint y, jint w, jint h) {
	jboolean isCopy;
	const char *szPath = env->GetStringUTFChars(path, &isCopy);
	wchar_t* pszPath = new wchar_t[strlen(szPath)+1];
	mbstowcs(pszPath, szPath, strlen(szPath)+1);
	env->ReleaseStringUTFChars(path, szPath);

	DWORD hr = 0;
	

	SHFILEINFOW sfi = {0};
	hr = SHGetFileInfo(pszPath, 0, &sfi, sizeof(SHFILEINFOW), SHGFI_ICON);
	
	if(hr != 0) {
		return ca_hedlund_desktopicons_DesktopIcons_ICON_NOT_FOUND;
	}

	drawImage(env, bufferedImage, sfi.hIcon, x, y, w, h);

	delete [] pszPath;
}

/*
 * Class:     ca_hedlund_desktopicons_DesktopIcons
 * Method:    _drawIconForFileType
 * Signature: (Ljava/lang/String;Ljava/awt/image/BufferedImage;IIII)I
 */
JNIEXPORT jint JNICALL Java_ca_hedlund_desktopicons_DesktopIcons__1drawIconForFileType
  (JNIEnv *env, jclass DesktopIcons, jstring type, jobject bufferedImage, jint x , jint y, jint w, jint h)  {
	return 2;
}

/*
 * Class:     ca_hedlund_desktopicons_DesktopIcons
 * Method:    _drawStockIcon
 * Signature: (ILjava/awt/image/BufferedImage;IIII)I
 */
JNIEXPORT jint JNICALL Java_ca_hedlund_desktopicons_DesktopIcons__1drawStockIcon
  (JNIEnv *env, jclass DesktopIcons, jint iconId, jobject bufferedImage, jint x, jint y, jint w, jint h) {
	return 2;
}

