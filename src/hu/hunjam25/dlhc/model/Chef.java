package hu.hunjam25.dlhc.model;

import java.util.Deque;

import hu.hunjam25.dlhc.GameObject;

public class Chef extends GameObject {

    Deque<Food> foodTodo;

    Food currentFood;
    Deque<Ingredient> todo;

    // null if not at workstation
    Workstation currWorkstation;

    @Override
    public void tick(float dt) {

    }
}
