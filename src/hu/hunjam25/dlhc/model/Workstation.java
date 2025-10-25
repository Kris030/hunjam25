package hu.hunjam25.dlhc.model;

import hu.hunjam25.dlhc.AssetManager;
import hu.hunjam25.dlhc.GameObject;
import hu.hunjam25.dlhc.Vec2;
import hu.hunjam25.dlhc.gameplay.ChoppingBoard;
import hu.hunjam25.dlhc.gameplay.Minigame;
import hu.hunjam25.dlhc.view.AnimatedSprite;
import hu.hunjam25.dlhc.view.UiElement;

import java.awt.*;

public class Workstation extends GameObject {

    public static enum WorkstationType {
        ChoppingBoard,
        Oven,
        Fridge,
        Stove,
        Sink,
        Trash,
        Belt,

        ;
    }

    private AnimatedSprite animatedSprite;
    // private Sprite sprite;
    public WorkstationType type = WorkstationType.Stove;

    // relative to GameObject position
    Vec2 workingOffset;

    public int workers = 0;

    public Workstation(WorkstationType type, Vec2 position, Vec2 workingOffset) {
        this.type = type;
        this.position = position;
        this.workingOffset = workingOffset;

        this.animatedSprite = new AnimatedSprite(AssetManager.getAnim(type.name()), 0);
        this.animatedSprite.scaleWidth();
        if (type == WorkstationType.Trash) {
            setOnFire();
        }
        // sprite.scaleToTileMax();
        // sprite.scaleWidth();
        // float sc = sprite.getRatio();
        // System.out.println(sc);

        // if (sc < 1)
        // this.position.y += (1f - (1f / sc)) / 2f;
        // else
        // this.position.y -= (1f - (1f / sc)) / 2f;
    }

    @Override
    public void render(Graphics2D gd) {
        super.render(gd);
        if (workers > 0) {
            animatedSprite.setIdx(1);
        } else {
            animatedSprite.setIdx(0);
        }

        animatedSprite.render(gd);
        renderUiElements(gd);
    }

    public boolean hasWorker() {
        return workers > 0;
    }

    private void setOnFire() {
        UiElement timr = new UiElement();
        timr.visible = true;
        timr.scale = 0.2f;

        AnimatedSprite timerSprite = new AnimatedSprite(AssetManager.getAnim("fire"), 0.2f);
        timerSprite.frozen = false;
        timerSprite.start();
        timr.setAnimatedSprite(timerSprite);
        timr.addOffset(new Vec2(0.0f, 0.0f));
        addUiElement(timr);

        UiElement timer = new UiElement();
        timer.visible = true;
        timer.scale = 0.2f;

        AnimatedSprite timrSprite = new AnimatedSprite(AssetManager.getAnim("smoke"), 0.4f);
        timrSprite.frozen = false;
        timrSprite.start();
        timer.setAnimatedSprite(timrSprite);
        timer.addOffset(new Vec2(0.0f, 0.5f));
        addUiElement(timer);
    }

    public Minigame getMinigame() {
        switch (type) {
            case ChoppingBoard:
                return new ChoppingBoard();
            case Fridge:
                break;
            case Oven:
                break;
            case Stove:
                break;
            case Trash:
                break;
        }

        // FIXME
        return null;
    }

    public Vec2 getOffsettedPosition() {
        return position.add(workingOffset);
    }
}
