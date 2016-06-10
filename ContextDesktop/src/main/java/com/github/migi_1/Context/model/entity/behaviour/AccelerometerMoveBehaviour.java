package com.github.migi_1.Context.model.entity.behaviour;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javafx.concurrent.Task;

import com.github.migi_1.Context.main.Main;
import com.github.migi_1.Context.utility.Filter;
import com.github.migi_1.ContextMessages.AccelerometerMessage;
import com.jme3.math.Vector3f;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

/**
 * A Movebehaviour that listens to AccelerometerMessages.
 */
@SuppressWarnings("rawtypes")
//The reason this is implements the networking listener rather than the MessageListener in
//the Messages module, is because it's an abstract class (blame java 1.7), but this class
//already extends MoveBehaviour.
public class AccelerometerMoveBehaviour extends MoveBehaviour implements MessageListener {

    /** The factor for the accelerometer force for deciding the speed.*/
    private static final float FACTOR = -0.1f;
    private static final float MAX_SPEED = 1.0f;

    private Filter<String> ipFilter;
    private Timer timer;
    private boolean ducking;
    private boolean jumping;
    private boolean busy;
    /**
     * Constructor for AccelerometerMoveBehaviour.
     * Also automatically registers this behaviour to the server.
     *
     * @param ipFilter
     * 		A filter for ip addresses, so that certain ip addresses can
     * 		be ignored.
     */
    @SuppressWarnings("unchecked")
    public AccelerometerMoveBehaviour(Filter<String> ipFilter) {
        this.ipFilter = ipFilter;
        timer = new Timer();

        Main.getInstance().getServer().getServer().addMessageListener(this);
    }

    /**
     * Called when the server receives any message.
     */
    @Override
    public void messageReceived(Object source, Message m) {
        if (m instanceof AccelerometerMessage) { //Check that it's an AccelerometerMessage
            AccelerometerMessage message = (AccelerometerMessage) m;

            if (ipFilter.filter(((HostedConnection) source).getAddress())) { //Check if the filter allows this address
                checkForJump(message);


                float zSpeed = message.getY_force(); //Y on the gyroscope is Z on JMonkey
                zSpeed *= FACTOR;
                zSpeed = Math.min(zSpeed, MAX_SPEED);
                zSpeed = Math.max(zSpeed, -MAX_SPEED);

                super.setMoveVector(new Vector3f(0, 0, zSpeed));
            }
        }
    }

    private void checkForJump(AccelerometerMessage message) {
        if (message.getZ_force() < -10 && !busy) {
            busy = true;
            timer = new Timer();
            System.out.println("duck!");

            timer.schedule(new BusyTask(), 1000);

        } else if (message.getZ_force() > 17 && !busy) {
            busy = true;
            timer = new Timer();
            System.out.println("jump!"); 

            timer.schedule(new BusyTask(), 1000);

        } 
    }

    class BusyTask extends TimerTask {     

        public void run() {
            busy = false;
            timer.cancel();
        }
    }

    @Override
    public void updateMoveVector() {
        // TODO Auto-generated method stub

    }




}
