package com.github.migi_1.Context.enemy;

import java.util.ArrayList;
import java.util.LinkedList;

import com.github.migi_1.Context.model.LevelPiece;
import com.github.migi_1.Context.model.entity.Carrier;
import com.github.migi_1.Context.model.entity.Commander;
import com.jme3.bounding.BoundingBox;
import com.jme3.math.Vector3f;

/**
 * Class which handles everything that has to do with spawning enemies. The
 * enemyspawner has an enemyFactory which makes the actual enemies. The spawner
 * is responsible for placing the enemies in the right location.
 *
 * @author TUDelft SID
 *
 */
public class EnemySpawner {

    private static final int MAX_NUM_ENEMIES = 12;
    private EnemyFactory enemyFactory;
    private LinkedList<Enemy> enemies;
    private LinkedList<Enemy> deleteList;
    private double currentLevelPiece;
    private double lastLevelPiece;
    private Vector3f commanderLocation;
    private ArrayList<Carrier> carriers;

    /**
     * Constructor of the EnemySpawner.
     *
     * @param commander
     *            to find the offset of the carriers and therefore the spots
     *            where to spawn the enemies.
     * @param carriers
     *            the carriers which have spots where the enemies will walk to.
     */
    public EnemySpawner(Commander commander, ArrayList<Carrier> carriers) {
        this.carriers = carriers;
        enemies = new LinkedList<Enemy>();
        deleteList = new LinkedList<Enemy>();
        commanderLocation = commander.getModel().getLocalTranslation();
        currentLevelPiece = 0;
        lastLevelPiece = -1;
        enemyFactory = new EnemyFactory(carriers);
    }

    /**
     * Determines how many enemies to spawn and where, returns a list of said
     * enemies. It first calculates the levelPiece the carriage is currently on.
     * It then spawns enemies randomly on both sides of the path, adds them to
     * an empty list and returns it. It also adds enemies to the list of total
     * enemies, which keeps track of all enemies in the game. The maximum amount
     * of enemies in the game is 12.
     *
     * @return list of new enemies to add to the game.
     */
    public LinkedList<Enemy> generateEnemies() {
        currentLevelPiece = -Math.floor(commanderLocation.x
                / ((BoundingBox) (new LevelPiece()).getModel().getWorldBound()).getXExtent());
        LinkedList<Enemy> newEnemies = new LinkedList<Enemy>();
        if ((enemies.size() < MAX_NUM_ENEMIES) && currentLevelPiece != lastLevelPiece) {
            lastLevelPiece = currentLevelPiece;
            double random = Math.random();
            if (random > 0.70 && random < 0.85) {
                newEnemies.add(enemyFactory.createEnemy3(currentLevelPiece));
            } else if (random > 0.85 && random < 0.95) {
                newEnemies.add(enemyFactory.createEnemy1(currentLevelPiece));
                newEnemies.add(enemyFactory.createEnemy3(currentLevelPiece));
            } else if (random > 0.95) {
                newEnemies.add(enemyFactory.createEnemy1(currentLevelPiece));
                newEnemies.add(enemyFactory.createEnemy2(currentLevelPiece));
                newEnemies.add(enemyFactory.createEnemy3(currentLevelPiece));
            }
        }

        enemies.addAll(newEnemies);
        return newEnemies;
    }

    /**
     * Creates a list of enemies to delete. Enemies which need to be deleted are
     * enemies which have no health and enemies which have fallen too far behind
     * the commander.
     *
     * @return List of enemies to delete from the game.
     */
    public LinkedList<Enemy> deleteEnemies() {
        for (Enemy enemy : enemies) {
            if (enemy.getHealth() <= 0) {
                deleteList.add(enemy);
                enemy.getSpot().setOccupied(false);
            }
            if (enemy.getModel().getLocalTranslation().distance(commanderLocation)
                    > ((BoundingBox) (new LevelPiece()).getModel().getWorldBound()).getXExtent() * 3
                    && enemy.getModel().getLocalTranslation().x > commanderLocation.x) {

                deleteList.add(enemy);
                if (enemy.getSpot() != null) {
                    enemy.getSpot().setOccupied(false);
                }
            }
            if (((EnemyMoveBehaviour) enemy.getMoveBehaviour()).getTargetSpot().getCarrier().isDead()) {
                deleteList.add(enemy);
                enemy.getSpot().setOccupied(false);
            }

        }
        enemies.removeAll(deleteList);
        return deleteList;
    }

    /**
     * Return the carriers.
     *
     * @return the carriers the enemySpawner uses.
     */
    public ArrayList<Carrier> getCarriers() {
        return carriers;
    }

    /**
     * Getter for the enemies list.
     * Used in testing.
     * @return the enemies list.
     */
    public LinkedList<Enemy> getEnemies() {
        return enemies;
    }

    /**
     * Setter for the enemies list.
     * Used in testing ONLY.
     * @param newEnemies the new enemies list.
     */
    public void setEnemies(LinkedList<Enemy> newEnemies) {
        enemies = newEnemies;
    }
}
