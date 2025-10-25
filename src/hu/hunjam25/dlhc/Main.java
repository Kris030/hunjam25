package hu.hunjam25.dlhc;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.IOException;

public class Main {

    static JFrame frame;

    public static void main(String[] args) throws IOException {
        // force enable hardware acceleration
        System.setProperty("sun.java2d.opengl", "true");

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Failed to set look and feel");
        }

        Game.init();
        Kitchen.init();

        frame = new JFrame("Don't let him cook");

        frame.setUndecorated(true); // removes title bar & borders
        frame.setBounds(0, 0, Game.SCREEN_WIDTH * Game.TILE_SIZE, Game.SCREEN_HEIGHT * Game.TILE_SIZE);


        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // f.setIconImage(ImageIO.read(Main.class.getResource("icon.png")));
        // f.setLayout(new FlowLayout());
        frame.setLocationRelativeTo(null);

        Canvas c = new Canvas();
        frame.add(c);

        c.addKeyListener(Game.listener);

        frame.setVisible(true);

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

            g.setColor(Color.ORANGE); // background color
            g.fillRect(0, 0, frame.getWidth(), frame.getHeight());
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

