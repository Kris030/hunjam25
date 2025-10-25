package hu.hunjam25.dlhc.view;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import hu.hunjam25.dlhc.Game;

public class AnimatedSprite implements IRenderable {

    private BufferedImage[] images;

    private float[] spriteScales;
    private float[] spriteOffsets;

    public boolean centered = true;

    public boolean mirrored = false;

    public boolean frozen = true;

    private int idx = 0;

    private float frozenAge;

    public float animLength;

    public float animStarted;

    public float spriteScale = 1;

    public AnimatedSprite(BufferedImage[] images, float animLength) {
        this.images = images;
        this.animLength = animLength;
        spriteScales = new float[images.length];
        Arrays.fill(spriteScales, 1f);
        spriteOffsets = new float[images.length];
        Arrays.fill(spriteOffsets, 0f);
    }

    float getAge() {
        return Game.now - animStarted;
    }

    public void freeze() {
        frozen = true;
        frozenAge = getAge();
    }

    public void setIdx(int idx) {
        this.idx = idx;
        idx %= images.length;
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
        float age = frozen ? frozenAge : getAge() % animLength;
        if (!frozen) {
            idx = (int) (images.length * age / animLength);
        }

        idx %= images.length;
        BufferedImage image = images[idx];

        int x = 0, y = 0;

        if (centered) {
            x = -(int) (image.getWidth() * 0.5f);
            y = -(int) (image.getHeight() * 0.5f);
        }

        if (mirrored) {
            Sprite.mirrorX(gd);
        }
        var tf = gd.getTransform();
        gd.scale(spriteScales[idx], spriteScales[idx]);

        gd.drawImage(image, x, y  + (int) (spriteOffsets[idx] / spriteScales[idx]), null);

        //gd.drawImage(image, x, y, null);

        gd.setTransform(tf);
    }

    public void scaleWidth() {
        for (int i = 0; i < images.length; i++) {
            spriteScales[i] = 1f * 120f / images[i].getWidth();
            var ratio = images[i].getWidth() / (float) images[i].getHeight();
            spriteOffsets[i] = 0f; //-60f;
            if (ratio < 1)
                spriteOffsets[i] += 1f * 120f * (1f - (1f/ratio))/2f;
            else
                spriteOffsets[i] += 1f * 120f * (1f - (1f/ratio))/2f;
        }
    }

    public void setScale(float scale) {
        spriteScale = scale;
        for (int i = 0; i < spriteScales.length; i++) {
            spriteScales[i] = scale;
        }
    }
}
