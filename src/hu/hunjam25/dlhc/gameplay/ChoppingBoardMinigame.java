package hu.hunjam25.dlhc.gameplay;


import hu.hunjam25.dlhc.*;
import hu.hunjam25.dlhc.model.Chef;
import hu.hunjam25.dlhc.model.Food;
import hu.hunjam25.dlhc.model.Ingredient;
import hu.hunjam25.dlhc.model.Workstation;
import hu.hunjam25.dlhc.view.AnimatedSprite;
import hu.hunjam25.dlhc.view.Sprite;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class ChoppingBoardMinigame extends Minigame {

    private Sprite knife = new Sprite(AssetManager.getImage("minigame.chopping.kes"));
    private Sprite tomatoSlice = new Sprite(AssetManager.getImage("minigame.chopping.pari_szelet"));
    private Sprite choppingBoard = new Sprite(AssetManager.getImage("minigame.chopping.vagodeszka"));

    private AnimatedSprite tomato = new AnimatedSprite(AssetManager.getAnim("minigame.chopping.pari"), 0);
    private AnimatedSprite rat = new AnimatedSprite(AssetManager.getAnim("minigame.chopping.remi_karddal"), 0);

    private static final int TOMATO_COUNT = 5;
    private static final Rectangle2D.Float TOMATO_BOUNDS = new Rectangle2D.Float(
            -0.9f, -0.17f, 1.0f, 0.8f
    );

    private static final Vec2 KNIFE_POSITION = new Vec2(0.8f, -0.1f);
    private static final Vec2 KNIFE_ROT_OFFSET = new Vec2(0.2f, 0.2f);

    private static final float ANIM_TIME = 0.5f;
    private float animStart = -1.0f;

    private boolean previouslyPressed = false;
    private final Vec2 tomatos[];

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

        if (click) {
            startCut();
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

        knife.spriteScale = worldScale;
        g.translate(KNIFE_POSITION.x(), KNIFE_POSITION.y());
        if (animStart > 0.0f) {
            float t = (Main.now - animStart) / ANIM_TIME;
            if (t > 1.0f) {
                animStart = -1.0f;
            } else {
                float phi = Utils.interpolateExp(t, 20.0f, 0.0f, (float) -Math.PI * 0.5f);
                g.translate(KNIFE_ROT_OFFSET.x(), KNIFE_ROT_OFFSET.y());
                g.rotate(phi);
                g.translate(-KNIFE_ROT_OFFSET.x(), -KNIFE_ROT_OFFSET.y());
            }
        }
        knife.render(g);

        g.translate(KNIFE_ROT_OFFSET.x(), KNIFE_ROT_OFFSET.y());
        g.fill(new Ellipse2D.Float(
                0.0f, 0.0f, 0.05f, 0.05f
        ));

        g.setTransform(tf);
        g.setColor(Color.RED);
        g.fill(TOMATO_BOUNDS);
    }

    private Vec2 getTomatoPosition() {
        return new Vec2(
                Food.r.nextFloat(TOMATO_BOUNDS.x, TOMATO_BOUNDS.x + TOMATO_BOUNDS.width),
                Food.r.nextFloat(TOMATO_BOUNDS.y, TOMATO_BOUNDS.y + TOMATO_BOUNDS.height)
        );
    }

    private void startCut() {
        animStart = Main.now;
    }
}