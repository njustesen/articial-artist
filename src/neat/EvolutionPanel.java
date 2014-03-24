package neat;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class EvolutionPanel extends JPanel {

	List<BufferedImage> pictures;
	JScrollPane scrollPane;
	
	public EvolutionPanel() {
		super();
		this.pictures = new ArrayList<BufferedImage>();
		this.scrollPane = new JScrollPane();
		this.scrollPane.setPreferredSize(new Dimension(1000, 700));
		this.scrollPane.setVisible(true);
		add(scrollPane, BorderLayout.CENTER);
		JFrame f = new JFrame();
        f.setSize(1000,700);
        f.setTitle("Evolution Panel");
        f.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(this);   
        f.pack();
        f.setVisible(true);
	}

	public void clearPictures() {
		pictures.clear();
		scrollPane.removeAll();
	}

	public void addPictures(List<BufferedImage> pictures) {
		pictures.addAll(pictures);
		
		for(BufferedImage image : pictures){
			PictureComponent pic = new PictureComponent(image);
			scrollPane.add(pic);
		}
		//scrollPane.add(comp);
	}

	
	
}
