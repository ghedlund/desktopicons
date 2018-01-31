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

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class TestIconForFile {
	
	public static void main(String[] args) throws Exception {
		final String path = "pom.xml";
		
		final Runnable onEDT = () -> {
			final JFrame f = new JFrame();
			final JLabel lbl = new JLabel();
			
			JButton btn = new JButton("Load");
			btn.addActionListener( (e) -> {
				try {
					final ImageIcon icon = new ImageIcon(
							DesktopIcons.getIconForFileType("xml", 64, 64));
					lbl.setIcon(icon);
					f.pack();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			});
			
			f.getContentPane().setLayout(new BorderLayout());
			f.getContentPane().add(lbl, BorderLayout.CENTER);
			f.getContentPane().add(btn, BorderLayout.EAST);
			
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			f.pack();
			f.setVisible(true);
		};
		SwingUtilities.invokeLater(onEDT);
	}

}
