package hu.hunjam25.dlhc.view;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Sprite implements IRenderable {

    private BufferedImage image;

    public Sprite(BufferedImage image) {
        this.image = image;
    }

    // itt egy frame select valahogy jó lenne
    // (dt vagy tárolja a jelenlegit és inkrementál)
    @Override
    public void render(Graphics2D gd) {
        gd.drawImage(image, 0, 0, null);
    }
}
