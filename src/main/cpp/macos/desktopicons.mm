/*
 * Copyright (C) 2012-2018 Gregory Hedlund
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 
 *    http://www.apache.org/licenses/LICENSE-2.0
 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// cocoa includes
#import <Cocoa/Cocoa.h>

#import <jni.h>
#import <jawt_md.h>

#include "JNIUtilities.h"

#include "../jniload.h"
#include "ca_hedlund_desktopicons_DesktopIcons.h"
#include "../common.h"

int ToARGB(float a, float r, float g, float b) {
    int retVal = 0;
    
    retVal |= (int)(255*a) << 24;
    retVal |= (int)(255*r) << 16;
    retVal |= (int)(255*g) << 8;
    retVal |= (int)(255*b);
    
    return retVal;
}

/**
 * Resizes images and returns a 72dpi RGB image.
 */
NSBitmapImageRep* getResizedBitmap(NSImage* sourceImg, int w, int h) {
    NSBitmapImageRep *rep = [[NSBitmapImageRep alloc]
                             initWithBitmapDataPlanes:NULL
                             pixelsWide:w
                             pixelsHigh:h
                             bitsPerSample:8
                             samplesPerPixel:4
                             hasAlpha:YES
                             isPlanar:NO
                             colorSpaceName:NSCalibratedRGBColorSpace
                             bytesPerRow:0
                             bitsPerPixel:0];
    NSSize size = NSMakeSize(w, h);
    [rep setSize:size];
    
    [NSGraphicsContext saveGraphicsState];
    [NSGraphicsContext setCurrentContext:[NSGraphicsContext graphicsContextWithBitmapImageRep:rep]];
    [sourceImg drawInRect:NSMakeRect(0, 0, w, h) fromRect:NSZeroRect operation:NSCompositingOperationCopy fraction:1.0];
    [NSGraphicsContext restoreGraphicsState];

    return rep;
}

void drawImage(JNIEnv *env, jobject bufferedImage, NSImage* sourceImg, int x, int y, int w, int h) {
    // convert to 72dpi bitmap
    NSBitmapImageRep* imageRep = getResizedBitmap(sourceImg, w, h);
    
    // draw into java graphics context
    for(int col = 0; col < w; col++) {
        for(int row = 0; row < h; row++) {
            NSColor* color = [imageRep colorAtX:col y:row];
            int argb = ToARGB([color alphaComponent],
                              [color redComponent],
                              [color greenComponent],
                              [color blueComponent]);
            drawPixel(env, bufferedImage, x+col, y+row, argb);
        }
    }
    [imageRep release];
}

/*
 * Class:     ca_hedlund_desktopicons_DesktopIcons
 * Method:    _drawIconForPath
 * Signature: (Ljava/lang/String;Ljava/awt/image/BufferedImage;IIII)I
 */
JNIEXPORT jint JNICALL Java_ca_hedlund_desktopicons_DesktopIcons__1drawIconForPath
    (JNIEnv * env, jclass DesktopIcons, jstring path, jobject img, jint x, jint y, jint w, jint h) {
        int retVal = 0;
        
        NSString* nsPath = (path != NULL ? NormalizedPathNSStringFromJavaString(env, (jstring)path) : nil);
        if(nsPath == nil) {
            return ca_hedlund_desktopicons_DesktopIcons_FILE_NOT_FOUND;
        }
        
        NSWorkspace* workspace = [NSWorkspace sharedWorkspace];
        NSImage* iconImage = [workspace iconForFile:nsPath];
        
        if(iconImage == NULL) {
            return ca_hedlund_desktopicons_DesktopIcons_ICON_NOT_FOUND;
        }
        
        drawImage(env, img, iconImage, x, y, w, h);
        
        return retVal;
}

/*
 * Class:     ca_hedlund_desktopicons_DesktopIcons
 * Method:    _drawIconForFileType
 * Signature: (Ljava/lang/String;Ljava/awt/image/BufferedImage;IIII)I
 */
JNIEXPORT jint JNICALL Java_ca_hedlund_desktopicons_DesktopIcons__1drawIconForFileType
    (JNIEnv * env, jclass DesktopIcons, jstring type, jobject img, jint x, jint y, jint w, jint h) {
        int retVal = 0;
        
        NSString* nsType = (type != NULL ? JavaStringToNSString(env, (jstring)type) : nil);
        if(nsType == NULL) {
            return ca_hedlund_desktopicons_DesktopIcons_ICON_NOT_FOUND;
        }
        
        NSWorkspace* workspace = [NSWorkspace sharedWorkspace];
        NSImage* iconImage = [workspace iconForFileType:nsType];
        
        if(iconImage == NULL) {
            return ca_hedlund_desktopicons_DesktopIcons_ICON_NOT_FOUND;
        }
        
        drawImage(env, img, iconImage, x, y, w, h);
        
        return retVal;
}

/*
 * Class:     ca_hedlund_desktopicons_DesktopIcons
 * Method:    _drawStockIcon
 * Signature: (ILjava/awt/image/BufferedImage;IIII)I
 */
JNIEXPORT jint JNICALL Java_ca_hedlund_desktopicons_DesktopIcons__1drawStockIcon
    (JNIEnv * env, jclass DesktopIcons, jint iconId, jobject img, jint x, jint y, jint w, jint h) {
        int retVal = 0;
        
        NSWorkspace* workspace = [NSWorkspace sharedWorkspace];
        NSImage* iconImage = [workspace iconForFileType:NSFileTypeForHFSTypeCode(iconId)];
        
        if(iconImage == NULL) {
            return ca_hedlund_desktopicons_DesktopIcons_ICON_NOT_FOUND;
        }
        
        drawImage(env, img, iconImage, x, y, w, h);
        
        return retVal;
}
