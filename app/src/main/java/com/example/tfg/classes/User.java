package com.example.tfg.classes;

import java.util.ArrayList;

public class User {

    private String id;
    private String username;
    private String email;
    private boolean isCoach;
    private int fee;
    private int availableCredits;
    private String boxId;

    //Constructor vacío
    public User() {
    }

    public User(String username) {
        this.username = username;
    }

    //Constructor parametrizado
    public User(String username, String email, boolean isCoach, String boxId, int availableCredits, int fee) {
        this.username = username;
        this.email = email;
        this.isCoach = isCoach;
        this.boxId = boxId;
        this.availableCredits = availableCredits;
        this.fee = fee;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isCoach() {
        return isCoach;
    }

    public void setCoach(boolean coach) {
        isCoach = coach;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public int getAvailableCredits() {
        return availableCredits;
    }

    public void setAvailableCredits(int availableCredits) {
        this.availableCredits = availableCredits;
    }

    public String getBoxId() {
        return boxId;
    }

    public void setBoxId(String boxId) {
        this.boxId = boxId;
    }

}
