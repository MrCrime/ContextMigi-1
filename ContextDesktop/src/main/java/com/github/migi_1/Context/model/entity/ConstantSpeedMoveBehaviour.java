package com.github.migi_1.Context.model.entity;

import com.jme3.math.Vector3f;
/**
 * MoveBehaviour for moving at a constant speed at a certain direction.
 * @author Damian
 *
 */
public class ConstantSpeedMoveBehaviour extends MoveBehaviour {

	private Vector3f moveVector;

	private float decay;

	/**
	 * Constructor for ConstantSpeedMoveBehaviour.
	 *
	 * @param moveVector
	 * 		The direction and speed of this behaviour.
	 */
	public ConstantSpeedMoveBehaviour(Vector3f moveVector) {
		this.moveVector = moveVector;
		this.decay = 1.0f;
	}

	/**
	 * Returns the move vector.
	 * @return the moveVector.
	 */
    @Override
    public Vector3f getMoveVector() {
        updateMoveVector();
        return moveVector.mult(decay);
    }

    /**
     * Collision has taken place.
     */
    public void collided() {

        decay = 0.0f;
    }

    /**
     * MoveVector is updated.
     */
    @Override
    public void updateMoveVector() {

        // regain speed
        if (decay < 1.0f) {
            decay = decay + 0.01f;
        }
        else {
            decay = 1.0f;
        }

    }

    /**
     * Return the decay term.
     * @return decay
     */
    public float getDecay() {
        return decay;
    }

    /**
     * Set the decay term.
     * @param decay Term to set
     */
    public void setDecay(float decay) {
        this.decay = decay;
    }
}
