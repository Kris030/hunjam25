package hu.hunjam25.dlhc.screens;

import hu.hunjam25.dlhc.GameObject;
import hu.hunjam25.dlhc.Kitchen;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Comparator;

public interface IScreen {

    void init();

    void render(Graphics2D g);

    void tick(float dt);
}
