package hu.hunjam25.dlhc.gameplay;


import hu.hunjam25.dlhc.AssetManager;
import hu.hunjam25.dlhc.Game;
import hu.hunjam25.dlhc.Vec2;
import hu.hunjam25.dlhc.model.Chef;
import hu.hunjam25.dlhc.model.Food;
import hu.hunjam25.dlhc.model.Ingredient;
import hu.hunjam25.dlhc.model.Workstation;
import hu.hunjam25.dlhc.view.AnimatedSprite;
import hu.hunjam25.dlhc.view.Sprite;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;

public class ChoppingBoardMinigame extends Minigame {

    private Sprite knife = new Sprite(AssetManager.getImage("minigame.chopping.kes"));
    private Sprite tomatoSlice = new Sprite(AssetManager.getImage("minigame.chopping.pari_szelet"));
    private Sprite choppingBoard = new Sprite(AssetManager.getImage("minigame.chopping.vagodeszka"));

    private AnimatedSprite tomato = new AnimatedSprite(AssetManager.getAnim("minigame.chopping.pari"), 0);
    private AnimatedSprite rat = new AnimatedSprite(AssetManager.getAnim("minigame.chopping.remi_karddal"), 0);

    private static final int TOMATO_COUNT = 5;
    private static final Rectangle2D.Float TOMATO_BOUNDS = new Rectangle2D.Float(
            -0.9f, -0.28f, 1.0f, 0.95f
    );

    private boolean previouslyPressed = false;
    private Vec2 tomatos[];

    public ChoppingBoardMinigame(Workstation workstation, Chef chef, Ingredient ingredient) {
        super(workstation, chef, ingredient);

        tomatos = new Vec2[TOMATO_COUNT];
        for (int i = 0; i < TOMATO_COUNT; i++) {
            tomatos[i] = getTomatoPosition();
        }
    }

    protected float getResult() {
        return 0.0f;
    }

    public void tick(float dt) {
        boolean click = false;
        if (Game.keysPressed.contains(KeyEvent.VK_SPACE)) {
            if (!previouslyPressed)
                click = true;
            previouslyPressed = true;
        } else {
            previouslyPressed = false;
        }
    }

    protected void renderGame(Graphics2D g) {
        var tf = g.getTransform();
        choppingBoard.spriteScale = worldScale;
        choppingBoard.render(g);

        tomato.setScale(worldScale);
        for (int i = 0; i < TOMATO_COUNT; i++) {
            g.translate(tomatos[i].x(), tomatos[i].y());
            tomato.render(g);
            g.setTransform(tf);
        }

//        g.setColor(Color.RED);
//        g.fill(TOMATO_BOUNDS);
    }

    private Vec2 getTomatoPosition() {
        return new Vec2(
                Food.r.nextFloat(TOMATO_BOUNDS.x, TOMATO_BOUNDS.x + TOMATO_BOUNDS.width),
                Food.r.nextFloat(TOMATO_BOUNDS.y, TOMATO_BOUNDS.y + TOMATO_BOUNDS.height)
        );
    }
}