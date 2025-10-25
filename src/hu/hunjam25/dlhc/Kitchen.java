package hu.hunjam25.dlhc;

import java.util.ArrayList;

import hu.hunjam25.dlhc.gameplay.Minigame;
import hu.hunjam25.dlhc.model.Chef;
import hu.hunjam25.dlhc.model.Ingredient;
import hu.hunjam25.dlhc.model.Rat;
import hu.hunjam25.dlhc.model.Workstation;
import hu.hunjam25.dlhc.view.Sprite;

public class Kitchen {

    public static ArrayList<Workstation> workstations = new ArrayList<>();

    public static ArrayList<Chef> chefs = new ArrayList<>();

    public static Minigame minigame;

    public static Rat rat;

    // TODO:
    public static Sprite background = new Sprite(Game.getImage("tiles"));

    // TODO: BFS if the kitchen isn't a square?
    public static Workstation findClosestWorkStation(Ingredient ingredient) {
        // TODO: implement
        return null;
    }

    public static void minigameEnded(float result) {
        minigame = null;
        minigame.chef.pushResult(result);
    }

    public static void init() {
        rat = new Rat();
    }
}
