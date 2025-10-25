package hu.hunjam25.dlhc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

public class AssetManager {

    private static HashMap<String, BufferedImage> imageStorage;

    private static HashMap<String, BufferedImage[]> animStorage;

    static void init() throws IOException {
        // TODO: better
        imageStorage = new HashMap<>();
        animStorage = new HashMap<>();
        addProgramerArt();

        addProgrammerRemy();

        //addAllWorkSations();

        addWorkStations();

        addRemiAnim();
    }

    private static void addProgrammerRemy() throws IOException {
        for (int i = 1; i <= 6; ++i){
            imageStorage.put("remi" + i,ImageIO.read(Path.of("art","programmerArt","remi", "animalt_remi" + i + ".png").toFile()));
        }
    }

    private static void addAllWorkSations() throws IOException {
        imageStorage.put("stove_front", ImageIO.read(Path.of("art","organized", "stove", "stove_front_on.png").toFile()));
        imageStorage.put("sink_front_on", ImageIO.read(Path.of("art","organized", "sink", "sink_front_on.png").toFile()));
        imageStorage.put("sink_front_off", ImageIO.read(Path.of("art","organized", "sink", "sink_front_off.png").toFile()));
        imageStorage.put("counter_front", ImageIO.read(Path.of("art","organized", "counter", "counter_front.png").toFile()));
        imageStorage.put("sink_back_off", ImageIO.read(Path.of("art","organized", "sink", "sink_behind_off.png").toFile()));
        imageStorage.put("sink_back_on", ImageIO.read(Path.of("art","organized", "sink", "sink_behind_on.png").toFile()));
        imageStorage.put("stove_back_off", ImageIO.read(Path.of("art","organized", "stove", "stove_behind_off.png").toFile()));
        imageStorage.put("stove_back_on", ImageIO.read(Path.of("art","organized", "stove", "stove_behind_on.png").toFile()));
        imageStorage.put("counter_back", ImageIO.read(Path.of("art","organized", "counter", "counter_behind.png").toFile()));
        imageStorage.put("fridge_closed", ImageIO.read(Path.of("art","organized", "fridge", "fridge_closed.png").toFile()));
        imageStorage.put("fridge_open", ImageIO.read(Path.of("art","organized", "fridge", "fridge_open.png").toFile()));
    }

    private static void addWorkStations() throws IOException {
        imageStorage.put("Stove", ImageIO.read(Path.of("art","organized", "stove", "stove_front_on.png").toFile()));
        imageStorage.put("Sink", ImageIO.read(Path.of("art","organized", "sink", "sink_front_off.png").toFile()));
        imageStorage.put("Counter", ImageIO.read(Path.of("art","organized", "counter", "counter_front.png").toFile()));
        imageStorage.put("Fridge", ImageIO.read(Path.of("art","organized", "fridge", "fridge_closed.png").toFile()));
    }

    private static void addProgramerArt() throws IOException {
        imageStorage.put("rat", ImageIO.read(Path.of("art","gonosz_remi.png").toFile()));
        imageStorage.put("tiles", ImageIO.read(Path.of("art","programmerArt", "tiles.png").toFile()));
        imageStorage.put("dot", ImageIO.read(Path.of("art", "programmerArt","dot.png").toFile()) );
        imageStorage.put("mark", ImageIO.read(Path.of("art","programmerArt","mark.png" ).toFile()));
        imageStorage.put("chef", ImageIO.read(Path.of("art","programmerArt","chef.png").toFile()));
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

    public static BufferedImage getImage(String name) {
        return imageStorage.get(name);
    }

    public static BufferedImage[] getAnim(String name) {
        return animStorage.get(name);
    }
}
