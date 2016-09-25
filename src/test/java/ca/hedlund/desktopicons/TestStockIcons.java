package ca.hedlund.desktopicons;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Enumeration;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class TestStockIcons {

	public static void main(String[] args) throws Exception {
		final Runnable onEDT = () -> {
			final JFrame testFrame = new TestFrame();
			testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			testFrame.pack();
			testFrame.setVisible(true);
		};
		SwingUtilities.invokeLater(onEDT);
		
	}
	
	static class TestFrame extends JFrame {
		
		public TestFrame() {
			super("Stock Icons");
			
			init();
		}
		
		private void init() {
			
			final int width = 64;
			final int height = 64;
			
			final GridBagLayout layout = new GridBagLayout();
			final GridBagConstraints gbc = new GridBagConstraints();
			final JPanel panel = new JPanel(layout);
			
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.anchor = GridBagConstraints.NORTHWEST;
			gbc.fill = GridBagConstraints.NONE;
			gbc.gridheight = 1;
			gbc.gridwidth = 1;
			
			if(NativeUtilities.isMacOs()) {
				int col = 0;
				for(MacOSStockIcon stockIcon:MacOSStockIcon.values()) {
					gbc.gridx = col;
					
					final JLabel label = new JLabel(stockIcon.toString());
					try {
						final ImageIcon icn = 
								new ImageIcon(DesktopIcons.getStockIcon(stockIcon, width, height));
						label.setIcon(icn);
						label.setHorizontalTextPosition(SwingConstants.CENTER);
						label.setVerticalTextPosition(SwingConstants.BOTTOM);
					} catch (DesktopIconException e) {
						e.printStackTrace();
					}
					panel.add(label, gbc);
					
					++col;
					if(col == 5) {
						gbc.gridy++;
						col = 0;
					}
				}
			} else if(NativeUtilities.isWindows()) {
				int col = 0;
				for(WindowsStockIcon stockIcon:WindowsStockIcon.values()) {
					gbc.gridx = col;
					
					final JLabel label = new JLabel(stockIcon.toString());
					try {
						final ImageIcon icn = 
								new ImageIcon(DesktopIcons.getStockIcon(stockIcon, width, height));
						label.setIcon(icn);
						label.setHorizontalTextPosition(SwingConstants.CENTER);
						label.setVerticalTextPosition(SwingConstants.BOTTOM);
					} catch (DesktopIconException e) {
						e.printStackTrace();
					}
					panel.add(label, gbc);
					
					++col;
					if(col == 5) {
						gbc.gridy++;
						col = 0;
					}
				}
			}
			
			super.getContentPane().setLayout(new BorderLayout());
			super.getContentPane().add(new JScrollPane(panel), BorderLayout.CENTER);
		}
		
	}
	
}
