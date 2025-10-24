package hu.hunjam25.dlhc.view;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

public class RatView implements IRenderable{
    static private final Path path = Path.of("art\\rat.png");
    static private BufferedImage image;

    public RatView(){
        try {
            image = ImageIO.read(path.toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void render(Graphics2D gd) {
        //gd.scale(0.1,0.1);
        gd.drawImage(image,0,0,null);
    }
}
