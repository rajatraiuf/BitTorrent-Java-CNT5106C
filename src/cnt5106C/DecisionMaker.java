/**
 * The real Control Thread, running in parallel, used to decide who to choke, who to unchoke and stuffs like that.
 */

package cnt5106C;

import java.time.LocalTime;
import java.util.*;

import java.util.Timer; 
import java.util.TimerTask;

public class DecisionMaker extends Thread{
	public List<Integer> preferredPeers = Collections.synchronizedList(new ArrayList<Integer>()); //An array that maintains preferred peerInfos.
	
	/**
	 * Update preferred peers
	 */
	private class updatePreferredPeers extends TimerTask 
	{ 
		public void run() 
		{ 
			if (ControlSystem.peers.size()>0)
			{
				preferredPeers.clear();
			}
			while(preferredPeers.size()<ControlSystem.preferredNeighborsCount){
				int index = (int)(Math.random() * ControlSystem.peers.size()); 
				// System.out.println("Picking random peer at index"+String.valueOf(index));
				// System.out.println("Picking random peer at index"+String.valueOf(index));
				if(index != ControlSystem.index && !preferredPeers.contains(index))
					preferredPeers.add(index);
			}
			System.out.println("Updated " +String.valueOf(ControlSystem.peerId) +" preferred peers to :"+ preferredPeers);
		} 
		
	} 	  

	/**
	 * Optimistically unchoke a peer  
	 */
	private class optimisiticUnchoke extends TimerTask 
	{ 
		public void run() 
		{	if(preferredPeers.size()<=ControlSystem.preferredNeighborsCount){
				while(true){
					int index = (int)(Math.random() * ControlSystem.peers.size()); 
					if(index != ControlSystem.index && !preferredPeers.contains(index))
					{
						preferredPeers.add(index);
						break;
					}
				}
				System.out.println("Optimistic Updated " +String.valueOf(ControlSystem.peerId) +" preferred peers to :"+ preferredPeers);
			}
		} 
		
	} 
	  
	/**
	 *Main method of decision maker as a thread.
	 */
	
	public void run() {
		Timer timerUpdate = new Timer();//create a new Timer
		Timer timerOptUpdate = new Timer();
		TimerTask task1 = new optimisiticUnchoke();
		TimerTask task2 = new updatePreferredPeers();
		timerUpdate.schedule(task1,1000,5000);
		timerOptUpdate.schedule(task2,6000, 10000);
		while(true) {
			/*
			 * The code bellow is only for testing.
			 * The decisionMaker is trying to send a "Hi" message to each connected peer every 5 seconds.
			 */
			try {
				for(Integer p: preferredPeers) {
					if(ControlSystem.peers.get(p).isConnected) {
						// System.out.println("CONNECTED HERE");
						String debugMsg = "Hi, i'm PP peer " + ControlSystem.peerId + ", it's " + LocalTime.now();
						ControlSystem.messageQueues.get(p).put(
							Message.actualMessageWrapper(p , 8,debugMsg.getBytes()));
					}
					else{
						// System.out.println("NOT CONNECTED");

					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
