/*
 * The handler to handle any functionalities about bitfield message.
 */

package cnt5106C.MessageHandlers;

import java.util.Arrays;
import cnt5106C.*;
import cnt5106C.Message.*;

public class BitfieldHandler {
	/**
	 * Construct a bitfield message.
	 * @param remotePeerId the peerid of remote peer
	 * @return the message just has been constructed
	 */
	public static Message construct(int remotePeerId) {
		byte[] messagePayload;//The payload of bitfield;
		
		int numOfPieces = PeerProcess.fileSize/PeerProcess.pieceSize;
		int numOfZeros = (8 - (numOfPieces % 8)) % 8;//Number of zero we need to add in the end
		int numOfPayloadBytes = (numOfPieces + numOfZeros) / 8;
		
		messagePayload = new byte[numOfPayloadBytes];
		for(int i = 0; i < numOfPayloadBytes; i++) {//For each byte in payload
			byte onePayload = 0;//A temporary buffer of byte
			for(int j = 0; j < 8; j++) {//For each bit in a byte
				int indexOfPiece = i * 8 + j;
				if(indexOfPiece < numOfPieces) {
					boolean hasPiece = PeerProcess.peers.get(PeerProcess.index).getFilePieces(i * 8 + j);
					if(hasPiece) {
						onePayload |= (1 << (7 - j));//change the highest jth bit in that byte to 1
					}else {
						onePayload &= ~(1 << (7 - j));//change the highest jth bit in that byte to 0
					}
				}
			}
			messagePayload[i] = onePayload;
		}
		
		System.out.println("peer " + PeerProcess.index + " send a bitfield message to " + remotePeerId + " with payload " + Arrays.toString(messagePayload));
		
		return Message.actualMessageWrapper(remotePeerId, 5, messagePayload);
	}
	
	/**
	 * Handle a bitfield message in a proper way.
	 * @param m the message to be handled
	 */
	public static void handle(Message m) throws Exception {
		//TODO this is just for testing
		byte[]payLoad = m.messagePayload;
		
		int numOfPieces = PeerProcess.fileSize/PeerProcess.pieceSize;
		int numOfZeros = (8 - (numOfPieces % 8)) % 8;//Number of zero we need to add in the end
		int numOfPayloadBytes = (numOfPieces + numOfZeros) / 8;
		boolean ifInterested = false;
		for(int i = 0; i < numOfPayloadBytes; i++) {//For each byte in payload
			for(int j = 0; j < 8; j++) {//For each bit in a byte
				int indexOfPiece = i * 8 + j;
				//we create a mask like 00x00000 and do "and operation" to that received byte
				boolean hasFile = (payLoad[i] & (1 << (7 - j))) != 0;
				if(hasFile) {
					PeerProcess.peers.get(m.remotePeerIndex).setFilePieces(indexOfPiece, true);
					if (!PeerProcess.peers.get(PeerProcess.index).getFilePieces(indexOfPiece)) // If peer does not have a file piece then send an interested message to the neighbor 
						ifInterested = true;
				}else {
					PeerProcess.peers.get(m.remotePeerIndex).setFilePieces(indexOfPiece, false);
				}
			}
		}
		//TODO create a interest/not interest message and send it
		System.out.println("Receive a test bitfield message from peer " + m.remotePeerId + ", whose payload is " + Arrays.toString(payLoad));
		PeerProcess.messageQueues.get(m.remotePeerIndex).add(InterestHandler.construct(m.remotePeerId, ifInterested));
	}
}
