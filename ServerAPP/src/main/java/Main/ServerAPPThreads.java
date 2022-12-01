/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;

import Client.*;
import db.interfaces.*;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author andre
 */
public class ServerAPPThreads implements Runnable {

    private static Socket socket;
    private static DBManager dbManager;
    private static PatientManager patientManager;
    private static EEGManager EEGManager;

    public ServerAPPThreads(Socket socket, DBManager dbManager) {
        this.socket = socket;
        this.dbManager = dbManager;
    }

    @Override
    public void run() {
        patientManager = dbManager.getPatient();
        EEGManager = dbManager.getEEGSample();
        InputStream inputStream;
        OutputStream outputStream;
        ObjectInputStream objectInputStream = null;
        ObjectOutputStream objectOutputStream = null;
        Patient patient;
        EEGSample eegSample;
        ArrayList<EEGSample> eegs = null;

        try {
            inputStream = socket.getInputStream();
            System.out.println("Connection from the direction " + socket.getInetAddress());
            objectInputStream = new ObjectInputStream(inputStream);
            outputStream = socket.getOutputStream();
            objectOutputStream = new ObjectOutputStream(outputStream);
            int gb = 1;
            while (gb == 1) {
                int op = inputStream.read();
                switch (op) {
                    case 1:
                        gb = inputStream.read();
                        if (gb == 0) {
                            try {
                                patient = (Patient) (objectInputStream.readObject());
                                patientManager.newPatient(patient);
                                patient = patientManager.getPatient(patient.getUsername(), patient.getPassword());
                                objectOutputStream.writeObject(patient);
                            } catch (EOFException ex) {
                                System.out.println("All data have been correctly read.");
                            } catch (IOException | ClassNotFoundException ex) {
                                System.out.println("Unable to read from the client.");
                                Logger.getLogger(ServerAPPThreads.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        break;
                    case 2:
                        gb = inputStream.read();
                        if (gb == 0) {
                            try {
                                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                                String username = bufferedReader.readLine();
                                String password = bufferedReader.readLine();
                                patient = patientManager.getPatient(username, password);
                                objectOutputStream.writeObject(patient);
                            } catch (EOFException ex) {
                                System.out.println("All data have been correctly read.");
                            } catch (IOException ex) {
                                System.out.println("Unable to read from the client.");
                                Logger.getLogger(ServerAPPThreads.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        break;
                }
            }

            int gb2 = 1;
            int gb3;
            File file;
            while (gb2 == 1) {
                int op2 = inputStream.read();
                switch (op2) {
                    case 1:
                        gb3 = 1;
                        while (gb3 == 1) {
                            gb3 = 0;
                            gb2 = inputStream.read();
                            if (gb2 == 0) {
                                try {
                                    eegSample = (EEGSample) (objectInputStream.readObject());
                                    String pathAux = (String) (eegSample.getPatient_id() + "__" + eegSample.getDos());
                                    String path = "C:/sqlite/db/" + pathAux + ".txt";
                                    eegSample.setPath(path);
                                    file = new File(path);
                                    file.createNewFile();
                                    try {
                                        FileWriter fw = new FileWriter(file);
                                        for (int i = 0; i < eegSample.getEeg().size(); i++) {
                                            fw.write(Integer.toString(eegSample.getEeg().get(i)));
                                        }
                                        fw.write("_");
                                        for (int i = 0; i < eegSample.getElg().size(); i++) {
                                            fw.write(Integer.toString(eegSample.getElg().get(i)));
                                        }
                                        fw.close();
                                    } catch (IOException e) {
                                        System.out.println("ERROR WRITING FILE");
                                        e.printStackTrace();
                                    }
                                    EEGManager.newEEGSample(eegSample);
                                } catch (EOFException ex) {
                                    System.out.println("All data have been correctly read.");
                                } catch (IOException | ClassNotFoundException ex) {
                                    System.out.println("Unable to read from the client.");
                                    Logger.getLogger(ServerAPPThreads.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                gb3 = inputStream.read();
                            }
                        }
                        break;
                    case 2:
                        int patient_id = inputStream.read();
                        eegs = EEGManager.getEEGs(patient_id);
                        outputStream.write(eegs.size());
                        for (int i = 0; i < eegs.size(); i++) {
                            objectOutputStream.writeObject(eegs.get(i));
                        }
                        gb3 = 1;
                        while (gb3 == 1) {
                            gb3 = 0;
                            gb2 = inputStream.read();
                            if (gb2 == 0) {
                                int sampleId = inputStream.read();
                                int position = -1;
                                for (int i = 0; i < eegs.size(); i++) {
                                    if (eegs.get(i).getId() == sampleId) {
                                        position = i;
                                    }
                                }
                                file = new File(eegs.get(position).getPath());
                                Scanner myReader = new Scanner(file);
                                String data = myReader.nextLine();
                                String[] parts = data.split("_");
                                myReader.close();
                                ArrayList<Integer> eeg = ConvertToArray(parts[0]);
                                ArrayList<Integer> elg = ConvertToArray(parts[1]);
                                objectOutputStream.writeObject(eeg);
                                objectOutputStream.writeObject(elg);
                                gb3 = inputStream.read();
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerAPPThreads.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private ArrayList<Integer> ConvertToArray(String sample) {
        String replace = sample.replace("[", "");
        String replace1 = replace.replace("]", "");
        String replace2 = replace1.replace(" ", "");
        ArrayList<String> myList = new ArrayList<>(Arrays.asList(replace2.split("")));
        ArrayList<Integer> result1 = new ArrayList<>();
        for (String stringValue : myList) {
            try {
                result1.add(Integer.parseInt(stringValue));
            } catch (NumberFormatException nfe) {
                System.out.println("Could not parse " + nfe);
            }
        }
        return result1;
    }

    private static void releaseResources(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream, Socket socket) {
        try {
            objectInputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(ServerAPPThreads.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            objectOutputStream.close();

        } catch (IOException ex) {
            Logger.getLogger(ServerAPPThreads.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            socket.close();

        } catch (IOException ex) {
            Logger.getLogger(ServerAPPThreads.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
