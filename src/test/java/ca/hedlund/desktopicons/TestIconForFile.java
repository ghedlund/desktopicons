package ca.hedlund.desktopicons;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class TestIconForFile {
	
	public static void main(String[] args) throws Exception {
		final String path = "mkv";
		
		final Runnable onEDT = () -> {
			final JFrame f = new JFrame();
			final JLabel lbl = new JLabel();
			
			JButton btn = new JButton("Load");
			btn.addActionListener( (e) -> {
				try {
					final ImageIcon icon = new ImageIcon(
							DesktopIcons.getStockIcon(defaultIcon, 64, 64));
					lbl.setIcon(icon);
					f.pack();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			});
			
			f.getContentPane().setLayout(new BorderLayout());
			f.getContentPane().add(lbl, BorderLayout.CENTER);
			f.getContentPane().add(btn, BorderLayout.EAST);
			
			f.pack();
			f.setVisible(true);
		};
		SwingUtilities.invokeLater(onEDT);
	}
	
	final static StockIcon defaultIcon = () -> { return 1; };

}
