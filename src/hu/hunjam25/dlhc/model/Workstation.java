package hu.hunjam25.dlhc.model;

import java.awt.Point;

import hu.hunjam25.dlhc.GameObject;

public class Workstation extends GameObject {

    public static enum WorkstationType {
        ChoppingBoard,
        Oven,
        Fridge,
        Stove,

        ;
    }

    public Workstation(WorkstationType type, Point.Float position, Point.Float workingOffset) {
        this.type = type;
        this.position = position;
        this.workingOffset = workingOffset;
    }

    WorkstationType type;

    // relative to GameObject position
    Point.Float workingOffset;

}
