package hu.hunjam25.dlhc.sound;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.*;
import javax.swing.*;

public record SoundBuffer(AudioFormat format, byte[] audioData) {


    public static SoundBuffer read(InputStream inp) throws IOException, UnsupportedAudioFileException {
        try (AudioInputStream stream = AudioSystem.getAudioInputStream(new ByteArrayInputStream(inp.readAllBytes()))) {
            return new SoundBuffer(stream.getFormat(), stream.readAllBytes());
        }
    }

    public Clip play(Runnable callback) throws LineUnavailableException {
        Clip clip = (Clip) AudioSystem.getLine(new DataLine.Info(Clip.class, this.format()));

        clip.open(this.format(), this.audioData(), 0, this.audioData().length);
        clip.start();

        clip.addLineListener(event -> {
            if (event.getType() == LineEvent.Type.STOP) {
                clip.close();
                if(callback != null){
                    callback.run();
                }
            }
        });

        return clip;
    }
}
