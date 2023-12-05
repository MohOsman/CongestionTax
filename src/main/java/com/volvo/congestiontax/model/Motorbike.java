package com.volvo.congestiontax.model;

import com.volvo.congestiontax.model.Vehicle;

public class Motorbike extends Vehicle {

    public Motorbike(String type, String registrationNumber) {
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
}
