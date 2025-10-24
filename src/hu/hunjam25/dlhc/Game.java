package hu.hunjam25.dlhc;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import hu.hunjam25.dlhc.gameplay.Minigame;

public class Game {

    public static KeyListener listener = new KeyListener() {
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

    public static Set<Integer> keysPressed = new HashSet<>();

    public static ArrayList<GameObject> objects = new ArrayList<>();

    static Minigame minigame;

    static void tick(float dt) {
        if (minigame != null) {
            minigame.tick(dt);
        }

        objects.forEach(o -> o.tick(dt));
    }

    static void render(Graphics2D g) {
        objects.forEach( o -> o.render(g));
    }
}
