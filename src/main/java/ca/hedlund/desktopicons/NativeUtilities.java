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
		} else if(isLinux()) {
			lib = "lib" + libName + ".so";
			folder += "linux";
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
