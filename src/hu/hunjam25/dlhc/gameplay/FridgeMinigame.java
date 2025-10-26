package hu.hunjam25.dlhc.gameplay;

import hu.hunjam25.dlhc.AssetManager;
import hu.hunjam25.dlhc.Game;
import hu.hunjam25.dlhc.Utils;
import hu.hunjam25.dlhc.model.Chef;
import hu.hunjam25.dlhc.model.Ingredient;
import hu.hunjam25.dlhc.model.Workstation;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class FridgeMinigame extends Minigame {

    private float state = 0.0f;
    private float renderState = 0.0f;
    private boolean previouslyPressed = false;

    private final float DIFFICULTY = 0.75f;
    private final float CLICK_SENSITIVITY = 0.1f;
    private final float SMOOTHNESS = 2.0f;

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
        g.setColor(Color.BLACK);
        g.fill(new Rectangle2D.Float(
                -renderAreaSize.x() * 0.5f, -renderAreaSize.y() * 0.5f,
                renderAreaSize.x(), renderAreaSize.y())
        );

        BufferedImage frame;
        if (state <= -1.0f) {
            frame = AssetManager.getImage("huto_minigame_veszit");
        } else if (Math.abs(state) < 1.0f) {
            var anim = state > 0.0f
                    ? AssetManager.getAnim("huto_minigame_nyer")
                    : AssetManager.getAnim("huto_minigame_veszit");
            frame = anim[(int) ((anim.length - 1) * Math.abs(renderState))];
        } else {
            frame = AssetManager.getImage("huto_minigame_nyer");
        }

        g.translate(-renderAreaSize.x() * 0.5, -renderAreaSize.y() * 0.5);
        g.scale(renderAreaSize.x() / frame.getWidth(), renderAreaSize.y() / frame.getHeight());
        g.drawImage(frame, null, 0, 0);
    }
}