package com.github.migi_1.ContextApp;

import com.github.migi_1.ContextMessages.AccelerometerMessage;
import java.io.IOException;

import com.jme3.network.Client;
import com.jme3.network.Network;
import com.jme3.network.serializing.Serializer;

/**
 * A wrapper class for a com.jme3.network.client object.
 * 
 * SINGLETON class.
 */
public class ClientWrapper {
    
	/** The default port on which servers are running. */
	private static final int PORT = 4321;
        /** The amount of times the client should restart before sending an error.*/
        private static final int RESTART_ATTEMPTS = 10;
	
	/** The wrapped client Object. */
	private Client client;
        
	//Every message types is registered by the Serializer in this class initializer.
        static {
            Serializer.registerClass(AccelerometerMessage.class);
        }
	
	public ClientWrapper() {
            this.client = createClient(PORT, RESTART_ATTEMPTS);
        }
        
        private Client createClient(int port, int restartAttempts) {
            return null;
        }
	
	/**
	 * Gets the Client.
	 * 
	 * @return
	 *      The wrapped Client object. Null if the Client isn't running.
	 */
	public Client getClient() {
            return client;
	}
}
