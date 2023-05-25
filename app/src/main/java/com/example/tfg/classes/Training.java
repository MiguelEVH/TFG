package com.example.tfg.classes;

public class Training {

    private String id;
    private String name;
    private String trainingStarts;
    private String trainingEnds;
    private int reservesDone;
    private int capacity;

    public Training() {
    }

    public Training(String id, String name, String trainingStarts, String trainingEnds, int reservesDone, int capacity) {
        this.id = id;
        this.name = name;
        this.trainingStarts = trainingStarts;
        this.trainingEnds = trainingEnds;
        this.reservesDone = reservesDone;
        this.capacity = capacity;
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

    public String getTrainingStarts() {
        return trainingStarts;
    }

    public void setTrainingStarts(String trainingStarts) {
        this.trainingStarts = trainingStarts;
    }

    public String getTrainingEnds() {
        return trainingEnds;
    }

    public void setTrainingEnds(String trainingEnds) {
        this.trainingEnds = trainingEnds;
    }

    public int getReservesDone() {
        return reservesDone;
    }

    public void setReservesDone(int reservesDone) {
        this.reservesDone = reservesDone;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
