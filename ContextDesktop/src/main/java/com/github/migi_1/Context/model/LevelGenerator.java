package com.github.migi_1.Context.model;

import java.util.LinkedList;

import com.jme3.bounding.BoundingBox;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

/**
 * Class responsible for generating and deleting the levelpieces
 * at the correct time.
 * @author Marcel
 *
 */
public class LevelGenerator {

    private static final int LEVEL_PIECES = 15;

    private LinkedList<LevelPiece> levelPieces;
    private LinkedList<Path> pathPieces;

    private Vector3f locationNextPiece;
    private Vector3f locationNextPath;

    /**
     * Constructor of the levelGenerator.
     * @param locationNextPiece location of the next piece to be placed.
     */
    public LevelGenerator(Vector3f locationNextPiece) {
       levelPieces = new LinkedList<LevelPiece>();
       pathPieces = new LinkedList<Path>();
       this.locationNextPiece = locationNextPiece.clone();
       this.locationNextPath = locationNextPiece.clone();
    }

    /**
     * Returns the levelpieces to add and which location to place them at.
     * @param commanderLocation to check if new pieces need to be added.
     * @return the list of pieces to be added.
     */
    public LinkedList<LevelPiece> getLevelPieces(Vector3f commanderLocation) {
        while (levelPieces.size() < LEVEL_PIECES) {
            LevelPiece levelPiece = new LevelPiece();
            levelPiece.move(locationNextPiece);
            levelPieces.add(levelPiece);
            BoundingBox bb = (BoundingBox) levelPiece.getModel().getWorldBound();
            //shift orientation to where the next level piece should spawn
            locationNextPiece.x -= 2 * bb.getXExtent() - 3.1f;
        }
        return levelPieces;
    }

    /**
     * Checks how far away the player is from the last piece, it checks which pieces are
     * ready to be deleted and adds them to the delete list.
     * @param commanderLocation to check how far away the commander is from the last platform.
     * @return the list of pieces to delete.
     */
    public LinkedList<LevelPiece> deleteLevelPieces(Vector3f commanderLocation) {
        LinkedList<LevelPiece> deleteList = new LinkedList<LevelPiece>();
        Boolean done = false;
        while (!done) {
            if (levelPieces.size() > 0) {
                LevelPiece checkLevelPiece = levelPieces.peek();
                Vector3f v = checkLevelPiece.getModel().getLocalTranslation();
                Vector2f v1 = new Vector2f(v.getX(), v.getY());
                Vector2f v2 = new Vector2f(commanderLocation.x, commanderLocation.y);
                if (v1.distance(v2) > 500) {
                   deleteList.add(levelPieces.poll());
                }
                else {
                    done = true;
                }
           }
            else {
                done = true;
            }
        }

       return deleteList;
    }

    /**
     * Returns the levelpieces to add and which location to place them at.
     * @param commanderLocation to check if new pieces need to be added.
     * @return the list of pieces to be added.
     */
    public LinkedList<Path> getPathPieces(Vector3f commanderLocation) {
        while (pathPieces.size() < LEVEL_PIECES) {
            Path path = new Path();
            path.move(locationNextPath);
            pathPieces.add(path);
            BoundingBox bb = (BoundingBox) path.getModel().getWorldBound();
            //shift orientation to where the next level piece should spawn
            locationNextPath.x -= 2 * bb.getXExtent() - 2.0f;
        }
        return pathPieces;
    }

    /**
     * Checks how far away the player is from the last piece, it checks which pieces are
     * ready to be deleted and adds them to the delete list.
     * @param commanderLocation to check how far away the commander is from the last platform.
     * @return the list of pieces to delete.
     */
    public LinkedList<Path> deletePathPieces(Vector3f commanderLocation) {
        LinkedList<Path> deleteList = new LinkedList<Path>();
        Boolean done = false;
        while (!done) {
            if (pathPieces.size() > 0) {
                Path path = pathPieces.peek();
                Vector3f v = path.getModel().getLocalTranslation();
                Vector2f v1 = new Vector2f(v.getX(), v.getY());
                Vector2f v2 = new Vector2f(commanderLocation.x, commanderLocation.y);
                if (v1.distance(v2) > 500) {
                   deleteList.add(pathPieces.poll());
                }
                else {
                    done = true;
                }
           }
            else {
                done = true;
            }
        }

       return deleteList;
    }

    /**
     * Return the number of LevelPiece objects.
     * @return Value of LEVEL_PIECES attribute
     */
    public int getNumberOfLevelPieces() {
        return LEVEL_PIECES;
    }

    /**
     * Set level pieces linked list.
     * Used in testing.
     * @param levelPieces the new levelPieces linked list.
     */
    public void setLevelPieces(LinkedList<LevelPiece> levelPieces) {
        this.levelPieces = levelPieces;
    }

    /**
     * Set path pieces linked list.
     * Used in testing.
     * @param pathList the new pathPieces linked list.
     */
    public void setPathPieces(LinkedList<Path> pathList) {
        this.pathPieces = pathList;

    }

}
