package hu.hunjam25.dlhc.model;

import hu.hunjam25.dlhc.AssetManager;
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
    private Sprite sprite;
    public WorkstationType type = WorkstationType.Stove;

    // relative to GameObject position
    Point.Float workingOffset;

    public Workstation(WorkstationType type, Point.Float position, Point.Float workingOffset) {
        this.type = type;
        this.position = position;
        this.workingOffset = workingOffset;


        this.sprite = new Sprite(AssetManager.getImage(type.name()));
        sprite.scaleToTileMin();
    }


    @Override
    public void render(Graphics2D gd) {
        super.render(gd);
        sprite.render(gd);
    }
}
