package com.example.placementapplication;

public class Profile {
    private int userID;
    private String username;
    private String name;
    private String email;
    private int number;
    private String DOB;
    private String address;
    private String about;
    private String experience;
    private String university;

    public Profile(int userID, String username, String name, String email, int number, String dob, String address, String about, String experience, String university) {
        this.userID = userID;
        this.username = username;
        this.name = name;
        this.email = email;
        this.number = number;
        this.DOB = dob;
        this.address = address;
        this.about = about;
        this.experience = experience;
        this.university = university;
    }
}
