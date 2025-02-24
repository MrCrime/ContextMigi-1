package com.github.migi_1.Context.model.entity.behaviour;

import java.util.ArrayList;
import java.util.Collection;

import com.github.migi_1.Context.utility.DistanceVectorAggregator;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

/**
 * A RotateBehaviour specially designed for a Platform.
 * It uses the similarity of the platform's AccelerometerMoveBehaviours used for steering,
 * to determine how badly the Platform is rotating.
 */
public class PlatformRotateBehaviour extends RotateBehaviour {

	private Collection<AccelerometerMoveBehaviour> carrierBehaviours;
	private final Quaternion initialRotation;
	private Quaternion rotation;

	private final float baseAmplitude = 0.01f;
	private final int speed = 200;
	private float disSimilarity = 0.0f;
	private float time = 0f;

	/**
	 * Constructor for PlatformRotateBehaviour.
	 *
	 * @param carrierBehaviours
	 * 		The AccelerometerMoveBehaviours used by the Platform for steering.
	 * @param initialRotation
	 * 		The current rotation of the platform.
	 */
	public PlatformRotateBehaviour(
			Collection<AccelerometerMoveBehaviour> carrierBehaviours,
			Quaternion initialRotation) {

		super();
		this.carrierBehaviours = carrierBehaviours;
		this.initialRotation = new Quaternion(initialRotation);
		this.rotation = initialRotation;
	}

	@Override
	public void updateRotateVector() {
		if (time >= 2 * Math.PI) {
			//After 2PI, the platform has made extra one succesful rotation,

			time = 0; //reset time
			rotation.set(initialRotation); //reset initial location to prevent floating point errors
			disSimilarity = getDisSimilarity().z; //Update the dissimilarity
		} else {
			time += (float) (Math.PI / speed);

			float amplitude = (baseAmplitude * disSimilarity);

			Vector3f res = new Vector3f(
					//Using cos for the proper rotation, the amplitude is how hard the platform
					//is rotating. For the x-rotation, we use time*2 as it seemed to give a nicer effect.
					(float) (Math.cos(time) * amplitude),
					0.0f,
					0.0f
				);

			super.setRotateVector(res);
		}
	}

	/**
	 * Gets the dissimilarity of the accelerometer using a DistanceVectorAggregator.
	 *
	 * @return
	 * 		The calculated dissimilarity.
	 */
	private Vector3f getDisSimilarity() {
		DistanceVectorAggregator aggregator = new DistanceVectorAggregator();

		Collection<Vector3f> vectors = new ArrayList<>(carrierBehaviours.size());

		for (MoveBehaviour behaviour : carrierBehaviours) {
			vectors.add(behaviour.getMoveVector());
		}

		return aggregator.aggregate(vectors);
	}

	/**
	 * Setter for the time attribute.
	 * @param newTime the new time.
	 */
	public void setTime(float newTime) {
	    time = newTime;
	}

	/**
	 * Getter for the dissimilarity.
	 * @return the dissimilarity.
	 */
	public float getDisSimilarityAttribute() {
	    return disSimilarity;
	}

}
