package hu.hunjam25.dlhc;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.util.HashSet;
import java.util.Set;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Game {

    public static final int SCREEN_WIDTH = 16;
    public static final int SCREEN_HEIGHT = 9;

    public static final int MAP_OFFSET_X = 1;
    public static final int MAP_OFFSET_Y = -1;

    // in tiles
    public static final int MAP_WIDTH = 14;
    public static final int MAP_HEIGHT = 7;

    public static final Vec2 CENTER = new Vec2(Game.MAP_OFFSET_X + Game.MAP_WIDTH / 2f,
                    Game.MAP_OFFSET_Y + Game.MAP_HEIGHT / 2f);

    public static final int TILE_SIZE = 120;

    public static void keepOnMap(Vec2 position) {
        position = new Vec2(
                min(Game.MAP_WIDTH + Game.MAP_OFFSET_X, max(Game.MAP_OFFSET_X, position.x())),
                min(Game.MAP_HEIGHT + Game.MAP_OFFSET_Y, max(Game.MAP_OFFSET_Y, position.y())));
    }

    public static Vec2 gameToScreen(Vec2 game) {
        return new Vec2(game.x() * TILE_SIZE, (MAP_HEIGHT - game.y()) * TILE_SIZE);
    }

    public static Vec2 screenToGame(Vec2 screen) {
        return new Vec2(screen.x() / TILE_SIZE, (MAP_HEIGHT * TILE_SIZE - screen.y()) / TILE_SIZE);
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

        Kitchen.particleEffects.removeAll(Kitchen.particleEffectKillList);
        Kitchen.particleEffectKillList.clear();
    }

    static void render(Graphics2D g) {
        AffineTransform transform = g.getTransform();

        g.translate(-TILE_SIZE * 0.5f, -TILE_SIZE * 0.5f);
        Kitchen.background.render(g);
        g.translate(TILE_SIZE * 0.5f, TILE_SIZE * 0.5f);

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
