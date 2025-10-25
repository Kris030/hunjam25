package hu.hunjam25.dlhc;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.stream.Stream;

import hu.hunjam25.dlhc.gameplay.Minigame;
import hu.hunjam25.dlhc.model.Chef;
import hu.hunjam25.dlhc.model.Ingredient;
import hu.hunjam25.dlhc.model.Rat;
import hu.hunjam25.dlhc.model.Workstation;
import hu.hunjam25.dlhc.view.AnimatedSprite;
import hu.hunjam25.dlhc.view.Sprite;

public class Kitchen {

    public static ArrayList<Workstation> workstations = new ArrayList<>();

    public static ArrayList<Chef> chefs = new ArrayList<>();

    public static Minigame minigame;

    public static Rat rat;

    public static Sprite background = new Sprite(Game.getImage("tiles"));


    static {
        background.centered = false;
    }

    // TODO: BFS if the kitchen isn't a square?
    public static Workstation findClosestWorkStation(Point.Float position, Ingredient ingredient) {
        var _ws = workstations.stream()
                .filter(ws -> ws.type.equals(ingredient.workstationType))
                .min((ws1, ws2) -> Double.compare(position.distance(ws1.position), position.distance(ws2.position)))
                .orElse(null);
        return _ws;
    }

    public static void minigameEnded(float result) {
        minigame = null;
        minigame.chef.pushResult(result);
    }

    public static void init() {
        rat = new Rat();
        workstations.add(new Workstation(Workstation.WorkstationType.ChoppingBoard, new Point.Float(0f, 2f), new Point.Float(0f, -0.5f)));
        workstations.add(new Workstation(Workstation.WorkstationType.Fridge, new Point.Float(5f, 0f), new Point.Float(-0.5f, 0f)));
        chefs.add(new Chef());
    }

    public static Stream<GameObject> getGameObjects() {
        // render order
        return Stream.concat(
                Stream.concat(workstations.stream(), chefs.stream()),
                minigame == null ? Stream.of(rat) : Stream.of(rat, minigame));
    }
}
