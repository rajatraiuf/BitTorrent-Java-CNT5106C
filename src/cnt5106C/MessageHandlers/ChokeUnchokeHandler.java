/*
 * The handler to handle any functionalities about interested/not interested message.
 */

package cnt5106C.MessageHandlers;


import java.util.ArrayList;
import cnt5106C.*;
import cnt5106C.Message.*;

public class ChokeUnchokeHandler {
	/**
	 * Construct a Interested message.
	 * @param remotePeerId the peerid of remote peer
	 * @return the message just has been constructed
	 */
	public static Message construct(int remotePeerId, boolean isChoke) {
		if (isChoke){
			System.out.println("Sending choke byte field "+remotePeerId);
			return Message.actualMessageWrapper(remotePeerId, 0 , new byte[0]);
		}
		else {
			System.out.println("Sending unchoke byte field "+remotePeerId);
			return Message.actualMessageWrapper(remotePeerId, 1 , new byte[0]);
		}
	}
	
	/**
	 * Handle a choke/unchoke message in a proper way.
	 * @param m the message to be handled
	 */
	public static void handle(Message m, boolean isChoke) throws Exception {
		//TODO this is just for testing
		if(isChoke){
			System.out.println("Received a choke message from peer " + m.remotePeerId);
		}
		else{
			System.out.println("RECEIVED a unchoke message from peer " + m.remotePeerId);
			// Send request if needed
			DynamicPeerInfo p = PeerProcess.peers.get(m.remotePeerIndex);
			ArrayList<Integer> interestedList = p.getInterestedList();
			// System.out.println("Interested list of " + m.remotePeerId + " : " + interestedList);
			if (interestedList.size()>0){
				int requestIndex = interestedList.get((int)(Math.random() * interestedList.size()));
				System.out.println("REQUESTING peer " + m.remotePeerId+" for piece #" + requestIndex);
				PeerProcess.messageQueues.get(m.remotePeerIndex).add(RequestHandler.construct(m.remotePeerId, requestIndex));
			}
		}
	}
}
