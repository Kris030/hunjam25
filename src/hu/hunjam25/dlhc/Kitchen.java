package hu.hunjam25.dlhc;

import java.awt.Point;
import java.util.ArrayList;
import java.util.stream.Stream;

import hu.hunjam25.dlhc.gameplay.Minigame;
import hu.hunjam25.dlhc.model.Chef;
import hu.hunjam25.dlhc.model.Ingredient;
import hu.hunjam25.dlhc.model.Rat;
import hu.hunjam25.dlhc.model.Workstation;
import hu.hunjam25.dlhc.view.Sprite;
import hu.hunjam25.dlhc.view.UiElement;

public class Kitchen {

    public static ArrayList<Workstation> workstations = new ArrayList<>();

    public static ArrayList<Chef> chefs = new ArrayList<>();

    public static Minigame minigame;

    public static Rat rat;

    public static Sprite background = new Sprite(AssetManager.getImage("tiles"));

    public static float rating = 1.0f;

    static {
        background.centered = false;
    }

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
        workstations.add(new Workstation(Workstation.WorkstationType.Stove, new Point.Float(0f, 2f),
                new Point.Float(0f, -0.5f)));
        workstations.add(new Workstation(Workstation.WorkstationType.Fridge, new Point.Float(5f, 0f),
                new Point.Float(-0.5f, 0f)));
        chefs.add(new Chef());

        var element = new UiElement();
        element.visible = true;
        element.scale = 0.5f;
        rat.addUiElement(element);
    }

    public static Stream<GameObject> getGameObjects() {
        // render order
        return Stream.concat(
                Stream.concat(workstations.stream(), chefs.stream()),
                minigame == null ? Stream.of(rat) : Stream.of(rat, minigame));
    }

    public static void increaseRating(Float[] ingRatings, Float timeDelay) {
        /*
         * TODO: create function for how time delay and minigames affect rating
         */

        // keep rating between 0 and 1
        Math.clamp(rating, 0.0f, 1.0f);
    }
}
