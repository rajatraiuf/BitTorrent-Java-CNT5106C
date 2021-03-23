package cnt5106C;

import java.io.*;

public class HandShake implements java.io.Serializable {

    private String header = "P2PFILESHARINGPROJ";
    private byte[] zeroBits = new byte[10];
    private String peerID;
    private static final long serialVersionUID = 11111111111111111L;



    public HandShake(String peerID) {
        this.peerID = peerID;
    }

    public String getHeader() {
        return header;
    }

    public byte[] getZeroBits() {
        return zeroBits;
    }

    public void setZeroBits(byte[] zeroBits) {
        this.zeroBits = zeroBits;
    }

    public String getPeerID() {
        return peerID;
    }

    public void setPeerID(String peerID) {
        this.peerID = peerID;
    }

    public String getHandShakeMsg() {
        String handShakeMsg = getHeader() + "\n" + getZeroBits() + "\n" + getPeerID();
        return handShakeMsg;
    }
}
