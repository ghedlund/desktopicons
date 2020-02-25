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

package ca.hedlund.desktopicons;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Some common utilities for the application.
 */
public class NativeUtilities {
	
	/**
	 * Are we using Mac OS?
	 */
	public static boolean isMacOs() {
//		String mrjVersion = System.getProperty("mrj.version");
//		
//		if(mrjVersion != null)
//			return true;
//		else
//			return false;
		String osname = System.getProperty("os.name");
		
		if(osname.toLowerCase().contains("mac")) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isWindows() {
		String osname = System.getProperty("os.name");
		
		if(osname.toLowerCase().indexOf("windows") >= 0) 
			return true;
		else
			return false;
	}
	
	public static boolean isLinux() {
		String osname = System.getProperty("os.name");
		
		int nixIdx = osname.indexOf("nix");
		int nuxIdx = osname.indexOf("nux");
		
		if(nixIdx >= 0 || nuxIdx >= 0)
			return true;
		else
			return false;
	}
	
	/**
	 * Load native library that is included in the META-INF/lib
	 * folder.
	 * 
	 * The library should be in the following folder:
	 * 
	 * META-INF/lib/${platform}/${libname}
	 * 
	 * where platform is one of: win32, macos
	 * 
	 * @param libName
	 * 
	 * @throws IOException
	 */
	public static void loadLibrary(String libName)
		throws IOException {
		final String expectedPath = libraryPath(libName);
		final ClassLoader cl = NativeUtilities.class.getClassLoader();
		
		final InputStream is = cl.getResourceAsStream(expectedPath);
		if(is == null) {
			throw new FileNotFoundException(libName);
		}
		
		// copy stream to temporary location
		final File tempFile = File.createTempFile(libName, NativeUtilities.class.getName());
		tempFile.mkdirs();
		
		final FileOutputStream fos = new FileOutputStream(tempFile);
		final byte[] buffer = new byte[1024];
		int read = -1;
		while((read = is.read(buffer)) >= 0) {
			fos.write(buffer, 0, read);
		}
		fos.flush();
		fos.close();
		is.close();
		
		// load temporary file
		System.load(tempFile.getAbsolutePath());
	}
	
	/**
	 * Determine library path based on current platform.
	 * 
	 * @param libName
	 * @return expected library path
	 */
	private static String libraryPath(String libName) {
		String lib = libName;
		String folder = "META-INF/lib/";
		
		if(isMacOs()) {
			lib = "lib" + libName + ".jnilib";
			folder += "macos";
		} else if(isWindows()) {
			lib = libName + ".dll";
			folder += "win32";
			
			String arch = System.getProperty("os.arch");
			if(arch.equals("amd64")) arch = "x64";
			folder += "-" + arch;
		}
		
		return folder + "/" + lib;
	}
}
