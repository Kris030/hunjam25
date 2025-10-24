package hu.hunjam25.dlhc.gameplay;

import java.awt.Graphics2D;

import hu.hunjam25.dlhc.GameObject;
import hu.hunjam25.dlhc.Kitchen;
import hu.hunjam25.dlhc.model.Chef;
import hu.hunjam25.dlhc.model.Ingredient;
import hu.hunjam25.dlhc.model.Workstation;

public abstract class Minigame extends GameObject {

    public Ingredient ingredient;
    public Workstation workstation;
    public Chef chef;

    @Override
    public void tick(float dt) {
        // TODO
        boolean ended = true;
        if (ended) {
            Kitchen.minigameEnded(-1);
        }
    }

    @Override
    public void render(Graphics2D gd) {
    }
}
