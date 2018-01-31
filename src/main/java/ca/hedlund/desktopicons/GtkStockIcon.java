package ca.hedlund.desktopicons;

public enum GtkStockIcon implements StockIcon {
	FOLDER("folder"),
	FOLDER_OPEN("folder-open"),
	FOLDER_HOME("folder-home"),
	FOLDER_DOCUMENTS("folder-documents"),
	FOLDER_DOWNLOADS("folder-downloads"),
	FOLDER_IMAGES("folder-images"),
	FOLDER_MUSIC("folder-music"),
	FOLDER_PICTURES("folder-pictures"),
	FOLDER_VIDEO("folder-video"),
	FOLDER_REMOTE("folder-remote"),
	SYSTEM_USERS("system-users"),
	CONFIG_USERS("config-users"),
	SETTINGS("gnome-settings"),
	DOCUMENT("misc");

	private final String iconName;
	
	private GtkStockIcon(String iconName) {
		this.iconName = iconName;
	}
	
	public String getIconName() {
		return this.iconName;
	}
	
	public static GtkStockIcon getStockIcon(int ordinal) {
		return values()[ordinal];
	}
	
	@Override
	public int getId() {
		return ordinal();
	}

}
