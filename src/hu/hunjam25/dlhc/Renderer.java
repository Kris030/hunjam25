package hu.hunjam25.dlhc;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

class Renderer {

    static void render(Graphics2D g) {
        drawImage(g);
    }

    static private void drawImage(Graphics2D g){
        try {
            var image = ImageIO.read(new File("C:\\projects\\Javaprog\\jam\\art\\fungorium.png"));
            g.drawImage(image,0,0,null);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

}

