package Clustering;

/**
 * kelas distance
 * representasi dari jarak
 */



public class Distance implements Comparable<Distance> {

    private Double distance;


    public Distance() {
        this(0.0);
    }


    public Distance(Double distance) {
        this.distance = distance;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }


    public boolean isNaN() {
        return distance == null || distance.isNaN();
    }

    @Override
    public int compareTo(Distance distance) {
        if(distance==null){
            return 1;
        }
        else {
            return getDistance().compareTo(distance.getDistance());
        }
        //return distance == null ? 1 : getDistance().compareTo(distance.getDistance());
    }

    @Override
    public String toString() {
        return String.format("distance : %.2f", distance);
    }
}
