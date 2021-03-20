/**
 * The dynamic data structure to save a single peer Information.
 */

package cnt5106C;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class DynamicPeerInfo {
	public int index;//Index of the remote peer. Follow the order in peerInfo.cfg
	public boolean isConnected;//If the local host already established a TCP connection to remote host
	public boolean isChoked;//If the remote peer is chocked or not
	public boolean isInterested;//If the remote peer is interested or not
	public int peerId; //The id of the remote peer, for example, 1001
	public String address; //The domain name address of the peer, for example, lin114-00.cise.ufl.edu
	public InetAddress ipAddress; //The ip address of this peer.
	public int port; //The port of the peer, for example, 6001
	public boolean hasFileInitially; //If the peer has the file initially, this suppose to be true.
	private ArrayList<Boolean> filePieces; //A BitSet that keep track of whether a peer has any piece or not. 
							  //If the bit at n is 1, then this peer has piece n right now.
	private final Object lock;//For thread safe
	
	/**
	 * The constructor of PeerInfo data structure.
	 * @param peerId
	 * @param address
	 * @param port
	 * @param hasFileInitially
	 * @param numOfPieces How many pieces in the file.
	 * @param index index of the remote host
	 */
	public DynamicPeerInfo(int peerId, String address, int port, boolean hasFileInitially, int numOfPieces, int index, boolean isChoked, boolean isInterested) {
		this.lock = new Object();
		this.isConnected = false;
		this.peerId = peerId;
		this.address = address;
		this.isChoked = isChoked;
		this.isInterested = isInterested;
		try {
			ipAddress = InetAddress.getByName(address);//Get the real ip address from host name.
		}catch(UnknownHostException e) {
			e.printStackTrace();
		}
		this.port = port;
		this.index = index;
		this.hasFileInitially = hasFileInitially;
		filePieces = new ArrayList<Boolean>();
		for(int i = 0; i < numOfPieces; i++) {
			if(hasFileInitially) {
				filePieces.add(true);
			}else {
				filePieces.add(false);
			}
		}
	}
	
	public void setFilePieceState(int index, boolean value) {
		synchronized(lock){
			filePieces.set(index, value);
		}
	}
	
	public boolean getFilePieceState(int index) {
		boolean result;
		synchronized(lock){
			result = filePieces.get(index);
		}
		return result;
	}
	
	public boolean isFilePiecesEmpty() {
		boolean result = true;
		synchronized(lock){
			for(Boolean b: filePieces) {
				if(b == true) {
					result = false;
				}
			}
		}
		return result;
	}
}