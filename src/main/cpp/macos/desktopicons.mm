/*
 * desktopicons - Load system icons for paths, types and stock icons.
 * Copyright (C) 2016, Gregory Hedlund <ghedlund@mun.ca>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

// cocoa includes
#import <Foundation/Foundation.h>
#import <Cocoa/Cocoa.h>

#import <jni.h>
#import <jawt_md.h>
#import <JavaNativeFoundation/JavaNativeFoundation.h>

#include "../jniload.h"
#include "desktopicons.h"
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
    [sourceImg drawInRect:NSMakeRect(0, 0, w, h) fromRect:NSZeroRect operation:NSCompositeCopy fraction:1.0];
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
        
        NSString* nsPath = (path != NULL ? JNFJavaToNSString(env, (jstring)path) : nil);
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
        
        NSString* nsType = (type != NULL ? JNFJavaToNSString(env, (jstring)type) : nil);
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
