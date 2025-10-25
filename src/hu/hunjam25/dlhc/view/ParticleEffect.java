package hu.hunjam25.dlhc.view;

import java.awt.Graphics2D;

import hu.hunjam25.dlhc.Game;
import hu.hunjam25.dlhc.GameObject;
import hu.hunjam25.dlhc.Kitchen;

public class ParticleEffect extends GameObject {

    private AnimatedSprite animation;

    public ParticleEffect(AnimatedSprite animation) {
        this.animation = animation;
        animation.unFreeze();
        animation.start();
    }

    @Override
    public void tick(float dt) {
        super.tick(dt);

        if (animation.getAge() >= animation.animLength) {
            Kitchen.particleEffectKillList.add(this);
        }
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);
        animation.render(g);
    }
}
