package cnt5106C.FileHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;


public class FileSender {
//	
	private static DataOutputStream dataOutputStream = null;
    private static DataInputStream dataInputStream = null;
    private final static String FILENAME = "syllabus.pdf";
    private final static int CHUNKSIZE = 10240;
//    private static File file = new File("/Users/weinalyu/Dropbox/aTextbook/A_Machine_Learning_UF/HW2P2.pdf");
    private static File file = new File("/Users/weinalyu/Dropbox/aTextbook/A_Computer_Network/syllabus.pdf");
    
    


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

	


