package hu.hunjam25.dlhc.view;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class Sprite implements IRenderable{
    private ArrayList<Path> path;
    private ArrayList<BufferedImage> image;

    public Sprite(ArrayList<Path> p){
        path = p;
        for (Path pa : path) {
            try {
                image.add(ImageIO.read(pa.toFile()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void render(Graphics2D gd) { //itt egy frame select valahogy jó lenne (dt vagy tárolja a jelenlegit és inkrementál)
        gd.scale(0.1,0.1);
        gd.drawImage(image.getFirst(),30,0,null);
    }
}
