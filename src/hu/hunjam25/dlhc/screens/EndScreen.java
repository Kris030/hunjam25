package hu.hunjam25.dlhc.screens;

import hu.hunjam25.dlhc.AssetManager;
import hu.hunjam25.dlhc.view.Sprite;

import java.awt.*;

public class EndScreen implements IScreen {

    public static Sprite backg;

    @Override
    public void init() {
        backg = new Sprite(AssetManager.getImage("mark"));
        backg.centered = false;
    }

    @Override
    public void render(Graphics2D g) {
        backg.render(g);
    }

    @Override
    public void tick(float dt) {

    }
}
