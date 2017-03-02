package com.company;


/**
 * Created by liza on 11/11/2016.
 */
public class ParkingPlace {
    int parkNumber;
    int placeNumber;
    boolean isEmpty;
    boolean isCarPresent;
    boolean permanentClient= false;
    String clientName;
    String numberCar;
    String modelCar;
    String colorCar;
    long enterTime;
    double ammountToPayTemporaryClient =0;
    double ammountToPayPermanentClient= 130;


    public ParkingPlace(int placeNumber, boolean isEmpty, int parkNumber) {
        this.placeNumber = placeNumber;
        this.isEmpty = isEmpty;
        this.parkNumber = parkNumber;
        this.isCarPresent = false;
    }

    public void addCar(int parkNumber, int placeNumber, boolean isEmpty, boolean permanentClient,
                       String clientName, String numberCar, String modelCar, String colorCar, long enterTime) {
        this.parkNumber = parkNumber;
        this.placeNumber = placeNumber;
        this.isEmpty = isEmpty;
        this.isCarPresent= true;
        this.permanentClient = permanentClient;
        this.clientName = clientName;
        this.numberCar = numberCar;
        this.modelCar = modelCar;
        this.colorCar = colorCar;
        this.enterTime = enterTime;
    }

    public void clean() {
        this.isEmpty = true;
        this.permanentClient = false;
        this.clientName = null;
        this.numberCar = null;
        this.modelCar = null;
        this.colorCar = null;
        this.enterTime = 0;
    }

    public void info(){
        System.out.print("Car status: ");
        System.out.print(isCarPresent?"is present; ":"not present; ");
        System.out.println("Car number: "+ numberCar+"; Client name: "+ clientName+"; car model: "
                + modelCar+"; car color: "+ colorCar+";" );
    }


}
