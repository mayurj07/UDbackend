package com.udelvr.shipment;


public class Computation {

    // distance calculation in Miles
    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    public static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    public static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    public static float calculateAmount(double sLatitude, double sLongitude, double dLatitude, double dLongitude, String packageWeight) {
        double weight = Double.parseDouble(packageWeight);

        //calculate distance between 2 points
        double dis = distance(sLatitude,sLongitude,dLatitude,dLongitude);
        double a = weight * dis;
        float amount =  (float) a;
        return amount;
    }

}
