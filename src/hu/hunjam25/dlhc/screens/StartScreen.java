package hu.hunjam25.dlhc.screens;

import hu.hunjam25.dlhc.AssetManager;
import hu.hunjam25.dlhc.Game;
import hu.hunjam25.dlhc.Main;
import hu.hunjam25.dlhc.view.AnimatedSprite;
import hu.hunjam25.dlhc.view.Sprite;

import java.awt.*;
import java.awt.event.KeyEvent;

public class StartScreen implements IScreen {

    public static AnimatedSprite backg;

    private boolean started = false;

    @Override
    public void init() {
        backg = new AnimatedSprite(AssetManager.getAnim("radio"), 1);
        backg.centered = false;
        backg.setLooping(false);
    }

    @Override
    public void start() {
    }

    @Override
    public void render(Graphics2D g) {
        backg.render(g);
    }

    @Override
    public void tick(float dt) {
        if (backg.hasFinished() && !started) {
            Main.startGame();
            started = true;
        }

        if (!Game.keysPressed.isEmpty()) {
            backg.unFreeze();
            backg.start();
        }
    }

    @Override
    public void stop() {

    }
}
