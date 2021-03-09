/**
 * The real Control Thread, running in parallel, used to decide who to choke, who to unchoke and stuffs like that.
 */

package cnt5106C;

import java.time.LocalTime;
import java.util.*;

import cnt5106C.Message.*;

public class DecisionMaker extends Thread{
	public Set<Integer> preferredPeers = Collections.synchronizedSet(new HashSet<Integer>()); //An array that maintains preferred peerInfos.
	/**
	 * Update preferred peers
	 */
	private class updatePreferredPeers extends TimerTask 
	{ 
		public void run() 
		{ 
			if (PeerProcess.peers.size()>0)
			{
				preferredPeers.clear();
			}
			if(preferredPeers.size()<PeerProcess.preferredNeighborsCount){
				int index = (int)(Math.random() * PeerProcess.peers.size()); 
				// System.out.println("Picking random peer at index"+String.valueOf(index));
				if(index != PeerProcess.index && !preferredPeers.contains(index) && PeerProcess.peers.get(index).isInterested )
					preferredPeers.add(index);
			}
			//iterate over all peers to check and send proper choke/unchoke msgs
			for(DynamicPeerInfo p :PeerProcess.peers) {
				if(p.isConnected ) {
					if(preferredPeers.contains(p.index) && p.isChoked){
						PeerProcess.messageQueues.get(p.index).add(Message.actualMessageWrapper(p.index , 1,new byte[0]));
						p.isChoked=false;
					}
					else if(!p.isChoked){
						PeerProcess.messageQueues.get(p.index).add(Message.actualMessageWrapper(p.index , 0,new byte[0]));
						p.isChoked=true;
					} 
				}
			}
			System.out.println("Updated " +String.valueOf(PeerProcess.peerId) +" preferred peers to :"+ preferredPeers);
		} 
		
	} 	  

	/**
	 * Optimistically unchoke a peer  
	 */
	private class optimisiticUnchoke extends TimerTask 
	{ 
		public void run() 
		{	if(preferredPeers.size()<=PeerProcess.preferredNeighborsCount){
				// while(true){
				int index = (int)(Math.random() * PeerProcess.peers.size()); 
				if(index != PeerProcess.index && !preferredPeers.contains(index) && PeerProcess.peers.get(index).isInterested){
					preferredPeers.add(index);
					DynamicPeerInfo optpeer= PeerProcess.peers.get(index);
					if(optpeer.isConnected ) {
						if(preferredPeers.contains(index) && optpeer.isChoked){
							PeerProcess.messageQueues.get(index).add(Message.actualMessageWrapper(PeerProcess.index , 1,new byte[0]));
							optpeer.isChoked=false;
						}
						else if(!optpeer.isChoked){
								PeerProcess.messageQueues.get(index).add(Message.actualMessageWrapper(PeerProcess.index , 0,new byte[0])); 
								optpeer.isChoked=true;
						}
					}
					// break;
				}
				// }
				System.out.println("Optimistic Updated " +String.valueOf(PeerProcess.peerId) +" preferred peers to :"+ preferredPeers);
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
		timerUpdate.schedule(task1,1000,PeerProcess.unchokingInterval*1000);
		timerOptUpdate.schedule(task2,6000, PeerProcess.optUnchokingInterval*1000);
		while(true) {
			/*
			 * The code bellow is only for testing.
			 * The decisionMaker is trying to send a "Hi" message to each connected peer every 5 seconds.
			 */
			try {
				for(Integer p: preferredPeers) {
					if(PeerProcess.peers.get(p).isConnected) {
						// System.out.println("CONNECTED HERE");
						String debugMsg = "Hi, i'm PP peer " + PeerProcess.peerId + ", it's " + LocalTime.now();
						PeerProcess.messageQueues.get(p).put(
							Message.actualMessageWrapper(p , 8,debugMsg.getBytes()));
						sleep(100);
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
