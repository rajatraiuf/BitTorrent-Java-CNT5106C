/**
 * The real Control Thread, running in parallel, used to decide who to choke, who to unchoke and stuffs like that.
 */

package cnt5106C;

import java.time.LocalTime;
import java.util.*;

import javax.naming.ldap.Control;

public class DecisionMaker extends Thread{
	public ArrayList<DynamicPeerInfo> preferredPeers = new ArrayList<DynamicPeerInfo>(); //An array that maintains preferred peerInfos.
	/**
	 * Update preferred peers
	 */
	private void updatePreferredPeers() {
		if (ControlSystem.peers.size()>0)
		{
			preferredPeers.clear();
		}
		while(preferredPeers.size()<ControlSystem.preferredNeighborsCount){
			int index = (int)(Math.random() * ControlSystem.peers.size()); 
			// System.out.println("Picking random peer at index"+String.valueOf(index));
			if(ControlSystem.peers.get(index).peerId != ControlSystem.peerId
			&& preferredPeers.size()<ControlSystem.preferredNeighborsCount && !preferredPeers.contains(ControlSystem.peers.get(index)))
				preferredPeers.add(ControlSystem.peers.get(index));
		}
		System.out.println("Updated " +String.valueOf(ControlSystem.peerId) +" preferred peers to :"+ preferredPeers);
	}
	/**
	 *Main method of decision maker as a thread.
	 */
	
	public void run() {
		while(true) {
			/*
			 * The code bellow is only for testing.
			 * The decisionMaker is trying to send a "Hi" message to each connected peer every 5 seconds.
			 */
			try {
				updatePreferredPeers();
				for(DynamicPeerInfo p: preferredPeers) {
					if(p.isConnected) {
						// System.out.println("CONNECTED HERE");
						ControlSystem.messageQueues.get(p.index).put(
							new Message(
									("Hi, i'm PP peer " + ControlSystem.peerId + ", it's " + LocalTime.now()).getBytes()
									, p.index
									, false)
							);
					}
					else{
						// System.out.println("NOT CONNECTED");

					}
				}
				sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
