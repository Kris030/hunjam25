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

    public int workers = 0;

    public Workstation(WorkstationType type, Point.Float position, Point.Float workingOffset) {
        this.type = type;
        this.position = position;
        this.workingOffset = workingOffset;

        this.sprite = new Sprite(AssetManager.getImage(type.name()));
        // sprite.scaleToTileMax();
        sprite.scaleWidth();
        float sc = sprite.getRatio();
        // System.out.println(sc);

        if (sc < 1)
            this.position.y += (1f - (1f / sc)) / 2f;
        else
            this.position.y -= (1f - (1f / sc)) / 2f;
    }

    @Override
    public void render(Graphics2D gd) {
        super.render(gd);
        sprite.render(gd);
    }

    public boolean hasWorker() {
        return workers > 0;
    }
}
