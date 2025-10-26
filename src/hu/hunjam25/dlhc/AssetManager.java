package hu.hunjam25.dlhc;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.function.Function;
import java.util.stream.IntStream;

import javax.imageio.ImageIO;
import javax.sound.sampled.UnsupportedAudioFileException;

import hu.hunjam25.dlhc.model.Workstation.WorkstationType;
import hu.hunjam25.dlhc.sound.SoundBuffer;

public class AssetManager {

    private static HashMap<String, BufferedImage> imageStorage;

    private static HashMap<String, BufferedImage[]> animStorage;

    private static HashMap<String, SoundBuffer> soundStorage;

    static void init() throws IOException, UnsupportedAudioFileException {
        // TODO: better
        imageStorage = new HashMap<>();
        animStorage = new HashMap<>();
        soundStorage = new HashMap<>();

        addImg("no_texture", img(Path.of("art", "programmerArt", "no.png")));

        addSounds();

        addProgramerArt();

        addProgrammerRemy();

        addWorkStationAnimations();

        addWorkStations();

        addRemiAnim();
        addRadio();

        addClockAnimation();

        addRatMeter();

        addChefAnimations(3);

        addImg("minigame_keret", img(Path.of("art", "minigame_keret.png")));

        addImg("app_icon", img(Path.of("art", "icon.png")));

        addHutoMinigame();

        addChoppingMinigame();
    }

    private static void addChoppingMinigame() throws IOException {
        addImg("minigame.chopping.kes", img(Path.of("art", "szeleteles", "kes.png")));
        addImg("minigame.chopping.pari_szelet", img(Path.of("art", "szeleteles", "pari_szelet.png")));
        addImg("minigame.chopping.vagodeszka", img(Path.of("art", "szeleteles", "vagodeszka.png")));

        addAnim("minigame.chopping.pari", imgs(4, i -> Path.of("art", "szeleteles", "pari_" + (i + 1) + ".png")));

        addAnim("minigame.chopping.remi_karddal",
                imgs(2, i -> Path.of("art", "szeleteles", "remi_karddal_" + i + ".png")));
    }

    private static void addHutoMinigame() throws IOException {
        Path nyer = Path.of("art", "minigame_huto_nyeres");
        Path veszt = Path.of("art", "minigame_huto_vesztes");

        addAnim("huto_minigame_nyer",
                imgs(89, i -> nyer.resolve(Path.of("patkany_nyer_animacio",
                        String.format("minigame_huto_patkany_nyer_000%02d.png", i)))));

        addAnim("huto_minigame_veszit",
                imgs(89, i -> veszt.resolve(Path.of("patkany_veszit_animacio",
                        String.format("minigame_huto_patkany_veszit_000%02d.png", i)))));

        addImg("huto_minigame_nyer", img(nyer.resolve(Path.of("patkany_final_win", "patkany_final_win.png"))));
        addImg("huto_minigame_veszit", img(veszt.resolve(Path.of("patkany_final_defeat", "patkany_final_defeat.png"))));
    }

    private static void addRatMeter() throws IOException {
        addAnim("ratMeter", imgs(5, i -> Path.of("art", "patkany", "patkany_0" + (i + 1) + ".png")));
    }

    private static BufferedImage img(Path p) throws IOException {
        return ImageIO.read(getResInputStream(p));
    }

    private static InputStream getResInputStream(Path p) throws FileNotFoundException {
        // FIXME: first line works in jar, second works from IDE
        // return AssetManager.class.getClassLoader().getResourceAsStream(p.toString());
        return new FileInputStream(p.toFile());
    }

    private static BufferedImage[] imgs(Path... paths) throws IOException {
        BufferedImage[] images = new BufferedImage[paths.length];
        for (int i = 0; i < images.length; i++) {
            images[i] = img(paths[i]);
        }

        return images;
    }

    private static BufferedImage[] imgs(int n, Function<Integer, Path> pathTransformer) throws IOException {
        return imgs(IntStream.range(0, n).boxed().map(pathTransformer).toArray(Path[]::new));
    }

    private static void addImg(String name, BufferedImage image) {
        imageStorage.put(name, image);
    }

    private static void addAnim(String name, BufferedImage... images) {
        animStorage.put(name, images);
    }

    private static void addProgrammerRemy() throws IOException {
        for (int i = 1; i <= 6; i++) {
            addImg("remi" + i, img(Path.of("art", "programmerArt", "remi", "animalt_remi" + i + ".png")));
        }
    }

    private static void addChefAnimations(int n) throws IOException {
        for (int i = 0; i < n; i++) {
            String nm = "chef" + (i + 1);

            addAnim(nm, imgs(
                    Path.of("art", "organized", "sefek", nm, "fel.png"),
                    Path.of("art", "organized", "sefek", nm, "oldal.png"),
                    Path.of("art", "organized", "sefek", nm, "le.png"),
                    Path.of("art", "organized", "sefek", nm, "fel_shocked.png"),
                    Path.of("art", "organized", "sefek", nm, "oldal_shocked.png"),
                    Path.of("art", "organized", "sefek", nm, "le_shocked.png")));
        }
    }

