package hu.hunjam25.dlhc.model;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.*;

import hu.hunjam25.dlhc.AssetManager;
import hu.hunjam25.dlhc.Game;
import hu.hunjam25.dlhc.GameObject;
import hu.hunjam25.dlhc.Kitchen;
import hu.hunjam25.dlhc.view.AnimatedSprite;
import hu.hunjam25.dlhc.view.Sprite;
import hu.hunjam25.dlhc.view.UiElement;

import static java.lang.Math.abs;

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

        animatedSprite.frozen = true;
        animatedSprite.setScale(0.375f);

        //TODO: real stuff here
        UiElement timr = new UiElement();
        timr.visible = true;
        timr.scale = 0.2f;
        AnimatedSprite timerSprite = new AnimatedSprite(AssetManager.getAnim("clock"), 4);
        timerSprite.frozen = false;
        timerSprite.start();
        timr.setAnimatedSprite(timerSprite);
        timr.addOffset(0.5f, 0.0f);
        addUiElement(timr);
        UiElement timer = new UiElement();
        timer.visible = true;
        timer.scale = 0.2f;
        AnimatedSprite timrSprite = new AnimatedSprite(AssetManager.getAnim("ratMeter"), 2);
        timrSprite.frozen = false;
        timrSprite.start();
        timer.setAnimatedSprite(timrSprite);
        timer.addOffset(-0.5f, 0.0f);
        addUiElement(timer);
    }

    private void startNewFood() {
        currentFood = foodTodo.remove();
        currIngredient = 0;
        todo = currentFood.ingredients;
        results = new float[todo.length];
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
    float[] results;

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
                if (pathFindingTo == null)
                    return;
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
            animatedSprite.setIdx(0);
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
                    Kitchen.decreaseRating(results, timeDelay);

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
        if (stopped()) {
            animatedSprite.setIdx(0);
            return false;
        }
        Point.Float velocity = (Point.Float) pathFindingTargetPosition.clone();
        velocity.x -= position.x;
        velocity.y -= position.y;

        double length = velocity.distance(0, 0);
        if (length == 0) {
            animatedSprite.setIdx(0);
            return true;
        }
        velocity.x /= length;
        velocity.y /= length;

        position.x += velocity.x * SPEED * dt;
        position.y += velocity.y * SPEED * dt;

        int spriteIdx = facing(velocity);
        if (confused) spriteIdx = spriteIdx + 3;
        if (velocity.x < 0) animatedSprite.mirrored = true;
        else animatedSprite.mirrored = false;
        animatedSprite.setIdx(spriteIdx);

        return length <= 0.1f;
    }

    public void stopForDuration(float seconds) {
        stoppedUntil = Game.now + seconds;
    }

    private boolean stopped() {
        return Game.now < stoppedUntil;
    }

    /**
     * add result handling for each ingredient, should be called by the minigame
     * when it ends
     * 
     * @param result should be in {@code[0;0.10]} with the highend being rare
     */
    public void pushResult(float result) {
        results[currIngredient] += result;
    }

    private AnimatedSprite animatedSprite =  new AnimatedSprite(AssetManager.getAnim("chef1"), 0.5f);
    private  boolean confused = false;

    @Override
    public void render(Graphics2D gd) {
        super.render(gd);
        animatedSprite.render(gd);
        renderUiElements(gd);
    }

    private static int facing(Point2D.Float vel) { //fentről óramutatóval 4 forgási fázis indexe
        if (vel.x == 0.0f && vel.y == 0.0f) {return 0;}
        if (vel.y > 0.0f && abs(vel.y) > abs(vel.x)) {return 0;} //felfele gyorsabb -> 0. index
        if (vel.y < 0.0f && abs(vel.y) > abs(vel.x)) {return 2;} //lefele gyorsbb -> 2. index
        //if (vel.x > 0f && abs(vel.y) < abs(vel.x)) {return 1;} //jobbra gyorsabb -> 1. index
        return 1; //egyébként 3. index
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
