package cnt5106C.config;

import java.io.*;
import java.text.ParseException;
import java.util.Vector;
import java.io.IOException;

public class PeerInfo {

	private class PeerInfoClass {
	    private String peerId;
	    private String peerAddress;
	    private String peerPort;
	    private boolean hasFile;

	    public PeerInfoClass() {
	        this.peerId = "0000";
	        this.peerAddress = "0.0.0.0";
	        this.peerPort = "12345";
	        this.hasFile = false;
	    }

	    public PeerInfoClass(String peerId, String peerAddress, String peerPort, boolean hasFile) {
	        this.peerId = peerId;
	        this.peerAddress = peerAddress;
	        this.peerPort = peerPort;
	        this.hasFile = hasFile;
	    }
	}

	private final Vector<PeerInfoClass> peerInfoVector = new Vector<PeerInfoClass>();

	public void run()  {

		try {
			BufferedReader configFileReader = new BufferedReader(
				new InputStreamReader(new FileInputStream("PeerInfo.cfg")));
			String line = null;
			while ((line = configFileReader.readLine()) != null) {
				line = line.trim();

				String[] tokens = line.split(" ");
				peerInfoVector.addElement(new PeerInfoClass(
				tokens[0].trim(),
				tokens[1].trim(),
				tokens[2].trim(),
				tokens[3].trim() == "1"));
			}
		} catch (IOException e) {
			System.err.println("File not loaded");
		}
	}

	public Vector<PeerInfoClass> getPeerInfo() {
		return new Vector<PeerInfoClass>(peerInfoVector);
	}

	public static void main(String[] args) {
		PeerInfo peerInfo = new PeerInfo();
		peerInfo.run();
	}
}
