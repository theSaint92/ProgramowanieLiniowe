package gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class Rysunek extends JPanel {

	private static final long serialVersionUID = -785402414734497960L;
	
	private BufferedImage image;

	public Rysunek(BufferedImage i) {
		this.image = i;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
	    g.drawImage(image, 0, 0, this); // see javadoc for more info on the parameters            
	}
}
