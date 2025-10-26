package hu.hunjam25.dlhc.screens;

import java.awt.*;

public interface IScreen {

    void init();

    void start();

    void render(Graphics2D g);

    void tick(float dt);

    void stop();
}
