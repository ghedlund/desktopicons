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

/**
 * Standard Finder icons.
 * 
 */
public enum MacOSStockIcon implements StockIcon {
	ClipboardIcon ("CLIP"),
	ClippingUnknownTypeIcon ("clpu"),
	ClippingPictureTypeIcon ("clpp"),
	ClippingTextTypeIcon ("clpt"),
	ClippingSoundTypeIcon ("clps"),
	DesktopIcon ("desk"),
	FinderIcon ("FNDR"),
	FontSuitcaseIcon ("FFIL"),
	FullTrashIcon ("ftrh"),
	GenericApplicationIcon ("APPL"),
	GenericCDROMIcon ("cddr"),
	GenericControlPanelIcon ("APPC"),
	GenericControlStripModuleIcon ("sdev"),
	GenericComponentIcon ("thng"),
	GenericDeskAccessoryIcon ("APPD"),
	GenericDocumentIcon ("docu"),
	GenericEditionFileIcon ("edtf"),
	GenericExtensionIcon ("INIT"),
	GenericFileServerIcon ("srvr"),
	GenericFontIcon ("ffil"),
	GenericFontScalerIcon ("sclr"),
	GenericFloppyIcon ("flpy"),
	GenericHardDiskIcon ("hdsk"),
	GenericIDiskIcon ("idsk"),
	GenericRemovableMediaIcon ("rmov"),
	GenericMoverObjectIcon ("movr"),
	GenericPCCardIcon ("pcmc"),
	GenericPreferencesIcon ("pref"),
	GenericQueryDocumentIcon ("qery"),
	GenericRAMDiskIcon ("ramd"),
	GenericSharedLibaryIcon ("shlb"),
	GenericStationeryIcon ("sdoc"),
	GenericSuitcaseIcon ("suit"),
	GenericURLIcon ("gurl"),
	GenericWORMIcon ("worm"),
	InternationalResourcesIcon ("ifil"),
	KeyboardLayoutIcon ("kfil"),
	SoundFileIcon ("sfil"),	
	SystemSuitcaseIcon ("zsys"),
	TrashIcon ("trsh"),
	TrueTypeFontIcon ("tfil"),
	TrueTypeFlatFontIcon ("sfnt"),
	TrueTypeMultiFlatFontIcon ("ttcf"),
	UserIDiskIcon ("udsk"),
	UnknownFSObjectIcon ("unfs");
	
	private final String fourByteCode;
	
	MacOSStockIcon(String fourByteCode) {
		this.fourByteCode = fourByteCode;
		
		if(this.fourByteCode.length() != 4)
			throw new IllegalArgumentException("Illegal code length");
	}
	
	public String getFourByteCode() {
		return this.fourByteCode;
	}
	
	@Override
	public int getId() {
		int retVal = 0;
		
		int offset = 24;
		for(int i = 0; i < fourByteCode.length(); i++) {
			retVal |= fourByteCode.getBytes()[i] << offset;
			offset -= 8;
		}
		
		return retVal;
	}
}
