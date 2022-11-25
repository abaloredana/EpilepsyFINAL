/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author andre
 */
public class Db {

    private static OutputStream outputStream = null;
    private static InputStream inputStream = null;
    private static ObjectOutputStream objectOutputStream = null;
    private static ObjectInputStream objectInputStream = null;
    private static Socket socket = null;

    public Db() {
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public ObjectOutputStream getObjectOutputStream() {
        return objectOutputStream;
    }

    public ObjectInputStream getObjectInputStream() {
        return objectInputStream;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setOutputStream(OutputStream outputStream) {
        Db.outputStream = outputStream;
    }

    public void setInputStream(InputStream inputStream) {
        Db.inputStream = inputStream;
    }

    public void setObjectOutputStream(ObjectOutputStream objectOutputStream) {
        Db.objectOutputStream = objectOutputStream;
    }

    public void setObjectInputStream(ObjectInputStream objectInputStream) {
        Db.objectInputStream = objectInputStream;
    }

    public void setSocket(Socket socket) {
        Db.socket = socket;
    }

}
