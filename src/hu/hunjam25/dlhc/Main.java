package hu.hunjam25.dlhc;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Failed to set look and feel");
        }

        JFrame f = new JFrame("Don't let him cook");




        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // f.setIconImage(ImageIO.read(Main.class.getResource("icon.png")));
        // f.setLayout(new FlowLayout());
        f.setLocationRelativeTo(null);

        Canvas c = new Canvas();
        f.add(c);

        f.setVisible(true);

        double FPS = 60;

        boolean running = true;
        while (running) {
            double wait = 1000 / FPS;
            long start = System.nanoTime();

            BufferStrategy bs = c.getBufferStrategy();
            if (bs == null) {
                c.createBufferStrategy(2);
                continue;
            }

            Graphics2D g = (Graphics2D) bs.getDrawGraphics();
            // Utils.setRenderingHints(g);


            Renderer.render(g);

            bs.show();
            g.dispose();

            long realWait = Math.round(wait - (System.nanoTime() - start) / 1_000_000);

            if (realWait > 0) {
                try {
                    Thread.sleep(realWait);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

