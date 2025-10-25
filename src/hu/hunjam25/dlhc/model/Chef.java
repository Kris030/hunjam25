package hu.hunjam25.dlhc.model;

import java.awt.*;
import java.util.*;
import java.util.List;

import hu.hunjam25.dlhc.Game;
import hu.hunjam25.dlhc.GameObject;
import hu.hunjam25.dlhc.Kitchen;
import hu.hunjam25.dlhc.view.Sprite;

public class Chef extends GameObject {
    private final static int DEFAULT_FOOD_COUNT = 3;
    private final static float SPEED = 0.005f;

    public Chef(int foodCount) {
        positionToCenter();
        foodTodo = new java.util.ArrayDeque<>();
        for (int i = 0; i < foodCount; i++) {
            foodTodo.add(Food.RandomFood());
        }
        results = new ArrayList<>();
        currentFood = foodTodo.remove();
        currIngredient = 0;
        todo = Arrays.asList(currentFood.ingredients);
        pathFindingTo = Kitchen.findClosestWorkStation(position, todo.get(currIngredient));
    }

    public Chef() {
        this(DEFAULT_FOOD_COUNT);
    }

    Queue<Food> foodTodo;

    Food currentFood;
    int currIngredient;
    List<Ingredient> todo;
    List<Float> results;

    // null if not at workstation
    Workstation currWorkstation;
    float startedWorkAt;

    Workstation pathFindingTo;
    Point.Float pathFindingTargetPosition;

    boolean finished = false;

    @Override
    public void tick(float dt) {
        if (finished)
            return;
        if (currWorkstation == null) {
            if (pathFindingTargetPosition == null) {
                pathFindingTargetPosition = pathFindingTo.getPosition();
                pathFindingTargetPosition.x += pathFindingTo.workingOffset.x;
                pathFindingTargetPosition.y += pathFindingTo.workingOffset.y;
            }

            // we aren't at a station, go to pathFindingTargetPosition
            if (stepTowardsTarget(dt)) {
                currWorkstation = pathFindingTo;
                position = pathFindingTargetPosition;
                pathFindingTargetPosition = null;
                pathFindingTo = null;
                startedWorkAt = Game.now;
            }
        } else {
            // work at workstation
            if (Game.now - startedWorkAt >= todo.get(currIngredient).durationSeconds) {
                currWorkstation = null;
                currIngredient++;
                // TODO: results handling
                // food finished
                if (currIngredient >= todo.size()) {
                    // TODO: results handling
                    if (foodTodo.isEmpty()) {
                        finished = true;
                        return;
                    }
                    currentFood = foodTodo.remove();
                    todo = Arrays.asList(currentFood.ingredients);
                    currIngredient = 0;
                }
                pathFindingTo = Kitchen.findClosestWorkStation(position, todo.get(currIngredient));
            }
        }
    }

    /// return true if the chef has arrived at the target
    private boolean stepTowardsTarget(float dt) {
        Point.Float velocity = (Point.Float) pathFindingTargetPosition.clone();
        velocity.x -= position.x;
        velocity.y -= position.y;

        double length = velocity.distance(0, 0);
        if (length == 0)
            return true;
        velocity.x /= length;
        velocity.y /= length;

        position.x += velocity.x * SPEED * dt;
        position.y += velocity.y * SPEED * dt;

        return length <= /* TODO exact distance? */ 0.1f;
    }

    public void pushResult(float result) {
        results.add(result);
    }

    private Sprite sprite = new Sprite(Game.getImage("chef"));

    @Override
    public void render(Graphics2D gd) {
        super.render(gd);
        sprite.render(gd);
        // System.out.println("chef drawn");
    }
}
