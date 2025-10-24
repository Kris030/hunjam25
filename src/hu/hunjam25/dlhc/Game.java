package hu.hunjam25.dlhc;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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

    private static HashMap<String, BufferedImage> imageStorage;

    static void init() throws IOException {
        // TODO: better
        imageStorage = new HashMap<>();
        imageStorage.put("rat", ImageIO.read(Path.of("art", "rat.png").toFile()));
    }

    public static BufferedImage getImage(String name) {
        return imageStorage.get(name);
    }

    public static Set<Integer> keysPressed = new HashSet<>();

    public static float now;

    static void tick(float dt) {
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
        Kitchen.background.render(g);

        for (Workstation w : Kitchen.workstations) {
            w.render(g);
        }

        Kitchen.rat.render(g);

        for (Chef c : Kitchen.chefs) {
            c.render(g);
        }

        if (Kitchen.minigame != null) {
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
