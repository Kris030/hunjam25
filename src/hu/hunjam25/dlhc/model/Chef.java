package hu.hunjam25.dlhc.model;

import java.util.ArrayList;
import java.util.Deque;

import hu.hunjam25.dlhc.Game;
import hu.hunjam25.dlhc.GameObject;
import hu.hunjam25.dlhc.Kitchen;

public class Chef extends GameObject {

    Deque<Food> foodTodo;

    Food currentFood;
    int currIngredient;
    ArrayList<Ingredient> todo;
    ArrayList<Float> results;

    // null if not at workstation
    Workstation currWorkstation;
    float startedWorkAt;

    Workstation pathFindingTo;

    @Override
    public void tick(float dt) {
        if (currWorkstation == null) {
            // we aren't at a station, go to pathFindingTo

            boolean close = false;
            if (close) {
                currWorkstation = pathFindingTo;
                pathFindingTo = null;
                startedWorkAt = Game.now;
            }
        } else {
            // work at workstation

            if (Game.now - startedWorkAt >= todo.get(currIngredient).durationSeconds) {
                currWorkstation = null;
                pathFindingTo = Kitchen.findClosestWorkStation(todo.get(currIngredient));
            }
        }
    }

    public void pushResult(float result) {
        results.add(result);
    }
}
