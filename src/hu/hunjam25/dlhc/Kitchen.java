package hu.hunjam25.dlhc;

import java.util.ArrayList;
import java.util.stream.Stream;

import hu.hunjam25.dlhc.gameplay.Minigame;
import hu.hunjam25.dlhc.model.Chef;
import hu.hunjam25.dlhc.model.Ingredient;
import hu.hunjam25.dlhc.model.Rat;
import hu.hunjam25.dlhc.model.Workstation;
import hu.hunjam25.dlhc.view.ParticleEffect;
import hu.hunjam25.dlhc.view.Sprite;
import hu.hunjam25.dlhc.view.UiElement;

public class Kitchen {

    public static ArrayList<Workstation> workstations = new ArrayList<>();

    public static ArrayList<Chef> chefs = new ArrayList<>();

    public static Minigame minigame;

    public static Rat rat;

    public static Sprite floor = new Sprite(AssetManager.getImage("new_tiles2"));
    public static Sprite wallpaper = new Sprite(AssetManager.getImage("wallpaper"));
    static {
        wallpaper.centered = false;
        wallpaper.spriteScale = 1.35f;
    }


    public static ArrayList<ParticleEffect> particleEffects = new ArrayList<>();

    public static ArrayList<ParticleEffect> particleEffectKillList = new ArrayList<>();

    /// real number in [0;1]
    public static float rating = 1.0f;

    public static boolean isOnFire = false;

    static {
        floor.centered = false;
    }

    public static Workstation findClosestWorkStation(Vec2 pos, Ingredient ingredient) {
        return workstations.stream()
                .filter(ws -> ws.type.equals(ingredient.workstationType))
                .min((ws1, ws2) -> Double.compare(
                        pos.dist(ws1.getOffsettedPosition()),
                        pos.dist(ws2.getOffsettedPosition())))
                .orElse(null);
    }

    public static void minigameEnded(float result) {
        minigame = null;
        minigame.chef.pushResult(result);
    }

    public static void init() {
        rat = new Rat();

        // trash workstation must be first in Kitchen.workstations!!!
        workstations.add(new Workstation(Workstation.WorkstationType.Trash, new Vec2(2f, 1f),
                new Vec2(0f, 0.5f)));
        // trash workstation must be first in Kitchen.workstations!!!
        workstations.add(new Workstation(Workstation.WorkstationType.Stove, new Vec2(12f, 3f),
                new Vec2(0f, -0.5f)));
        workstations.add(new Workstation(Workstation.WorkstationType.Fridge, new Vec2(5f, 3f),
                new Vec2(0, -0.5f)));
        workstations.add(new Workstation(Workstation.WorkstationType.Sink, new Vec2(6f, 3f),
                new Vec2(0, -0.5f)));
        workstations.add(new Workstation(Workstation.WorkstationType.ChoppingBoard, new Vec2(8f, 3f),
                new Vec2(0, -0.5f)));
        workstations.add(new Workstation(Workstation.WorkstationType.Oven, new Vec2(9f, 3f),
                new Vec2(0, -0.5f)));
        workstations.add(new Workstation(Workstation.WorkstationType.Belt, new Vec2(12f, 1f),
                new Vec2(-0.5f, 0f)));

        chefs.add(new Chef());
        chefs.add(new Chef());
        chefs.add(new Chef());

        var element = new UiElement();
        element.visible = true;
        element.scale = 0.5f;
        rat.addUiElement(element);
    }

    public static Stream<GameObject> getGameObjects() {
        // render order
        Stream<GameObject> s = Stream.concat(workstations.stream(), chefs.stream());

        s = Stream.concat(s, Stream.of(rat));

        s = Stream.concat(s, particleEffects.stream());

        if (minigame != null) {
            s = Stream.concat(s, Stream.of(minigame));
        }

        return s;
    }

    public static void decreaseRating(float[] ingredientResults, float timeDelay) {
        float deduction = -0.1f;
        if (timeDelay > 6) {
            deduction += (timeDelay - 6) / 100f;
        }
        float sum = 0f;
        for (float ir : ingredientResults) {
            sum += ir;
        }
        deduction += (sum / ingredientResults.length) / 100f;

        rating -= deduction;
        // keep rating between 0 and 1
        rating = Math.clamp(rating, 0.0f, 1.0f);
    }

    public static void startMinigame(Workstation w) {
        minigame = w.getMinigame();
    }

    public static void startFire() {
        isOnFire = true;
        chefs.forEach(c -> c.addHazard(Kitchen.workstations.get(0), Ingredient.TrashFire));
    }
}
