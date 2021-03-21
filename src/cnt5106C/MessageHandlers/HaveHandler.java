package cnt5106C.MessageHandlers;

import java.io.IOException;
import java.nio.ByteBuffer;

import cnt5106C.PeerProcess;
import cnt5106C.Message.Message;

public class HaveHandler {
	public static Message construct(int remotePeerId, int index) throws IOException {
		ByteBuffer msg = ByteBuffer.allocate(4);
		msg.put(ByteBuffer.allocate(4).putInt(index).array());
		return Message.actualMessageWrapper(remotePeerId, 4, msg.array());
	}
	
	public static void handle(Message m) {
		int fileIndex = ByteBuffer.wrap(m.messagePayload).getInt();
		PeerProcess.peers.get(m.remotePeerIndex).setFilePieceState(fileIndex, true);
	}
}
