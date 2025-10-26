package hu.hunjam25.dlhc.gameplay;

import hu.hunjam25.dlhc.AssetManager;
import hu.hunjam25.dlhc.Game;
import hu.hunjam25.dlhc.model.Chef;
import hu.hunjam25.dlhc.model.Ingredient;
import hu.hunjam25.dlhc.model.Workstation;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;

public class FridgeMinigame extends Minigame {

    private float state = 0.0f;
    private boolean previouslyPressed = false;

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

        if (click) {
            state += 0.1f;
        }
        state = Math.clamp(state, -1.0f, 1.0f);
    }

    protected void renderGame(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fill(new Rectangle2D.Float(
                -renderAreaSize.x() * 0.5f, -renderAreaSize.y() * 0.5f,
                renderAreaSize.x(), renderAreaSize.y())
        );

        //g.scale(0.5, 0.5);

        var anim = state > 0.0f
                ? AssetManager.getAnim("huto_minigame_nyer")
                : AssetManager.getAnim("huto_minigame_veszit");

        var frame = anim[(int) ((anim.length - 1) * Math.abs(state))];

        // setColor, translate(x, y), fill(-1, -1, 2, 2)
        g.scale(1.0f / frame.getWidth(), 1.0f / frame.getHeight());
        g.drawImage(frame, null, 0, 0);
    }
}
