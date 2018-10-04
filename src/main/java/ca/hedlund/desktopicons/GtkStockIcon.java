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
