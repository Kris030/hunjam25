package hu.hunjam25.dlhc.model;

import hu.hunjam25.dlhc.*;
import hu.hunjam25.dlhc.view.AnimatedSprite;

import java.awt.*;
import java.awt.event.KeyEvent;

import static hu.hunjam25.dlhc.Game.keepOnMap;

public class Rat extends GameObject {

    // How close do you have to be to start minigame
    public static final float RAT_RANGE = 0.9f;
    static float SPEED = 5f;

    private AnimatedSprite remi;

    public Rat() {
        positionToCenter();
        remi = new AnimatedSprite(AssetManager.getAnim("remi"), 2.5f);
        remi.setScale(0.25f);

        remi.unFreeze();
        remi.start();
    }

    @Override
    public void tick(float dt) {
        super.tick(dt);

        if (Kitchen.minigame == null || Kitchen.minigame.isGameEnded()) {
            ratMotion(dt);
        }

        if ((Kitchen.minigame == null || Kitchen.minigame.isGameEnded()) && Game.keysPressed.contains(KeyEvent.VK_M)) {
            var w = Kitchen.workstations.stream()
                    .min((w1, w2) -> Double.compare(
                            w1.getOffsettedPosition().dist(this.position),
                            w2.getOffsettedPosition().dist(this.position)))

                    // there always will be a workstation
                    .get();

            float dist = w.getOffsettedPosition().dist(this.position);
            if (dist < RAT_RANGE && w.hasWorker()) {
                var chef = Kitchen.chefs.stream()
                        .filter(c -> c.currWorkstation == w)
                        .min((c1, c2) -> Float.compare(
                                c1.getPosition().dist(this.position), c2.getPosition().dist(this.position)))
                        .get();

                Kitchen.startMinigame(w, chef, chef.todo[chef.currIngredient]);
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

        position = keepOnMap(position);
    }

    @Override
    public void render(Graphics2D gd) {
        super.render(gd);

        remi.render(gd);
        renderUiElements(gd);
    }
}
