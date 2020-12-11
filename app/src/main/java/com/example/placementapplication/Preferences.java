package com.example.placementapplication;

public class Preferences {

    private int prefID;
    private int userID;
    private String paid;
    private String type;
    private String subject;
    private int miles;
    private int relocate;

    public Preferences(int prefID, int userID, String paid, String type, String subject, int miles, int relocate) {
        this.prefID = prefID;
        this.userID = userID;
        this.paid = paid;
        this.type = type;
        this.subject = subject;
        this.miles = miles;
        this.relocate = relocate;
    }
}

