package hu.hunjam25.dlhc;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

import hu.hunjam25.dlhc.model.Rat;

public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Failed to set look and feel");
        }

        Kitchen.init();

        JFrame f = new JFrame("Don't let him cook");

        //f.setUndecorated(true); // removes title bar & borders
        f.setBounds(10,10,1920,1080);
        //f.setExtendedState(JFrame.MAXIMIZED_BOTH); // maximize


        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // f.setIconImage(ImageIO.read(Main.class.getResource("icon.png")));
        // f.setLayout(new FlowLayout());
        f.setLocationRelativeTo(null);

        Canvas c = new Canvas();
        f.add(c);

        c.addKeyListener(Game.listener);

        f.setVisible(true);

        double FPS = 60;

        long gameStart = System.nanoTime();

        boolean running = true;
        while (running) {
            double wait = 1000 / FPS;
            long start = System.nanoTime();

            BufferStrategy bs = c.getBufferStrategy();
            if (bs == null) {
                c.createBufferStrategy(2);
                continue;
            }

            Game.now = (start - gameStart) * 0.000000001f;
            Game.tick((float) wait);

            Graphics2D g = (Graphics2D) bs.getDrawGraphics();
            // Utils.setRenderingHints(g);

            g.setColor(Color.WHITE); // background color
            g.fillRect(0, 0, f.getWidth(), f.getHeight());
            Game.render(g);


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

