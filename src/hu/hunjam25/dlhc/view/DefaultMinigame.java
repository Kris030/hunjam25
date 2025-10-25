package hu.hunjam25.dlhc.view;

import hu.hunjam25.dlhc.Game;

import java.awt.*;

public class DefaultMinigame extends Minigame {

    @Override
    protected void renderGame(Graphics2D gd) {
        gd.setColor(Color.RED);
        gd.fillRect(0, 0, (int) Game.MINIGAME_SIZE.getX(), (int)Game.MINIGAME_SIZE.getY());
    }
}
