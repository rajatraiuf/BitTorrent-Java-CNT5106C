/*
 * The handler to handle any functionalities about interested/not interested message.
 */

package cnt5106C.MessageHandlers;


import java.nio.ByteBuffer;
import cnt5106C.*;
import cnt5106C.Message.*;

public class RequestHandler {
	/**
	 * Construct a request message.
	 * @param remotePeerId the peerid of remote peer
	 * @param filePieceIndex the piece number to request from the remote peer
	 * @return the message just has been constructed
	 */
	public static Message construct(int remotePeerId,int filePieceIndex) {
		byte[] result = ByteBuffer.allocate(4).putInt(filePieceIndex).array();
		System.out.println("Sending a request message to peerId "+ remotePeerId+" for file piece index "+ filePieceIndex);
		return Message.actualMessageWrapper(remotePeerId, 6, result);
	}
	
	/**
	 * Handle a request message in a proper way.
	 * @param m the message to be handled
	 */
	public static void handle(Message m) throws Exception {
		int payLoad = ByteBuffer.wrap(m.messagePayload).getInt();
		if(!PeerProcess.peers.get(m.remotePeerIndex).isChoked) {
			PeerProcess.messageQueues.get(m.remotePeerIndex).put(FilePieceHandler.construct(m.remotePeerId, payLoad));
		}
		System.out.println("Received a request message from peer " + m.remotePeerId + ", for piece index " + payLoad);
	}
}
