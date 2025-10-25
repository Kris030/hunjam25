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

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Game {

    public static final int SCREEN_WIDTH = 10;
    public static final int SCREEN_HEIGHT = 7;

    public static final int MAP_OFFSET_X = 1;
    public static final int MAP_OFFSET_Y = -1;

    // in tiles
    public static final int MAP_WIDTH = 8;
    public static final int MAP_HEIGHT = 5;

    public static final int TILE_SIZE = 120;

    public static void keepOnMap(Point2D.Float position) {
        position.x = max(Game.MAP_OFFSET_X, position.x);
        position.x = min(Game.MAP_WIDTH + Game.MAP_OFFSET_X, position.x);
        position.y = max(Game.MAP_OFFSET_Y, position.y);
        position.y = min(Game.MAP_HEIGHT + Game.MAP_OFFSET_Y, position.y);
    }

    private static HashMap<String, BufferedImage> imageStorage;

    public static Point2D.Float gameToScreen(Point2D.Float game) {
        return new Point2D.Float(game.x * TILE_SIZE, (MAP_HEIGHT - game.y) * TILE_SIZE);
    }

    public static Point2D.Float screenToGame(Point2D.Float screen) {
        return new Point2D.Float(screen.x / TILE_SIZE, (MAP_HEIGHT * TILE_SIZE - screen.y) / TILE_SIZE);
    }

    static void init() throws IOException {
        // TODO: better
        imageStorage = new HashMap<>();
        imageStorage.put("rat", ImageIO.read(Path.of("art", "rat.png").toFile()));
        imageStorage.put("tiles", ImageIO.read(Path.of("art", "tiles.png").toFile()));
        imageStorage.put("dot", ImageIO.read(Path.of("art", "dot.png").toFile()));
    }

    public static BufferedImage getImage(String name) {
        return imageStorage.get(name);
    }

    // null if not in fullscreen
    private static Rectangle lastBounds;

    public static void toggleFullscreen() {
        // FIXME
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

        Kitchen.getGameObjects().forEach(o -> o.tick(dt));
    }

    static void render(Graphics2D g) {
        AffineTransform transform = g.getTransform();
        Kitchen.background.render(g);

        Kitchen.getGameObjects().forEach(o -> {
            g.setTransform(transform);
            o.render(g);
        });
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
