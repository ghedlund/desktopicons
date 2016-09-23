/**
 * 
 */
package ca.hedlund.desktopicons;

import java.awt.Image;

public class DesktopIcons {

	static {
		// TODO load native library
	}

	public native Image getIconForPath(String path);

	public native Image getIconForExtension(String extension);

}

