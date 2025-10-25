package hu.hunjam25.dlhc.gameplay;

import hu.hunjam25.dlhc.Game;
import hu.hunjam25.dlhc.model.Chef;
import hu.hunjam25.dlhc.model.Ingredient;
import hu.hunjam25.dlhc.model.Workstation;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;

public class DefaultMinigame extends Minigame {

    public DefaultMinigame(Workstation workstation, Chef chef, Ingredient ingredient) {
        super(workstation, chef, ingredient);
    }

    @Override
    protected float getResult() {
        return 0.0f;
    }

    @Override
    public void tick(float dt) {
        if (Game.keysPressed.contains(KeyEvent.VK_E)) {
            endGame();
        }

        x += dt / 200;

        if (x >= 1) {
            x = -1;
        }
    }

    float x = -1;

    @Override
    protected void renderGame(Graphics2D gd) {
        gd.setColor(Color.GRAY);
        gd.fill(new Rectangle2D.Float(
                -renderAreaSize.x() * 0.5f, -renderAreaSize.y() * 0.5f,
                renderAreaSize.x(), renderAreaSize.y())
        );

        gd.setColor(Color.RED);
        gd.translate(x, 0);
        gd.scale(0.5, 0.5);
        gd.fillRect(-1, -1, 2, 2);
    }
}
