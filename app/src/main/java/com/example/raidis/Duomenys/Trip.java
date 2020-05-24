package com.example.raidis.Duomenys;

public class Trip {
        public String uid;
        public String destFrom;
        public String destTo;
        public String tripDate;
        public String tripTime;
        public String carName;
        public String carFreeSeats;
        public String kelionesID;

        public Trip() {
        }

        public Trip(String uid, String destFrom, String destTo, String tripDate, String tripTime, String carName, String carFreeSeats, String kelionesID) {
            this.uid = uid;
            this.destFrom = destFrom;
            this.destTo = destTo;
            this.tripDate = tripDate;
            this.tripTime = tripTime;
            this.carName = carName;
            this.carFreeSeats = carFreeSeats;
            this.kelionesID = kelionesID;
        }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDestFrom() {
        return destFrom;
    }

    public void setDestFrom(String destFrom) {
        this.destFrom = destFrom;
    }

    public String getDestTo() {
        return destTo;
    }

    public void setDestTo(String destTo) {
        this.destTo = destTo;
    }

    public String getTripDate() {
        return tripDate;
    }

    public void setTripDate(String tripDate) {
        this.tripDate = tripDate;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getCarFreeSeats() {
        return carFreeSeats;
    }

    public void setCarFreeSeats(String carFreeSeats) {
        this.carFreeSeats = carFreeSeats;
    }

    public String getKelionesID() {
        return kelionesID;
    }

    public void setKelionesID(String kelionesID) {
        this.kelionesID = kelionesID;
    }

    public String getTripTime() {
        return tripTime;
    }

    public void setTripTime(String tripTime) {
        this.tripTime = tripTime;
    }
}
