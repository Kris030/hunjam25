package hu.hunjam25.dlhc.model;

import java.util.Random;

public enum Food {

    Gulyas(new Ingredient[] { Ingredient.Tomato, Ingredient.Egg }),

    Pori(new Ingredient[] { Ingredient.Tomato, Ingredient.Egg }),

    ;

    private static Random r = new Random(System.currentTimeMillis());

    public Ingredient[] ingredients;

    Food(Ingredient[] ingredients) {
        this.ingredients = ingredients;
    }

    public static Food RandomFood() {
        Food[] foods = Food.values();
        int id = r.nextInt(foods.length);
        return foods[id];
    }
}
