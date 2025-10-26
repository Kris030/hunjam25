package hu.hunjam25.dlhc.model;

import java.util.Random;

public enum Food {

    Gulyas(new Ingredient[] { Ingredient.Water, Ingredient.Flour, Ingredient.Meat, Ingredient.Carrot,
            Ingredient.Potato, Ingredient.Plating }),
    Sandwich(new Ingredient[] { Ingredient.Tomato, Ingredient.Egg, Ingredient.Lettuce, Ingredient.Plating }),
    Burg(new Ingredient[] { Ingredient.Tomato, Ingredient.Chicken, Ingredient.Cheese, Ingredient.Lettuce, Ingredient.Meat,
            Ingredient.Plating }),
    Eggs(new Ingredient[] { Ingredient.Egg, Ingredient.Water, Ingredient.Egg, Ingredient.Mushroom, Ingredient.Egg,
            Ingredient.Plating }),
    Meatballs(new Ingredient[] { Ingredient.Meat, Ingredient.Tomato, Ingredient.Meat, Ingredient.Plating }),
    Spaghetti(new Ingredient[] { Ingredient.Flour, Ingredient.Water, Ingredient.Meat, Ingredient.Tomato,
            Ingredient.Plating }),
    Soup(new Ingredient[] { Ingredient.Water, Ingredient.Tomato, Ingredient.Plating }),
    MacNCheese(new Ingredient[] { Ingredient.Cheese, Ingredient.Water, Ingredient.Flour, Ingredient.Plating }),
    ;

    public static Random r = new Random(System.currentTimeMillis());

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
