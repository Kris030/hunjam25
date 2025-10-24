package hu.hunjam25.dlhc;

import hu.hunjam25.dlhc.view.IRenderable;
import hu.hunjam25.dlhc.view.RatView;

import java.awt.*;

public class GameObject implements IRenderable {

    public GameObject() {
    }

    protected Point.Float position = new Point.Float(0f, 0f);

    protected IRenderable view;

    public void tick(float dt) {

    }

    public void setPosition(Point.Float position) {
        this.position = position;
    }

    public IRenderable getView() {
        return view;
    }

    public Point.Float getPosition() {
        return (Point.Float) position.clone();
    }

    @Override
    public void render(Graphics2D gd) {
        if (view != null) {
            view.render(gd);
        }
    }
}
