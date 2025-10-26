package hu.hunjam25.dlhc;

import hu.hunjam25.dlhc.screens.IScreen;
import hu.hunjam25.dlhc.screens.StartScreen;
import hu.hunjam25.dlhc.sound.SoundBuffer;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.util.*;
import java.util.List;

public class Game implements IScreen {

    public static final int SCREEN_WIDTH = 16;
    public static final int SCREEN_HEIGHT = 9;

    public static final int MAP_OFFSET_X = 1;
    public static final int MAP_OFFSET_Y = -3;

    // in tiles
    public static final int MAP_WIDTH = 14;
    public static final int MAP_HEIGHT = 5;

    public static final Vec2 GLOBAL_SKEW = new Vec2(-0.3f, 0f);

    public static final Vec2 CENTER = new Vec2(
            Game.MAP_WIDTH / 2f,
            Game.MAP_HEIGHT / 2f);

    public static final int TILE_SIZE = 120;

    public static final Vec2 SCREEN_SIZE = new Vec2(
            SCREEN_WIDTH * TILE_SIZE, SCREEN_HEIGHT * TILE_SIZE);

    public static final Vec2 MINIGAME_FRAME_SIZE = new Vec2(1169, 916);
    public static final Vec2 MINIGAME_CONTENT_TOPLEFT_OFFSET = new Vec2(150, 207);
    public static final Vec2 MINIGAME_CONTENT_BOTTOMRIGHT_OFFSET = new Vec2(1018, 795);
    public static final Vec2 MINIGAME_CONTENT_SIZE = MINIGAME_CONTENT_BOTTOMRIGHT_OFFSET
            .sub(MINIGAME_CONTENT_TOPLEFT_OFFSET)
            .add(new Vec2(2, 2));

    private static final List<Integer> winCheatCode = List.of(KeyEvent.VK_W, KeyEvent.VK_I, KeyEvent.VK_N);
    private static final List<Integer> loseCheatCode = List.of(KeyEvent.VK_L, KeyEvent.VK_O, KeyEvent.VK_S,
            KeyEvent.VK_E);

    public static Vec2 keepOnMap(Vec2 position) {
        return new Vec2(
                Math.clamp(position.x(), 1, MAP_WIDTH - 2),
                Math.clamp(position.y(), 0.5f, MAP_HEIGHT - 0.5f));
    }

    public static Vec2 gameToScreen(Vec2 game) {
        return new Vec2((game.x() + MAP_OFFSET_X + GLOBAL_SKEW.x() * -game.y()),
                (-MAP_OFFSET_Y + MAP_HEIGHT - game.y()) + GLOBAL_SKEW.y() * game.x()).mul(TILE_SIZE);
    }

    public static Vec2 screenToGame(Vec2 screen) {
        return new Vec2(screen.x() / TILE_SIZE, (MAP_HEIGHT * TILE_SIZE - screen.y()) / TILE_SIZE);
    }

    // null if not in fullscreen
    private static boolean fullscreen = false;

    public static void toggleFullscreen() {
        var device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        fullscreen = !fullscreen;
        Main.frame.dispose();

        if (fullscreen) {
            Main.frame.setUndecorated(true);
            device.setFullScreenWindow(Main.frame);
        } else {
            Main.frame.setUndecorated(false);
            device.setFullScreenWindow(null);
            Main.frame.setVisible(true);
        }
    }

    public static Set<Integer> keysPressed = new HashSet<>();

    @Override
    public void tick(float dt) {
        if (keysPressed.contains(KeyEvent.VK_F11)) {
            toggleFullscreen();
        }

        if (keysPressed.containsAll(winCheatCode)) {
            Main.endGame(true);
        }
        if (keysPressed.containsAll(loseCheatCode)) {
            Main.endGame(false);
        }

        Kitchen.getGameObjects().forEach(o -> o.tick(dt));
        if (Kitchen.minigame != null) {
            if (Kitchen.minigame.isGameRunning())
                Kitchen.minigame.tick(dt);

            // TODO: better key
            if (!Kitchen.minigame.isGameEnded() && keysPressed.contains(KeyEvent.VK_ESCAPE)) {
                Kitchen.minigame.endGame();
            } else if (Kitchen.minigame.isDisappeared()) {
                Kitchen.minigame = null;
            }
        }

        Kitchen.chefs.removeIf(chef -> chef.finished);
        if (Kitchen.chefs.isEmpty()) {
            Main.endGame(Kitchen.rating <= 0.4);
        }

        Kitchen.updateStars();
        Kitchen.particleEffects.removeAll(Kitchen.particleEffectKillList);
        Kitchen.particleEffectKillList.clear();

        ArrayList<Clip> fkl = new ArrayList<>();
        for (var e : fadeoutList.entrySet()) {
            if (e.getValue() >= Main.now) {
                fkl.add(e.getKey());
            }
        }

        for (Clip c : fkl) {
            fadeoutList.remove(c);
            c.stop();
            c.close();
        }
    }

    @Override
    public void stop() {
        stopMusic();
    }

    @Override
    public void render(Graphics2D g) {
        AffineTransform transform = g.getTransform();

        // g.translate(1f * Game.TILE_SIZE, 0f);
        Kitchen.wallpaper.render(g);
        g.setTransform(transform);

        g.clipRect(MAP_OFFSET_X * TILE_SIZE, Math.abs(MAP_OFFSET_Y * TILE_SIZE), MAP_WIDTH * TILE_SIZE,
                MAP_HEIGHT * TILE_SIZE);
        g.shear(GLOBAL_SKEW.x(), GLOBAL_SKEW.y());
        Kitchen.floor.render(g);
        g.setTransform(transform);
        g.setClip(0, 0, SCREEN_WIDTH * TILE_SIZE, SCREEN_HEIGHT * TILE_SIZE);

        Kitchen.getGameObjects().sorted(Comparator.comparingDouble(GameObject::getY).reversed()).forEach(o -> {
            g.setTransform(transform);
            o.render(g);
        });
        g.setTransform(transform);
        Kitchen.woodFrame.render(g);

        Kitchen.stars.render(g);

        if (Kitchen.minigame != null) {
            g.setTransform(transform);
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

    public static SoundBuffer backgroundMusic;
    static Clip currentClip;

    static HashMap<Clip, Float> fadeoutList = new HashMap<>();

    private static void fadeOut(Clip c) {
        // c.loop(0);

        // var ctrl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);

        // var us = 300000;
        // ctrl.shift(ctrl.getValue(), 0.0f, us);

        // fadeoutList.put(c, Main.now + us / 1000000f);
        c.stop();
        c.close();
    }

    public static void playMusic(SoundBuffer music) {
        if (StartScreen.currentClip != null) {
            fadeOut(StartScreen.currentClip);
            StartScreen.currentClip = null;
        }

        if (currentClip != null) {
            fadeOut(currentClip);
        }

        try {
            currentClip = music.play(null);
            currentClip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (LineUnavailableException e) {
            System.err.println("No Music");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void init() {
        backgroundMusic = AssetManager.getSound("music");
    }

    @Override
    public void start() {
        Kitchen.init();
    }

    public static void stopMusic() {
        if (currentClip != null) {
            currentClip.close();
        }
    }
}
