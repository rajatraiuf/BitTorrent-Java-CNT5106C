package src.cnt5106c.config;

import java.util.*;
import java.io.*;

public class Config {

    public void Config() {}


    String numberOfPreferredNeighbors;
    String unchokingInterval;
    String optimisticUnchokingInterval;
    String fileName;
    String fileSize;
    String pieceSize;

    void run() {
        Properties cfg = new Properties();

        try {
            InputStream in = new FileInputStream("Common.cfg");
            cfg.load(in);

            numberOfPreferredNeighbors = cfg.getProperty("NumberOfPreferredNeighbors");
            unchokingInterval = cfg.getProperty("UnchokingInterval");
            optimisticUnchokingInterval = cfg.getProperty("OptimisticUnchokingInterval");
            fileName = cfg.getProperty("FileName");
            fileSize = cfg.getProperty("FileSize");
            pieceSize = cfg.getProperty("PieceSize");
            System.out.println(numberOfPreferredNeighbors);
            System.out.println(unchokingInterval);
            System.out.println(optimisticUnchokingInterval);
            System.out.println(fileName);
            System.out.println(fileSize);
            System.out.println(pieceSize);
        }
        catch(Exception e){
            e.printStackTrace();}
    }

    private int getNumberOfPreferredNeighbors() {
        return cfg.getProperty("NumberOfPreferredNeighbors");
    }

    public static int getUnchokingInterval() {
        return cfg.getProperty("UnchokingInterval");
    }

    public static int getOptimisticUnchokingInterval() {
        return cfg.getProperty("OptimisticUnchokingInterval");
    }


    public static String getFileName() {
        return cfg.getProperty("FileName");
    }


    public static int getFileSize() {
        return cfg.getProperty("FileSize");
    }

    public static int getPieceSize() {
        return cfg.getProperty("PieceSize");
    }

    // Main method for local run
	// public static void main(String args[])
	// {
	// 	Config read = new Config();
	// 	read.run();
	// }

}
