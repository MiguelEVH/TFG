package com.example.tfg.classes;

public class PersonalBestRecord {

    private String id;
    private String name;
    private double weight;

    public PersonalBestRecord() {
    }

    public PersonalBestRecord(String id, String name, double weight) {
        this.id = id;
        this.name = name;
        this.weight = weight;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
