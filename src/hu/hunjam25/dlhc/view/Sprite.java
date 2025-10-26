package hu.hunjam25.dlhc.view;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Sprite implements IRenderable {

    private BufferedImage image;

    public boolean centered = true;

    public boolean mirrored = false;

    public float spriteScale = 1;

    static void mirrorX(Graphics2D gd) {
        gd.scale(-1, 1);
    }

    // static void center(Graphics2D gd, int width, int height) {
    // gd.translate(-width * 0.5, -height * 0.5);
    // }

    public Sprite(BufferedImage image) {
        this.image = image;
    }

    // itt egy frame select valahogy jó lenne
    // (dt vagy tárolja a jelenlegit és inkrementál)
    @Override
    public void render(Graphics2D gd) {
        int x = 0, y = 0;
        if (centered) {
            x = -(int) (image.getWidth() * 0.5f);
            y = -(int) (image.getHeight() * 0.5f);
        }

        var tf = gd.getTransform();
        if (mirrored) {
            mirrorX(gd);
        }

        gd.scale(spriteScale, spriteScale);
        gd.drawImage(image, x, y, null);
        gd.setTransform(tf);
    }

    public void scaleToTileMax() {
        var width = image.getWidth();
        var height = image.getHeight();
        spriteScale = width > height ? 120f / width : 120f / height;
    }

    public void scaleToTileMin() {
        var width = image.getWidth();
        var height = image.getHeight();
        spriteScale = width < height ? 120f / width : 120f / height;
    }

    public void scaleWidth() {
        spriteScale = 120f / image.getWidth();
    }

    public float getRatio() {
        return image.getWidth() / (float) image.getHeight();
    }
}
