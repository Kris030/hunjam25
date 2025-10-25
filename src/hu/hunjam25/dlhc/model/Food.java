package hu.hunjam25.dlhc.model;

import java.util.Random;

public enum Food {

    Gulyas(new Ingredient[] { Ingredient.Water, Ingredient.Tomato, Ingredient.Chicken, Ingredient.Plating }),
    Sandwich(new Ingredient[] { Ingredient.Tomato, Ingredient.Egg, Ingredient.Salad, Ingredient.Plating }),
    Burg(new Ingredient[] { Ingredient.Tomato, Ingredient.Chicken, Ingredient.Salad, Ingredient.Plating }),
    ScrambledEggs(new Ingredient[] { Ingredient.Egg, Ingredient.Water, Ingredient.Egg, Ingredient.Plating }),
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
