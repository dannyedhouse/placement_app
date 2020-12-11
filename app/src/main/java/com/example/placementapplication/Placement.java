package com.example.placementapplication;

public class Placement {
    private int placementID;
    private String placementName;
    private String company;
    private String deadline;
    private String type;
    private String salary;
    private String location;
    private String description;
    private String subject;
    private int miles;
    private String paid;
    private String URL;

    public Placement(int ID, String placementName, String company, String deadline, String type, String salary, String location,
                     String description, String subject, int miles, String paid, String URL) {
        this.placementID = ID;
        this.placementName = placementName;
        this.company = company;
        this.deadline = deadline;
        this.type = type;
        this.salary = salary;
        this.location = location;
        this.description = description;
        this.subject = subject;
        this.miles = miles;
        this.paid = paid;
        this.URL = URL;
    }

}
