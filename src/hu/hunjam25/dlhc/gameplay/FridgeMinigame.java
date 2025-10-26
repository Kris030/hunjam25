package hu.hunjam25.dlhc.gameplay;

import hu.hunjam25.dlhc.AssetManager;
import hu.hunjam25.dlhc.Game;
import hu.hunjam25.dlhc.Utils;
import hu.hunjam25.dlhc.model.Chef;
import hu.hunjam25.dlhc.model.Ingredient;
import hu.hunjam25.dlhc.model.Workstation;
import hu.hunjam25.dlhc.view.AnimatedSprite;
import hu.hunjam25.dlhc.view.Sprite;

import java.awt.*;
import java.awt.event.KeyEvent;

public class FridgeMinigame extends Minigame {

    private float state = 0.0f;
    private float renderState = 0.0f;
    private boolean previouslyPressed = false;

    private final float DIFFICULTY = 0.5f;
    private final float CLICK_SENSITIVITY = 0.1f;
    private final float SMOOTHNESS = 2.0f;

    private final Sprite nyerFinal = new Sprite(AssetManager.getImage("huto_minigame_nyer"));
    private final Sprite veszitFinal = new Sprite(AssetManager.getImage("huto_minigame_veszit"));

    private final AnimatedSprite nyer = new AnimatedSprite(AssetManager.getAnim("huto_minigame_nyer"), 0);
    private final AnimatedSprite veszit = new AnimatedSprite(AssetManager.getAnim("huto_minigame_veszit"), 0);

    public FridgeMinigame(Workstation workstation, Chef chef, Ingredient ingredient) {
        super(workstation, chef, ingredient);
    }

    protected float getResult() {
        return (state + 1.0f) * 0.5f;
    }

    public void tick(float dt) {
        if (isGameEnded())
            return;

        boolean click = false;
        if (Game.keysPressed.contains(KeyEvent.VK_SPACE)) {
            if (!previouslyPressed)
                click = true;
            previouslyPressed = true;
        } else {
            previouslyPressed = false;
        }

        state -= DIFFICULTY * dt;
        if (click) {
            state += CLICK_SENSITIVITY;
        }

        if (state <= -1.0f || state >= 1.0f) {
            endGame();
        }

        renderState = Utils.interpolateExp(dt / SMOOTHNESS, renderState, state);
    }

    protected void renderGame(Graphics2D g) {
        if (state <= -1.0f) {
            veszitFinal.spriteScale = worldScale;
            veszitFinal.render(g);
        } else if (Math.abs(state) < 1.0f) {
            var anim = state > 0.0f ? nyer : veszit;
            anim.setIdx((int) ((anim.getNumberOfFrames() - 1) * Math.abs(renderState)));
            anim.setScale(worldScale);

            anim.render(g);
        } else {
            nyerFinal.spriteScale = worldScale;
            nyerFinal.render(g);
        }
    }
}
