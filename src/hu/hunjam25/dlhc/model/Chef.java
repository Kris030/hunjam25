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

    private Point.Float velocity = new Point.Float(0f, 0f);

    public Chef(int foodCount){
        foodTodo = new java.util.ArrayDeque<>();
        for (int i = 0; i<foodCount; i++){
            foodTodo.add(Food.RandomFood());
        }
        results = new ArrayList<>();
        currentFood = foodTodo.poll();
        currIngredient = 0;
        todo = Arrays.asList(currentFood.ingredients);
        pathFindingTo = Kitchen.findClosestWorkStation(position, todo.get(currIngredient));
    }

    public Chef(){
        this(DEFAULT_FOOD_COUNT);
    }

    Deque<Food> foodTodo;

    Food currentFood;
    int currIngredient;
    List<Ingredient> todo;
    List<Float> results;

    // null if not at workstation
    Workstation currWorkstation;
    float startedWorkAt;

    Workstation pathFindingTo;
    Point.Float pathFindingTargetPosition;

    @Override
    public void tick(float dt) {
        if (currWorkstation == null) {
            if (pathFindingTargetPosition == null){
                pathFindingTargetPosition = pathFindingTo.getPosition();
                pathFindingTargetPosition.x += pathFindingTo.workingOffset.x;
                pathFindingTargetPosition.y += pathFindingTo.workingOffset.y;
            }

            // we aren't at a station, go to pathFindingTargetPosition
            stepTowardsTarget(dt);
            
            boolean close = position.distance(pathFindingTargetPosition) <= /*TODO exact distance?*/ 0.5f;
            if (close) {
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
                pathFindingTo = Kitchen.findClosestWorkStation(position, todo.get(currIngredient));
            }
        }
    }

    private void stepTowardsTarget(float dt) {
        velocity = pathFindingTargetPosition;
        velocity.x -= position.x;
        velocity.y -= position.y;

        double length = velocity.distance(0, 0);
        velocity.x /= length;
        velocity.y /= length;

        position.x += velocity.x * SPEED * dt;
        position.y += velocity.y * SPEED * dt;
    }

    public void pushResult(float result) {
        results.add(result);
    }


    private Sprite sprite = new Sprite(Game.getImage("chef"));

    @Override
    public void render(Graphics2D gd) {
        super.render(gd);
        sprite.render(gd);
        //System.out.println("chef drawn");
    }
}
