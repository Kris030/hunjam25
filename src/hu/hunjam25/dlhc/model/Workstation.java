package hu.hunjam25.dlhc.model;

import hu.hunjam25.dlhc.Game;
import hu.hunjam25.dlhc.GameObject;
import hu.hunjam25.dlhc.view.Sprite;

import java.awt.*;

public class Workstation extends GameObject {

    public static enum WorkstationType {
        ChoppingBoard,
        Oven,
        Fridge,
        Stove,

        ;
    }

    WorkstationType type;

    private Sprite mark = new Sprite(Game.getImage("mark"));

    @Override
    public void render(Graphics2D gd) {
        super.render(gd);
        mark.render(gd);
    }
}
