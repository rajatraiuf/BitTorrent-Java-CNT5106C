/**
 * The component to read peerInfo.cfg.
 */

package cnt5106C.config;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Vector;

import cnt5106C.DynamicPeerInfo;
import java.io.IOException;

public class PeerInfo {
	public static ArrayList<DynamicPeerInfo> readPeerInfo(int numOfPieces) {
		ArrayList<DynamicPeerInfo> peers = new ArrayList<DynamicPeerInfo>();
		try {
			BufferedReader configFileReader = new BufferedReader(
				new InputStreamReader(new FileInputStream("C:\\Users\\Rajat Rai\\Projects\\BitTorrent\\BitTorrent-Java-CNT5106C\\src\\cnt5106C\\config\\PeerInfo.cfg")));
			String line = null;
			int index = 0;
			while ((line = configFileReader.readLine()) != null) {
				line = line.trim();

				String[] tokens = line.split(" ");
				peers.add(new DynamicPeerInfo(
					Integer.parseInt(tokens[0].trim()),
					tokens[1].trim(),
					Integer.parseInt(tokens[2].trim()),
					tokens[3].trim().equals("1"),
					numOfPieces,
					index,
					true,
					false));
				index++;
			}
		} catch (IOException e) {
			System.err.println("File not loaded");
		}
		return peers;
	}
}
