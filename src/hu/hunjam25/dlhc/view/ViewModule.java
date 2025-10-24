package hu.hunjam25.dlhc.view;

import java.awt.*;
import java.util.List;

public class ViewModule {

    private List<IRenderable> renderables;

    public void addRenderable(IRenderable r){
        renderables.add(r);
    }

    public void renderAll(Graphics2D g){
        renderables.forEach( r -> r.render(g));
    }

}
