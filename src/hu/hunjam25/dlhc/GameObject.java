package hu.hunjam25.dlhc;

import hu.hunjam25.dlhc.view.IRenderable;
import hu.hunjam25.dlhc.view.UiElement;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class GameObject implements IRenderable {

    protected ArrayList<UiElement> uiElements = new ArrayList<>();

    protected Vec2 position = new Vec2(0f, 0f);

    public void tick(float dt) {

    }

    public void positionToCenter() {
        position = Game.CENTER;
    }

    public void setPosition(Vec2 position) {
        this.position = position;
    }

    public Vec2 getPosition() {
        return position;
    }

    @Override
    public void render(Graphics2D gd) {
        var screen = Game.gameToScreen(position);
        gd.translate(screen.x(), screen.y());
    }

    public void addUiElement(UiElement element) {
        uiElements.add(element);
    }

    public void renderUiElements(Graphics2D gd) {
        uiElements.forEach(ui -> ui.render(gd));
    }

    public IRenderable getUiElement(int index) {
        return uiElements.get(index);
    }

    public void clearUiElements() {
        uiElements.clear();
    }
}
