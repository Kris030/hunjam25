package hu.hunjam25.dlhc;

import hu.hunjam25.dlhc.screens.CreditsScreen;
import hu.hunjam25.dlhc.screens.EndScreen;
import hu.hunjam25.dlhc.screens.IScreen;
import hu.hunjam25.dlhc.screens.StartScreen;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.IOException;

public class Main {

    static JFrame frame;

    static Game game;
    static StartScreen startScreen;
    static EndScreen endScreen;
    static CreditsScreen creditsScreen;

    private static IScreen currentScreen;
    public static float now, lastTick;

    public static void main(String[] args) throws IOException, UnsupportedAudioFileException {
        // force enable hardware acceleration
        System.setProperty("sun.java2d.opengl", "true");

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Failed to set look and feel");
        }

        init();

        currentScreen = startScreen;
        currentScreen.start();

        frame = new JFrame("Don't let him cook");

        frame.setUndecorated(true);
        frame.setResizable(false);
        frame.setSize(Game.SCREEN_WIDTH * Game.TILE_SIZE, Game.SCREEN_HEIGHT * Game.TILE_SIZE);
        frame.setLocationRelativeTo(null);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setIconImage(AssetManager.getImage("app_icon"));

        Canvas c = new Canvas();
        frame.add(c);

        c.addKeyListener(Game.listener);

        frame.setVisible(true);
        c.requestFocus();

        double FPS = 60;

        long gameStart = System.nanoTime();
        currentScreen.init();

        boolean running = true;
        while (running) {
            double wait = 1000 / FPS;
            long start = System.nanoTime();

            BufferStrategy bs = c.getBufferStrategy();
            if (bs == null) {
                c.createBufferStrategy(2);
                continue;
            }

            lastTick = now;
            now = (start - gameStart) * 0.000000001f;
            currentScreen.tick(now - lastTick);

            Graphics2D g = (Graphics2D) bs.getDrawGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g.setColor(Color.BLACK);
            g.clearRect(0, 0, c.getWidth(), c.getHeight());

            float scaleX = c.getWidth() / 1920f;
            float scaleY = c.getHeight() / 1080f;
            float scale = Math.min(scaleX, scaleY);
            g.scale(scale, scale);

            currentScreen.render(g);

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

    private static void init() {
        try {
            AssetManager.init();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        }

        game = new Game();
        startScreen = new StartScreen();
        endScreen = new EndScreen();
        creditsScreen = new CreditsScreen();

        startScreen.init();
        game.init();
        endScreen.init();
        creditsScreen.init();
    }

    private static void startScreen(IScreen screen) {
        currentScreen.stop();
        screen.start();
        currentScreen = screen;
    }

    public static void startGame() {
        startScreen(game);
    }

    public static void endGame(boolean didWin) {
        endScreen.setWon(didWin);
        startScreen(endScreen);
    }

    public static void startCredits(){
        startScreen(creditsScreen);
    }

}
