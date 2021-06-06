package com.reduse.lol_abyss.entity;

public class Champion implements Comparable<Champion>{

    Integer id;
    String imageName;

    public Champion(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    @Override
    public int compareTo(Champion another) {
        return imageName.compareTo(another.imageName);
    }
}