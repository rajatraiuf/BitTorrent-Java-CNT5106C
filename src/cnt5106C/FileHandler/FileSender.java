package cnt5106C.FileHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import java.util.ArrayList;

import cnt5106C.DynamicPeerInfo;
import cnt5106C.config.Config;
import cnt5106C.config.PeerInfo;


public class FileSender {
//	
	public static int peerId; //The peerId of this process, reading from console.
	public static int index; //The index of this process, counting from up to down in peerInfo.cfg
	public static ArrayList<DynamicPeerInfo> peers; //An array that saves all peerInfos.
	
	private static DataOutputStream dataOutputStream = null;
    private static DataInputStream dataInputStream = null;
    private final static String FILENAME = "syllabus.pdf";
    private final static int CHUNKSIZE = 10240;
//    private static File file = new File("/Users/weinalyu/Dropbox/aTextbook/A_Machine_Learning_UF/HW2P2.pdf");
    private static File file = new File("/Users/weinalyu/Dropbox/aTextbook/A_Computer_Network/syllabus.pdf");
	protected static int preferredNeighborsCount; //The number of preferred neighbors.
	private static int unchokingInterval; //The interval of switching unchocking neighbors.
	private static int optUnchokingInterval; //The interval of switching optimistic unchocking neighbors.
	private static String fileName; //The name of the file to be distributed.
	static int fileSize; //The size of the file in bytes.
	static int pieceSize; //The size of the piece in bytes.
	
	private static void readConfigFiles() {
		System.out.println("Reading config files.");
		Config.init();
		preferredNeighborsCount = Config.getNumberOfPreferredNeighbors();
		unchokingInterval = Config.getUnchokingInterval();
		optUnchokingInterval = Config.getOptimisticUnchokingInterval();
		fileName = Config.getFileName();
		fileSize = Config.getFileSize();
		pieceSize = Config.getPieceSize();
		int numOfPieces = fileSize/pieceSize;//How many pieces are there in a file.
		peers = PeerInfo.readPeerInfo(numOfPieces);
	}


	public static void main(String[] args) {

	    
        try {
        	Socket socket = new Socket("192.168.0.125", 45454);
//        	Socket socket = new Socket("localhost", 45454);
        	
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            

            sendFile();

            System.out.println("File Sent");
            
            dataInputStream.close();
            dataInputStream.close();
            
            socket.close();
        }catch (Exception e){
            e.printStackTrace();
        
        }
	}

    private static void sendFile() throws Exception{
    	int pieceNumber = 0;
        int bytes = 0;
        byte[] buffer = new byte[CHUNKSIZE];
        
        dataOutputStream.writeUTF(FILENAME);
        dataOutputStream.writeLong(file.length());
        
        FileInputStream fileInputStream = new FileInputStream(file);
        System.out.println("File Length is " + file.length());
        
        while (fileInputStream.available()!=0) { 

	        bytes = fileInputStream.read(buffer);
            dataOutputStream.write(buffer, 0, bytes);          
            dataOutputStream.flush();
            System.out.println("Sent " + pieceNumber + "th Chunk");
            ++pieceNumber;
        }
        fileInputStream.close();
    }
}



