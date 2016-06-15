package com.github.migi_1.ContextApp.BugEvent;

import android.util.Log;
import com.github.migi_1.ContextMessages.EnableSprayToAppMessage;
import com.github.migi_1.ContextMessages.MessageListener;

/**
 * Message handler for the enable spray message.
 * Receives EnableSprayToApp messages send to client (app).
 */
public class EnableSprayAppMessageHandler extends MessageListener<EnableSprayToAppMessage> {

    private RotateBugSprayActivity bugActivity;

    /**
     * Constructor for the EnableSprayAppMessageHandler.
     * @param activity the rotateBugSprayActivity,
     *          which is responsible for the bugEvent.
     */
    public EnableSprayAppMessageHandler(RotateBugSprayActivity activity) {
        this.bugActivity = activity;
        //Add message listener to the client in the activity.
        bugActivity.getClient().getClient().addMessageListener(this);
    }

    @Override
    public void messageReceived(Object source, EnableSprayToAppMessage message) {
        Log.d("rotate", "MESSAGE POS: " + message.getPosition().ordinal());
        Log.d("rotate", "BUG ACT POS: " + bugActivity.getPosition().ordinal());
        Log.d("rotate", "EQUAL? " + (message.getPosition().ordinal() == bugActivity.getPosition().ordinal()));
        if (message.getPosition().ordinal() == bugActivity.getPosition().ordinal()) {
            //Enable the spray for the position send in the message.
            Log.d("rotate", "ENABLING SPRAY BUTTON");
            bugActivity.enableSprayButton();
        }
    }

    @Override
    public Class<EnableSprayToAppMessage> getMessageClass() {
        return EnableSprayToAppMessage.class;
    }

}
