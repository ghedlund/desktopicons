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
package ca.hedlund.desktopicons;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

/**
 * Utility methods for retrieving OS specific icons for paths
 * and file types.
 *
 * @author Greg Hedlund <ghedlund@mun.ca>
 */
public class DesktopIcons {
	
	private final static Logger LOGGER = Logger.getLogger(DesktopIcons.class.getName());

	private final static String LIB_NAME = "desktopicons";
	
	private final static int DEFAULT_WIDTH = 32;
	private final static int DEFAULT_HEIGHT = 32;
	
	private final static int OK = 0;
	private final static int FILE_NOT_FOUND = 1;
	private final static int ICON_NOT_FOUND = 2;
	private final static int LIBRARY_NOT_FOUND = 3;
	
	private static boolean libraryLoaded = false;
	
	static {
		try {
			NativeUtilities.loadLibrary(LIB_NAME);
			libraryLoaded = true;
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Unable to load native dialogs library, will fallback to Swing", e);
		}
	}
	
	/**
	 * Return a 32x32px icon for the given path.
	 * 
	 * @param path
	 * @return
	 * @throws DesktopIconException
	 */
	public static Image getIconForPath(String path) throws DesktopIconException {
		return getIconForPath(path, DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
	
	/**
	 * Return the system icon for <code>path</code> with
	 * the given <code>width</code> and <code>height</code>.
	 * 
	 * @param path
	 * @param width
	 * @param height
	 * @return
	 * @throws DesktopIconException
	 */
	public static Image getIconForPath(String path, int width, int height) throws DesktopIconException {
		final BufferedImage bufferedImage = new BufferedImage(width, height, 
				BufferedImage.TYPE_INT_ARGB);
		final Runnable onEDT = () -> {
			try {
				drawIconForPath(path, bufferedImage, 0, 0, width, height);
			} catch (DesktopIconException e) {
				e.printStackTrace();
			}
		};
		if(SwingUtilities.isEventDispatchThread()) {
			onEDT.run();
		} else {
			try {
				SwingUtilities.invokeAndWait(onEDT);
			} catch (InvocationTargetException | InterruptedException e) {
				e.printStackTrace();
			}
		}
		return bufferedImage;
	}
	
	/**
	 * Draw system icon for path into the given {@link BufferedImage} using
	 * the given top-left location, <code>width</code>, and <code>height</code>.
	 * 
	 * @param path
	 * @param img
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @throws DesktopIconException
	 */
	public static void drawIconForPath(String path, BufferedImage img, int x, int y, int width, int height) 
		throws DesktopIconException {
		int err = 
				(libraryLoaded ? _drawIconForPath(path, img, x, y, width, height) : LIBRARY_NOT_FOUND);
		switch(err) {
		case OK:
			break;
			
		case FILE_NOT_FOUND:
			throw new DesktopIconException(new FileNotFoundException(path));
			
		case ICON_NOT_FOUND:
			throw new DesktopIconException("Icon not found");
			
		case LIBRARY_NOT_FOUND:
			throw new DesktopIconException(new UnsupportedOperationException());
			
		default:
			throw new DesktopIconException("An unknown error occurred");
		}
	}
	
	/**
	 * Native draw icon method.
	 * 
	 * @param path
	 * @param img
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return
	 */
	private synchronized static native int _drawIconForPath(String path, BufferedImage img, int x, int y, int width, int height);

	/**
	 * Return a 32x32px icon for the given file type.
	 * 
	 * @param type
	 * @return
	 * @throws DesktopIconException
	 */
	public static Image getIconForFileType(String type) throws DesktopIconException {
		return getIconForFileType(type, DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
	
	/**
	 * Return the system icon for <code>type</code> with
	 * the given <code>width</code> and <code>height</code>.
	 * 
	 * On macOS/Windows <code>type</code> should be the extension of the file.
	 * On Linux/gtk <code>type</code> is the name of the icon from the current
	 * gtk icon theme.
	 * 
	 * @param type
	 * @param width
	 * @param height
	 * @return
	 * @throws DesktopIconException
	 */
	public static Image getIconForFileType(String type, int width, int height) throws DesktopIconException {
		final BufferedImage bufferedImage = new BufferedImage(width, height, 
				BufferedImage.TYPE_INT_ARGB);
		final Runnable onEDT = () -> {
			try {
				drawIconForFileType(type, bufferedImage, 0, 0, width, height);
			} catch (DesktopIconException e) {
				e.printStackTrace();
			}
		};
		if(SwingUtilities.isEventDispatchThread()) {
			onEDT.run();
		} else {
			try {
				SwingUtilities.invokeAndWait(onEDT);
			} catch (InvocationTargetException | InterruptedException e) {
				e.printStackTrace();
			}
		}
		return bufferedImage;
	}
	
	/**
	 * Draw system icon for type into the given {@link BufferedImage} using
	 * the given top-left location, <code>width</code>, and <code>height</code>.
	 * 
	 * @param type
	 * @param img
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @throws DesktopIconException
	 */
	public static void drawIconForFileType(String type, BufferedImage img, int x, int y, int width, int height) 
		throws DesktopIconException {
		if(NativeUtilities.isWindows() && type.charAt(0) != '.') {
			type = "." + type;
		}
		int err = 
				(libraryLoaded ? _drawIconForFileType(type, img, x, y, width, height) : LIBRARY_NOT_FOUND);
		switch(err) {
		case OK:
			break;
			
		case ICON_NOT_FOUND:
			throw new DesktopIconException("Icon not found");
		
		case LIBRARY_NOT_FOUND:
			throw new DesktopIconException(new UnsupportedOperationException());
			
		default:
			throw new DesktopIconException("An unknown error occurred");
		}
	}
	
	/**
	 * Native draw icon method.
	 * 
	 * @param type
	 * @param img
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return
	 */
	private synchronized static native int _drawIconForFileType(String type, BufferedImage img, int x, int y, int width, int height);
	
	/**
	 * Return a 32x32px icon for the given stock icon id.
	 * Id should come from one of the enums {@link MacOSStockIcon} or
	 * {@link WindowsStockIcons}.
	 * 
	 * @param id
	 * @return
	 * @throws DesktopIconException
	 */
	public static Image getStockIcon(StockIcon icon) throws DesktopIconException {
		return getStockIcon(icon, DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
	
	/**
	 * Return the stock icon for <code>id</code> with
	 * the given <code>width</code> and <code>height</code>.
	 * 
	 * @param id
	 * @param width
	 * @param height
	 * @return
	 * @throws DesktopIconException
	 */
	public static Image getStockIcon(StockIcon icon, int width, int height) throws DesktopIconException {
		final BufferedImage bufferedImage = new BufferedImage(width, height, 
				BufferedImage.TYPE_INT_ARGB);
		final Runnable onEDT = () -> {
			try {
				drawStockIcon(icon, bufferedImage, 0, 0, width, height);
			} catch (DesktopIconException e) {
				e.printStackTrace();
			}
		};
		if(SwingUtilities.isEventDispatchThread()) {
			onEDT.run();
		} else {
			try {
				SwingUtilities.invokeAndWait(onEDT);
			} catch (InvocationTargetException | InterruptedException e) {
				e.printStackTrace();
			}
		}
		return bufferedImage;
	}
	
	/**
	 * Draw stock icon for id into the given {@link BufferedImage} using
	 * the given top-left location, <code>width</code>, and <code>height</code>.
	 * 
	 * @param id
	 * @param img
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @throws DesktopIconException
	 */
	public static void drawStockIcon(StockIcon icon, BufferedImage img, int x, int y, int width, int height) 
		throws DesktopIconException {
		int err = 
				(libraryLoaded ? _drawStockIcon(icon.getId(), img, x, y, width, height) : LIBRARY_NOT_FOUND);
		switch(err) {
		case OK:
			break;
			
		case ICON_NOT_FOUND:
			throw new DesktopIconException("Icon not found");
			
		case LIBRARY_NOT_FOUND:
			throw new DesktopIconException(new UnsupportedOperationException());
			
		default:
			throw new DesktopIconException("An unknown error occurred");
		}
	}
	
	/**
	 * Native draw icon method.
	 * 
	 * @param id
	 * @param img
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return
	 */
	private synchronized static native int _drawStockIcon(int id, BufferedImage img, int x, int y, int width, int height);
	
}

