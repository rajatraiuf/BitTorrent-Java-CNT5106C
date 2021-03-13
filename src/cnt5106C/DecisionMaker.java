/**
 * The real Control Thread, running in parallel, used to decide who to choke, who to unchoke and stuffs like that.
 */

package cnt5106C;

import java.time.LocalTime;
import java.util.*;

import cnt5106C.Message.*;
import cnt5106C.MessageHandlers.ChokeUnchokeHandler;

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
			while(preferredPeers.size()<PeerProcess.preferredNeighborsCount){
				int index = (int)(Math.random() * PeerProcess.peers.size()); 
				// System.out.println("Picking random peer at index"+String.valueOf(index));
				if(index != PeerProcess.index && !preferredPeers.contains(index) && PeerProcess.peers.get(index).isInterested )
					preferredPeers.add(index);
			}
			//iterate over all peers to check and send proper choke/unchoke msgs
			for(DynamicPeerInfo p :PeerProcess.peers) {
				if(p.isConnected ) {
					if(preferredPeers.contains(p.index)){
						if (p.isChoked){
							System.out.println("Unchoking [choked] random peer at index"+p.peerId);
							PeerProcess.messageQueues.get(p.index).add(ChokeUnchokeHandler.construct(p.peerId , false));
							p.isChoked=false;
						}
					}
					else if(!p.isChoked){
						System.out.println("Choking [unchoked] random peer at index"+p.peerId);
						PeerProcess.messageQueues.get(p.index).add(ChokeUnchokeHandler.construct(p.peerId , true));
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
				int index = (int)(Math.random() * PeerProcess.peers.size()); 
				if(index != PeerProcess.index && !preferredPeers.contains(index) && PeerProcess.peers.get(index).isInterested){
					preferredPeers.add(index);
					DynamicPeerInfo optpeer= PeerProcess.peers.get(index);
					if(optpeer.isConnected ) {
						if(preferredPeers.contains(index) && optpeer.isChoked){
							PeerProcess.messageQueues.get(index).add(ChokeUnchokeHandler.construct(optpeer.peerId, false));
							optpeer.isChoked=false;
						}
						else if(!optpeer.isChoked){
									PeerProcess.messageQueues.get(index).add(ChokeUnchokeHandler.construct(optpeer.peerId, true)); 
									optpeer.isChoked=true;
							}
						}
					}
				System.out.println("Optimistic Updated " +PeerProcess.peerId +" preferred peers to :"+ preferredPeers);
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
					if(PeerProcess.peers.get(p).isConnected && !PeerProcess.peers.get(p).isChoked) {
						// System.out.println("CONNECTED HERE");
						String debugMsg = "Hi, i'm PP peer " + PeerProcess.peerId + ", it's " + LocalTime.now();
						PeerProcess.messageQueues.get(p).put(
							Message.actualMessageWrapper(p , 8,debugMsg.getBytes()));
						// sleep(50);
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
