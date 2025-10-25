package hu.hunjam25.dlhc.view;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Sprite implements IRenderable {

    private BufferedImage image;

    public boolean centered = false;

    public boolean mirrored = false;

    static void mirrorX(Graphics2D gd) {
        gd.scale(-1f, 1f);
    }

    static void center(Graphics2D gd, int width, int height) {
        gd.translate(-width * 0.5f, -height * 0.5f);
    }

    public Sprite(BufferedImage image) {
        this.image = image;
    }

    // itt egy frame select valahogy jó lenne
    // (dt vagy tárolja a jelenlegit és inkrementál)
    @Override
    public void render(Graphics2D gd) {
        if (centered) {
            center(gd, image.getWidth(), image.getHeight());
        }

        if (mirrored) {
            mirrorX(gd);
        }

        gd.drawImage(image, 0, 0, null);
    }
}
