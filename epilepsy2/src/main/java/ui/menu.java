/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import Client.EEGSample;
import GUI.SocketOb;
import GUI.Welcome;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author andre
 */
public class menu {

    private static Welcome welcome;
    public static boolean v1 = true;
    private static SocketOb db;

    public static void main(String[] args) throws Exception {
        db = new SocketOb();
        connectToServer();
        welcome = new Welcome(db);
        welcome.setVisible(v1);
    }

    private static ArrayList<EEGSample> requestEEGSamples() throws IOException, ClassNotFoundException {
        ArrayList<EEGSample> eegs1 = new ArrayList<>();
        //outputStream.write(patient_id);
        //int eegs_size = inputStream.read();
        //for (int i = 0; i < eegs_size; i++) {
        //  eegs1.add((EEGSample) objectInputStream.readObject());
        //}
        return eegs1;
    }

    private static void connectToServer() {
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
    }

    private static void releaseResources(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream, Socket socket) {
        try {
            objectInputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            objectOutputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
