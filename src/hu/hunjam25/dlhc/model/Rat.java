package hu.hunjam25.dlhc.model;

import hu.hunjam25.dlhc.Game;
import hu.hunjam25.dlhc.GameObject;
import hu.hunjam25.dlhc.view.RatView;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;

public class Rat extends GameObject {
    static float SPEED = 0.1f;

    private boolean mirrored = false;


    public Rat(){
        view = new RatView();
    }

    @Override
    public void tick(float dt) {
        super.tick(dt);
        ratMotion(dt);
    }

    private void ratMotion(float dt){
        var keys = Game.keysPressed;

        Point2D.Float velocity = new Point2D.Float(0f,0f);

        if(keys.contains(KeyEvent.VK_UP)){
            velocity.y += SPEED * dt;
        }

        if(keys.contains(KeyEvent.VK_DOWN)){
            velocity.y -= SPEED * dt;
        }
        if(keys.contains(KeyEvent.VK_LEFT)){
            velocity.x -= SPEED * dt;
        }
        if(keys.contains(KeyEvent.VK_RIGHT)) {
            velocity.x += SPEED * dt;
        }
        if(velocity.x != 0f && velocity.y != 0f){
            velocity.x /=  velocity.distance(0,0);
            velocity.y /=  velocity.distance(0,0);
        }
        if(velocity.x != 0f){
            mirrored = velocity.x < 0f;
        }


        position.x +=  velocity.x * SPEED * dt;
        position.y +=  velocity.y * SPEED * dt;
    }

    @Override
    public void render(Graphics2D gd) {
        gd.translate(position.x, -position.y);
        gd.scale( mirrored ? 0.1f: -0.1f ,0.1f);
        view.render(gd);
    }
}
