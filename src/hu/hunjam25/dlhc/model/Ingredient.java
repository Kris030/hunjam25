package hu.hunjam25.dlhc.model;

import hu.hunjam25.dlhc.model.Workstation.WorkstationType;

public enum Ingredient {

    Tomato(WorkstationType.ChoppingBoard, 10),
    Tomatoes(WorkstationType.ChoppingBoard, 20),
    Cheese(WorkstationType.Fridge, 8),
    Flour(WorkstationType.Stove, 10),
    Carrot(WorkstationType.Sink, 5),
    Potato(WorkstationType.ChoppingBoard, 20),
    Egg(WorkstationType.Fridge, 15),
    Lettuce(WorkstationType.Sink, 5),
    Chicken(WorkstationType.Oven, 8),
    Water(WorkstationType.Stove, 18),
    Mushroom(WorkstationType.Sink, 7),
    Meat(WorkstationType.Oven, 15),
    Plating(WorkstationType.Belt, 5),
    TrashFire(WorkstationType.Trash, 15)

    ;

    public WorkstationType workstationType;

    public float durationSeconds;

    Ingredient(WorkstationType workspace, float durationSeconds) {
        this.workstationType = workspace;
        this.durationSeconds = durationSeconds;
    }

}
