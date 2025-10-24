package hu.hunjam25.dlhc.view;

import hu.hunjam25.dlhc.Game;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class RatView implements IRenderable {

    static private BufferedImage image;

    public RatView() {
        image = Game.getImage("rat");
    }

    @Override
    public void render(Graphics2D gd) {
        gd.drawImage(image, 0, 0, null);
    }
}
