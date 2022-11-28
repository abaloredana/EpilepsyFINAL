/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import GUI.SocketOb;
import GUI.Welcome;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author andre
 */
public class menu {

    private static Welcome welcome;
    public static boolean v1 = true;

    public static void main(String[] args) throws Exception {
        SocketOb db;
        db = new SocketOb();
        try {
            db.setSocket(new Socket("localhost", 9000));
            db.setOutputStream(db.getSocket().getOutputStream());
            db.setObjectOutputStream(new ObjectOutputStream(db.getOutputStream()));
            db.setInputStream(db.getSocket().getInputStream());
            db.setObjectInputStream(new ObjectInputStream(db.getInputStream()));
        } catch (IOException ex) {
            System.out.println("It was not possible to connect to the server.");
            System.exit(-1);
            Logger.getLogger(menu.class.getName()).log(Level.SEVERE, null, ex);
        }

        welcome = new Welcome(db);
        welcome.setVisible(v1);
    }

    private static void releaseResources(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream, Socket socket,
            InputStream inputStream, OutputStream outputStream) {
        try {
            objectInputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(menu.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            objectOutputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(menu.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(menu.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            outputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(menu.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            inputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
