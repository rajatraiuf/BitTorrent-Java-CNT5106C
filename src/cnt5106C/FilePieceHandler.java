package cnt5106C;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.ClientInfoStatus;
import java.util.Arrays;

import cnt5106C.config.*;
import jdk.vm.ci.meta.JavaKind.FormatWithToString;

public class FilePieceHandler {
		
	
	public static Message filePiece(int remotePeerID, int index) throws IOException {
		int chunkSize = ControlSystem.pieceSize;
		int fileSize = ControlSystem.fileSize;
		
		RandomAccessFile file = new RandomAccessFile("/Users/weinalyu/Dropbox/aTextbook/A_Computer_Network/syllabus.pdf", "rw");
		byte[] messagePayload = new byte[chunkSize];
		int loc = chunkSize * index;
		int bytesRead = file.read(messagePayload, loc, chunkSize);
		file.close();
		
		return Message.actualMessageWrapper(remotePeerID, 
				7, messagePayload);
	}
	
	public static void handle(Message m) throws Exception {
		//TODO this is just for testing
		int bytes = 0;
		int chunkSize = ControlSystem.pieceSize;
		int fileSize = ControlSystem.fileSize;
		byte[] buffer = new byte[chunkSize];
//		String fileName = ControlSystem.fileName;
        

        FileInputStream dataInputStream;
        
		while (fileSize > 0 && (bytes = dataInputStream.read(buffer, 0, (int)Math.min(buffer.length, fileSize))) != -1) {
            fileOutputStream.write(buffer,0,bytes);
            fileSize -= bytes;
            System.out.println("Receive a chunk, file size to be received ");
        }
    }

}
