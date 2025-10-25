package hu.hunjam25.dlhc.view;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Sprite implements IRenderable {

    private BufferedImage image;

    public boolean centered = false;

    public boolean mirrored = false;

    void mirror(Graphics2D gd){
        var width = image.getWidth();
        gd.translate(width/2f, 0);
        gd.scale(-1f,1f);
        gd.translate(-width/2f,0);
    }

    public Sprite(BufferedImage image) {
        this.image = image;
    }

    // itt egy frame select valahogy jó lenne
    // (dt vagy tárolja a jelenlegit és inkrementál)
    @Override
    public void render(Graphics2D gd) {
        int x = 0;
        int y = 0;
        if(centered){
            x = (int) -(image.getWidth()/2f);
            y = (int) -(image.getHeight()/2f);
        }
        if(mirrored){
            mirror(gd);
        }

        gd.drawImage(image, x,y, null);
    }
}
