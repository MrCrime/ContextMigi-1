package com.github.migi_1.context.networking.clientside;

import java.net.InetAddress;

/**
 * Used by the ServerFinder class to notify a client that was looking for servers.
 */
public interface ServerDiscoveryHandler {

	/**
	 * What happens when a server has been discovered.
	 * 
	 * @param server
	 * 		The address of the server found.
	 */
	public void onServerDiscovery(InetAddress server);
	
}
