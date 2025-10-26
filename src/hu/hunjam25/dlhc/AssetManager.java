package hu.hunjam25.dlhc;

import hu.hunjam25.dlhc.model.Workstation;
import hu.hunjam25.dlhc.sound.SoundBuffer;

import javax.imageio.ImageIO;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

public class AssetManager {

    private static HashMap<String, BufferedImage> imageStorage;

    private static HashMap<String, BufferedImage[]> animStorage;

    private static HashMap<String, SoundBuffer> soundStorage;

    static void init() throws IOException, UnsupportedAudioFileException {
        // TODO: better
        imageStorage = new HashMap<>();
        animStorage = new HashMap<>();
        soundStorage = new HashMap<>();

        img("no_texture", "art", "programmerArt", "no.png");

        addSounds();

        addProgramerArt();

        addProgrammerRemy();

        addWorkStationAnimations();

        addWorkStations();

        addRemiAnim();

        addClockAnimation();

        addRatMeter();

        addChefAnimations("chef1");
        addChefAnimations("chef2");
        addChefAnimations("chef3");

        img("minigame_keret", "art", "minigame_keret.png");
    }

    private static void addRatMeter() throws IOException {
        BufferedImage[] ratMeter = new BufferedImage[5];

        for (int i = 1 ; i <= 5; ++i){
            ratMeter[i - 1] = ImageIO.read(Path.of("art","patkany","patkany_0" + i + ".png").toFile());
        }
        animStorage.put("ratMeter", ratMeter);
    }

    private static void img(String name, String pathFirst, String... pathRest) throws IOException {
        imageStorage.put(name, ImageIO.read(Path.of(pathFirst, pathRest).toFile()));
    }


    private static void addProgrammerRemy() throws IOException {
        for (int i = 1; i <= 6; ++i) {
            img("remi" + i, "art", "programmerArt", "remi", "animalt_remi" + i + ".png");
        }
    }

    private static void addChefAnimations(String name) throws IOException {
        BufferedImage[] chef = new BufferedImage[]{
                ImageIO.read(Path.of( "art", "organized", "sefek", name, "fel.png").toFile() ),
                ImageIO.read(Path.of( "art", "organized", "sefek", name, "oldal.png").toFile() ),
                ImageIO.read(Path.of( "art", "organized", "sefek", name, "le.png").toFile() ),
                ImageIO.read(Path.of( "art", "organized", "sefek", name, "fel_shocked.png").toFile() ),
                ImageIO.read(Path.of( "art", "organized", "sefek", name, "oldal_shocked.png").toFile() ),
                ImageIO.read(Path.of( "art", "organized", "sefek", name, "le_shocked.png").toFile() ),
        };
        animStorage.put(name, chef);
    }

    private static void addWorkStationAnimations() throws IOException {
        BufferedImage[] stove = new BufferedImage[]{
               ImageIO.read(Path.of( "art", "organized", "stove", "stove_front_off.png").toFile() ),
                ImageIO.read(Path.of( "art", "organized", "stove", "stove_front_on.png").toFile() ),
        };
        animStorage.put(Workstation.WorkstationType.Stove.name(), stove);
        animStorage.put(Workstation.WorkstationType.Oven.name(), stove);

        BufferedImage[] fridge = new BufferedImage[]{
                ImageIO.read( Path.of( "art", "organized", "fridge", "fridge_closed.png").toFile()),
                ImageIO.read( Path.of( "art", "organized", "fridge", "fridge_open.png").toFile() )
        };
        animStorage.put(Workstation.WorkstationType.Fridge.name(), fridge);

        BufferedImage[] sink = new BufferedImage[]{
                ImageIO.read(Path.of("art", "organized", "sink", "sink_front_off.png").toFile()),
                ImageIO.read(Path.of("art", "organized", "sink", "sink_front_on.png").toFile()),
        };

        animStorage.put("Sink", sink);

        BufferedImage[] choppingBoard = new BufferedImage[]{
                ImageIO.read(Path.of("art", "organized", "counter", "counter_front.png").toFile())
        };

        animStorage.put(Workstation.WorkstationType.ChoppingBoard.name(), choppingBoard);

        BufferedImage[] fire = new BufferedImage[]{
                ImageIO.read(Path.of("art", "organized", "ego_kuka", "tuz_1.png").toFile()),
                ImageIO.read(Path.of("art", "organized", "ego_kuka", "tuz_2.png").toFile())
        };

        animStorage.put("fire", fire);

        BufferedImage[] smoke = new BufferedImage[]{
                ImageIO.read(Path.of("art", "organized", "ego_kuka", "fust_1.png").toFile()),
                ImageIO.read(Path.of("art", "organized", "ego_kuka", "fust_2.png").toFile())
        };

        animStorage.put("smoke", smoke);

        BufferedImage[] bin = new BufferedImage[]{
                ImageIO.read(Path.of("art", "organized", "ego_kuka", "kuka.png").toFile())
        };

        animStorage.put(Workstation.WorkstationType.Trash.name(), bin);
    }

    private static void addClockAnimation() throws IOException {
        BufferedImage[] clock = new BufferedImage[5];

        for (int i = 1 ; i <= 5; ++i){
            clock[i - 1] = ImageIO.read(Path.of("art","ora","ora_0" + i + ".png").toFile());
        }

        animStorage.put("clock", clock);
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
        imageStorage.put("new_tiles", ImageIO.read(Path.of("art", "programmerArt", "new_tiles.png").toFile()));
        imageStorage.put("new_tiles2", ImageIO.read(Path.of("art", "programmerArt", "new_tiles2.png").toFile()));
        imageStorage.put("wallpaper", ImageIO.read(Path.of("art", "organized","wallpaper_smudge.jpeg").toFile()));
        imageStorage.put("wood", ImageIO.read(Path.of("art", "organized","wood.png").toFile()));

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
        soundStorage.put("ready", SoundBuffer.read(Path.of("art", "sounds", "ready_ding.wav")));
    }

    public static BufferedImage getImage(String name) {
        return imageStorage.getOrDefault(name, imageStorage.get("no_texture"));
    }

    public static BufferedImage[] getAnim(String name) {
        return animStorage.getOrDefault(name, new BufferedImage[]{
                imageStorage.get("no_texture"),
                imageStorage.get("no_texture"),
                imageStorage.get("no_texture")
        });
    }

    public static SoundBuffer getSound(String name){
        return soundStorage.getOrDefault(name,soundStorage.get("pipe") );
    }
}
