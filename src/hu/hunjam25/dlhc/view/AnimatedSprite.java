package hu.hunjam25.dlhc.view;

import hu.hunjam25.dlhc.Main;
import hu.hunjam25.dlhc.Vec2;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class AnimatedSprite implements IRenderable {

    private BufferedImage[] images;

    private float[] spriteScales;
    private float[] spriteOffsets;

    public boolean centered = true;

    public boolean mirrored = false;

    private boolean frozen = true;

    private int idx = 0;

    private float frozenAge;

    public float animLength;

    public float animStarted;

    private boolean looping = true;

    public AnimatedSprite(BufferedImage[] images, float animLength) {
        this.images = images;
        this.animLength = animLength;
        spriteScales = new float[images.length];
        Arrays.fill(spriteScales, 1f);
        spriteOffsets = new float[images.length];
        Arrays.fill(spriteOffsets, 0f);
    }

    float getAge() {
        return Main.now - animStarted;
    }

    public void freeze() {
        frozen = true;
        frozenAge = getAge();
    }

    public void setIdx(int idx) {
        idx %= images.length;
        this.idx = idx;
    }

    public void unFreeze() {
        frozen = false;
    }

    public void start() {
        animStarted = Main.now;
    }

    // itt egy frame select valahogy jó lenne
    // (dt vagy tárolja a jelenlegit és inkrementál)
    @Override
    public void render(Graphics2D gd) {
        float age = frozen ? frozenAge : getAge() % animLength;
        if (!frozen) {
            idx = (int) (images.length * age / animLength);
        }

        if (idx >= images.length - 1 && !looping) {
            frozen = true;
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

        gd.drawImage(image, x, y + (int) (spriteOffsets[idx] / spriteScales[idx]), null);

        // gd.drawImage(image, x, y, null);

        gd.setTransform(tf);
    }

    public void scaleWidth() {
        for (int i = 0; i < images.length; i++) {
            spriteScales[i] = 1f * 120f / images[i].getWidth();
            var ratio = images[i].getWidth() / (float) images[i].getHeight();
            spriteOffsets[i] = 0f; // -60f;
            spriteOffsets[i] += 1f * 120f * (1f - (1f / ratio)) / 2f;
        }
    }

    public void setScale(float scale) {
        for (int i = 0; i < spriteScales.length; i++) {
            spriteScales[i] = scale;
        }
    }

    public int getNumberOfFrames() {
        return images.length;
    }

    public void setLooping(boolean b) {
        looping = b;
    }

    public boolean hasFinished() {
        return !looping && idx == images.length - 1;
    }

    public Vec2 getCurrFrameSize() {
        return new Vec2(images[idx].getWidth(), images[idx].getHeight());
    }
}
