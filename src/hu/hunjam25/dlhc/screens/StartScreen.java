package hu.hunjam25.dlhc.screens;

import hu.hunjam25.dlhc.AssetManager;
import hu.hunjam25.dlhc.view.AnimatedSprite;
import hu.hunjam25.dlhc.view.Sprite;

import java.awt.*;

public class StartScreen implements IScreen {

    public static AnimatedSprite backg;

    @Override
    public void init() {
        backg = new AnimatedSprite(AssetManager.getAnim("radio"), 4 );
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
