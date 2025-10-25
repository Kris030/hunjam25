package hu.hunjam25.dlhc.view;

import hu.hunjam25.dlhc.Game;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public abstract class Minigame implements IRenderable {

    private static final Point2D offset = new Point2D.Float(
            (float)(Game.SCREEN_SIZE.getX() - Game.MINIGAME_SIZE.getX()) / 2.0f,
            (float)(Game.SCREEN_SIZE.getY() - Game.MINIGAME_SIZE.getY()) / 2.0f
    );

    private final float animTime = 0.3f;
    private float animStart = -1.0f;

    public void start() {
        animStart = Game.now;
    }

    public void stop() {
        animStart = -1.0f;
    }

    protected abstract void renderGame(Graphics2D g);

    @Override
    public void render(Graphics2D gd) {
        if (animStart > 0.0f) {
            float t = (Game.now - animStart) / animTime;
            Point2D currentSize;
            if (t <= 1.0f) {
                currentSize = new Point2D.Float(
                        (float)Game.MINIGAME_SIZE.getX() * t,
                        (float)Game.MINIGAME_SIZE.getY() * t
                );
            }
            else {
                currentSize = Game.MINIGAME_SIZE;
            }
            Point2D currentOffset = new Point2D.Float(
                    (float)(Game.MINIGAME_SIZE.getX() - currentSize.getX()) / 2.0f,
                    (float)(Game.MINIGAME_SIZE.getY() - currentSize.getY()) / 2.0f
            );

            gd.setTransform(
                    AffineTransform.getTranslateInstance(offset.getX(), offset.getY()
            ));
            gd.clipRect((int)currentOffset.getX(), (int)currentOffset.getY(), (int)currentSize.getX(), (int)currentSize.getY());
            renderGame(gd);
        }
    }
}
