package com.rakshak.vaahan.data;

public class AccDetails {
    private String userID;
    private String Name;
    private String Mob;
    private String Aadhar;
    private String DrLisc;
    private String Addr;

    public String getUserID() {
        return userID;
    }

    public String getName() {
        return Name;
    }

    public String getMob() {
        return Mob;
    }

    public String getAadhar() {
        return Aadhar;
    }

    public String getDrLisc() {
        return DrLisc;
    }

    public String getAddr() {
        return Addr;
    }

    public AccDetails(String userID, String name, String mob, String aadhar, String drLisc, String addr) {
        this.userID = userID;
        Name = name;
        Mob = mob;
        Aadhar = aadhar;
        DrLisc = drLisc;
        Addr = addr;
    }
// Getters and setters
    // Note: Add getters and setters for all fields
}
