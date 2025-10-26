package hu.hunjam25.dlhc.model;

import hu.hunjam25.dlhc.*;
import hu.hunjam25.dlhc.sound.SoundBuffer;
import hu.hunjam25.dlhc.view.AnimatedSprite;
import hu.hunjam25.dlhc.view.UiElement;

import javax.sound.sampled.LineUnavailableException;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Arrays;
import java.util.Queue;

import static java.lang.Math.abs;

public class Chef extends GameObject {
    private final static int DEFAULT_FOOD_COUNT = 3;
    private final static float MAX_SPEED = 4f;
    private final static float MIN_SPEED = 0.5f;

    private static int count = 0;

    int workingIdx = 0;

    private SoundBuffer ding;

    float speed = MIN_SPEED;

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

    public Chef(int foodCount) {
        ++count;
        animatedSprite = new AnimatedSprite(AssetManager.getAnim("chef" + ((count % 3) + 1)), 0.5f);
        positionToCenter();
        float maxWidthOffset = Game.MAP_WIDTH / 2.5f;
        this.position = this.position.add(new Vec2(Food.r.nextFloat(-maxWidthOffset, maxWidthOffset), 0));
        foodTodo = new java.util.ArrayDeque<>();
        for (int i = 0; i < foodCount; i++) {
            foodTodo.add(Food.RandomFood());
        }

        animatedSprite.freeze();
        animatedSprite.setScale(0.375f);

        ding = AssetManager.getSound("ready");
        startNewFood();
    }

    private void addRatMeter() {
        float min = -1.0f * (todo.length + 1) / 6.0f;

        UiElement ratTimer = new UiElement();
        ratTimer.visible = true;
        ratTimer.scale = 0.15f;
        AnimatedSprite ratSprite = new AnimatedSprite(AssetManager.getAnim("ratMeter"), 2);
        ratSprite.freeze();
        ratSprite.setIdx(0);
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
        startedCurrentFoodAt = Main.now;
        addRatMeter();
        addClockTimers();
        addIngredientSprites();
    }

    private void addClockTimers() {
        for (int i = 0; i < todo.length; i++) {
            float min = -1.0f * (todo.length + 1) / 6.0f;

            UiElement timr = new UiElement();
            timr.visible = true;
            timr.scale = 0.1f;
            AnimatedSprite timerSprite = new AnimatedSprite(AssetManager.getAnim("clock"),
                    todo[i].durationSeconds * 5f / 4f);
            if (i != 0) {
                timerSprite.freeze();
            } else {
                timerSprite.unFreeze();
            }
            timerSprite.start();
            timr.setAnimatedSprite(timerSprite);
            timr.addOffset(new Vec2(min + (i + 1) * 0.4f, 0.2f));
            addUiElement(timr);
        }
    }

    private void addIngredientSprites() {
        for (int i = 0; i < todo.length; i++) {
            float min = -1.0f * (todo.length + 1) / 6.0f;

            UiElement ing = new UiElement();
            ing.visible = true;
            ing.scale = 0.3f;
            AnimatedSprite ingSprite = new AnimatedSprite(AssetManager.getAnim(todo[i].name().toLowerCase()), 1);
            ing.setAnimatedSprite(ingSprite);
            ing.addOffset(new Vec2(min + (i + 1) * 0.4f, -0.25f));
            addUiElement(ing);
        }
    }

    public Chef() {
        this(DEFAULT_FOOD_COUNT);
    }

    @Override
    public void tick(float dt) {
        if (finished) {
            return;
        }

        if (currWorkstation == null) {
            searching(dt);
        } else {
            working();
        }
    }

    private void working() {
        workingIdx = facing(currWorkstation.workingOffset.mul(-1f));
        this.animatedSprite.setIdx(workingIdx);
        // work at workstation
        if (isJobOver()) {
            finishJob();
        }
    }

