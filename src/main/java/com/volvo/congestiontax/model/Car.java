package com.volvo.congestiontax.model;

public class Car extends Vehicle {

    public Car(String type, String registrationNumber) {
        super(type, registrationNumber);
    }

    @Override
    public void setType(String type) {
        super.setType(type);
    }

    @Override
    public void setRegistrationNumber(String registrationNumber) {
        super.setRegistrationNumber(registrationNumber);
    }

    @Override
    public String getType() {
        return super.getType();
    }

    @Override
    public String getRegistrationNumber() {
        return super.getRegistrationNumber();
    }
}
