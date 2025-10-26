package hu.hunjam25.dlhc.screens;

import hu.hunjam25.dlhc.AssetManager;
import hu.hunjam25.dlhc.view.Sprite;

import java.awt.*;

public class CreditsScreen implements IScreen {
    private Sprite credits;

    @Override
    public void init() {
        credits =  new Sprite(AssetManager.getImage("credits"));
        credits.centered = false;
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
