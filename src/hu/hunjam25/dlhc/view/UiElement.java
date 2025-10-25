package hu.hunjam25.dlhc.view;

import hu.hunjam25.dlhc.AssetManager;
import hu.hunjam25.dlhc.Vec2;

import java.awt.*;

public class UiElement implements IRenderable {

    public boolean visible = true;

    private Vec2 offset = new Vec2(0f, 1f);

    public float scale = 1f;

    private Sprite mark = new Sprite(AssetManager.getImage("mark"));

    public boolean animated = false;

    public AnimatedSprite animatedSprite;

    @Override
    public void render(Graphics2D gd) {
        if (!visible) {
            return;
        }

        var screenOffset = new Vec2(offset.x() * 120f, -offset.y() * 120f);
        // var screenOffset = Game.gameToScreen(offset);

        var tf = gd.getTransform();

        gd.translate(screenOffset.x(), screenOffset.y());
        gd.scale(scale, scale);

        if (!animated)
            mark.render(gd);
        else
            animatedSprite.render(gd);

        gd.setTransform(tf);
    }

    public void setAnimatedSprite(AnimatedSprite animatedSprite) {
        this.animatedSprite = animatedSprite;
        animated = true;
    }

    public void addOffset(Vec2 offs) {
        offset = offset.add(offs);
    }
}
