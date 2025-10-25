package hu.hunjam25.dlhc;

import hu.hunjam25.dlhc.view.IRenderable;
import hu.hunjam25.dlhc.view.UiElement;

import java.awt.Point;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class GameObject implements IRenderable {

    private ArrayList<IRenderable> uiElements = new ArrayList<>();

    protected Point.Float position = new Point.Float(0f, 0f);

    public void tick(float dt) {

    }

    public void positionToCenter() {
        position = (Point.Float) Game.CENTER.clone();
    }

    public void setPosition(Point.Float position) {
        this.position = position;
    }

    public Point.Float getPosition() {
        return (Point.Float) position.clone();
    }

    @Override
    public void render(Graphics2D gd) {
        var pos = new Point2D.Float(position.x, position.y);
        var screen = Game.gameToScreen(pos);
        gd.translate(screen.x, screen.y);
    }

    public void addUiElement(UiElement element) {
        uiElements.add(element);
    }

    public void renderUiElements(Graphics2D gd){
        uiElements.forEach(ui -> ui.render(gd));
    }
}
