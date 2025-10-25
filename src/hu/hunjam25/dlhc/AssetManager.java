package hu.hunjam25.dlhc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

public class AssetManager {

    private static HashMap<String, BufferedImage> imageStorage;

    static void init() throws IOException {
        // TODO: better
        imageStorage = new HashMap<>();
        imageStorage.put("rat", ImageIO.read(Path.of("art","gonosz_remi.png").toFile()));
        imageStorage.put("tiles", ImageIO.read(Path.of("art","programmerArt", "tiles.png").toFile()));
        imageStorage.put("dot", ImageIO.read(Path.of("art", "programmerArt","dot.png").toFile()) );
        imageStorage.put("mark", ImageIO.read(Path.of("art","programmerArt","mark.png" ).toFile()));
        imageStorage.put("chef", ImageIO.read(Path.of("art","programmerArt","chef.png").toFile()));

        for (int i = 1; i <= 6; ++i){
            imageStorage.put("remi" + i,ImageIO.read(Path.of("art","programmerArt","remi", "animalt_remi" + i + ".png").toFile()));
        }
        /*
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
        */
        imageStorage.put("Stove", ImageIO.read(Path.of("art","organized", "stove", "stove_front_on.png").toFile()));
        imageStorage.put("Sink", ImageIO.read(Path.of("art","organized", "sink", "sink_front_off.png").toFile()));
        imageStorage.put("Counter", ImageIO.read(Path.of("art","organized", "counter", "counter_front.png").toFile()));
        imageStorage.put("Fridge", ImageIO.read(Path.of("art","organized", "fridge", "fridge_closed.png").toFile()));
    }

    public static BufferedImage getImage(String name) {
        return imageStorage.get(name);
    }
}
