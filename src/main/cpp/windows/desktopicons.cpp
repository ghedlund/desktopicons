#include <windows.h>
#include <gdiplus.h>
#include <shellapi.h>
#include <jni.h>

#include "../../../../target/generated-sources/cpp/include/desktopicons.h"
#include "../jniload.h"
#include "../common.h"

using namespace Gdiplus;

Bitmap* GetResizedBitmap(Gdiplus::Bitmap* bmp, int w, int h) {
	UINT o_height = bmp->GetHeight();
	UINT o_width = bmp->GetWidth();
	INT n_width = w;
	INT n_height = h;
	double ratio = ((double)o_width) / ((double)o_height);
	if (o_width > o_height) {
		// Resize down by width
		n_height = static_cast<UINT>(((double)n_width) / ratio);
	}
	else {
		n_width = static_cast<UINT>(n_height * ratio);
	}
	Bitmap* newBitmap = new Gdiplus::Bitmap(n_width, n_height, bmp->GetPixelFormat());
	Graphics graphics(newBitmap);
	graphics.DrawImage(bmp, 0, 0, n_width, n_height);
	return newBitmap;
}

Bitmap* IconToBitmap(HICON hIcon) {
	// Get the icon info
	ICONINFO iconInfo = { 0 };
	GetIconInfo(hIcon, &iconInfo);

	// Get the screen DC
	HDC dc = GetDC(NULL);

	// Get icon size info
	BITMAP bm = { 0 };
	GetObject(iconInfo.hbmColor, sizeof(BITMAP), &bm);

	// Set up BITMAPINFO
	BITMAPINFO bmi = { 0 };
	bmi.bmiHeader.biSize = sizeof(BITMAPINFOHEADER);
	bmi.bmiHeader.biWidth = bm.bmWidth;
	bmi.bmiHeader.biHeight = -bm.bmHeight;
	bmi.bmiHeader.biPlanes = 1;
	bmi.bmiHeader.biBitCount = 32;
	bmi.bmiHeader.biCompression = BI_RGB;

	// Extract the color bitmap
	int nBits = bm.bmWidth * bm.bmHeight;
	INT32* colorBits = new INT32[nBits];
	GetDIBits(dc, iconInfo.hbmColor, 0, bm.bmHeight, colorBits, &bmi, DIB_RGB_COLORS);

	// Check whether the color bitmap has an alpha channel.
	// (On my Windows 7, all file icons I tried have an alpha channel.)
	BOOL hasAlpha = FALSE;
	for (int i = 0; i < nBits; i++) {
		if ((colorBits[i] & 0xff000000) != 0) {
			hasAlpha = TRUE;
			break;
		}
	}

	// If no alpha values available, apply the mask bitmap
	if (!hasAlpha) {
		// Extract the mask bitmap
		INT32* maskBits = new INT32[nBits];
		GetDIBits(dc, iconInfo.hbmMask, 0, bm.bmHeight, maskBits, &bmi, DIB_RGB_COLORS);
		// Copy the mask alphas into the color bits
		for (int i = 0; i < nBits; i++) {
			if (maskBits[i] == 0) {
				colorBits[i] |= 0xff000000;
			}
		}
		delete[] maskBits;
	}

	// Release DC and GDI bitmaps
	ReleaseDC(NULL, dc);
	::DeleteObject(iconInfo.hbmColor);
	::DeleteObject(iconInfo.hbmMask);

	// Create GDI+ Bitmap
	Gdiplus::Bitmap* bmp = new Gdiplus::Bitmap(bm.bmWidth, bm.bmHeight, bm.bmWidth * 4, PixelFormat32bppARGB, (BYTE*)colorBits);

	return bmp;
}

void DrawImage(JNIEnv* env, jobject bufferedImage, HICON hIcon, 
	int x, int y, int w, int h) {
	Bitmap* origBmp = IconToBitmap(hIcon);
	Bitmap* pBmp = GetResizedBitmap(origBmp, w, h);
	
	Gdiplus::Color color;
	for(int col = 0; col < w; col++) {
		for(int row = 0; row < h; row++) {
			pBmp->GetPixel(col, row, &color);
			drawPixel(env, bufferedImage, x + col, y + row, color.MakeARGB(color.GetA(), color.GetR(), color.GetG(), color.GetB()));
		}
	}

	delete pBmp;
	delete origBmp;
}

