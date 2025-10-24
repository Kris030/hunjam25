package hu.hunjam25.dlhc.view;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Renderer {

    static private List<IRenderable> renderables = new ArrayList<>();

    public static void render(Graphics2D g) {
        renderAll(g);
    }

    static public void addRenderable(IRenderable r){
        renderables.add(r);
    }

    static private void renderAll(Graphics2D g){
        renderables.forEach( r -> r.render(g));
    }



    static private void drawImage(Graphics2D g,String path ){
        try {
            var image = ImageIO.read(new File(path));
            g.drawImage(image,0,0,null);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

}

