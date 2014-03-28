package paint;

import java.awt.*;
import javax.swing.JPanel;



class Surface extends JPanel {

    //Lines
    private void paintLine(Graphics g, int x1, int y1, int x2, int y2, int width, Color color) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        g2d.drawLine(x1, y1, x2,y2);
    }

    //Circles
    private void paintCircle(Graphics g, int x, int y,  int radius, Color color) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(color);
        g2d.fillOval(x, y, radius, radius);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //Examples
        paintLine(g, 30, 30, 200, 30, 10, Color.green);
        paintCircle(g, 50, 50, 15, Color.cyan);
    }
}