    private void finishJob() {
        if (Kitchen.minigame != null && !Kitchen.minigame.isGameEnded()) {
            Kitchen.minigame.endGame();
        }

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
            playDing();
            if (lookForNewJob())
                return;
        }
        pathFindingTo = Kitchen.findClosestFreeWorkStation(position, todo[currIngredient]);
    }

    private void setRatMeter() {
        float countOfSabotages = 0;
        for (float res : results) {
            if (res > 0f)
                countOfSabotages++;
        }
        var as = ((UiElement) this.getUiElement(0)).animatedSprite;
        as.setIdx((int) (countOfSabotages / (results.length + 1) * as.getNumberOfFrames()));
    }

    private void playDing() {
        try {
            ding.play();
        } catch (LineUnavailableException e) {
            System.err.println("No sound");
            throw new RuntimeException(e);
        }
    }

    private boolean lookForNewJob() {
        float ingredientSumTime = 0;
        for (var ing : todo)
            ingredientSumTime += ing.durationSeconds;
        float timeDelay = Main.now - startedCurrentFoodAt - ingredientSumTime;
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
        return Main.now - startedWorkAt >= todo[currIngredient].durationSeconds
                || (!Kitchen.isOnFire && todo[currIngredient] == Ingredient.TrashFire);
    }

    /***
     *
     * @param dt
     */
    private void searching(float dt) {
        if (pathFindingTargetPosition == null) {
            if (pathFindingTo == null) {
                // System.out.println("Nowhere to Go");
                confused = true;
                findClosestWorkStation();
                if (pathFindingTo != null) {
                    confused = false;
                }
                return;
            }
            pathFindingTargetPosition = pathFindingTo.getPosition().add(pathFindingTo.workingOffset);
        }

        boolean arrivedAtStation = stepTowardsTarget(dt);
        if (arrivedAtStation) {
            currWorkstation = pathFindingTo;
            currWorkstation.setWorker(this);
            pathFindingTargetPosition = null;
            pathFindingTo = null;
            startedWorkAt = Main.now;
            speed = (MIN_SPEED * 2f + MAX_SPEED) / 3f;
            startTimer(currIngredient);
        }
    }

    private void startTimer(int id) {
        var as = ((UiElement) this.getUiElement(id + 1)).animatedSprite;
        as.unFreeze();
        as.start();
    }

    private void setTimersAfterTask(int id) {
        for (int i = 1; i < todo.length + 1; i++) {
            var as = ((UiElement) this.getUiElement(i)).animatedSprite;
            as.freeze();
            as.setIdx(id + 1 > i ? 4 : 0);
        }
    }

    /// return true if the chef has arrived at the target
    private boolean stepTowardsTarget(float dt) {
        if (position.dist(pathFindingTargetPosition) < 1f && speed > MIN_SPEED) {
            speed -= 0.15f;
        } else if (speed < MAX_SPEED) {
            speed += 0.15f;
        }
        speed = Math.clamp(speed, MIN_SPEED, MAX_SPEED);
        if (stopped()) {
            speed = MIN_SPEED;
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

        position = position.add(velocity.mul(speed * dt));

        int spriteIdx = facing(velocity);
        if (confused) {
            spriteIdx = 5;
        }
        if (velocity.x() < 0)
            animatedSprite.mirrored = true;
        else
            animatedSprite.mirrored = false;
        animatedSprite.setIdx(spriteIdx);
        // Workstation taken
        if (pathFindingTo.hasWorker()) {
            findClosestWorkStation();
            return false;
        }

        return length <= 0.1f;
    }

    private void findClosestWorkStation() {
        pathFindingTo = Kitchen.findClosestFreeWorkStation(position, todo[currIngredient]);
        pathFindingTargetPosition = null;
    }

    public void stopForDuration(float seconds) {
        stoppedUntil = Main.now + seconds;
    }

    private boolean stopped() {
        return Main.now < stoppedUntil;
    }

    /**
     * add result handling for each ingredient, should be called by the minigame
     * when it ends
     * 
     * @param result should be in {@code[0;0.10]} with the highend being rare
     */
    public void pushResult(float result) {
        results[currIngredient] += result;
        setRatMeter();
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
