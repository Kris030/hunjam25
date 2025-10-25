package hu.hunjam25.dlhc.model;

import hu.hunjam25.dlhc.AssetManager;
import hu.hunjam25.dlhc.Game;
import hu.hunjam25.dlhc.GameObject;
import hu.hunjam25.dlhc.Kitchen;
import hu.hunjam25.dlhc.Vec2;
import hu.hunjam25.dlhc.view.AnimatedSprite;
import hu.hunjam25.dlhc.view.Sprite;

import java.awt.*;
import java.awt.event.KeyEvent;

import static hu.hunjam25.dlhc.Game.keepOnMap;

public class Rat extends GameObject {

    static float SPEED = 0.005f;

    private Sprite dot = new Sprite(AssetManager.getImage("dot"));
    //private Sprite ratView = new Sprite(Game.getImage("rat"));

    private AnimatedSprite remi ;


    public Rat() {
        positionToCenter();
        //ratView.spriteScale = 0.25f;
        //position =  new Vec2(5f, 4f);
        remi = new AnimatedSprite(AssetManager.getAnim("remi"), 2.5f);
        remi.setScale(0.25f);

        remi.unFreeze();
        remi.start();
    }

    @Override
    public void tick(float dt) {
        super.tick(dt);

        ratMotion(dt);

        if (Kitchen.minigame == null && Game.keysPressed.contains(KeyEvent.VK_M)) {
            var w = Kitchen.workstations.stream()
                    .min((w1, w2) -> Double.compare(
                            w1.getOffsettedPosition().dist(this.position),
                            w2.getOffsettedPosition().dist(this.position)))

                    // there allways will be a workstation
                    .orElse(null);

            if (w.getPosition().dist(this.position) < 0.1f) {
                Kitchen.startMinigame(w);
                position = w.getOffsettedPosition();
            }
        }
    }

    private void ratMotion(float dt) {
        var keys = Game.keysPressed;

        float xx = 0, yy = 0;

        if (keys.contains(KeyEvent.VK_UP) || keys.contains(KeyEvent.VK_W)) {
            yy += 1;
        }

        if (keys.contains(KeyEvent.VK_DOWN) || keys.contains(KeyEvent.VK_S)) {
            yy -= 1;
        }

        if (keys.contains(KeyEvent.VK_LEFT) || keys.contains(KeyEvent.VK_A)) {
            xx -= 1;
        }

        if (keys.contains(KeyEvent.VK_RIGHT) || keys.contains(KeyEvent.VK_D)) {
            xx += 1;
        }

        Vec2 velocity = new Vec2(xx, yy);

        if (velocity.x() != 0f && velocity.y() != 0f) {
            velocity = velocity.norm();
        }

        if (velocity.x() != 0f) {
            remi.mirrored = velocity.x() > 0f;
        }

        position = position.add(velocity.mul(SPEED * dt));

        keepOnMap(position);
    }

    @Override
    public void render(Graphics2D gd) {
        super.render(gd);
        //ratView.render(gd);

        remi.render(gd);
        dot.render(gd);

        renderUiElements(gd);
    }
}
