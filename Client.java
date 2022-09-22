package Project2;

import org.lwjgl.Sys;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Client {
    private Socket socket;
    public DataInputStream dataInputStream;
    public DataOutputStream dataOutputStream;
    public PrintWriter printWriter;
//    private ClientThread clientThread;
    public int playerID;

    public String inputString;

    public Client(String address, int port) {
        System.out.println("---Client---");
        try {
            socket = new Socket(address, port);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream((socket.getOutputStream()));
            printWriter = new PrintWriter(socket.getOutputStream(), true);
            playerID = dataInputStream.readInt();
            System.out.println("Connected to server as Player #" + playerID);
        } catch (Exception e) {
            System.out.println("Exception in Client constructor");
            e.printStackTrace();
        }
    }

    public void disconnect() {
      try {
        dataInputStream.close();
        dataOutputStream.close();
        socket.close();
      } catch(IOException e) { e.printStackTrace();}
    }
}