    private static void addWorkStationAnimations() throws IOException {
        BufferedImage[] stoveOven = imgs(new Path[] {
                Path.of("art", "organized", "stove", "stove_front_off.png"),
                Path.of("art", "organized", "stove", "stove_front_on.png"),
        });
        animStorage.put(WorkstationType.Stove.name(), stoveOven);
        animStorage.put(WorkstationType.Oven.name(), stoveOven);

        addAnim(WorkstationType.Fridge.name(), imgs(
                Path.of("art", "organized", "fridge", "fridge_closed.png"),
                Path.of("art", "organized", "fridge", "fridge_open.png")));

        addAnim("Sink", imgs(
                Path.of("art", "organized", "sink", "sink_front_off.png"),
                Path.of("art", "organized", "sink", "sink_front_on.png")));

        addAnim(WorkstationType.ChoppingBoard.name(), imgs(
                Path.of("art", "organized", "counter", "counter_front.png")));

        addAnim("fire", imgs(2, i -> Path.of("art", "organized", "ego_kuka", "tuz_" + (i + 1) + ".png")));
        addAnim("smoke", imgs(2, i -> Path.of("art", "organized", "ego_kuka", "fust_" + (i + 1) + ".png")));

        addAnim(WorkstationType.Trash.name(), imgs(
                Path.of("art", "organized", "ego_kuka", "kuka.png")));

        addAnim("Belt", imgs(9, i -> Path.of("art", "organized", "belt", i + ".png")));
    }

    private static void addClockAnimation() throws IOException {
        addAnim("clock", imgs(5, i -> Path.of("art", "ora", "ora_0" + (i + 1) + ".png")));
    }

    private static void addWorkStations() throws IOException {
        addImg("Stove", img(Path.of("art", "organized", "stove", "stove_front_on.png")));
        addImg("Sink", img(Path.of("art", "organized", "sink", "sink_front_off.png")));
        addImg("Counter", img(Path.of("art", "organized", "counter", "counter_front.png")));
        addImg("Fridge", img(Path.of("art", "organized", "fridge", "fridge_closed.png")));
    }

    private static void addProgramerArt() throws IOException {
        addImg("rat", img(Path.of("art", "gonosz_remi.png")));
        addImg("tiles", img(Path.of("art", "programmerArt", "tiles.png")));
        addImg("new_tiles", img(Path.of("art", "programmerArt", "new_tiles.png")));
        addImg("new_tiles2", img(Path.of("art", "programmerArt", "new_tiles2.png")));
        addImg("wallpaper", img(Path.of("art", "organized", "wallpaper_smudge.jpeg")));
        addImg("wood", img(Path.of("art", "organized", "wood.png")));

        addImg("dot", img(Path.of("art", "programmerArt", "dot.png")));
        addImg("mark", img(Path.of("art", "programmerArt", "mark.png")));
        addImg("chef", img(Path.of("art", "programmerArt", "chef.png")));
    }

    private static void addRemiAnim() throws IOException {
        addAnim("remi",
                imgs(69, i -> Path.of("art", "organized", "remi",
                        "gonosz_remi2_000" + String.format("%02d", i) + ".png")));
    }

    private static void addRadio() throws IOException {
        addAnim("radio",
                imgs(6, i -> Path.of("art", "radio",
                        "radio_0" + (i + 1) + ".png")));
    }

    private static void addIngredients() throws IOException {

    }

    private static void addSound(String name, Path path) throws IOException, UnsupportedAudioFileException {
        soundStorage.put(name, SoundBuffer.read(getResInputStream(path)));
    }

    private static void addSounds() throws IOException, UnsupportedAudioFileException {
        addSound("pipe", Path.of("art", "metal-pipe.wav"));
        addSound("ready", Path.of("art", "sounds", "ready_ding.wav"));
        addSound("music", Path.of("art", "sounds", "music.wav"));
    }

    public static BufferedImage getImage(String name) {
        if(!imageStorage.containsKey(name)){
            System.out.println(name + " is missing - getimage");
        }
        return imageStorage.getOrDefault(name, imageStorage.get("no_texture"));
    }

    public static BufferedImage[] getAnim(String name) {
        if(!animStorage.containsKey(name)){
            System.out.println(name + " is missing - getAnim");
        }
        BufferedImage img = imageStorage.get("no_texture");
        return animStorage.getOrDefault(name, new BufferedImage[] { img, img, img });
    }

    public static SoundBuffer getSound(String name) {
        return soundStorage.getOrDefault(name, soundStorage.get("pipe"));
    }
}
