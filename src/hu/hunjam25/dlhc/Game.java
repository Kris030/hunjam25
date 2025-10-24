package hu.hunjam25.dlhc;

import java.awt.*;
import java.util.ArrayList;

import hu.hunjam25.dlhc.gameplay.Minigame;

class Game {

    static class Motion{

    }

    static ArrayList<GameObject> objects = new ArrayList<>();

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
