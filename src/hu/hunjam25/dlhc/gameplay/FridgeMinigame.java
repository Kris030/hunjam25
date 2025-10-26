package hu.hunjam25.dlhc.gameplay;

import hu.hunjam25.dlhc.AssetManager;
import hu.hunjam25.dlhc.Game;
import hu.hunjam25.dlhc.Utils;
import hu.hunjam25.dlhc.model.Chef;
import hu.hunjam25.dlhc.model.Ingredient;
import hu.hunjam25.dlhc.model.Workstation;
import hu.hunjam25.dlhc.sound.SoundBuffer;
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

    private final Sprite winFinal = new Sprite(AssetManager.getImage("huto_minigame_nyer"));
    private final Sprite loseFinal = new Sprite(AssetManager.getImage("huto_minigame_veszit"));

    private final AnimatedSprite win = new AnimatedSprite(AssetManager.getAnim("huto_minigame_nyer"), 0);
    private final AnimatedSprite lose = new AnimatedSprite(AssetManager.getAnim("huto_minigame_veszit"), 0);

    public FridgeMinigame(Workstation workstation, Chef chef, Ingredient ingredient) {
        super(workstation, chef, ingredient);
    }

    protected float getResult() {
        return (state + 1.0f) * 0.5f;
    }

    public void tick(float dt) {
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

        renderState = Utils.interpolateExp(dt / SMOOTHNESS, 100.0f, renderState, state);
    }

    protected void renderGame(Graphics2D g) {
        if (state <= -1.0f) {
            loseFinal.spriteScale = worldScale;
            loseFinal.render(g);
        } else if (Math.abs(state) < 1.0f) {
            var anim = state > 0.0f ? win : lose;
            anim.setIdx((int) ((anim.getNumberOfFrames() - 1) * Math.abs(renderState)));
            anim.setScale(worldScale);

            anim.render(g);
        } else {
            winFinal.spriteScale = worldScale;
            winFinal.render(g);
        }
    }

    @Override
    public SoundBuffer getMusic() {
        return AssetManager.getSound("fridgeMusic");
    }
}
