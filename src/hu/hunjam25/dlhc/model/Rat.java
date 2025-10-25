package hu.hunjam25.dlhc.model;

import hu.hunjam25.dlhc.Game;
import hu.hunjam25.dlhc.GameObject;
import hu.hunjam25.dlhc.view.RatView;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Rat extends GameObject {
    static float SPEED = 0.005f;

    private boolean mirrored = false;

    public Rat() {
        view = new RatView();
        position.x = 3;
        position.y = 7;
    }

    @Override
    public void tick(float dt) {
        super.tick(dt);
        ratMotion(dt);
    }

    private void ratMotion(float dt) {
        var keys = Game.keysPressed;

        Point2D.Float velocity = new Point2D.Float(0f, 0f);

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
            mirrored = velocity.x < 0f;
        }

        position.x += velocity.x * SPEED * dt;
        position.y += velocity.y * SPEED * dt;

        position.x = max(0,position.x);
        position.x = min(Game.MAP_X,position.x);
        position.y = max(0,position.y);
        position.y = min(Game.MAP_Y,position.y);
    }

    @Override
    public void render(Graphics2D gd) {
        var screen = Game.gameToScreen(position);
        gd.translate(screen.x,screen.y);
        gd.scale(mirrored ? 1f : -1f, 1f);
        view.render(gd);
    }
}
