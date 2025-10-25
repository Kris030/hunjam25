package hu.hunjam25.dlhc.model;

public enum Food {

    Gulyas(new Ingredient[] {Ingredient.Tomato, Ingredient.Tomato}),
    Pori(new Ingredient[] {Ingredient.Tomato, Ingredient.Tomato}),

    ;

    public Ingredient[] ingredients;

    Food(Ingredient[] ingredients) {
        this.ingredients = ingredients;
    }

    public static Food RandomFood() {
        Food[] foods = Food.values();
        int id = (int) (Math.random() * foods.length);
        return foods[id];
    }
}
