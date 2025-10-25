package hu.hunjam25.dlhc.model;

import java.awt.*;
import java.util.*;

import hu.hunjam25.dlhc.AssetManager;
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
        startNewFood();
    }

    private void startNewFood() {
        currentFood = foodTodo.remove();
        currIngredient = 0;
        todo = currentFood.ingredients;
        results = new Float[todo.length];
        Arrays.fill(results, 0f);
        pathFindingTo = Kitchen.findClosestWorkStation(position, todo[currIngredient]);
        startedCurrentFoodAt = Game.now;
    }

    public Chef() {
        this(DEFAULT_FOOD_COUNT);
    }

    Queue<Food> foodTodo;

    Food currentFood;

    int currIngredient;
    Ingredient[] todo;
    Float[] results;

    // null if not at workstation
    Workstation currWorkstation;
    float startedWorkAt;

    float startedCurrentFoodAt;

    Workstation pathFindingTo;
    Point.Float pathFindingTargetPosition;

    float stoppedUntil = 0f;

    Ingredient tempIfOnFire = null;

    public boolean finished = false;

    @Override
    public void tick(float dt) {
        if (finished) {
            return; // TODO: valami dispose
        }
        if (currWorkstation == null) {
            if (pathFindingTargetPosition == null) {
                pathFindingTargetPosition = pathFindingTo.getPosition();
                pathFindingTargetPosition.x += pathFindingTo.workingOffset.x;
                pathFindingTargetPosition.y += pathFindingTo.workingOffset.y;
            }

            boolean arrivedAtStation = stepTowardsTarget(dt);
            if (arrivedAtStation) {
                currWorkstation = pathFindingTo;
                currWorkstation.workers++;
                position = pathFindingTargetPosition;
                pathFindingTargetPosition = null;
                pathFindingTo = null;
                startedWorkAt = Game.now;
            }
        } else {
            // work at workstation
            if (Game.now - startedWorkAt >= todo[currIngredient].durationSeconds) {
                currWorkstation.workers--;
                currWorkstation = null;

                if (tempIfOnFire == null) {
                    currIngredient++;
                } else {
                    fixTempIngredient();
                }

                // food finished
                if (currIngredient >= todo.length) {
                    double ingredientSumTime = 0;
                    for (var ing : todo)
                        ingredientSumTime += ing.durationSeconds;
                    Float timeDelay = Game.now - startedCurrentFoodAt - (float) ingredientSumTime;
                    Kitchen.increaseRating(results, timeDelay);

                    if (foodTodo.isEmpty()) {
                        currIngredient = 0;
                        finished = true;
                        return;
                    }
                    startNewFood();
                }
                pathFindingTo = Kitchen.findClosestWorkStation(position, todo[currIngredient]);
            }
        }
    }

    /// return true if the chef has arrived at the target
    private boolean stepTowardsTarget(float dt) {
        if (stopped())
            return false;
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

        return length <= 0.1f;
    }

    public void stopForDuration(float seconds) {
        stoppedUntil = Game.now + seconds;
    }

    private boolean stopped() {
        return Game.now < stoppedUntil;
    }

    public void pushResult(float result) {
        results[currIngredient] += result;
    }

    private Sprite sprite = new Sprite(AssetManager.getImage("chef"));

    @Override
    public void render(Graphics2D gd) {
        super.render(gd);
        sprite.render(gd);
    }

    public void addHazard(Workstation trash, Ingredient fire) {
        currWorkstation = null;
        pathFindingTargetPosition = null;
        pathFindingTo = trash;
        tempIfOnFire = todo[currIngredient];
        todo[currIngredient] = fire;
    }

    private void fixTempIngredient() {
        todo[currIngredient] = tempIfOnFire;
        tempIfOnFire = null;
    }
}
