/**
 * The real Control Thread, running in parallel, used to decide who to choke, who to unchoke and stuffs like that.
 */

package cnt5106C;

import java.util.*;

import cnt5106C.Message.*;
import cnt5106C.MessageHandlers.ChokeUnchokeHandler;

public class DecisionMaker extends Thread{
	private Set<Integer> preferredPeers = Collections.synchronizedSet(new HashSet<Integer>()); //An array that maintains preferred peerInfos.
	private int optUnchokedPeer = -1;
	/**
	 * Update preferred peers
	 */
	private class updatePreferredPeers extends TimerTask 
	{ 
		public void run() 
		{ 
			synchronized(preferredPeers){
				if (PeerProcess.peers.size()>0)
						preferredPeers.clear();

				Collections.sort(PeerProcess.peers, new DynamicComparator());
				var i=0;
				while(preferredPeers.size() < Math.min(
						PeerProcess.preferredNeighborsCount, 
						PeerProcess.interestedPeerNumber - ((optUnchokedPeer == -1) ? 0 : 1))
						&& i<PeerProcess.peers.size()){
							if (i!=PeerProcess.index){
								DynamicPeerInfo p = PeerProcess.peers.get(i);
								System.out.println("Choosing preferred peer "+p.peerId+" with received chunk count : "+p.chunkCount);
								preferredPeers.add(p.index);
								p.chunkCount=0;
							}
							i++;
						}

				//iterate over all peers to check and send proper choke/unchoke msgs
				for(DynamicPeerInfo p :PeerProcess.peers) {
					if(p.isConnected ) {
						if(preferredPeers.contains(p.index) && p.isChoked){
							System.out.println("Unchoking peer "+p.peerId);
							PeerProcess.messageQueues.get(p.index).add(ChokeUnchokeHandler.construct(p.peerId , false));
							p.isChoked = false;
						}
						else if(!p.isChoked && optUnchokedPeer != p.index){
							System.out.println("Choking peer "+p.peerId);
							PeerProcess.messageQueues.get(p.index).add(ChokeUnchokeHandler.construct(p.peerId , true));
							p.isChoked = true;
						}
					}
				}
				System.out.println("Updated " +String.valueOf(PeerProcess.peerId) +" preferred peers to "+ preferredPeers + " and optimisticPeerIndex is " + optUnchokedPeer);
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
							if(optpeer.isChoked){
								PeerProcess.messageQueues.get(index).add(ChokeUnchokeHandler.construct(optpeer.peerId, false));
								optpeer.isChoked=false;
							}
							if(oldopt != -1 && oldopt != index && !PeerProcess.peers.get(oldopt).isChoked) {
								PeerProcess.messageQueues.get(index).add(ChokeUnchokeHandler.construct(PeerProcess.peers.get(oldopt).peerId, true));
								PeerProcess.peers.get(oldopt).isChoked=true;
							}
							System.out.println("Optimisticslly updated " +PeerProcess.peerId +" preferred peers from " + oldopt + " to :"+ optUnchokedPeer);
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
	@Override
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
			 * The decisionMaker is trying to send a "Hi" message to each connected peer every 10 seconds.
			 */
			try {
				synchronized(preferredPeers) {
					for(DynamicPeerInfo p: PeerProcess.peers) {
						if(p.isConnected) {
							String debugMsg = "Hi, I'm peer id " + PeerProcess.peerId + "/" + PeerProcess.index + ", Thank you for unchoking me.";
							PeerProcess.messageQueues.get(p.index).put(
								Message.actualMessageWrapper(p.index , 8,debugMsg.getBytes()));
						}	
					}
				}
				sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}
	}
}
