package com.example.raidis.Duomenys;

public class Vartotojas {

    public String uid;
    public String name;
    public String profileImage;
    public String email;
    public String city;

    public Vartotojas(){}

    public Vartotojas(String uid, String name, String profileImage, String email, String city){
        this.uid = uid;
        this.name = name;
        this.profileImage = profileImage;
        this.email = email;
        this.city = city;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
