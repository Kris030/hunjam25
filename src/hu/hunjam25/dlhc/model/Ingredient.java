package hu.hunjam25.dlhc.model;

import hu.hunjam25.dlhc.model.Workstation.WorkstationType;

public enum Ingredient {

    Tomato(WorkstationType.ChoppingBoard, 3),
    Egg(WorkstationType.ChoppingBoard, 2),
    Beef(WorkstationType.ChoppingBoard, 10),
    Carrots(WorkstationType.ChoppingBoard, 7),
    ;

    public WorkstationType workspace;

    public double durationSeconds;

    Ingredient(WorkstationType workspace, double durationSeconds) {
        this.workspace = workspace;
        this.durationSeconds = durationSeconds;
    }

}
