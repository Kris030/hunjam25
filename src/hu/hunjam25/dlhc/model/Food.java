package hu.hunjam25.dlhc.model;

public enum Food {

    Gulyas(new Ingredient[] {}),
    Pori(new Ingredient[] {}),

    ;

    public Ingredient[] ingredients;

    Food(Ingredient[] ingredients) {
        this.ingredients = ingredients;
    }

}
