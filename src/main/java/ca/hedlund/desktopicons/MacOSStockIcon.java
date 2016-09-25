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
