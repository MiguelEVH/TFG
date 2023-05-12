package com.example.tfg.classes;

public class User {

    private String username;
    private String email;
    private boolean isCoach;
    private int fee;
    private int availableCredits;
    private String boxId;

    //Constructor vacío
    public User() {
    }

    //Constructor parametrizado
    public User(String username, String email, boolean isCoach, String boxId) {
        this.username = username;
        this.email = email;
        this.isCoach = isCoach;
        this.boxId = boxId;
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

    public boolean isCoach() {
        return isCoach;
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
