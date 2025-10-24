package hu.hunjam25.dlhc.model;

import java.util.Deque;

public class Chef {

    Deque<Food> foodTodo;

    Food currentFood;
    Deque<Ingredient> todo;

    // null if not at workstation
    Workstation currWorkstation;

}
