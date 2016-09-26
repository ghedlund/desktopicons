# desktopicons

desktopicons is a JNI library allowing access to system icons such as:

 * Icons for file paths
 * Icons for file types
 * Stock icons

## Supported Operating Systems

 * Windows XP+
 * macOS 10.7+

## Usage

 1. Get icon for a file:

   ```java
import ca.hedlund.desktopicons.*;
...

String path = "....";
try {
	java.awt.Image img = DesktopIcons.getIconForPath(path);
} catch (DesktopoIconException e) {
	e.printStackTrace();
}
```

 2. Get icon for a file type (xml)

   ```java
String type = "xml";
try {
	Image img = DesktopIcons.getIconForFileType(type);
} catch (DesktopIconException e) {
	e.printStackTrace();
}
```

 3. Get stock icon (recycle/trash)

   ```java
StockIcon trashIcon = 
	(NativeUtilites.isMacOs() ? MacOSStockIcons.TrashIcon : WindowsStockIcon.RECYCLE_BIN );
try {
	Image img = DesktopIcons.getStockIcon(trashIcon);
} catch (DesktopIconException e) {
	e.printStackTrace();
}
```

The methods above all return a 32x32px image. Variant methods are available allowing different sizes as well as
a methods for drawing directly to a java.awt.image.BufferedImage.

