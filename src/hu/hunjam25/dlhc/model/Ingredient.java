package hu.hunjam25.dlhc.model;

import hu.hunjam25.dlhc.model.Workstation.WorkstationType;

public enum Ingredient {

    Tomato(WorkstationType.ChoppingBoard, 10),
    Egg(WorkstationType.Fridge, 15),
    Salad(WorkstationType.Sink, 5),
    Chicken(WorkstationType.Oven, 8),
    Water(WorkstationType.Stove, 18),
    TrashFire(WorkstationType.Trash, 20),
    Plating(WorkstationType.Belt, 5);

    public WorkstationType workstationType;

    public double durationSeconds;

    Ingredient(WorkstationType workspace, double durationSeconds) {
        this.workstationType = workspace;
        this.durationSeconds = durationSeconds;
    }

}
