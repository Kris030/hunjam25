package hu.hunjam25.dlhc;

import hu.hunjam25.dlhc.view.IRenderable;

import java.awt.*;

public class GameObject implements IRenderable{

    public GameObject(){
    }


    private Point.Float position = new Point.Float(0f,0f);

    private IRenderable view;


    public void tick(Double dt){

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

    }
}
