package hu.hunjam25.dlhc.sound;

import java.io.IOException;
import java.nio.file.Path;

import javax.sound.sampled.*;

public record SoundBuffer(AudioFormat format, byte[] audioData) {

    public static SoundBuffer read(Path path) throws IOException, UnsupportedAudioFileException {
        try (AudioInputStream stream = AudioSystem.getAudioInputStream(path.toFile())) {
            return new SoundBuffer(stream.getFormat(), stream.readAllBytes());
        }
    }

    public Clip play() throws LineUnavailableException {
        Clip clip = (Clip) AudioSystem.getLine(new DataLine.Info(Clip.class, this.format()));

        clip.open(this.format(), this.audioData(), 0, this.audioData().length);
        clip.start();

        clip.addLineListener(event -> {
            if (event.getType() == LineEvent.Type.STOP) {
                clip.close();
            }
        });

        return clip;
    }
}
