package src.cnt5106c.handshake;

import java.util.*;

public class PeerInfoClass {
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
