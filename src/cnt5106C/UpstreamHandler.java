/**
 * The thread responsible for sending messages to another host.
 */

package cnt5106C;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class UpstreamHandler extends Thread{
	private Socket socket; //The socket connected to the remote host.
	private int index; //Index of the specific remote host to communicate;
	private int myIndex; //Index of the local host.
	private ArrayList<DynamicPeerInfo> peers; //The peerInfo of the remote hosts.
	private ObjectOutputStream output;//The output stream of the socket.
	private ArrayList<LinkedBlockingQueue<InterThreadMessage>> queue;//The message queue for thread communication.
	
	public void send(String msg) {
		try {
			output.writeObject(msg);
			output.flush();
			System.out.println("Send a message to peer " + index + " : " + msg);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * The constructor of the thread. We need the socket, peerInfo and message queue to create the new thread.
	 * @param socket
	 * @param queue A queue to communicate with other threads.
	 * @param index
	 * @param myIndex
	 * @param peerInfo
	 */
	UpstreamHandler(Socket socket, ArrayList<DynamicPeerInfo> peers, ArrayList<LinkedBlockingQueue<InterThreadMessage>> queue, int index, int myIndex){
		this.socket = socket;
		this.peers = peers;
		this.queue = queue;
		this.index = index;
		this.myIndex = myIndex;
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
			try {
				InterThreadMessage message = queue.get(index).take();//This is a blocking queue and supposed to be thread safe
				message.execute(this);//Core of this project
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
