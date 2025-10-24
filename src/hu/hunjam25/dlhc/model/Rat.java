package hu.hunjam25.dlhc.model;

import hu.hunjam25.dlhc.Game;
import hu.hunjam25.dlhc.GameObject;
import hu.hunjam25.dlhc.view.RatView;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Rat extends GameObject {
    static float SPEED = 5f;

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

        if(keys.contains(KeyEvent.VK_UP)){
            position.y += SPEED * dt;
            System.out.println(position.y);
        }
    }

    @Override
    public void render(Graphics2D gd) {
        gd.translate(position.x, position.y);
        view.render(gd);
    }
}
