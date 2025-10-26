package hu.hunjam25.dlhc;

import hu.hunjam25.dlhc.gameplay.ChoppingBoardMinigame;
import hu.hunjam25.dlhc.gameplay.FridgeMinigame;
import hu.hunjam25.dlhc.gameplay.Minigame;
import hu.hunjam25.dlhc.gameplay.OvenMinigame;
import hu.hunjam25.dlhc.model.*;
import hu.hunjam25.dlhc.view.AnimatedSprite;
import hu.hunjam25.dlhc.view.ParticleEffect;
import hu.hunjam25.dlhc.view.Sprite;

import java.util.ArrayList;
import java.util.stream.Stream;

public class Kitchen {

    public static final int CHEF_COUNT = 3;
    public static ArrayList<Workstation> workstations = new ArrayList<>();

    public static ArrayList<Chef> chefs = new ArrayList<>();

    public static Minigame minigame;

    public static Rat rat;

    public static Sprite floor = new Sprite(AssetManager.getImage("new_tiles2"));
    public static Sprite wallpaper = new Sprite(AssetManager.getImage("wallpaper"));
    public static Sprite woodFrame = new Sprite(AssetManager.getImage("wood"));

    public static AnimatedSprite stars = new AnimatedSprite(AssetManager.getAnim("stars"), 0);

    static {
        woodFrame.centered = false;
        wallpaper.centered = false;
        wallpaper.spriteScale = 1.35f;

        stars.centered = false;
        stars.setScale(0.4f);
    }


    public static ArrayList<ParticleEffect> particleEffects = new ArrayList<>();

    public static ArrayList<ParticleEffect> particleEffectKillList = new ArrayList<>();

    /// real number in [0;1]
    public static float rating = 1.0f;

    public static boolean isOnFire = false;

    static {
        floor.centered = false;
    }

    public static Workstation findClosestFreeWorkStation(Vec2 pos, Ingredient ingredient) {
        return workstations.stream()
                .filter(ws -> ws.type.equals(ingredient.workstationType) && !ws.hasWorker())
                .min((ws1, ws2) -> Double.compare(
                        pos.dist(ws1.getOffsettedPosition()),
                        pos.dist(ws2.getOffsettedPosition())))
                .orElse(null);
    }

    public static void minigameEnded(float result) {
        minigame.chef.pushResult(result);
    }

    public static void init() {
        rat = new Rat();

        //defLaylout();
        for (int i = 0; i < Game.MAP_WIDTH - 1; ++i) {
            workstations.add(new Workstation(Workstation.WorkstationType.values()[Food.r.nextInt(Workstation.WorkstationType.values().length)], new Vec2((float) i, 5f),
                    new Vec2(0, -0.5f)));
        }

        for (int i = 1; i < Game.MAP_HEIGHT; ++i) {
            workstations.add(new Workstation(Workstation.WorkstationType.values()[Food.r.nextInt(Workstation.WorkstationType.values().length)], new Vec2(0f, (float) i),
                    new Vec2(0.5f, 0f)));
        }

        for (int i = 1; i < Game.MAP_HEIGHT; ++i) {
            workstations.add(new Workstation(Workstation.WorkstationType.values()[Food.r.nextInt(Workstation.WorkstationType.values().length)], new Vec2(Game.MAP_WIDTH - 2f, (float) i),
                    new Vec2(-0.5f, 0f)));
        }

        for (int i = 0; i < Game.MAP_WIDTH - 1; ++i) {
            workstations.add(new Workstation(Workstation.WorkstationType.ChoppingBoard, new Vec2((float) i, 0f),
                    new Vec2(0, 0.5f)));
        }

        for (int i = 0; i < CHEF_COUNT; ++i) {
            chefs.add(new Chef());
        }
    }

    private static void defLaylout() {
        // trash workstation must be first in Kitchen.workstations!!!
        workstations.add(new Workstation(Workstation.WorkstationType.Trash, new Vec2(2f, 5f),
                new Vec2(0f, 0.5f)));
        // trash workstation must be first in Kitchen.workstations!!!
        workstations.add(new Workstation(Workstation.WorkstationType.Stove, new Vec2(12f, 5f),
                new Vec2(0f, -0.5f)));
        workstations.add(new Workstation(Workstation.WorkstationType.Fridge, new Vec2(5f, 5f),
                new Vec2(0, -0.5f)));
        workstations.add(new Workstation(Workstation.WorkstationType.Sink, new Vec2(6f, 5f),
                new Vec2(0, -0.5f)));
        workstations.add(new Workstation(Workstation.WorkstationType.ChoppingBoard, new Vec2(8f, 5f),
                new Vec2(0, -0.5f)));
        workstations.add(new Workstation(Workstation.WorkstationType.Oven, new Vec2(9f, 5f),
                new Vec2(0, -0.5f)));
        workstations.add(new Workstation(Workstation.WorkstationType.Belt, new Vec2(12f, 5f),
                new Vec2(-0.5f, 0f)));
    }

    public static Stream<GameObject> getGameObjects() {
        // render order
        Stream<GameObject> s = Stream.concat(workstations.stream(), chefs.stream());

        s = Stream.concat(s, Stream.of(rat));

        s = Stream.concat(s, particleEffects.stream());

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

    public static void startMinigame(Workstation workstation, Chef chef, Ingredient ingredient) {
        minigame = switch (workstation.type) {
            case Fridge -> new FridgeMinigame(workstation, chef, ingredient);
            case ChoppingBoard -> new ChoppingBoardMinigame(workstation, chef, ingredient);
            case Stove -> new OvenMinigame(workstation, chef, ingredient);
            case Oven -> new OvenMinigame(workstation, chef, ingredient);
//            case Trash -> null;
            default -> new ChoppingBoardMinigame(workstation, chef, ingredient);
        };
    }

    public static void startFire() {
        isOnFire = true;
        chefs.forEach(c -> c.addHazard(Kitchen.workstations.get(0), Ingredient.TrashFire));
    }

    public static void updateStars(){
        stars.setIdx((int)(rating * 5f));
    }
}
