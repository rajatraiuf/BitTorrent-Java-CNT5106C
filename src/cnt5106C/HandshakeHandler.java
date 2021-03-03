/*
 * The handler to handle any functionalities about handshake message.
 */
package cnt5106C;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class HandshakeHandler {
	private static String header = "P2PFILESHARINGPROJ";//TODO it is temporary
	private static byte[] zeroBytes = new byte[10];//The zero bytes of handshake message
	
	/**
	 * The constructer.
	 */
	HandshakeHandler() {
		for(int i = 0; i < 10; i++) {
			zeroBytes[i] = 0;//Fill in the zero bytes.
		}
	}
	
	/**
	 * Check if a message is a handshake message.
	 * @param m is the message to be check
	 * @return true or false
	 */
	public static boolean isHandshake(Message m) {
		if(m.msg == null) {
			return false;//In these situations it can not be a legal handshake message.
		}
		if(Arrays.equals(Arrays.copyOfRange(m.msg, 0, 18), header.getBytes())
				&& Arrays.equals(Arrays.copyOfRange(m.msg, 18, 28), zeroBytes)) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * Construct a handshake message.
	 * @param remotePeerIndex the index of remote peer
	 * @return the message just has been constructed
	 */
	public static Message construct(int remotePeerIndex) {
		//String msg = header + zeroBytes.toString() + ControlSystem.peers.get(ControlSystem.index).peerId;
		byte[] headerBytes = header.getBytes();
		byte[] payload = ByteBuffer.allocate(4).putInt(ControlSystem.peers.get(ControlSystem.index).peerId).array();
		ByteBuffer msg = ByteBuffer.allocate(headerBytes.length + 10 + payload.length);
		msg.put(headerBytes);
		msg.put(zeroBytes);
		msg.put(payload);
		// System.out.println("Constructing a handshake message to " + remotePeerIndex + " " + Arrays.toString(msg.array()));
		System.out.println("Constructing a handshake message to " + remotePeerIndex);
		return new Message(msg.array(), remotePeerIndex, false);
	}
	
	/**
	 * Handle a handshake message in a proper way.
	 * @param m the message to be handled
	 * @throws Exception an exception that the peerID doesn't match
	 */
	public static void handle(Message m) throws Exception {
		if(Arrays.equals(Arrays.copyOfRange(m.msg, 28, m.msg.length)
				, ByteBuffer.allocate(m.msg.length - 28).putInt(ControlSystem.peers.get(m.remotePeerIndex).peerId).array()
				)) {
			// System.out.println("Receive a hand shake message from peer " + m.remotePeerIndex + " " + Arrays.toString(m.msg));
			System.out.println("Receive a hand shake message from peer " + m.remotePeerIndex);
			
			//Since it is a handshake message, we want to send a bitfield message to remote peer.
			Message message = BitfieldHandler.construct(m.remotePeerIndex);
			System.out.println("So we send a bitfield message to it " + Arrays.toString(message.msg));
			ControlSystem.messageQueues.get(m.remotePeerIndex).put(message);//Put it into the right queue to send it.
		}else {
			//TODO an exception that the peerID doesn't match
			throw new Exception();
		}
	}
}
