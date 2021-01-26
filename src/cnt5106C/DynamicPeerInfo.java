/**
 * The dynamic data structure to save a single peer Information.
 */

package cnt5106C;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.BitSet;

public class DynamicPeerInfo {
	public int peerId; //The id of the peer, for example, 1001
	public String address; //The domain name address of the peer, for example, lin114-00.cise.ufl.edu
	public InetAddress ipAddress; //The ip address of this peer.
	public int port; //The port of the peer, for example, 6001
	public boolean hasFileInitially; //If the peer has the file initially, this suppose to be true.
	private BitSet filePieces; //A BitSet that keep track of whether a peer has any piece or not. 
							  //If the bit at n is 1, then this peer has piece n right now.
	
	/**
	 * The constructor of PeerInfo data structure.
	 * @param peerId
	 * @param address
	 * @param port
	 * @param hasFileInitially
	 * @param numOfPieces How many pieces in the file.
	 */
	public DynamicPeerInfo(int peerId, String address, int port, boolean hasFileInitially, int numOfPieces) {
		this.peerId = peerId;
		this.address = address;
		try {
			ipAddress = InetAddress.getByName(address);//Get the real ip address from host name.
		}catch(UnknownHostException e) {
			e.printStackTrace();
		}
		this.port = port;
		this.hasFileInitially = hasFileInitially;
		filePieces = new BitSet(numOfPieces);
	}
}