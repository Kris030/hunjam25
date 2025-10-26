package hu.hunjam25.dlhc.screens;

import hu.hunjam25.dlhc.AssetManager;
import hu.hunjam25.dlhc.view.AnimatedSprite;
import hu.hunjam25.dlhc.view.Sprite;

import java.awt.*;

public class EndScreen implements IScreen {

    public static AnimatedSprite backg;

    @Override
    public void init() {
        backg = new AnimatedSprite( AssetManager.getAnim("chef1"), 1);
        backg.centered = false;
    }

    @Override
    public void start() {
        backg.start();
    }

    @Override
    public void render(Graphics2D g) {
        backg.render(g);
    }

    @Override
    public void tick(float dt) {

    }

    @Override
    public void stop() {

    }
}
