/*
 * The handler to handle any functionalities about bitfield message.
 */

package cnt5106C;

import java.util.Arrays;

public class BitfieldHandler {
	/**
	 * Construct a bitfield message.
	 * @param remotePeerIndex the index of remote peer
	 * @return the message just has been constructed
	 */
	public static Message construct(int remotePeerIndex) {
		byte[] messagePayload;//The payload of bitfield;
		
		//TODO this is just for testing
		messagePayload = "Hello, this is the payload of bitfield".getBytes();
		System.out.println("Constructing a bitfield message...");
		return Message.actualMessageWrapper(remotePeerIndex, 5, messagePayload);
	}
	
	/**
	 * Handle a bitfield message in a proper way.
	 * @param m the message to be handled
	 */
	public static void handle(Message m) throws Exception {
		//TODO this is just for testing
		byte[]payLoad = m.messagePayload;
		System.out.println("Receive a test bitfield message");
		// System.out.println("Receive a test bitfield message, whose payload is " + Arrays.toString(payLoad));
	}
}
