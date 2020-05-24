package com.example.raidis.Duomenys;

public class Chat {

    private String siuntejas;
    private String gavejas;
    private String zinute;

    public Chat(String siuntejas, String gavejas, String zinute) {
        this.siuntejas = siuntejas;
        this.gavejas = gavejas;
        this.zinute = zinute;
    }

    public Chat() {
    }

    public String getSiuntejas() {
        return siuntejas;
    }

    public void setSiuntejas(String siuntejas) {
        this.siuntejas = siuntejas;
    }

    public String getGavejas() {
        return gavejas;
    }

    public void setGavejas(String gavejas) {
        this.gavejas = gavejas;
    }

    public String getZinute() {
        return zinute;
    }

    public void setZinute(String zinute) {
        this.zinute = zinute;
    }
}
