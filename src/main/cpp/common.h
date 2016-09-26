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

