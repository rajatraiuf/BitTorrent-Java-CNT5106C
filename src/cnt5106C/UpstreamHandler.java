/**
 * The thread responsible for sending messages to another host.
 */

package cnt5106C;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class UpstreamHandler extends Thread{
	private Socket socket; //The socket connected to the remote host.
	private int index; //Index of the specific remote host to communicate;
	private ArrayList<PeerInfo> peers; //The peerInfo of the remote hosts.
	private ObjectOutputStream output;//The output stream of the socket.
	private LinkedBlockingQueue<InterThreadMessage> queue;//The message queue for thread communication.
	
	/**
	 * The constructor of the thread. We need the socket, peerInfo and message queue to create the new thread.
	 * @param socket
	 * @param peerInfo
	 * @param queue A queue to communicate with other threads.
	 */
	UpstreamHandler(Socket socket, ArrayList<PeerInfo> peers, LinkedBlockingQueue<InterThreadMessage> queue){
		this.socket = socket;
		this.peers = peers;
		this.queue = queue;
		try {
			output = new ObjectOutputStream(socket.getOutputStream());
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *Run the thread.
	 */
	public void run() {
		System.out.println("Upstream thread start to work.");
		while(true) {
			
		}
	}
}
