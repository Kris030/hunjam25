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

    public Workstation(WorkstationType type, Point.Float position, Point.Float workingOffset) {
        this.type = type;
        this.position = position;
        this.workingOffset = workingOffset;
    }

    public WorkstationType type;

    // relative to GameObject position
    Point.Float workingOffset;

    private Sprite mark = new Sprite(Game.getImage("mark"));

    @Override
    public void render(Graphics2D gd) {
        super.render(gd);
        mark.render(gd);
    }
}
