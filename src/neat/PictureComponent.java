package neat;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class PictureComponent extends JPanel {
	
	BufferedImage picture;

	public PictureComponent(BufferedImage picture) {
		super();
		this.picture = picture;
	}
	
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(picture, 0, 0, null); // see javadoc for more info on the parameters            
    }
	
}
