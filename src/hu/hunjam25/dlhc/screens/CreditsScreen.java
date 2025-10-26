package hu.hunjam25.dlhc.screens;

import hu.hunjam25.dlhc.AssetManager;
import hu.hunjam25.dlhc.view.AnimatedSprite;

import java.awt.*;

public class CreditsScreen implements IScreen {
    private AnimatedSprite credits;

    @Override
    public void init() {
        credits =  new AnimatedSprite(AssetManager.getAnim("credits"), 0.5f);
        credits.centered = false;
        credits.start();
        credits.unFreeze();
        credits.setLooping(true);
    }

    @Override
    public void start() {

    }

    @Override
    public void render(Graphics2D g) {
        credits.render(g);
    }

    @Override
    public void tick(float dt) {

    }

    @Override
    public void stop() {
    }
}
