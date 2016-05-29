package com.github.migi_1.Context.model.entity;

import com.jme3.math.Vector3f;

public class CarrierMoveBehaviour extends MoveBehaviour {

    private Vector3f moveVector;

    private int immobalized;

    private Commander commander;

    private Boolean catchUp;

    private Carrier carrier;

    private Vector3f relativeLocation;

    public CarrierMoveBehaviour(Vector3f moveVector, Carrier carrier) {
        this.moveVector = moveVector;
        this.immobalized = 0;
        this.carrier = carrier;
        this.catchUp = false;
    }

    public void collided() {
        immobalized = 60;
        catchUp = true;
    }

    @Override
    public Vector3f getMoveVector() {
        updateMoveVector();
        if (immobalized > 0) {
            return new Vector3f(0, 0, 0);
        }
        if (catchUp) {
            return moveVector.mult(4.0f);
        }
        return moveVector;
    }

    @Override
    public void updateMoveVector() {
        if (immobalized > 0) {
            immobalized -= 1;
        }
        Vector3f destination = commander.getModel().getLocalTranslation().add(relativeLocation);

        if (carrier.getModel().getLocalTranslation().x < destination.x) {
            catchUp = false;
            carrier.getModel().setLocalTranslation(destination);
        }

    }

    public void setCommander(Commander commander) {
        this.commander = commander;
    }

    public void setRelativeLocation(Vector3f relativeLocation) {
        this.relativeLocation = relativeLocation;

    }

}
