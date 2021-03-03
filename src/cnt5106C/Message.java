/**
 * The data structure which is used by different threads in a host to communicate with each other.
 */

package cnt5106C;

import java.nio.*;
import java.util.Arrays;

public class Message {
	public byte[] msg;//The context of the msg
	public int remotePeerIndex;//Which peer should send or receive this msg
	private boolean fromRemotePeer;//If this message is from remote peer, then we need to decode it and decide how to react.
								//Otherwise, this must be a message generated by internal decision maker or message processing function,
								//Then it is supposed to be sent to the remote peer immediately
	public int messageLength;//If the message is an actual message, then its message Length should be here.
	public int messageType;//If the message is an actual message, then its message type (in numbers) should be here.
	public byte[] messagePayload;//If the message is an actual message, then its payload is here.
	
	/**
	 * The constructor of message.
	 * @param msg
	 * @param remotePeerIndex
	 * @param test
	 */
	public Message(byte[] msg, int remotePeerIndex, boolean fromRemotePeer) {
		this.msg = msg;
		this.remotePeerIndex = remotePeerIndex;
		this.fromRemotePeer = fromRemotePeer;
	}
	
	/**
	 * Message receiving thread will execute a message as soon as it retrieve it.
	 * @param out the Handler to send any msg
	 * @throws Exception 
	 */
	public void execute(MessageSendingThread out) throws Exception {
		if(fromRemotePeer) {
			decodeAndHandle();
		}else {
			out.send(msg);//If this message is supposed to be sent outside, then we do it.
		}
	}
	
	/**
	 * The wrapper takes the payload and make it a real message whose format satisfies the given document
	 * @param remotePeerIndex
	 * @param messageType
	 * @param messagePayload
	 * @return the message we got finally
	 */
	public static Message actualMessageWrapper(int remotePeerIndex, int messageType, byte[] messagePayload) {
		ByteBuffer msg = ByteBuffer.allocate(5 + messagePayload.length);
		msg.put(ByteBuffer.allocate(4).putInt(5+messagePayload.length).array());
		msg.put((byte)messageType);
		msg.put(messagePayload);
		return new Message(
				msg.array(),
				remotePeerIndex, false
				);
	}
	
	/**
	 * Decode and handle the message from remote peer.
	 * @throws Exception 
	 */
	private void decodeAndHandle() throws Exception {
		//First we need to decode to see what kind of message it is.
		if(HandshakeHandler.isHandshake(this)) {
			//This is a handshake message received from remote peer.
			HandshakeHandler.handle(this);
		}else {
			//This is an actual message
			if(msg == null || msg.length < 5) {
				//not a legal message
				throw new Exception();
			}else {
				//We use ByteBuffer to wrap byte array to integer
				messageLength = ByteBuffer.wrap(Arrays.copyOfRange(msg, 0, 4)).getInt();
				messageType = msg[4];
				System.out.println("Decoding a actual message, its type number is " + messageType);
				if(msg.length == 5) {
					//check if there is any payload
					System.out.println("No payload found in message");
					messagePayload = null;
				}else {
					messagePayload = Arrays.copyOfRange(msg, 5, messageLength);
				}
				switch(messageType) {
				case 0:
					//TODO choke message
					break;
				case 1:
					//TODO unchoke message
					break;
				case 2:
					//TODO interested message
					break;
				case 3:
					//TODO not interested message
					break;
				case 4:
					//TODO have message
					break;
				case 5:
					//bitfield message
					BitfieldHandler.handle(this);
					break;
				case 6:
					//TODO request message
					break;
				case 7:
					//TODO piece message
					break;
				case 8: 
					System.out.println(new String(messagePayload));
					break;
				default:
					//not a legal message
					throw new Exception();
				}
			}
		}
	}
}
