package hu.hunjam25.dlhc.model;

import hu.hunjam25.dlhc.Game;
import hu.hunjam25.dlhc.GameObject;
import hu.hunjam25.dlhc.view.AnimatedSprite;
import hu.hunjam25.dlhc.view.Sprite;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import static hu.hunjam25.dlhc.Game.keepOnMap;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class Rat extends GameObject {
    static float SPEED = 0.005f;

    static Point2D.Float velocity = new Point2D.Float(0f, 0f);

    private Sprite dot = new Sprite(Game.getImage("dot"));
    private Sprite ratView = new Sprite(Game.getImage("rat"));

    public static AnimatedSprite remi ;


    public Rat() {
        positionToCenter();
        ratView.spriteScale = 0.25f;

        BufferedImage[] frames = new BufferedImage[]{
                Game.getImage("remi1") ,
                Game.getImage("remi2") ,
                Game.getImage("remi3") ,
                Game.getImage("remi4") ,
                Game.getImage("remi5") ,
                Game.getImage("remi6")
        };
        remi = new AnimatedSprite(frames, 1f);
        remi.unFreeze();
        remi.start();
    }

    @Override
    public void tick(float dt) {
        super.tick(dt);
        ratMotion(dt);
    }

    private void ratMotion(float dt) {
        var keys = Game.keysPressed;

        velocity = new Point2D.Float(0f, 0f);

        if (keys.contains(KeyEvent.VK_UP)) {
            velocity.y += 1;
        }

        if (keys.contains(KeyEvent.VK_DOWN)) {
            velocity.y -= 1;
        }

        if (keys.contains(KeyEvent.VK_LEFT)) {
            velocity.x -= 1;
        }

        if (keys.contains(KeyEvent.VK_RIGHT)) {
            velocity.x += 1;
        }

        if (velocity.x != 0f && velocity.y != 0f) {
            var length = velocity.distance(0, 0);
            velocity.x /= length;
            velocity.y /= length;
        }

        if (velocity.x != 0f) {
            ratView.mirrored = velocity.x > 0f;
        }

        position.x += velocity.x * SPEED * dt;
        position.y += velocity.y * SPEED * dt;

        keepOnMap(position);
    }

    @Override
    public void render(Graphics2D gd) {
        super.render(gd);
        //ratView.render(gd);
        dot.render(gd);

        remi.render(gd);
    }
}
