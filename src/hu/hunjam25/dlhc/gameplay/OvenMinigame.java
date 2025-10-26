package hu.hunjam25.dlhc.gameplay;

import hu.hunjam25.dlhc.AssetManager;
import hu.hunjam25.dlhc.Game;
import hu.hunjam25.dlhc.Utils;
import hu.hunjam25.dlhc.model.Chef;
import hu.hunjam25.dlhc.model.Food;
import hu.hunjam25.dlhc.model.Ingredient;
import hu.hunjam25.dlhc.model.Workstation;
import hu.hunjam25.dlhc.sound.SoundBuffer;
import hu.hunjam25.dlhc.view.Sprite;

import java.awt.*;
import java.awt.event.KeyEvent;

public class OvenMinigame extends Minigame {

    private float state = 0.0f;
    private float renderState = 0.0f;
    private boolean left = false;
    private float jumping = 0.0f;
    private float leftBurner = 0.0f;
    private float rightBurner = 0.0f;
    private int onBurner = 0;
    private final float back = 0.9f;
    private final float speed = 2.0f;

    private final Sprite backg = new Sprite(AssetManager.getImage("ovenBackg"));
    private final Sprite leftKnob = new Sprite(AssetManager.getImage("leftKnob"));
    private final Sprite rightKnob = new Sprite(AssetManager.getImage("rightKnob"));
    private final Sprite leftFlame = new Sprite(AssetManager.getImage("leftFlame"));
    private final Sprite rightFlame = new Sprite(AssetManager.getImage("rightFlame"));



    //private final Sprite nyerFinal = new Sprite(AssetManager.getImage("huto_minigame_nyer"));
    //private final Sprite veszitFinal = new Sprite(AssetManager.getImage("huto_minigame_veszit"));

    //private final AnimatedSprite nyer = new AnimatedSprite(AssetManager.getAnim("huto_minigame_nyer"), 0);
    //private final AnimatedSprite veszit = new AnimatedSprite(AssetManager.getAnim("huto_minigame_veszit"), 0);

    public OvenMinigame(Workstation workstation, Chef chef, Ingredient ingredient) {
        super(workstation, chef, ingredient);
        onBurner = Food.r.nextInt(2);
        leftBurner = Food.r.nextFloat() - 1.1f;
        rightBurner = Food.r.nextFloat() - 1.1f;
    }

    protected float getResult() {
        return Math.max(0f, (leftBurner + rightBurner) * 0.66f);
    }

    public void tick(float dt) {
        if (isGameEnded())
            return;

        leftBurner -= dt * back * (0.5f + 0.5f *  Food.r.nextFloat());
        rightBurner -= dt * back * (0.5f + 0.5f *  Food.r.nextFloat());

        jumping -= dt;

        if (jumping < 0.0f) {
            float add = 0.0f;
            if (Game.keysPressed.contains(KeyEvent.VK_RIGHT)) {
                add += speed * dt;
            }
            if (Game.keysPressed.contains(KeyEvent.VK_LEFT)) {
                add -= speed * dt;
            }
            if (onBurner == 0) {
                leftBurner += add;
            } else {
                rightBurner += add;
            }
        }

        if (Game.keysPressed.contains(KeyEvent.VK_SPACE) && jumping <= 0.0f)
        {
            jumping = 0.2f;
            onBurner = Math.abs(onBurner - 1);
        }

        if (leftBurner < -1f) leftBurner = -1f;
        if (leftBurner > 1f) leftBurner = 1f;
        if (rightBurner < -1f) rightBurner = -1f;
        if (rightBurner > 1f) rightBurner = 1f;


        if (leftBurner >= 0.5f && rightBurner >= 0.5f) {
            endGame();
        }
    }



    protected void renderGame(Graphics2D g) {
        backg.spriteScale = worldScale;
        backg.render(g);

        //g.translate(100, 100);
        g.scale(0.0025f, 0.0025f);
        g.translate(-350f, 250f);
        g.rotate(leftBurner);
        leftKnob.render(g);
        g.rotate(-leftBurner);
        g.translate(700f, 0.0f);
        g.rotate(rightBurner);
        //g.translate(-100, -100);
        rightKnob.render(g);
        g.rotate(-rightBurner);
        g.translate(-350f, -250.0f);

        if (leftBurner >= 0.5) {
            float rot = Food.r.nextFloat() / 2f;
            g.translate(-350f, -150);
            g.rotate(rot);
            leftFlame.render(g);
            g.rotate(-rot);
            g.translate(350f, 150);
        }
        if (rightBurner >= 0.5) {
            float rot = Food.r.nextFloat() / 2f;
            g.translate(350f, -150);
            g.rotate(rot);
            rightFlame.render(g);
            g.rotate(-rot);
            g.translate(-350f, 150);
        }
    }

    @Override
    public SoundBuffer getMusic() {
        return AssetManager.getSound(null);
    }
}
