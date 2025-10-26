package hu.hunjam25.dlhc.gameplay;

import hu.hunjam25.dlhc.*;
import hu.hunjam25.dlhc.model.Chef;
import hu.hunjam25.dlhc.model.Food;
import hu.hunjam25.dlhc.model.Ingredient;
import hu.hunjam25.dlhc.model.Workstation;
import hu.hunjam25.dlhc.sound.SoundBuffer;
import hu.hunjam25.dlhc.view.AnimatedSprite;
import hu.hunjam25.dlhc.view.Sprite;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.stream.Stream;

public class ChoppingBoardMinigame extends Minigame {

    private Sprite knife = new Sprite(AssetManager.getImage("minigame.chopping.kes"));
    private Sprite choppingBoard = new Sprite(AssetManager.getImage("minigame.chopping.vagodeszka"));

    private AnimatedSprite[] tomatos;
    private AnimatedSprite rat = new AnimatedSprite(AssetManager.getAnim("minigame.chopping.remi_karddal"), 1.0f);

    private SoundBuffer failSound = AssetManager.getSound("fail_sound");
    private SoundBuffer swordSwingSound = AssetManager.getSound("kard_csapas");
    private SoundBuffer cutSound = AssetManager.getSound("vagas_sound");
    private SoundBuffer winSound = AssetManager.getSound("win_sound");

    private static final int TOMATO_COUNT = 4;
    private static final Rectangle2D.Float TOMATO_BOUNDS = new Rectangle2D.Float(
            -0.9f, -0.17f, 1.0f, 0.8f);

    private static final Vec2 KNIFE_POSITION = new Vec2(1.0f, -0.1f);
    private static final Vec2 KNIFE_ROT_OFFSET = new Vec2(0.2f, 0.2f);

    private static final Vec2 RAT_POSITION = new Vec2(0.16f, 0.10f);

    private static final float ANIM_LENGTH = 0.5f;
    private float animStart = -1.0f;

    private static final float CUT_INTERVAL = 2.0f * (60.0f / 110.0f);
    private static final float MISTAKE_DELTA = 0.5f;
    private int cutCount = 0;
    private float error = 0.0f;

    private boolean previouslyPressed = false;
    private final Vec2 tomatoPositions[];

    public ChoppingBoardMinigame(Workstation workstation, Chef chef, Ingredient ingredient) {
        super(workstation, chef, ingredient);

        tomatoPositions = new Vec2[TOMATO_COUNT];

        tomatos = new AnimatedSprite[TOMATO_COUNT];
        for (int i = 0; i < TOMATO_COUNT; i++) {
            tomatos[i] = new AnimatedSprite(AssetManager.getAnim("minigame.chopping.pari"), 0.4f);
            tomatos[i].setLooping(false);
        }

        float s = worldScale * tomatos[0].getCurrFrameSize().maxComp();
        for (int i = 0; i < TOMATO_COUNT; i++) {
            int k = i;
            do {
                tomatoPositions[i] = new Vec2(
                        Food.r.nextFloat(TOMATO_BOUNDS.x, TOMATO_BOUNDS.x + TOMATO_BOUNDS.width),
                        Food.r.nextFloat(TOMATO_BOUNDS.y, TOMATO_BOUNDS.y + TOMATO_BOUNDS.height));

            } while (k != 0 && Stream.of(tomatoPositions)
                    .limit(k - 1)
                    .anyMatch(o -> o.dist(tomatoPositions[k]) <= s));
        }
    }

    protected float getResult() {
        if (cutCount == 0) {
            return 0.0f;
        } else {
            return 1.0f - error / cutCount;
        }
    }

    private boolean firstTick = true;

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
            rat.unFreeze();
            rat.start();

            float diff = getGameTime() % CUT_INTERVAL;
            diff = Math.min(diff, CUT_INTERVAL - diff);
            if (diff > MISTAKE_DELTA)
                error += 1.0f;
        }

        if (Math.floor(getGameTime() / CUT_INTERVAL) > cutCount) {
            startCut();
            tomatos[cutCount].unFreeze();
            tomatos[cutCount].start();
            cutCount++;

            if (cutCount == TOMATO_COUNT) {
                endGame();
            }
        }
    }

    protected void renderGame(Graphics2D g) {
        var tf = g.getTransform();
        choppingBoard.spriteScale = worldScale;
        choppingBoard.render(g);

        for (int i = 0; i < TOMATO_COUNT; i++) {
            var tomato = tomatos[i];
            g.translate(tomatoPositions[i].x(), tomatoPositions[i].y());
            tomato.setScale(worldScale);
            tomato.render(g);
            g.setTransform(tf);
        }

        knife.spriteScale = worldScale;
        g.translate(KNIFE_POSITION.x(), KNIFE_POSITION.y());
        if (animStart > 0.0f) {
            float t = (Main.now - animStart) / ANIM_LENGTH;
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
        g.fill(new Ellipse2D.Float(0.0f, 0.0f, 0.05f, 0.05f));

        g.setTransform(tf);
        g.translate(RAT_POSITION.x(), RAT_POSITION.y());
        rat.setScale(worldScale);
        rat.render(g);
        //g.setColor(Color.RED);
        //g.fill(TOMATO_BOUNDS);
    }

    private void startCut() {
        animStart = Main.now;
    }

    @Override
    public SoundBuffer getMusic() {
        return AssetManager.getSound("chopping_music");
    }
}