/*
 * Class:     ca_hedlund_desktopicons_DesktopIcons
 * Method:    _drawIconForPath
 * Signature: (Ljava/lang/String;Ljava/awt/image/BufferedImage;IIII)I
 */
JNIEXPORT jint JNICALL Java_ca_hedlund_desktopicons_DesktopIcons__1drawIconForPath
	(JNIEnv *env, jclass DesktopIcons, jstring path, jobject bufferedImage, jint x, jint y, jint w, jint h) {
	CoInitialize(NULL);
	ULONG_PTR(gdiplusToken);
	GdiplusStartupInput gdiplusstartupinput;
	GdiplusStartup(&gdiplusToken, &gdiplusstartupinput, NULL);

	jboolean isCopy;
	const char *szPath = env->GetStringUTFChars(path, &isCopy);

	SHFILEINFO sfi = {0};
	SHGetFileInfo(szPath, 0, &sfi, sizeof(SHFILEINFO), SHGFI_ICON | SHGFI_DISPLAYNAME);
	if (sfi.hIcon == NULL) {
		return ca_hedlund_desktopicons_DesktopIcons_ICON_NOT_FOUND;
	}
	env->ReleaseStringUTFChars(path, szPath);
	
	DrawImage(env, bufferedImage, sfi.hIcon, x, y, w, h);
	
	DestroyIcon(sfi.hIcon);
	CoUninitialize();
	GdiplusShutdown(gdiplusToken);

	return 0;
}

/*
 * Class:     ca_hedlund_desktopicons_DesktopIcons
 * Method:    _drawIconForFileType
 * Signature: (Ljava/lang/String;Ljava/awt/image/BufferedImage;IIII)I
 */
JNIEXPORT jint JNICALL Java_ca_hedlund_desktopicons_DesktopIcons__1drawIconForFileType
  (JNIEnv *env, jclass DesktopIcons, jstring type, jobject bufferedImage, jint x , jint y, jint w, jint h)  {
	CoInitialize(NULL);
	ULONG_PTR(gdiplusToken);
	GdiplusStartupInput gdiplusstartupinput;
	GdiplusStartup(&gdiplusToken, &gdiplusstartupinput, NULL);

	jboolean isCopy;
	const char *szPath = env->GetStringUTFChars(type, &isCopy);

	SHFILEINFO sfi = { 0 };
	SHGetFileInfo(szPath, FILE_ATTRIBUTE_NORMAL, &sfi, sizeof(SHFILEINFO), SHGFI_USEFILEATTRIBUTES | SHGFI_ICON | SHGFI_DISPLAYNAME);
	if (sfi.hIcon == NULL) {
		return ca_hedlund_desktopicons_DesktopIcons_ICON_NOT_FOUND;
	}
	env->ReleaseStringUTFChars(type, szPath);

	DrawImage(env, bufferedImage, sfi.hIcon, x, y, w, h);

	DestroyIcon(sfi.hIcon);
	CoUninitialize();
	GdiplusShutdown(gdiplusToken);

	return 0;
}

/*
 * Class:     ca_hedlund_desktopicons_DesktopIcons
 * Method:    _drawStockIcon
 * Signature: (ILjava/awt/image/BufferedImage;IIII)I
 */
JNIEXPORT jint JNICALL Java_ca_hedlund_desktopicons_DesktopIcons__1drawStockIcon
  (JNIEnv *env, jclass DesktopIcons, jint iconId, jobject bufferedImage, jint x, jint y, jint w, jint h) {
	CoInitialize(NULL);
	ULONG_PTR(gdiplusToken);
	GdiplusStartupInput gdiplusstartupinput;
	GdiplusStartup(&gdiplusToken, &gdiplusstartupinput, NULL);

	SHSTOCKICONINFO iconInfo = { 0 };
	iconInfo.cbSize = sizeof(SHSTOCKICONINFO);
	SHGetStockIconInfo((SHSTOCKICONID)iconId, SHGSI_ICON, &iconInfo);
	if (iconInfo.hIcon == NULL) {
		return ca_hedlund_desktopicons_DesktopIcons_ICON_NOT_FOUND;
	}

	DrawImage(env, bufferedImage, iconInfo.hIcon, x, y, w, h);

	DestroyIcon(iconInfo.hIcon);
	CoUninitialize();
	GdiplusShutdown(gdiplusToken);
	return 0;
}

