package hu.hunjam25.dlhc;

import hu.hunjam25.dlhc.sound.SoundBuffer;

import javax.imageio.ImageIO;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import hu.hunjam25.dlhc.sound.Sound;
import hu.hunjam25.dlhc.sound.SoundBuffer;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

public class AssetManager {

    private static HashMap<String, BufferedImage> imageStorage;

    private static HashMap<String, BufferedImage[]> animStorage;

    private static HashMap<String, SoundBuffer> soundStorage;

    private static void img(String name, String pathFirst, String... pathRest) throws IOException {
        imageStorage.put(name, ImageIO.read(Path.of(pathFirst, pathRest).toFile()));
    }

    static void init() throws IOException, UnsupportedAudioFileException {
        // TODO: better
        imageStorage = new HashMap<>();
        animStorage = new HashMap<>();
        soundStorage = new HashMap<>();

        addSounds();

        addProgramerArt();

        addProgrammerRemy();

        // addAllWorkSations();

        addWorkStations();

        addRemiAnim();
    }

    private static void addProgrammerRemy() throws IOException {
        for (int i = 1; i <= 6; ++i) {
            img("remi" + i, "art", "programmerArt", "remi", "animalt_remi" + i + ".png");
        }
    }

    private static void addAllWorkSations() throws IOException {
        img("stove_front", "art", "organized", "stove", "stove_front_on.png");
        img("sink_front_on", "art", "organized", "sink", "sink_front_on.png");
        img("sink_front_off", "art", "organized", "sink", "sink_front_off.png");
        img("counter_front", "art", "organized", "counter", "counter_front.png");
        img("sink_back_off", "art", "organized", "sink", "sink_behind_off.png");
        img("sink_back_on", "art", "organized", "sink", "sink_behind_on.png");
        img("stove_back_off", "art", "organized", "stove", "stove_behind_off.png");
        img("stove_back_on", "art", "organized", "stove", "stove_behind_on.png");
        img("counter_back", "art", "organized", "counter", "counter_behind.png");
        img("fridge_closed", "art", "organized", "fridge", "fridge_closed.png");
        img("fridge_open", "art", "organized", "fridge", "fridge_open.png");
    }

    private static void addWorkStations() throws IOException {
        img("Stove", "art", "organized", "stove", "stove_front_on.png");
        img("Sink", "art", "organized", "sink", "sink_front_off.png");
        img("Counter", "art", "organized", "counter", "counter_front.png");
        img("Fridge", "art", "organized", "fridge", "fridge_closed.png");
    }

    private static void addProgramerArt() throws IOException {
        imageStorage.put("rat", ImageIO.read(Path.of("art", "gonosz_remi.png").toFile()));
        imageStorage.put("tiles", ImageIO.read(Path.of("art", "programmerArt", "tiles.png").toFile()));
        imageStorage.put("dot", ImageIO.read(Path.of("art", "programmerArt", "dot.png").toFile()));
        imageStorage.put("mark", ImageIO.read(Path.of("art", "programmerArt", "mark.png").toFile()));
        imageStorage.put("chef", ImageIO.read(Path.of("art", "programmerArt", "chef.png").toFile()));
    }

    private static void addRemiAnim() throws IOException {
        BufferedImage[] remiAnim = new BufferedImage[69];
        for (int i = 0; i < 69; ++i) {
            String num =  Integer.toString(i);
            if (num.length() == 1) {
                num = "0" + num;
            }
            remiAnim[i] = ImageIO.read(Path.of("art", "organized", "remi", "gonosz_remi2_000" + num + ".png").toFile());
        }
        animStorage.put("remi", remiAnim);
    }

    private static void addSounds() throws IOException, UnsupportedAudioFileException {
        soundStorage.put("pipe", SoundBuffer.read(Path.of("art", "metal-pipe.wav")));

        Sound s = new Sound(soundStorage.get("pipe"));

        try {
            var c = s.play();
            c.drain();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage getImage(String name) {
        return imageStorage.get(name);
    }

    public static BufferedImage[] getAnim(String name) {
        return animStorage.get(name);
    }
}
