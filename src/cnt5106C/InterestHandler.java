/*
 * The handler to handle any functionalities about interested/not interested message.
 */

package cnt5106C;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class InterestHandler {
	/**
	 * Construct a Interested message.
	 * @param remotePeerId the peerid of remote peer
	 * @return the message just has been constructed
	 */
	public static Message construct(int remotePeerId, boolean isInterested,int filePieceIndex) {
		byte[] result = ByteBuffer.allocate(4).putInt(filePieceIndex).array();
		System.out.println("sending interested byte field"+ByteBuffer.wrap(result).getInt());
		if (isInterested)
			return Message.actualMessageWrapper(remotePeerId, 2, result);
		else 
			return Message.actualMessageWrapper(remotePeerId, 3, result);
	}
	
	/**
	 * Handle a interested/not interested message in a proper way.
	 * @param m the message to be handled
	 */
	public static void handle(Message m) throws Exception {
		//TODO this is just for testing
		// byte[]payLoad = m.messagePayload;
		
		// int numOfPieces = ControlSystem.fileSize/ControlSystem.pieceSize;
		// int numOfZeros = (8 - (numOfPieces % 8)) % 8;//Number of zero we need to add in the end
		// int numOfPayloadBytes = (numOfPieces + numOfZeros) / 8;
		
		// for(int i = 0; i < numOfPayloadBytes; i++) {//For each byte in payload
		// 	for(int j = 0; j < 8; j++) {//For each bit in a byte
		// 		int indexOfPiece = i * 8 + j;
		// 		//we create a mask like 00x00000 and do "and operation" to that received byte
		// 		boolean hasFile = (payLoad[i] & (1 << (7 - j))) != 0;
		// 		if(hasFile) {
		// 			ControlSystem.peers.get(m.remotePeerIndex).setFilePieces(indexOfPiece, true);
		// 			if (!ControlSystem.peers.get(m.remotePeerIndex).getFilePieces(indexOfPiece)){
						
		// 			}
		// 		}else {
		// 			ControlSystem.peers.get(m.remotePeerIndex).setFilePieces(indexOfPiece, false);
		// 		}
		// 	}
		// }
		
		// //TODO create a interest/not interest message and send it
		
		System.out.println("Receive a test interested/not interested message from peer " + m.remotePeerId + ", whose payload is " + ByteBuffer.wrap(m.messagePayload).getInt());
	}
}
