/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import BITalino.BitalinoDemo;
import Client.EEGSample;
import Client.Patient;
import GUI.Db;
import GUI.Welcome;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author andre
 */
public class menu {

    static BufferedReader reader;

    private static String username;
    private static String password;
    private static int patient_id;
    private static String MAC;
    private static EEGSample eegSample;
    private static Patient patient;
    private static ArrayList<EEGSample> eegs;
    private static Welcome welcome;


    private static BufferedReader bufferedReader;
    public static boolean v1 = true;
    private static Db db;
    
    static int option = 0;
    public static void main(String[] args) throws Exception {
        
        //option = 0;
        //hola
        reader = new BufferedReader(new InputStreamReader(System.in));
        /*System.out.print("\nHello.\n\nYou are accesing your Bitalino device for Epilepsy monitoring.\n\n");
        option = reader.read();*/
        db = new Db();
        
        connectToServer();

        welcome = new Welcome(db);
        welcome.setVisible(v1);

        /*System.out.print("Username:");
        username = reader.readLine();
        System.out.print("Password:");
        password = reader.readLine();
        outputStream.write(option);*/
        //connectToServer();
        //password = reader.readLine();
        //outputStream.write(option);

/*        switch (option) {
            case 1:
                //patient = newUser();
                break;
            case 2:
                //patient = requestPatient();
                break;
            default:
                System.out.print("Not a valid option");
                break;
            // SALIR DEL PROGRAMA
        }

        patient_id = patient.getId();
        MAC = patient.getMAC();

        System.out.print("\nIf you want to record reading press 1\n");
        System.out.print("If you want to view past readings press 2\n");
        System.out.print("If you want to exit press 3:");
        option = Integer.parseInt(reader.readLine());
        //outputStream.write(option);

        switch (option) {
            case 1:
                recordEEG();
                break;
            case 2:
                eegs = requestEEGSamples();
                System.out.print(eegs);
                break;
            case 3:
                break;
            default:
                System.out.print("Not a valid option");
                break;
        }
        */
    }

    private static void recordEEG() throws IOException {
        System.out.println("Today's date:");
        String dos = reader.readLine();
        System.out.println("Extra Simptoms or observations:(if you do not have any press X)");
        String observations = reader.readLine();
        System.out.println("Your EEG and ELG readings will start now:");
        BitalinoDemo bitalinoDemo = new BitalinoDemo();
        bitalinoDemo.recordSignal(MAC); ////////// ARREGLAR ESPECIFICACIONESSSSS
        ArrayList<Integer> eeg = bitalinoDemo.getList1();
        ArrayList<Integer> elg = bitalinoDemo.getList2();

        eegSample = new EEGSample(eeg, elg, dos, observations, patient_id);
        /*try {
            objectOutputStream.writeObject(eegSample);
        } catch (IOException ex) {
            System.out.println("Unable to write the objects on the server.");
            Logger.getLogger(menu.class.getName()).log(Level.SEVERE, null, ex);
        }*/

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
