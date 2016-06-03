package com.github.migi_1.Context.model.entity.behaviour;

import com.jme3.math.Vector3f;

/**
 * A MultiMoveBehaviour that sums of the move vectors of all the
 * sub behaviours it's composed of.
 */
public class SumMultiMoveBehaviour extends MultiMoveBehaviour {

	/**
	 * Constructor for SumMultiMoveBehaviour.
	 * Requires at least one MoveBehaviour.
	 * 
	 * @param behaviour1
	 * 		One of the behaviour this MultiMoveBehaviour is composed of.
	 * @param behaviour2
	 * 		The rest of the behaviours this MultiMoveBehaviour is composed of.
	 */
	public SumMultiMoveBehaviour(MoveBehaviour behaviour1, MoveBehaviour... behaviour2) {
		super(behaviour1, behaviour2);
	}

	/**
	 * Calculates the move vector of this behaviour. It's the sum
	 * of all the move vectors of the sub behaviours.
	 * 
	 * @return
	 * 		The move vector of this behaviour.
	 */
	@Override
	public Vector3f getMoveVector() {
		Vector3f result = Vector3f.ZERO;
		
		for (MoveBehaviour behaviour : super.behaviours) {
			result = result.add(behaviour.getMoveVector());
		}
		
		return result;
	}

}
