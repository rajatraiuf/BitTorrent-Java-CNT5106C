package src.cnt5106c.handshake;

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
    public void SendHandShakeMsg(String msg) {
        ObjectOutputStream out;
        try{
            out.writeObject(msg);
            out.flush();
        } catch(IOException ioException) {
            ioException.printStackTrace();
        }
    }
    public int ReceiveHandShake(ObjectInputStream in) throws IOException {
        try {
            HandShake msgIn = (String) in.readObject();
            return msgIn;
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }
    }

}
