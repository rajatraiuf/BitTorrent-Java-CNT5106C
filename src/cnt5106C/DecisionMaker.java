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
	public int optUnchokedPeer = -1;
	/**
	 * Update preferred peers
	 */
	private class updatePreferredPeers extends TimerTask 
	{ 
		public void run() 
		{ 
			synchronized(preferredPeers){
				if (PeerProcess.peers.size()>0)
				{
						preferredPeers.clear();
				}
				while(preferredPeers.size() < Math.min(
						PeerProcess.preferredNeighborsCount, 
						PeerProcess.interestedPeerNumber - ((optUnchokedPeer == -1) ? 0 : 1))){
					int index = (int)(Math.random() * PeerProcess.peers.size()); 
					// System.out.println("Picking random peer at index"+String.valueOf(index));
					if(
							index != PeerProcess.index 
							&& !preferredPeers.contains(index) 
							&& PeerProcess.peers.get(index).isInterested
							&& optUnchokedPeer != index)
						preferredPeers.add(index);
				}
				//iterate over all peers to check and send proper choke/unchoke msgs
				for(DynamicPeerInfo p :PeerProcess.peers) {
					if(p.isConnected ) {
						if(preferredPeers.contains(p.index)){
							if (p.isChokeingIt){
								System.out.println("Unchoking [choked] random peer at index"+p.peerId);
								PeerProcess.messageQueues.get(p.index).add(ChokeUnchokeHandler.construct(p.peerId , false));
								p.isChokeingIt = false;
							}
						}
						else if(!p.isChokeingIt && optUnchokedPeer != p.index){
							System.out.println("Choking [unchoked] random peer at index"+p.peerId);
							PeerProcess.messageQueues.get(p.index).add(ChokeUnchokeHandler.construct(p.peerId , true));
							p.isChokeingIt = true;
						}
					}
				}
				System.out.println("Updated " +String.valueOf(PeerProcess.peerId) +" preferred peers to :"+ preferredPeers + " and " + optUnchokedPeer);
			}
		} 
		
	} 	  

	/**
	 * Optimistically unchoke a peer  
	 */
	private class optimisiticUnchoke extends TimerTask 
	{ 
		public void run() 
		{	
			synchronized(preferredPeers) {
				if(preferredPeers.size() < PeerProcess.interestedPeerNumber) {
					boolean flag = true;
					while(flag) {
						int index = (int)(Math.random() * PeerProcess.peers.size()); 
						if(index != PeerProcess.index 
								&& !preferredPeers.contains(index) 
								&& PeerProcess.peers.get(index).isInterested){
							int oldopt = optUnchokedPeer;
							optUnchokedPeer = index;
							DynamicPeerInfo optpeer= PeerProcess.peers.get(index);
							if(optpeer.isChokeingIt){
								PeerProcess.messageQueues.get(index).add(ChokeUnchokeHandler.construct(optpeer.peerId, false));
								optpeer.isChokeingIt=false;
							}
							if(oldopt != -1 && oldopt != index && !PeerProcess.peers.get(oldopt).isChokeingIt) {
								PeerProcess.messageQueues.get(index).add(ChokeUnchokeHandler.construct(PeerProcess.peers.get(oldopt).peerId, true));
								PeerProcess.peers.get(oldopt).isChokeingIt=true;
							}
							System.out.println("Optimistic Updated " +PeerProcess.peerId +" preferred peers from " + oldopt + " to :"+ optUnchokedPeer);
							flag = false;
						}
					}
				}
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
				synchronized(preferredPeers) {
					for(DynamicPeerInfo p: PeerProcess.peers) {
						if(!p.isChockedByIt) {
							// System.out.println("CONNECTED HERE");
							String debugMsg = "Hi, i'm PP peer " + PeerProcess.peerId + "/" + PeerProcess.index + ", thank you for not choking me";
							PeerProcess.messageQueues.get(p.index).put(
								Message.actualMessageWrapper(p.index , 8,debugMsg.getBytes()));
						}	
					}
				}
				sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
