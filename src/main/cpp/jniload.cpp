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

#ifdef WIN32
#include <windows.h>
#endif

#ifdef LINUX
#include <X11/Xlib.h>
#include <gtk/gtk.h>
#endif

#include "jniload.h"

static JavaVM * jvm;

/* JNI Load */
JNIEXPORT jint JNICALL
	JNI_OnLoad(JavaVM *vm, void *reserved)
{
	jvm = vm;

#ifdef LINUX
	XInitThreads();
#endif

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
