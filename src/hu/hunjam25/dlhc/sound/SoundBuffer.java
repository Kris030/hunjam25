package hu.hunjam25.dlhc.sound;

import java.io.IOException;
import java.nio.file.Path;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public record SoundBuffer(AudioFormat format, byte[] audioData) {

    public static SoundBuffer read(Path path) throws IOException, UnsupportedAudioFileException {
        try (AudioInputStream stream = AudioSystem.getAudioInputStream(path.toFile())) {
            return new SoundBuffer(stream.getFormat(), stream.readAllBytes());
        }
    }
}
