package com.company;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;

public class Parking {

    public static List<ParkingPlace> parking = new ArrayList<>(5);
    public static List<ParkingPlace> dataBase = new ArrayList<>();
    static FileWorker fileWorker = new FileWorker();

    public static void main(String[] args) throws IOException {
        Scanner scan = new Scanner(System.in);
        String startWork;
        int inOut;
        int vacantPlaces = 5;
        double parkingIncome = 0;

        buildParking(1);

        System.out.println("Hello!\nThere are " + (parking.size() - vacantPlaces) + " cars now at the parking!");
        showOccupancy();
        System.out.println("Press Y if you are ready to start working! Press N to exit!");
        startWork = scan.next();
        if (startWork.equalsIgnoreCase("n")) {
            return;
        }

        do {
            System.out.println("PRESS 1- to accept car/register a new client. PRESS 0- to charge client/ release car." +
                    " PRESS 2- to get info. PRESS 3- to get financial report.");
            inOut = scan.nextInt();
            switch (inOut) {
                case 0:
                    clientMooveOut();
                    showOccupancy();
                    break;

                case 1:
                    clientMooveIn();
                    showOccupancy();
                    vacantPlaces--;
                    break;

                case 2:
                    System.out.println("To get INFO please insert parameter of search: 1-for car number; 2- for car color," +
                            " 3- for client name");
                    int searchNumber = scan.nextInt();
                    String searchParameter;

                    switch (searchNumber) {
                        case 1:
                            System.out.println("Please insert car number to perform a search");
                            searchParameter = scan.next();
                            for (ParkingPlace nextElement : parking) {
                                if (searchParameter.equalsIgnoreCase(nextElement.numberCar)) {
                                    nextElement.info();
                                }
                            }
                            break;
                        case 2:
                            System.out.println("Please insert car color to perform a search");
                            searchParameter = scan.next();
                            for (ParkingPlace nextElement : parking) {
                                if (searchParameter.equalsIgnoreCase(nextElement.colorCar)) {
                                    nextElement.info();
                                }
                            }
                            break;
                        case 3:
                            System.out.println("Please insert client's name to perform a search");
                            searchParameter = scan.next();
                            for (ParkingPlace nextElement : parking) {
                                if (searchParameter.equalsIgnoreCase(nextElement.clientName)) {
                                    nextElement.info();
                                }
                            }
                            break;

                        default:
                            System.out.println("Wrong parameters!");

                    }
                    System.out.println();
                    showOccupancy();

                case 3:
                    System.out.println("Would you like to get info from Data Base file? Press Y or N;");
                    String answer = scan.next();
                    if (answer.equalsIgnoreCase("y")) {
                        List<String> list = new ArrayList<String>();
                        String s = fileWorker.readFromFile();
                        String line;
                        System.out.println(s);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(s.getBytes())));
                        while ((line = reader.readLine()) != null) {
                            list.add(line);
                        }
                        reader.close();

                        parkingIncome = 0;
                        for (String nextElement : list) {
                            String temp = "";
                            for (int i = 2; nextElement.charAt(i) != '$'; i++) {
                                temp += nextElement.charAt(i);
                            }
                            parkingIncome += Double.parseDouble(temp);
                        }
                        System.out.println("Income: " + parkingIncome + "$;");

                    } else {
                        parkingIncome = 0;
                        for (ParkingPlace nextElement : dataBase) {
                            if (nextElement.permanentClient == true) {
                                parkingIncome += 130;
                            } else {
                                parkingIncome += nextElement.ammountToPayTemporaryClient;
                            }
                        }
                        System.out.println("INCOME of parking is: " + parkingIncome + "$;");
                        int presentCars = 0;
                        int presentPermanentCars = 0;
                        for (ParkingPlace nextElement : parking) {
                            if (nextElement.isCarPresent) {
                                presentCars++;

                                if (nextElement.permanentClient) {
                                    presentPermanentCars++;
                                }
                            }
                        }
                        System.out.println("Present cars: " + presentCars + "; " + presentPermanentCars + "- permanent clients;");
                        System.out.println("Vacant places:" + vacantPlaces);
                        System.out.println();
                    }

            }
            if (vacantPlaces == 0) {
                System.out.println(" We are sorry, all the parking places are busy! Hope to see you next time!");
            }
        }
        while (startWork.equalsIgnoreCase("y"));


    }

    private static void clientMooveOut() {
        Scanner scan = new Scanner(System.in);
        String carNumber;
        System.out.println("Please insert car number:");
        carNumber = scan.next();
        for (ParkingPlace nextElement : parking) {
            if (carNumber.equals(nextElement.numberCar)) {
                if (nextElement.permanentClient == true) {
                    System.out.println("This is a permanent client. Open the gate.");
                    nextElement.info();
                    nextElement.isCarPresent = false;
                } else {
                    long curTime = System.currentTimeMillis();
                    String curStringDate = new SimpleDateFormat("dd.MM.yyyy hh:mm").format(curTime);
                    System.out.println();
                    nextElement.ammountToPayTemporaryClient = ((curTime - nextElement.enterTime) * 1.5) / 10000;
                    System.out.println("INVOICE Date/Time: " + curStringDate + "\nAmount to pay: " +
                            nextElement.ammountToPayTemporaryClient + "$");

                    fileWorker.writeToFile(nextElement);
                    dataBase.add(nextElement);
                    nextElement.clean();
                }
                break;

            } else {
                System.out.println("Inserted car number is not correct! Please insert number again or call the police:)");
            }
        }
    }

    private static void clientMooveIn() {
        Scanner scan = new Scanner(System.in);
        String carNumber;
        String carModel;
        String carColor;
        String clientName;

        System.out.println("Please insert car number:");
        carNumber = scan.next();

        for (ParkingPlace nextElement : parking) {

            if (carNumber.equals(nextElement.numberCar)) {
                System.out.println("This is a permanent client. Open the gate.");
                nextElement.info();
                nextElement.isCarPresent = true;

                long curTime = System.currentTimeMillis();
                if (curTime - nextElement.enterTime >= (15 * 24 * 60 * 60)) {
                    System.out.println("Would you like to extend your subscription for the next month? Press Y or N;");
                    String answer = scan.next();
                    if (answer.equalsIgnoreCase("y")) {
                        nextElement.enterTime = curTime + (curTime - nextElement.enterTime);

                        fileWorker.writeToFile(nextElement);
                        dataBase.add(nextElement);
                    }
                }
                return;
            }
        }

        for (ParkingPlace nextElement : parking) {
            if (nextElement.isEmpty == true) {
                long curTime = System.currentTimeMillis();
                String curStringDate = new SimpleDateFormat("dd.MM.yyyy hh:mm").format(curTime);

                System.out.println("Please insert client name:");
                clientName = scan.next();
                System.out.println("Please insert car model:");
                carModel = scan.next();
                System.out.println("Please insert car color:");
                carColor = scan.next();

                System.out.println("Please note, our rates are: 1 hour - 1.5$. 1 month subscription- 130$");
                System.out.println("Would you like to acquire a subscription for 1 month period? Press Y or N");
                String suscribtion = scan.next();

                if (suscribtion.equalsIgnoreCase("y")) {
                    nextElement.addCar(nextElement.parkNumber, nextElement.placeNumber, false, true, clientName,
                            carNumber, carModel, carColor, curTime);
                    System.out.println();
                    System.out.println("CLIENT'S CARD\n Car model: " + carModel + "; Registration number: " + carNumber +
                            ".\n Entery Date/Time: " + curStringDate);
                    System.out.println();

                    fileWorker.writeToFile(nextElement);
                    dataBase.add(nextElement);
                } else {
                    nextElement.addCar(nextElement.parkNumber, nextElement.placeNumber, false, false, clientName,
                            carNumber, carModel, carColor, curTime);
                    System.out.println();
                    System.out.println("CLIENT'S CARD\n Car model: " + carModel + "; Registration number: " + carNumber +
                            ".\n Entery Date/Time: " + curStringDate);
                    System.out.println();
                }
                break;
            }

        }
    }

    private static void buildParking(int numberOfParking) {
        ParkingPlace place1 = new ParkingPlace(1, true, numberOfParking);
        ParkingPlace place2 = new ParkingPlace(2, true, numberOfParking);
        ParkingPlace place3 = new ParkingPlace(3, true, numberOfParking);
        ParkingPlace place4 = new ParkingPlace(4, true, numberOfParking);
        ParkingPlace place5 = new ParkingPlace(5, true, numberOfParking);

        parking.add(place1);
        parking.add(place2);
        parking.add(place3);
        parking.add(place4);
        parking.add(place5);
    }

    private static void showOccupancy() {
        for (ParkingPlace nextElement : parking) {
            if (nextElement.permanentClient == true) {
                long curTime = System.currentTimeMillis();
                if (curTime - nextElement.enterTime >= (30 * 24 * 60 * 60)) {
                    nextElement.clean();
                }
            }
        }
        System.out.println("Occupancy STATUS:");
        for (ParkingPlace nextElement : parking) {
            System.out.print("Place № " + nextElement.placeNumber);
            if (nextElement.isEmpty == true) {
                System.out.println(" is empty;");
            } else {
                System.out.print(" is occupied: ");
                nextElement.info();
            }
        }
        System.out.println();
    }
}
