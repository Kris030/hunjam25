package hu.hunjam25.dlhc.sound;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;

public class Sound {

    private SoundBuffer buffer;

    public Sound(SoundBuffer buffer) {
        this.buffer = buffer;
    }

    public Clip play() throws LineUnavailableException {
        Clip clip = (Clip) AudioSystem.getLine(new DataLine.Info(Clip.class, buffer.format()));
        clip.open(buffer.format(), buffer.audioData(), 0, buffer.audioData().length);

        clip.addLineListener(new LineListener() {
            @Override
            public void update(LineEvent event) {
                System.out.println(event.getType());
            }
        });

        clip.start();
        clip.drain();

        return clip;
    }
}
