package hu.hunjam25.dlhc.gameplay;

import hu.hunjam25.dlhc.*;
import hu.hunjam25.dlhc.model.Chef;
import hu.hunjam25.dlhc.model.Ingredient;
import hu.hunjam25.dlhc.model.Workstation;
import hu.hunjam25.dlhc.view.IRenderable;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class Minigame implements IRenderable {

    public Workstation workstation;
    public Chef chef;
    public Ingredient ingredient;

    protected Vec2 renderAreaSize;
    protected float worldScale;

    private enum State {
        ANIM_IN, ANIM_OUT, SHOWN
    }

    private State state = State.ANIM_IN;

    private static final float ANIM_IN_LENGTH = 1.0f;
    private static final float ANIM_OUT_LENGTH = 0.25f;

    private float animStart = Main.now;

    public Minigame(Workstation workstation, Chef chef, Ingredient ingredient) {
        this.workstation = workstation;
        this.chef = chef;
        this.ingredient = ingredient;
    }

    public void endGame() {
        if (isGameEnded()) {
            return;
        }

        animStart = Main.now;
        state = State.ANIM_OUT;

        Kitchen.minigameEnded(getResult());
    }

    public boolean isGameEnded() {
        return state == State.ANIM_OUT;
    }

    public boolean isDisappeared() {
        return isGameEnded() && age() >= 1.0f;
    }

    // mennyire zavart (0-1)
    protected abstract float getResult();

    public abstract void tick(float dt);

    protected abstract void renderGame(Graphics2D g);

    @Override
    public void render(Graphics2D g) {
        float t;
        if (state == State.SHOWN) {
            t = 1.0f;
        } else {
            t = age();

            if (t > 1.0f) {
                state = State.SHOWN;
            }
        }

        float tt = switch (state) {
            case SHOWN -> 1.0f;
            case ANIM_IN -> Utils.EaseOut(t, 0.0f, 1.0f, 1.0f);
            case ANIM_OUT -> Utils.EaseIn(t, 1.0f, -1.0f, 1.0f);
        };

        g.translate(Game.SCREEN_SIZE.x() * 0.5, Game.SCREEN_SIZE.y() * 0.5);

        var tf = g.getTransform();
        var clip = g.getClip();

        float whMin = Math.min(Game.MINIGAME_CONTENT_SIZE.x(), Game.MINIGAME_CONTENT_SIZE.y());
        float whMax = Math.max(Game.MINIGAME_CONTENT_SIZE.x(), Game.MINIGAME_CONTENT_SIZE.y());

        float ndcScale = whMin * 0.5f;
        g.scale(ndcScale * 1.01f, ndcScale * 1.01f);

        if (Game.MINIGAME_CONTENT_SIZE.x() > Game.MINIGAME_CONTENT_SIZE.y()) {
            renderAreaSize = new Vec2(2.0f * (whMax / whMin), 2.0f);
        } else {
            renderAreaSize = new Vec2(2.0f, 2.0f * (whMax / whMin));
        }

        worldScale = 1.0f / ndcScale;

        g.clip(new Rectangle2D.Float(
                -renderAreaSize.x() * 0.5f * tt, -renderAreaSize.y() * 0.5f * tt,
                renderAreaSize.x() * tt, renderAreaSize.y() * tt));
        renderGame(g);

        g.setTransform(tf);
        g.setClip(clip);

        g.scale(tt, tt);

        g.translate(-Game.MINIGAME_FRAME_SIZE.x() * 0.5f, -Game.MINIGAME_FRAME_SIZE.y() * 0.5f);
        g.translate(
                0.5f * (Game.MINIGAME_CONTENT_TOPLEFT_OFFSET.x()
                        - (Game.MINIGAME_FRAME_SIZE.x() - Game.MINIGAME_CONTENT_BOTTOMRIGHT_OFFSET.x())),
                -0.5f * (Game.MINIGAME_CONTENT_TOPLEFT_OFFSET.y()
                        - (Game.MINIGAME_FRAME_SIZE.y() - Game.MINIGAME_CONTENT_BOTTOMRIGHT_OFFSET.y())));
        g.drawImage(AssetManager.getImage("minigame_keret"), 0, 0, null);
    }

    private float age() {
        return (Main.now - animStart) / (state == State.ANIM_IN ? ANIM_IN_LENGTH : ANIM_OUT_LENGTH);
    }
}
