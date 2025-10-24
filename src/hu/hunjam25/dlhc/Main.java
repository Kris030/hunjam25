package hu.hunjam25.dlhc;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Failed to set look and feel");
        }

        JFrame f = new JFrame("Don't let him cook");

        f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        // f.setIconImage(ImageIO.read(Main.class.getResource("icon.png")));
        // f.setLayout(new FlowLayout());
        f.setLocationRelativeTo(null);

        Canvas c = new Canvas();
        f.add(c);

        f.setVisible(true);

        while (true) {
            BufferStrategy bs = c.getBufferStrategy();
            if (bs == null) {
                c.createBufferStrategy(2);
                continue;
            }

            Graphics2D g2d = (Graphics2D) bs.getDrawGraphics();


            g2d.dispose();

            // sleep
        }
    }
}

