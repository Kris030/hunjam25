package hu.hunjam25.dlhc.model;

import hu.hunjam25.dlhc.model.Workstation.WorkstationType;

public enum Ingredient {

    Tomato(WorkstationType.Stove, 3),
    Egg(WorkstationType.Fridge, 2),
    Beef(WorkstationType.Stove, 10),
    Carrots(WorkstationType.Stove, 7),
    TrashFire(WorkstationType.Trash, 4);

    public WorkstationType workstationType;

    public double durationSeconds;

    Ingredient(WorkstationType workspace, double durationSeconds) {
        this.workstationType = workspace;
        this.durationSeconds = durationSeconds;
    }

}
