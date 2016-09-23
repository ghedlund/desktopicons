/*
 * This file is part of nativedialogs for java
 * Copyright (C) 2016 Gregory Hedlund &lt;ghedlund@mun.ca&gt;
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

#ifdef WIN32
#include <windows.h>
#endif

#include "jniload.h"

static JavaVM * jvm;

/* JNI Load */
JNIEXPORT jint JNICALL
	JNI_OnLoad(JavaVM *vm, void *reserved)
{
	jvm = vm;
	
	return JNI_VERSION_1_4;
}

JNIEXPORT void JNICALL
	JNI_OnUnLoad(JavaVM *vm, void *reserved)
{
}

jint
GetJNIEnv(JNIEnv **env, bool *mustDetach) 
{
	jint getEnvErr = JNI_OK;
	
	*mustDetach = false;
	
	if(jvm)
	{
		getEnvErr = jvm->GetEnv((void**)env, JNI_VERSION_1_4);
		if(getEnvErr == JNI_EDETACHED) 
		{
			getEnvErr = jvm->AttachCurrentThread((void**)env, NULL);
			if(getEnvErr == JNI_OK) {
				*mustDetach = true;
			}
		}
	}
	
	return getEnvErr;
}