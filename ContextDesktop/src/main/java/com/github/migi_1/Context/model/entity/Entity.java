package com.github.migi_1.Context.model.entity;

import com.jme3.scene.Spatial;

/**
 * Represents an object that can be placed in an Environment. 
 *
 * @author Marcel
 *
 */
public abstract class Entity implements IMovable {

    private Spatial model;
    /** Move behaviour of particular Entity. */
    private MoveBehaviour moveBehaviour;

    /**
     * Constructor for abstract Entity where the default behaviour
     * is a StaticMoveBehaviour.
     */
    public Entity() {
        moveBehaviour = new StaticMoveBehaviour();
        this.model = getDefaultModel();
    }

    /**
     * Gets the MoveBehaviour of the Entity.
     * @return MoveBehaviour
     */
    @Override
    public MoveBehaviour getMoveBehaviour() {
        return moveBehaviour;
    }

    /**
     * Sets the MoveBehaviour of the Entity.
     * @param moveBehaviour MoveBehaviour
     */
    @Override
    public void setMoveBehaviour(MoveBehaviour moveBehaviour) {
        this.moveBehaviour = moveBehaviour;
    }

    /**
     * Gets the model of the Entity.
     */
    @Override
    public Spatial getModel() {
        return model;
    }

    /**
     * Sets the model of the Entity.
     */
    @Override
    public void setModel(Spatial model) {
        this.model = model;

    }

    /**
     * Get the default model for a particular instance.
     * @return defaultModel
     */
    public abstract Spatial getDefaultModel();
}
