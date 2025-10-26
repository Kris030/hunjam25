package hu.hunjam25.dlhc.screens;

import hu.hunjam25.dlhc.AssetManager;
import hu.hunjam25.dlhc.Game;
import hu.hunjam25.dlhc.Main;
import hu.hunjam25.dlhc.sound.SoundBuffer;
import hu.hunjam25.dlhc.view.AnimatedSprite;
import hu.hunjam25.dlhc.view.Sprite;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import java.awt.*;

public class StartScreen implements IScreen {

    private static AnimatedSprite backg;

    private static Sprite controls;

    private boolean started = false;

    static SoundBuffer menuMusic;
    public static Clip currentClip;

    @Override
    public void init() {
        backg = new AnimatedSprite(AssetManager.getAnim("radio"), 1);
        backg.centered = false;
        backg.setLooping(false);

        menuMusic = AssetManager.getSound("opening");


        controls = new Sprite(AssetManager.getImage("controls"));
    }

    @Override
    public void start() {
        playMenuMusic();
    }

    @Override
    public void render(Graphics2D g) {
        backg.render(g);

        g.translate(Game.SCREEN_WIDTH * Game.TILE_SIZE / 2f, (7+ Game.SCREEN_HEIGHT) * Game.TILE_SIZE / 2f);
        if((int)(Main.now * 2f) % 2 == 0){
            controls.render(g);
        }
        //g.translate(-Game.SCREEN_WIDTH / 2f, -Game.SCREEN_HEIGHT / 2f);
    }

    @Override
    public void tick(float dt) {
        if (backg.hasFinished() && !started) {
            Main.startGame();
            started = true;
        }



        if (!Game.keysPressed.isEmpty() && !started) {
            backg.unFreeze();
            backg.start();
        }
    }

    @Override
    public void stop() {

    }

    private static void playMenuMusic() {
        try {
            currentClip = menuMusic.play(() -> Game.playMusic(Game.backgroundMusic));
        } catch (LineUnavailableException e) {
            System.err.println("No Music");
            throw new RuntimeException(e);
        }
    }
}
