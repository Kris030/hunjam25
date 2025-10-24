package hu.hunjam25.dlhc.model;

import hu.hunjam25.dlhc.GameObject;

public class Workstation extends GameObject {

    public static enum WorkstationType {
        ChoppingBoard,
        Oven,
        Fridge,
        Stove,

        ;
    }

    WorkstationType type;

}
