package hu.hunjam25.dlhc;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

import hu.hunjam25.dlhc.model.Chef;
import hu.hunjam25.dlhc.model.Workstation;

public class Game {


    public static final int MAP_X = 10;
    public static final int MAP_Y = 6;

    public static final int TILE_SIZE = 120;

    private static HashMap<String, BufferedImage> imageStorage;

    public static Point2D.Float gameToScreen(Point2D.Float game){
        return new Point2D.Float(game.x * TILE_SIZE, (MAP_Y -game.y) * TILE_SIZE);
    }

    public static Point2D.Float screenToGame(Point2D.Float screen){
        return new Point2D.Float(screen.x / TILE_SIZE, (MAP_Y * TILE_SIZE -screen.y) / TILE_SIZE);
    }

    static void init() throws IOException {
        // TODO: better
        imageStorage = new HashMap<>();
        imageStorage.put("rat", ImageIO.read(Path.of("art", "rat.jpg").toFile()));
        imageStorage.put("tiles", ImageIO.read(Path.of("art", "tiles.png").toFile()));
        imageStorage.put("dot", ImageIO.read(Path.of("art", "dot.png").toFile()) );
    }

    public static BufferedImage getImage(String name) {
        return imageStorage.get(name);
    }

    // null if not in fullscreen
    private static Rectangle lastBounds;

    public static void toggleFullscreen() {
        if (lastBounds == null) {
            lastBounds = Main.frame.getBounds();

            Main.frame.setVisible(false);
            Main.frame.setUndecorated(true); // removes title bar & borders
            Main.frame.setVisible(true);
            GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice()
                    .setFullScreenWindow(Main.frame);
        } else {
            Main.frame.setUndecorated(false); // removes title bar & borders
            GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice()
                    .setFullScreenWindow(null);

            Main.frame.setBounds(lastBounds);
            lastBounds = null;
        }
    }

    public static Set<Integer> keysPressed = new HashSet<>();

    public static float now;

    static void tick(float dt) {
        if (keysPressed.contains(KeyEvent.VK_F11)) {
            toggleFullscreen();
        }

        Kitchen.background.tick(dt);

        for (Workstation w : Kitchen.workstations) {
            w.tick(dt);
        }

        Kitchen.rat.tick(dt);

        for (Chef c : Kitchen.chefs) {
            c.tick(dt);
        }

        if (Kitchen.minigame != null) {
            Kitchen.minigame.tick(dt);
        }
    }

    static void render(Graphics2D g) {
        AffineTransform transform = g.getTransform();
        Kitchen.background.render(g);

        for (Workstation w : Kitchen.workstations) {
            g.setTransform(transform);
            w.render(g);
        }

        g.setTransform(transform);
        Kitchen.rat.render(g);

        for (Chef c : Kitchen.chefs) {
            g.setTransform(transform);
            c.render(g);
        }

        if (Kitchen.minigame != null) {
            g.setTransform(transform);
            // TODO: render frame, translate, clip, etc
            Kitchen.minigame.render(g);
        }
    }

    static KeyListener listener = new KeyListener() {
        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            keysPressed.add(e.getKeyCode());
        }

        @Override
        public void keyReleased(KeyEvent e) {
            keysPressed.remove((Integer) e.getKeyCode());
        }
    };
}
