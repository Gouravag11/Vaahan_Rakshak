package com.rakshak.vaahan.data;

public class AddUserCar {
    private String regNo;
    private String brand;
    private String model;
    public AddUserCar(String regNo, String brand, String model) {
        this.regNo = regNo;
        this.brand = brand;
        this.model = model;
    }

    public String getRegNo() {
        return regNo;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }


}
