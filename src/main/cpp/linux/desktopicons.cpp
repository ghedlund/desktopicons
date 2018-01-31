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
#include <X11/Xlib.h>
#include <gtk/gtk.h>
#include <gio/gio.h>
#include <jni.h>

#include "../../../../target/generated-sources/cpp/include/desktopicons.h"
#include "../jniload.h"
#include "../common.h"

#include <stdio.h>
#include <iostream>

/*
 * Get icon from current gtk theme.  Object should be
 * released using g_object_unref
 *
 * @param name
 * @param size
 */
static GdkPixbuf* load_icon(const gchar* name, gint size)
{
	GError *error = NULL;
	GtkIconLookupFlags flags = GTK_ICON_LOOKUP_FORCE_SIZE | GTK_ICON_LOOKUP_GENERIC_FALLBACK;

	auto icon_theme = gtk_icon_theme_get_default();
	auto pixbuf = gtk_icon_theme_load_icon( icon_theme,
			name, size, flags, &error);

	if(!pixbuf) {
		std::cerr << error->message << "\n";
		g_error_free(error);
		return NULL;
	}

	return pixbuf;
}

static GdkPixbuf* load_icon_choice(const gchar* choices[], gint size) {
	GError *error = NULL;
	GtkIconLookupFlags flags = GTK_ICON_LOOKUP_FORCE_SIZE | GTK_ICON_LOOKUP_GENERIC_FALLBACK;

	auto icon_theme = gtk_icon_theme_get_default();
	auto icon_info = gtk_icon_theme_choose_icon(icon_theme, choices, size, flags);

	if(icon_info) {
		auto retVal = gtk_icon_info_load_icon(icon_info, &error);
		gtk_icon_info_free(icon_info);

		if(!retVal) {
			std::cerr << error->message << std::endl;
			g_error_free(error);
		}

		return retVal;
	} else {
		return NULL;
	}
}

static void draw_icon(JNIEnv *env, jobject bufferedImage, GdkPixbuf *pixbuf, int px, int py)
{
	auto width = gdk_pixbuf_get_width(pixbuf);
	auto height = gdk_pixbuf_get_height(pixbuf);
	auto rowstride = gdk_pixbuf_get_rowstride(pixbuf);
	auto n_channels = gdk_pixbuf_get_n_channels(pixbuf);
	auto pixels = gdk_pixbuf_get_pixels(pixbuf);

	for(int y = 0; y < height; y++) {
		for(int x = 0; x < width; x++) {
			auto p = pixels + (y * rowstride) + (x * n_channels);
			int argb = 0;
			for(int i = 0; i < 3; i++) {
				argb |= p[i] << (8 * i);
			}
			if(gdk_pixbuf_get_has_alpha(pixbuf)) {
				argb |= p[n_channels-1] << 24;
			}

			drawPixel(env, bufferedImage, px+x, py+y, argb);
		}
	}
}

static GtkIconInfo* findIconForType(const gchar* type, gint size) {
	GtkIconInfo *retVal = NULL;
	GtkIconLookupFlags flags = GTK_ICON_LOOKUP_FORCE_SIZE | GTK_ICON_LOOKUP_GENERIC_FALLBACK;
	auto icon_theme = gtk_icon_theme_get_default();

	return retVal;
}

/*
 * Class:     ca_hedlund_desktopicons_DesktopIcons
 * Method:    _drawIconForPath
 * Signature: (Ljava/lang/String;Ljava/awt/image/BufferedImage;IIII)I
 */
