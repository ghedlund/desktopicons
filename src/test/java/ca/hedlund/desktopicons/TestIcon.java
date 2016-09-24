package ca.hedlund.desktopicons;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class TestIcon {

	public static void main(String[] args) throws Exception {
		final Runnable onEDT = () -> {
			final JFrame testFrame = new JFrame("Test Icon Load");
			final JLabel label = new JLabel();
			label.setBackground(Color.white);
			final JButton loadButton = new JButton("Load Icon");
			loadButton.addActionListener( (e) -> {
				try {
					final BufferedImage img = 
							new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);
					
					DesktopIcons.drawIconForPath("/System/Library/CoreServices/Finder.app",
									img, 32, 32, 64, 64);
					label.setIcon(new ImageIcon(img));
					testFrame.pack();
				} catch (DesktopIconException ex) {
					ex.printStackTrace();
				}
			});
			
			
			testFrame.setLayout(new BorderLayout());
			testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			testFrame.add(label, BorderLayout.CENTER);
			testFrame.add(loadButton, BorderLayout.EAST);
			testFrame.pack();
			testFrame.setVisible(true);
		};
		SwingUtilities.invokeLater(onEDT);
		
	}
	
}
