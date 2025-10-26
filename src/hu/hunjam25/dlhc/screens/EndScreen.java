package hu.hunjam25.dlhc.screens;

import hu.hunjam25.dlhc.AssetManager;
import hu.hunjam25.dlhc.sound.SoundBuffer;
import hu.hunjam25.dlhc.view.AnimatedSprite;
import hu.hunjam25.dlhc.view.Sprite;

import javax.sound.sampled.LineUnavailableException;
import java.awt.*;

public class EndScreen implements IScreen {

    private static Sprite win;
    private static Sprite lose;

    private static SoundBuffer winSound;
    private static SoundBuffer loseSound;

    boolean didWin = false;

    @Override
    public void init() {
        win = new Sprite( AssetManager.getImage("win"));
        win.spriteScale = 1.2f;
        lose = new Sprite( AssetManager.getImage("lose"));
        lose.spriteScale = 1.2f;
        win.centered = false;
        lose.centered = false;

        winSound = AssetManager.getSound("winSound");
        loseSound = AssetManager.getSound("loseSound");
    }

    @Override
    public void start() {
        try {
            if(didWin){
                winSound.play(null);
            }else{
                loseSound.play(null);
            }
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void render(Graphics2D g) {
        if(didWin){
            win.render(g);
        }else{
            lose.render(g);
        }
    }

    @Override
    public void tick(float dt) {

    }

    @Override
    public void stop() {

    }

    public void setWon(boolean won) {
        this.didWin = won;
    }
}
