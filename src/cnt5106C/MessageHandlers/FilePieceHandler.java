package cnt5106C.MessageHandlers;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import cnt5106C.DynamicPeerInfo;
import cnt5106C.PeerProcess;
import cnt5106C.Message.Message;

public class FilePieceHandler {
	public static Message construct(int remotePeerId, int index) throws IOException {
		ByteBuffer msg = ByteBuffer.allocate(PeerProcess.pieceSize + 4);
		msg.put(ByteBuffer.allocate(4).putInt(index).array());
		msg.put(PeerProcess.fileHelper.getFilePieceInByteArray(index));
		return Message.actualMessageWrapper(remotePeerId, 7, msg.array());
	}

	public static void handle(Message m) throws IOException, InterruptedException {
		byte[] fileIndexInByte = Arrays.copyOfRange(m.messagePayload, 0, 4);
		byte[] realFile = Arrays.copyOfRange(m.messagePayload, 4, 4 + PeerProcess.pieceSize);
		int fileIndex = ByteBuffer.wrap(fileIndexInByte).getInt();
		PeerProcess.fileHelper.writeFilePieceInByteArray(fileIndex, realFile);
		DynamicPeerInfo selfPeer = PeerProcess.peers.get(PeerProcess.index);
		selfPeer.setFilePieceState(fileIndex, true);
		for (DynamicPeerInfo p : PeerProcess.peers) {
			if (p.isConnected) {
				PeerProcess.messageQueues.get(p.index).put(HaveHandler.construct(p.peerId, fileIndex));
				if (!p.isThereAnyInterestedFilePieces()) {
					PeerProcess.messageQueues.get(p.index).put(InterestHandler.construct(p.peerId, false));
				}
			}
		}

		DynamicPeerInfo rp = PeerProcess.peers.get(m.remotePeerIndex);
		rp.incrementChunkCounter();
		ArrayList<Integer> interestedList = rp.getInterestedList();
		// TODO: Tony, Please have a look
		PeerProcess.write("has downloaded the piece " + fileIndex + " from " + m.remotePeerId
				+ ". Now the number of pieces it has is "
				+ selfPeer.totalFilePiecesWeReceived);
		if (!interestedList.isEmpty()) {
			int requestIndex = interestedList.get((int) (Math.random() * interestedList.size()));
			PeerProcess.messageQueues.get(m.remotePeerIndex)
					.put(RequestHandler.construct(m.remotePeerId, requestIndex));
		}
	}
}
