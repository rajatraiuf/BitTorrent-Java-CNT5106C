package cnt5106C.FileHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class FileReceiver {
	
    private static DataOutputStream dataOutputStream = null;
    private static DataInputStream dataInputStream = null;
    private final static int CHUNKSIZE = 10240;
	
	public static void main(String[] args) {

        try {
        	ServerSocket serverSocket = new ServerSocket(45454);
            System.out.println("listening to port: 45454");
            Socket clientSocket = serverSocket.accept();
            System.out.println(clientSocket + " connected.");

            dataInputStream = new DataInputStream(clientSocket.getInputStream());
            dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());

            receiveFile();

            dataInputStream.close();
            dataOutputStream.close();
            clientSocket.close();
            serverSocket.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void receiveFile() throws Exception{
        int bytes = 0;
        byte[] buffer = new byte[CHUNKSIZE];
        
        String fileName = dataInputStream.readUTF();
        long size = dataInputStream.readLong();
        
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);

        while (size > 0 && (bytes = dataInputStream.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1) {
            fileOutputStream.write(buffer,0,bytes);
            size -= bytes;
            System.out.println("Receive a chunk, file size to be received " + size);
        }
        fileOutputStream.close();
    }
    
//    private static void writeBufferToFiles(byte[] buffer, String fileName) {
//    		//***fileName = fileName + (.001, .002 and etc.)
//		BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream(fileName));
//		bw.write(buffer);
//		bw.close();
//	}
}
