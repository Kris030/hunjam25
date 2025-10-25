package hu.hunjam25.dlhc;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.util.HashSet;
import java.util.Set;

import static java.lang.Math.*;

public class Game {

    public static final int SCREEN_WIDTH = 16;
    public static final int SCREEN_HEIGHT = 9;

    public static final int MAP_OFFSET_X = 1;
    public static final int MAP_OFFSET_Y = -3;

    // in tiles
    public static final int MAP_WIDTH = 14;
    public static final int MAP_HEIGHT = 5;

    public static final Vec2 CENTER = new Vec2(Game.MAP_WIDTH / 2f,
                     Game.MAP_HEIGHT / 2f);

    public static final int TILE_SIZE = 120;

    public static Vec2 keepOnMap(Vec2 position) {
        return new Vec2(
                min(Game.MAP_WIDTH, max(0, position.x())),
                min(Game.MAP_HEIGHT, max(0, position.y())));
    }

    public static Vec2 gameToScreen(Vec2 game) {
        return new Vec2((game.x() + MAP_OFFSET_X) * TILE_SIZE, (-MAP_OFFSET_Y + MAP_HEIGHT - game.y()) * TILE_SIZE);
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


        //g.translate(1f * Game.TILE_SIZE, 0f);
        Kitchen.wallpaper.render(g);
        g.setTransform(transform);
        Kitchen.woodFrame.render(g);

        g.clipRect(MAP_OFFSET_X * TILE_SIZE, abs(MAP_OFFSET_Y * TILE_SIZE), MAP_WIDTH * TILE_SIZE,MAP_HEIGHT*TILE_SIZE );
        g.shear(-0.3,0);
        Kitchen.floor.render(g);
        g.setTransform(transform);
        g.setClip(0,0,SCREEN_WIDTH * TILE_SIZE,SCREEN_HEIGHT * TILE_SIZE );

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
