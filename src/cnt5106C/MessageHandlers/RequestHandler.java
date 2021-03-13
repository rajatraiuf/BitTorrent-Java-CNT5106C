/*
 * The handler to handle any functionalities about interested/not interested message.
 */

package cnt5106C.MessageHandlers;


import java.util.Arrays;
import java.nio.ByteBuffer;
import cnt5106C.*;
import cnt5106C.Message.*;

public class RequestHandler {
	/**
	 * Construct a Interested message.
	 * @param remotePeerId the peerid of remote peer
	 * @return the message just has been constructed
	 */
	public static Message construct(int remotePeerId,int filePieceIndex) {
		byte[] result = ByteBuffer.allocate(4).putInt(filePieceIndex).array();
		System.out.println("sending interested byte field"+ByteBuffer.wrap(result).getInt());
		return Message.actualMessageWrapper(remotePeerId, 6, result);
	}
	
	/**
	 * Handle a interested/not interested message in a proper way.
	 * @param m the message to be handled
	 */
	public static void handle(Message m) throws Exception {
		//TODO this is just for testing
		int payLoad = ByteBuffer.wrap(m.messagePayload).getInt();
		System.out.println("Receive a test request message from peer " + m.remotePeerId + ", whose payload is " + payLoad);
	}
}
