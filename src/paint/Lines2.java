package paint;

import javax.swing.*;

public class Lines2 extends JFrame {

    public Lines2() {
        initUI();
    }
    private void initUI() {
        setTitle("Picture");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(new Surface());
        setSize(350, 250);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Lines2 lines = new Lines2();
                lines.setVisible(true);
            }
        });
    }
}