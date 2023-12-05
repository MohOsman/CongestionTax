package com.volvo.congestiontax.model;


public  class Vehicle {

    private String type;

    private String registrationNumber;

    public Vehicle(String type, String registrationNumber) {
        this.type = type;
        this.registrationNumber = registrationNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }
}
