package hu.hunjam25.dlhc.model;

public enum Food {

    Gulyas(new Ingredient[] {}),
    Pori(new Ingredient[] {}),

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
