package com.mirth.connect.simplesender;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class SimpleSenderServerTest {

    private static Socket socket;

	public static void main(String[] args) {
        try {
            socket = new Socket("localhost", 6661);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            byte[] bytes = new byte[9999];
            in.read(bytes);
            System.out.println(new String(bytes));
           
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	 try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
    }

}