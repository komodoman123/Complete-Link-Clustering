package Clustering;

import java.io.IOException;

public class test {


    public static void main(String[] args) throws IOException {
        createSampleCluster();
    }

    private static Cluster createSampleCluster() throws IOException {
        TfidfCalculation obj = new TfidfCalculation();
        double[][] distances = obj.calculateMatrix();

        String[] names = obj.returnFileNames();

        for (int i = 0; i < names.length; i++) {
            System.out.println(names[i]);
        }

        Clustering alg = new Clustering();
        Cluster cluster = alg.performClustering(distances, names);
        cluster.print(0);
        return cluster;

    }
}



/*
double[][] distances = new double[][] {
                { 0, 17, 21, 31, 23 },
                { 17, 0, 30, 34, 21},
                { 21, 30, 0, 28, 39},
                { 31, 34, 28, 0, 43 },
                { 23, 21, 39, 43, 0 }};
 */