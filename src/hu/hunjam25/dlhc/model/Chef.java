package hu.hunjam25.dlhc.model;

import hu.hunjam25.dlhc.*;
import hu.hunjam25.dlhc.view.AnimatedSprite;
import hu.hunjam25.dlhc.view.UiElement;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Arrays;
import java.util.Queue;

import static java.lang.Math.abs;

public class Chef extends GameObject {
    private final static int DEFAULT_FOOD_COUNT = 3;
    private final static float SPEED = 0.005f;

    private static int count = 0;
    private int workingIdx = 0;

    public Chef(int foodCount) {
        ++count;
        animatedSprite = new AnimatedSprite(AssetManager.getAnim("chef" + count), 0.5f);
        positionToCenter();
        foodTodo = new java.util.ArrayDeque<>();
        for (int i = 0; i < foodCount; i++) {
            foodTodo.add(Food.RandomFood());
        }

        animatedSprite.frozen = true;
        animatedSprite.setScale(0.375f);

        // TODO: real stuff here
        startNewFood();
        addRatMeter();
    }

    private void addRatMeter() {
        float min = -1.0f * (todo.length + 1) / 6.0f;

        UiElement ratTimer = new UiElement();
        ratTimer.visible = true;
        ratTimer.scale = 0.15f;
        AnimatedSprite ratSprite = new AnimatedSprite(AssetManager.getAnim("ratMeter"), 2);
        ratSprite.frozen = true;
        ratTimer.setAnimatedSprite(ratSprite);
        ratTimer.addOffset(new Vec2(min, 0.0f));
        addUiElement(ratTimer);
    }

    private void startNewFood() {
        this.clearUiElements();

        currentFood = foodTodo.remove();
        currIngredient = 0;
        todo = currentFood.ingredients;
        results = new float[todo.length];
        Arrays.fill(results, 0f);
        pathFindingTo = Kitchen.findClosestFreeWorkStation(position, todo[currIngredient]);
        startedCurrentFoodAt = Game.now;
        addRatMeter();
        addClockTimers();
    }

    private void addClockTimers() {
        for (int i = 0; i < todo.length; i++) {
            float min = -1.0f * (todo.length + 1) / 6.0f;

            UiElement timr = new UiElement();
            timr.visible = true;
            timr.scale = 0.1f;
            AnimatedSprite timerSprite = new AnimatedSprite(AssetManager.getAnim("clock"),
                    todo[i].durationSeconds * 5f / 4f);
            timerSprite.frozen = i == 0 ? false : true;
            timerSprite.start();
            timr.setAnimatedSprite(timerSprite);
            timr.addOffset(new Vec2(min + (i + 1) * 0.4f, 0.0f));
            addUiElement(timr);
        }
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
    Vec2 pathFindingTargetPosition;

    float stoppedUntil = 0f;

    Ingredient tempIfOnFire = null;

    public boolean finished = false;

    @Override
    public void tick(float dt) {
        if (finished) {
            return; // TODO: valami dispose
        }

        if (currWorkstation == null) {
            if(!searching(dt)){
                return;
            }
        }
        else {
            working();
        }
    }

    private void working() {
        workingIdx = facing(currWorkstation.workingOffset.mul(-1f) );
        animatedSprite.setIdx(workingIdx);
        // work at workstation
        if (isJobOver()) {
            finishJob();
        }
    }

    private void finishJob() {
        currWorkstation.setWorker(null);
        currWorkstation = null;

        if (!Kitchen.isOnFire) {
            currIngredient++;
            setTimersAfterTask(currIngredient);
        } else {
            Kitchen.isOnFire = false;
            fixTempIngredient();
        }

        // food finished
        if (currIngredient >= todo.length) {
            if (lookForNewJob()) return;
        }
        pathFindingTo = Kitchen.findClosestFreeWorkStation(position, todo[currIngredient]);
    }

    private boolean lookForNewJob() {
        float ingredientSumTime = 0;
        for (var ing : todo)
            ingredientSumTime += ing.durationSeconds;
        float timeDelay = Game.now - startedCurrentFoodAt - ingredientSumTime;
        Kitchen.decreaseRating(results, timeDelay);

        if (foodTodo.isEmpty()) {
            currIngredient = 0;
            finished = true;
            return true;
        }
        startNewFood();
        return false;
    }

    private boolean isJobOver() {
        return Game.now - startedWorkAt >= todo[currIngredient].durationSeconds
                || (!Kitchen.isOnFire && todo[currIngredient] == Ingredient.TrashFire);
    }

    /***
     *
     * @param dt
     * @return true if continue tick(), false if return
     */
    private boolean searching(float dt) {
        if (pathFindingTargetPosition == null) {
            if (pathFindingTo == null){
                System.out.println("Nowhere to Go");
                return false;
            }
            pathFindingTargetPosition = pathFindingTo.getPosition().add(pathFindingTo.workingOffset);
        }

        boolean arrivedAtStation = stepTowardsTarget(dt);
        if (arrivedAtStation) {
            currWorkstation = pathFindingTo;
            currWorkstation.setWorker(this);
            position = pathFindingTargetPosition;
            pathFindingTargetPosition = null;
            pathFindingTo = null;
            startedWorkAt = Game.now;
            startTimer(currIngredient);
        }
        return true;
    }

    private void startTimer(int id) {
        var as = ((UiElement) this.getUiElement(id + 1)).animatedSprite;
        as.frozen = false;
        as.start();
    }

    private void setTimersAfterTask(int id) {
        for (int i = 1; i < todo.length + 1; i++) {
            var as = ((UiElement) this.getUiElement(i)).animatedSprite;
            as.frozen = true;
            as.setIdx(id + 1 > i ? 4 : 0);
        }
    }

    /// return true if the chef has arrived at the target
    private boolean stepTowardsTarget(float dt) {
        if (stopped()) {
            animatedSprite.setIdx(0);
            return false;
        }
        Vec2 velocity = pathFindingTargetPosition.sub(position);

        float length = velocity.length();
        if (length == 0) {
            animatedSprite.setIdx(0);
            return true;
        }

        velocity = velocity.div(length);

        position = position.add(velocity.mul(SPEED * dt));

        int spriteIdx = facing(velocity);
        if (confused)
            spriteIdx = spriteIdx + 3;
        if (velocity.x() < 0)
            animatedSprite.mirrored = true;
        else
            animatedSprite.mirrored = false;
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

    private AnimatedSprite animatedSprite;
    private boolean confused = false;

    @Override
    public void render(Graphics2D gd) {
        super.render(gd);
        AffineTransform t = gd.getTransform();
        animatedSprite.render(gd);
        gd.setTransform(t);
        renderUiElements(gd);
    }

    private static int facing(Vec2 vel) { // fentről óramutatóval 4 forgási fázis indexe
        if (vel.x() == 0.0f && vel.y() == 0.0f) {
            return 0;
        }
        if (vel.y() > 0.0f && abs(vel.y()) > abs(vel.x())) {
            return 0;
        } // felfele gyorsabb -> 0. index
        if (vel.y() < 0.0f && abs(vel.y()) > abs(vel.x())) {
            return 2;
        } // lefele gyorsbb -> 2. index
          // if (vel.x > 0f && abs(vel.y) < abs(vel.x)) {return 1;} //jobbra gyorsabb ->
          // 1. index
        return 1; // egyébként 3. index
    }

    public void addHazard(Workstation trash, Ingredient fire) {
        currWorkstation.setWorker(null);
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
