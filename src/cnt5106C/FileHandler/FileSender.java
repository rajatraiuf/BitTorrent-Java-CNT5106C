package cnt5106C.FileHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import java.util.ResourceBundle.Control;

import cnt5106C.ControlSystem;
import cnt5106C.config.Config;

public class FileSender {
//	
	private static DataOutputStream dataOutputStream = null;
    private static DataInputStream dataInputStream = null;
    private final static String FILENAME = ControlSystem.fileName;
    private final static int CHUNKSIZE = ControlSystem.pieceSize;
    private static File file = new File("..\\Files\\"+"\tree.jpg");

	public static void main(String[] args) {

	    
        try {
           	Socket socket = new Socket("localhost", 45454);
        	
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

    private static void sendFile(int chunkIdx) throws Exception{
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

	


