package com.udelvr.shipment;

public class Location {
    private String type;
    private double[] coordinates = new double[2];

    public Location()
    {
    }

    public Location(String type,double[] coordinates) {
        this.type = type;
        this.coordinates=coordinates;
    }

    public Location(double Longitude, double Latitude) {
        this.type = "Point";
        this.coordinates[0]  = Longitude;
        this.coordinates[1]  = Latitude;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String[] getCoordinates() {
        String[] coOrd = new String[2];
        coOrd[1] = Double.toString(coordinates[0]);
        coOrd[0] = Double.toString(coordinates[1]);
        return coOrd;
    }

    public void setCoordinates(String[] coordinates) {
            this.coordinates[0]  = Double.parseDouble(coordinates[1]);
            this.coordinates[1]  = Double.parseDouble(coordinates[0]);
    }
}
