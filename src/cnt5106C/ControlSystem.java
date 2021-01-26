/**
 * The main process in every host to coordinate other components.
 */

package cnt5106C;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import cnt5106C.config.Config;
import cnt5106C.config.PeerInfo;

import java.io.IOException;
import java.net.*;

public class ControlSystem {
	private static int peerId; //The peerId of this process, reading from console.
	private static int index; //The index of this process, counting from up to down in peerInfo.cfg
	public static ArrayList<DynamicPeerInfo> peers; //An array that saves all peerInfos.
	
	private static int preferredNeighbors; //The number of preferred neighbors.
	private static int unchokingInterval; //The interval of switching unchocking neighbors.
	private static int optUnchokingInterval; //The interval of switching optimistic unchocking neighbors.
	private static String fileName; //The name of the file to be distributed.
	private static int fileSize; //The size of the file in bytes.
	private static int pieceSize; //The size of the piece in bytes.
	
	//A queue for all the threads to send message to each other.
	public static LinkedBlockingQueue<InterThreadMessage> messageQueue = new LinkedBlockingQueue<InterThreadMessage>();
	
	/**
	 * Read common.cfg and PeerInfo.cfg into some data structures.
	 */
	private static void readConfigFiles() {
		System.out.println("Reading config files.");
		Config.init();
		preferredNeighbors = Config.getNumberOfPreferredNeighbors();
		unchokingInterval = Config.getUnchokingInterval();
		optUnchokingInterval = Config.getOptimisticUnchokingInterval();
		fileName = Config.getFileName();
		fileSize = Config.getFileSize();
		pieceSize = Config.getPieceSize();
		int numOfPieces = fileSize/pieceSize;//How many pieces are there in a file.
		
		peers = PeerInfo.readPeerInfo(numOfPieces);
	}
	
	/**
	 * Find the index of a process. We need it because we should connect all the processes before.
	 * @param peerId of that process.
	 * @return index of that process.
	 */
	private static int getIndex(int peerId) {
		int index = 0;
		for(DynamicPeerInfo p : peers){
			if(p.peerId == peerId) {
				break; //We find this process.
			}else {
				index++;
			}
		}
		return index;
	}
	
	/**
	 * The main function for every peerProcess.
	 * @param args an argument from console, which is the peerId of that particular peerProcess
	 * @throws IOException When serverSocket doesn't work well.
	 */
	public static void main(String[] args) throws IOException {
		System.out.println("Mainthread begins to work.");
		peerId = Integer.parseInt(args[0]); //Read PeerId from console arguments.
		readConfigFiles();
		index = getIndex(peerId); //Find the index of this process.
		
		//We need to create sockets towards all the hosts before our index.
		for(int i = 0; i < index; i++) {
			try {
				//We need to create the socket towards remote port.
				System.out.println("Positively creating a socket to " + peers.get(i).address + " " + peers.get(i).port);
				Socket beforeSocket = new Socket(peers.get(i).address, peers.get(i).port);
				//Create the upstream handler.
				UpstreamHandler sendingThread = new UpstreamHandler(beforeSocket, peers, messageQueue, i, index);
				sendingThread.start();
				//Create the downstream handler.
				DownstreamHandler receivingThread = new DownstreamHandler(beforeSocket, peers, messageQueue, i, index);
				receivingThread.start();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		//We only need 1 serverSocket since we have only 1 port to listen for each host.
		ServerSocket serverSocket = new ServerSocket(peers.get(index).port);
		System.out.println("ServerSocket at port " + peers.get(index).port + " is ready.");
		int counter = 1;//Keep track of while loops so we can calculate index of peers.
		try {
			while(true) {
				//This is only called by afterward peers. We will positively connect to peers
				//before, not listen to them.
				Socket afterwardSocket = serverSocket.accept();
		
				InetAddress ipAddress = afterwardSocket.getInetAddress();//Get the ipAddress of remote host from socket.
				System.out.println("Accepet a socket from "+ ipAddress.getHostName());
				//Create the upstream handler.
				UpstreamHandler sendingThread = new UpstreamHandler(afterwardSocket, peers, messageQueue, index + counter, index);
				sendingThread.start();
				//Create the downstream handler.
				DownstreamHandler receivingThread = new DownstreamHandler(afterwardSocket, peers, messageQueue, index + counter, index);
				receivingThread.start();
				counter++;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			serverSocket.close();
		}
	}
}
