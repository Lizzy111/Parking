package com.company;

import java.io.*;
import java.util.Date;
import java.util.List;

/**
 * Created by liza on 17/11/2016.
 */
public class FileWorker {
    String fileName = "Parking DataBase.csv";
    File file = new File(fileName);
    private static final String COMMA_DELIMITER = ";";

    public void writeToFile(ParkingPlace parkingPlace) {
        try {
            if (!(file.exists())) {
                file.createNewFile();
            }

            StringBuilder s = new StringBuilder();
            String oldFile = readFromFile();
            s.append(oldFile);
            s.append("- ").append(parkingPlace.permanentClient ? parkingPlace.ammountToPayPermanentClient : parkingPlace
                    .ammountToPayTemporaryClient).append("$");
            s.append(COMMA_DELIMITER);
            s.append(" Car number: ").append(parkingPlace.numberCar);
            s.append(COMMA_DELIMITER);
            s.append(" Data: " + new Date());
            // s.append(toString(parkingPlace)); здесь мы превращали все в одну строку и запятые разделители превращались
            // в обычные символы- запятые.
            s.append("\n");
            PrintWriter out = new PrintWriter(file.getAbsoluteFile());

            try {
                out.print(s);
            } finally {
                out.close();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
//
//    public StringBuilder toString(ParkingPlace nextElement){
//        StringBuilder s= new StringBuilder();
//
//
//        return s;
//    }

    public String readFromFile() {
        StringBuilder sb = new StringBuilder();

        try {
            BufferedReader in = new BufferedReader(new FileReader(file.getAbsoluteFile()));

            try {
                String s;
                while ((s = in.readLine()) != null) {
                    sb.append(s + "\n");
                }

            } finally {
                in.close();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }
}
