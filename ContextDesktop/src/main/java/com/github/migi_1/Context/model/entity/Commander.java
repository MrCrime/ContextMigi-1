package com.github.migi_1.Context.model.entity;

import com.github.migi_1.Context.utility.ProjectAssetManager;
import com.jme3.collision.Collidable;
import com.jme3.collision.CollisionResults;
import com.jme3.collision.UnsupportedCollisionException;
import com.jme3.math.Vector3f;

/**
 * Class that handles everything that have to do with the commander.
 * It implements collidable to register collisions.
 * It implements IMovable to register a movable behaviour. A movable behaviour
 * Sets how the commander moves after every tick.
 * @author Damian
 *
 */
public class Commander extends Entity implements Collidable {

    //String of the path to the commander model
    private static final String PATHNAME = "Models/ninja.j3o";

    /**
     * Constructor of the commander.
     * @param startLocation startLocation location where the Commander will be initialized
     */
    public Commander(Vector3f startLocation) {
        setModel(ProjectAssetManager.getInstance().getAssetManager().loadModel(PATHNAME));
        getModel().setLocalTranslation(startLocation);
        setMoveBehaviour(new AcceleratorMoveBehaviour());
    }


    /**
     * Method that registers a collision.
     * @param arg0 Collidable to check the collision against.
     * @param arg1 The register to where the collision results are added to.
     * @return whether the collision was succesul or not, 0 means succesful.
     * @throws UnsupportedCollisionException When the collision is unsupported by the jmonkey collision checker.
     */
    @Override
    public int collideWith(Collidable arg0, CollisionResults arg1)
            throws UnsupportedCollisionException {
        getModel().collideWith(arg0, arg1);
        return 0;
    }

}
