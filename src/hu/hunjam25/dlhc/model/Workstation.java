package hu.hunjam25.dlhc.model;

import hu.hunjam25.dlhc.AssetManager;
import hu.hunjam25.dlhc.GameObject;
import hu.hunjam25.dlhc.Vec2;
import hu.hunjam25.dlhc.view.AnimatedSprite;
import hu.hunjam25.dlhc.view.UiElement;

import java.awt.*;

public class Workstation extends GameObject {

    public enum WorkstationType {
        ChoppingBoard,
        Oven,
        Fridge,
        Stove,
        Sink,
        Trash,
        Belt,

        ;
    }

    private final AnimatedSprite animatedSprite;
    // private Sprite sprite;
    public WorkstationType type = WorkstationType.Stove;

    // relative to GameObject position
    Vec2 workingOffset;

    private Chef worker = null;

    public Workstation(WorkstationType type, Vec2 position, Vec2 workingOffset) {
        this.type = type;
        this.position = position;
        this.workingOffset = workingOffset;

        this.animatedSprite = new AnimatedSprite(AssetManager.getAnim(type.name()), 1);
        this.animatedSprite.scaleWidth();
        if (type == WorkstationType.Trash) {
            setOnFire();
        }
        animatedSprite.start();
    }

    @Override
    public void render(Graphics2D gd) {
        super.render(gd);
        if(type == WorkstationType.Belt){
            updateFrameForBelt();
        }else{
            updateFrame();
        }

        animatedSprite.render(gd);
        renderUiElements(gd);
    }

    private void updateFrame(){
        if (worker != null) {
            animatedSprite.setIdx(1);
        } else {
            animatedSprite.setIdx(0);
        }
    }

    private void updateFrameForBelt(){
        if (worker != null) {
            animatedSprite.unFreeze();
        } else {
            animatedSprite.freeze();
        }
    }

    public boolean hasWorker() {
        return worker!= null;
    }

    private void setOnFire() {
        UiElement timr = new UiElement();
        timr.visible = true;
        timr.scale = 0.2f;

        AnimatedSprite timerSprite = new AnimatedSprite(AssetManager.getAnim("fire"), 0.2f);
        timerSprite.unFreeze();
        timerSprite.start();
        timr.setAnimatedSprite(timerSprite);
        timr.addOffset(new Vec2(0.0f, 0.0f));
        addUiElement(timr);

        UiElement timer = new UiElement();
        timer.visible = true;
        timer.scale = 0.2f;

        AnimatedSprite timrSprite = new AnimatedSprite(AssetManager.getAnim("smoke"), 0.4f);
        timrSprite.unFreeze();
        timrSprite.start();
        timer.setAnimatedSprite(timrSprite);
        timer.addOffset(new Vec2(0.0f, 0.5f));
        addUiElement(timer);
    }

    public void setWorker(Chef worker) {
        this.worker = worker;
    }

    public Vec2 getOffsettedPosition() {
        return position.add(workingOffset);
    }
}
