package com.uog.soemhike.database;

public class Hike {
    public static final String ID = "ID";
    public static final String NAME = "name";
    public static final String LOCATION = "location";
    public static final String DATE = "date";
    public static final String PARKING = "parking";
    public static final String LENGTH = "length";
    public static final String DIFFICULTY = "difficulty";
    public static final String DESCRIPTION = "description";


    private Integer id;
    private String name;
    private String location;
    private String date;
    private String parking;
    private Double length;
    private String difficulty;
    private String description;


    public Hike() {
    }

    public Hike(Integer id, String name, String location, String date, String parking, Double length, String difficulty, String description) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.date = date;
        this.parking = parking;
        this.length = length;
        this.difficulty = difficulty;
        this.description = description;
    }

    public Hike(String name, String location, String date, String parking, Double length, String difficulty, String description) {
        this.name = name;
        this.location = location;
        this.date = date;
        this.parking = parking;
        this.length = length;
        this.difficulty = difficulty;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getParking() {
        return parking;
    }

    public void setParking(String parking) {
        this.parking = parking;
    }

    public double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}


