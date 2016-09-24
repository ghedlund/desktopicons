/**
 * 
 */
package ca.hedlund.desktopicons;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DesktopIcons {
	
	private final static Logger LOGGER = Logger.getLogger(DesktopIcons.class.getName());

	private final static String _LIB_NAME = "desktopicons";
	
	private final static int DEFAULT_WIDTH = 32;
	private final static int DEFAULT_HEIGHT = 32;
	
	static {
		try {
			NativeUtilities.loadLibrary(_LIB_NAME);
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Unable to load native dialogs library, will fallback to Swing", e);
		}
	}
	
	public static Image getIconForPath(String path) throws DesktopIconException {
		return getIconForPath(path, DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
	
	public static Image getIconForPath(String path, int width, int height) throws DesktopIconException {
		final BufferedImage bufferedImage = new BufferedImage(width, height, 
				BufferedImage.TYPE_INT_ARGB);
		drawIconForPath(path, bufferedImage, 0, 0, width, height);
		return bufferedImage;
	}
	
	public static void drawIconForPath(String path, BufferedImage img, int x, int y, int width, int height) 
		throws DesktopIconException {
		int err = _drawIconForPath(path, img, x, y, width, height);
		switch(err) {
		case 0:
			// ok!
			break;
			
		case 1:
			// file not found
			throw new DesktopIconException(new FileNotFoundException(path));
			
		default:
			throw new DesktopIconException("An unknown error occurred");
		}
	}
	
	private static native int _drawIconForPath(String path, BufferedImage img, int x, int y, int width, int height);

}

