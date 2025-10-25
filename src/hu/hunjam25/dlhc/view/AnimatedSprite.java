package hu.hunjam25.dlhc.view;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import hu.hunjam25.dlhc.Game;

public class AnimatedSprite implements IRenderable {

    private BufferedImage[] images;

    public boolean centered = true;

    public boolean mirrored = false;

    public boolean frozen = true;

    private float frozenAge;

    public float animLength;

    public float animStarted;

    public AnimatedSprite(BufferedImage[] images) {
        this.images = images;
    }

    float getAge() {
        return (Game.now - animStarted) % animLength;
    }

    public void freeze() {
        frozen = true;
        frozenAge = getAge();
    }

    public void unFreeze() {
        frozen = false;
    }

    public void start() {
        animStarted = Game.now;
    }

    // itt egy frame select valahogy jó lenne
    // (dt vagy tárolja a jelenlegit és inkrementál)
    @Override
    public void render(Graphics2D gd) {
        float age = frozen ? frozenAge : getAge();
        int idx = (int) (images.length * age / animLength);
        BufferedImage image = images[idx];

        if (centered) {
            Sprite.center(gd, image.getWidth(), image.getHeight());
        }

        if (mirrored) {
            Sprite.mirrorX(gd);
        }

        gd.drawImage(image, 0, 0, null);
    }
}
