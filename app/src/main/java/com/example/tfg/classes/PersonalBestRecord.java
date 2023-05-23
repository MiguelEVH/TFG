package com.example.tfg.classes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    //Método que comprueba si un peso es válido
    public boolean validWeight(double weight){
        //Patron de un double
        String regex = "\\d{1,3}+\\.\\d{1,2}";
        //Comprueba que el email introducido es válido
        if(String.valueOf(weight).matches(regex)){
            return true;
        }else{
            return false;
        }
    }
}


