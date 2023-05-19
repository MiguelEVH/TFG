package com.example.tfg.classes;

public class WorkoutExercise {

    private String id;
    private String name;
    private String url;
    private int image;

    //Constructor vacío
    public WorkoutExercise() {
    }

    //Constructor parametrizado
    public WorkoutExercise(String id, String name, String url, int image) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.image = image;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
