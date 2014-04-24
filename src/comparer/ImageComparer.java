package comparer;

import java.awt.image.BufferedImage;

public class ImageComparer {

  public static double difference(BufferedImage a, BufferedImage b)
  {
    int width1 = a.getWidth(null);
    int width2 = b.getWidth(null);
    int height1 = a.getHeight(null);
    int height2 = b.getHeight(null);
    if ((width1 != width2) || (height1 != height2)) {
      System.err.println("Error: Images dimensions mismatch");
      System.exit(1);
    }
    long diff = 0;
    for (int i = 0; i < height1; i++) {
      for (int j = 0; j < width1; j++) {
    	  try{
    		int rgb1 = a.getRGB(i, j);
			int rgb2 = b.getRGB(i, j);
			int r1 = (rgb1 >> 16) & 0xff;
			int g1 = (rgb1 >>  8) & 0xff;
			int b1 = (rgb1      ) & 0xff;
			int r2 = (rgb2 >> 16) & 0xff;
			int g2 = (rgb2 >>  8) & 0xff;
			int b2 = (rgb2      ) & 0xff;
			diff += Math.abs(r1 - r2);
			diff += Math.abs(g1 - g2);
			diff += Math.abs(b1 - b2);
    	  } catch (Exception e){
    		  continue;
    	  }
      }
    }
    double n = width1 * height1 * 3;
    double p = diff / n / 255.0;
    //System.out.println("diff percent: " + (p * 100.0));
    return p;
  }

}
