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

public enum WindowsStockIcon implements StockIcon {
	DOCNOASSOC(0),
	DOCASSOC(1),
	APPLICATION(2),
	FOLDER(3),
	FOLDEROPEN(4),
	DRIVE525(5),
	DRIVE35(6),
	DRIVEREMOVE(7),
	DRIVEFIXED(8),
	DRIVENET(9),
	DRIVENETDISABLED(10),
	DRIVECD(11),
	DRIVERAM(12),
	WORLD(13),
	SERVER(15),
	PRINTER(16),
	MYNETWORK(17),
	FIND(22),
	HELP(23),
	SHARE(28),
	LINK(29),
	SLOWFILE(30),
	RECYCLER(31),
	RECYCLERFULL(32),
	MEDIACDAUDIO(40),
	LOCK(47),
	AUTOLIST(49),
	PRINTERNET(50),
	SERVERSHARE(51),
	PRINTERFAX(52),
	PRINTERFAXNET(53),
	PRINTERFILE(54),
	STACK(55),
	MEDIASVCD(56),
	STUFFEDFOLDER(57),
	DRIVEUNKNOWN(58),
	DRIVEDVD(59),
	MEDIADVD(60),
	MEDIADVDRAM(61),
	MEDIADVDRW(62),
	MEDIADVDR(63),
	MEDIADVDROM(64),
	MEDIACDAUDIOPLUS(65),
	MEDIACDRW(66),
	MEDIACDR(67),
	MEDIACDBURN(68),
	MEDIABLANKCD(69),
	MEDIACDROM(70),
	AUDIOFILES(71),
	IMAGEFILES(72),
	VIDEOFILES(73),
	MIXEDFILES(74),
	FOLDERBACK(75),
	FOLDERFRONT(76),
	SHIELD(77),
	WARNING(78),
	INFO(79),
	ERROR(80),
	KEY(81),
	SOFTWARE(82),
	RENAME(83),
	DELETE(84),
	MEDIAAUDIODVD(85),
	MEDIAMOVIEDVD(86),
	MEDIAENHANCEDCD(87),
	MEDIAENHANCEDDVD(88),
	MEDIAHDDVD(89),
	MEDIABLURAY(90),
	MEDIAVCD(91),
	MEDIADVDPLUSR(92),
	MEDIADVDPLUSRW(93),
	DESKTOPPC(94),
	MOBILEPC(95),
	USERS(96),
	MEDIASMARTMEDIA(97),
	MEDIACOMPACTFLASH(98),
	DEVICECELLPHONE(99),
	DEVICECAMERA(100),
	DEVICEVIDEOCAMERA(101),
	DEVICEAUDIOPLAYER(102),
	NETWORKCONNECT(103),
	INTERNET(104),
	ZIPFILE(105),
	SETTINGS(106),
	DRIVEHDDVD(132),
	DRIVEBD(133),
	MEDIAHDDVDROM(134),
	MEDIAHDDVDR(135),
	MEDIAHDDVDRAM(136),
	MEDIABDROM(137),
	MEDIABDR(138),
	MEDIABDRE(139),
	CLUSTEREDDRIVE(140);
	
	private final int id;
	
	private WindowsStockIcon(int id) {
		this.id = id;
	}
	
	@Override
	public int getId() {
		return this.id;
	}
}