JNIEXPORT jint JNICALL Java_ca_hedlund_desktopicons_DesktopIcons__1drawIconForPath
	(JNIEnv *env, jclass DesktopIcons, jstring path, jobject bufferedImage, jint x, jint y, jint w, jint h) {
	jint retVal = 0;
	jboolean isCopy = false;
	GFileQueryInfoFlags queryFlags = G_FILE_QUERY_INFO_NONE;
	GError *error = NULL;

	XInitThreads();
	if(!gtk_init_check(0, NULL)) return -1;

	auto szPath = env->GetStringUTFChars(path, &isCopy);
	auto gFile = g_file_new_for_path(szPath);
	auto gFileInfo = g_file_query_info(gFile, "standard::icon", queryFlags, NULL, &error);

	if(gFileInfo) {
		auto gIcon = g_file_info_get_icon(gFileInfo);
		if(gIcon) {
			auto gThemedIcon = (GThemedIcon*)(gIcon);
			auto iconNames = g_themed_icon_get_names(gThemedIcon);

			auto icon = load_icon_choice(iconNames, w);
			if(icon) {
				draw_icon(env, bufferedImage, icon, x, y);
				g_object_unref(icon);
			} else {
				retVal = ca_hedlund_desktopicons_DesktopIcons_ICON_NOT_FOUND;
			}
		} else {
			retVal = ca_hedlund_desktopicons_DesktopIcons_ICON_NOT_FOUND;
		}
		//g_object_unref(gFileInfo);
	} else {
		std::cerr << error->message << "\n";
		g_error_free(error);
		retVal = ca_hedlund_desktopicons_DesktopIcons_FILE_NOT_FOUND;
	}

	//g_object_unref(gFile);
	env->ReleaseStringUTFChars(path, szPath);

	return retVal;
}

/*
 * Class:     ca_hedlund_desktopicons_DesktopIcons
 * Method:    _drawIconForFileType
 * Signature: (Ljava/lang/String;Ljava/awt/image/BufferedImage;IIII)I
 */
JNIEXPORT jint JNICALL Java_ca_hedlund_desktopicons_DesktopIcons__1drawIconForFileType
  (JNIEnv *env, jclass DesktopIcons, jstring type, jobject bufferedImage, jint x , jint y, jint w, jint h)  {
	jboolean isCopy = false;
	jint retVal = 0;

	XInitThreads();
	if(!gtk_init_check(0, NULL)) return -1;

	auto szType = env->GetStringUTFChars(type, &isCopy);
	auto icon = load_icon(szType, w);
	if(!icon) {
		retVal = ca_hedlund_desktopicons_DesktopIcons_ICON_NOT_FOUND;
	} else {
		draw_icon(env, bufferedImage, icon, x, y);
		g_object_unref(icon);
	}

	env->ReleaseStringUTFChars(type, szType);

	return retVal;
}

/*
 * Class:     ca_hedlund_desktopicons_DesktopIcons
 * Method:    _drawStockIcon
 * Signature: (ILjava/awt/image/BufferedImage;IIII)I
 */
JNIEXPORT jint JNICALL Java_ca_hedlund_desktopicons_DesktopIcons__1drawStockIcon
  (JNIEnv *env, jclass DesktopIcons, jint iconId, jobject bufferedImage, jint x, jint y, jint w, jint h) {
	const char* szEnumClazz = "ca/hedlund/desktopicons/GtkStockIcon";
	const char* szGetStockIconName = "getStockIcon";
	const char* szGetStockIconSig = "(I)Lca/hedlund/desktopicons/GtkStockIcon;";
	const char* szIconName = "getIconName";
	const char* szIconSig = "()Ljava/lang/String;";
	jboolean isCopy = false;
	jint retVal = 0;

	XInitThreads();
	if(!gtk_init_check(0, NULL)) return -1;

	auto GtkStockIcon = env->FindClass(szEnumClazz);
	auto szGetStockIconID = env->GetStaticMethodID(GtkStockIcon, szGetStockIconName, szGetStockIconSig);
	auto iconNameId = env->GetMethodID(GtkStockIcon, szIconName, szIconSig);

	auto stockIcon = env->CallStaticObjectMethod(GtkStockIcon, szGetStockIconID, iconId);
	if(stockIcon != NULL) {
		jstring jIconName = static_cast<jstring>(env->CallObjectMethod(stockIcon, iconNameId));
		auto iconName = env->GetStringUTFChars(jIconName, &isCopy);
		if(!iconName) {
			return 0;
		}

		auto icon = load_icon(iconName, w);
		if(icon) {
			draw_icon(env, bufferedImage, icon, x, y);
			g_object_unref(icon);
		} else {
			retVal = ca_hedlund_desktopicons_DesktopIcons_ICON_NOT_FOUND;
		}

		env->ReleaseStringUTFChars(jIconName, iconName);
	}

	return retVal;
}

