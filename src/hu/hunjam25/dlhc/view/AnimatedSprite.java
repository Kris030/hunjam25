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

    public float spriteScale = 1;

    public AnimatedSprite(BufferedImage[] images, float animLength) {
        this.images = images;
        this.animLength = animLength;
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

        int x = 0, y = 0;

        if (centered) {
            x = -(int) (image.getWidth() * 0.5f);
            y = -(int) (image.getHeight() * 0.5f);
        }

        if (mirrored) {
            Sprite.mirrorX(gd);
        }
        gd.scale(spriteScale, spriteScale);

        gd.drawImage(image, x, y, null);
    }
}
