package hu.hunjam25.dlhc.view;

import hu.hunjam25.dlhc.AssetManager;
import hu.hunjam25.dlhc.Game;

import java.awt.*;
import java.awt.geom.Point2D;

public class UiElement implements IRenderable{

    public boolean visible = true;

    private Point2D.Float offset = new Point2D.Float(0f,1f);

    public float scale = 1f;

    private Sprite mark = new Sprite(AssetManager.getImage("mark"));

    public boolean animated = false;

    private AnimatedSprite animatedSprite;

    @Override
    public void render(Graphics2D gd) {
        if(!visible){
            return;
        }

        var screenOffset = new Point2D.Float(offset.x * 120f, -offset.y * 120f);
        //var screenOffset = Game.gameToScreen(offset);
        gd.translate(screenOffset.x,screenOffset.y);

        gd.scale(scale,scale);
        if (!animated)
            mark.render(gd);
        else
            animatedSprite.render(gd);
        gd.scale(1/scale,1/scale);
        gd.translate(-screenOffset.x,-screenOffset.y);
    }

    public void setAnimatedSprite(AnimatedSprite animatedSprite) {
        this.animatedSprite = animatedSprite;
        animated = true;
    }
}
