package com.github.migi_1.Context.damageDealers;

import com.github.migi_1.Context.model.entity.IMovable;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 * This interface provides some methods that all DamageDealers must declare.
 * @author Marcel
 *
 */
public interface DamageDealer extends IMovable {

    /**
     * Return the model of the object.
     * @return the model
     */
    @Override
    Spatial getModel();

    /**
     * Set the physical model of the object.
     * @param model the physical model
     */
    @Override
    void setModel(Spatial model);

    /**
     * Move the object.
     * @param x Translation in x direction
     * @param y Translation in y direction
     * @param z Translation in z direction
     */
    void move(float x, float y, float z);

    /**
     * Scale the object.
     * @param f scale factor
     */
    @Override
    void scale(float f);

    /**
     * Move the object.
     * @param add Translation vector
     */
    @Override
    void move(Vector3f add);

    /**
     * Handle collisions between the model of the damage dealer and the platform.
     * @param platform The platform
     * @param results Collision results
     */
    void collideWith(Spatial platform, CollisionResults results);

}
