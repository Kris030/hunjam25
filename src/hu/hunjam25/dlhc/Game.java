package hu.hunjam25.dlhc;

import java.awt.*;
import java.util.ArrayList;

import hu.hunjam25.dlhc.gameplay.Minigame;

class Game {

    ArrayList<GameObject> objects;

    Minigame minigame;

    static void tick(float dt) {
        if (minigame != null) {
            minigame.tick();
        }

        for (GameObject g : objects) {
            g.tick();
        }
    }

    static void render(Graphics2D g) {
    }

}
