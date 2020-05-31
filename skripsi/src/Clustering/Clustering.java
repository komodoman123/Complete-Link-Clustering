package Clustering;

import java.util.*;

public class Clustering
{


    public Cluster performClustering(double[][] distances, String[] clusterNames) {

        checkArguments(distances, clusterNames);
        /* Setup model */
        List<Cluster> clusters = createClusters(clusterNames);
        DistanceMap linkages = createLinkages(distances, clusters);

        /* Process */
        HierarchyBuilder builder = new HierarchyBuilder(clusters, linkages);
        while (!builder.isTreeComplete())
        {
            builder.agglomerate();
        }

        return builder.getRootCluster();
    }



    private void checkArguments(double[][] distances, String[] clusterNames)
    {
        if (distances == null || distances.length == 0
                || distances[0].length != distances.length)
        {
            throw new IllegalArgumentException("Invalid distance matrix");
        }
        if (distances.length != clusterNames.length)
        {
            throw new IllegalArgumentException("Invalid cluster name array");
        }

        int uniqueCount = new HashSet<String>(Arrays.asList(clusterNames)).size();
        if (uniqueCount != clusterNames.length)
        {
            throw new IllegalArgumentException("Duplicate names");
        }
    }



    private DistanceMap createLinkages(double[][] distances,
                                       List<Cluster> clusters)
    {
        DistanceMap linkages = new DistanceMap();
        for (int col = 0; col < clusters.size(); col++)
        {
            for (int row = col + 1; row < clusters.size(); row++)
            {
                ClusterPair link = new ClusterPair();
                Cluster lCluster = clusters.get(col);
                Cluster rCluster = clusters.get(row);
                link.setLinkageDistance(distances[col][row]);
                link.setLeft(lCluster);
                link.setRight(rCluster);
                linkages.add(link);
            }
        }
        return linkages;
    }

    private List<Cluster> createClusters(String[] clusterNames)
    {
        List<Cluster> clusters = new ArrayList<Cluster>();
        for (String clusterName : clusterNames)
        {
            Cluster cluster = new Cluster(clusterName);
            clusters.add(cluster);
        }
        return clusters;
    }



}

/**
 @Override
 public List<Clustering.Cluster> performFlatClustering(double[][] distances,
 String[] clusterNames,  Double threshold) {

 checkArguments(distances, clusterNames);

List<Clustering.Cluster> clusters = createClusters(clusterNames);
    Clustering.DistanceMap linkages = createLinkages(distances, clusters);


    Clustering.HierarchyBuilder builder = new Clustering.HierarchyBuilder(clusters, linkages);
        return builder.flatAgg( threshold);
                }


 private List<Clustering.Cluster> createClusters(String[] clusterNames, double[] weights)
 {
 List<Clustering.Cluster> clusters = new ArrayList<Clustering.Cluster>();
 for (int i = 0; i < weights.length; i++)
 {
 Clustering.Cluster cluster = new Clustering.Cluster(clusterNames[i]);
 cluster.setDistance(new Clustering.Distance(0.0));
 clusters.add(cluster);
 }
 return clusters;
 }


 */


/**
 @Override
 public Clustering.Cluster performWeightedClustering(double[][] distances, String[] clusterNames,
 double[] weights)
 {

 checkArguments(distances, clusterNames);

 if (weights.length != clusterNames.length)
 {
 throw new IllegalArgumentException("Invalid weights array");
 }


List<Clustering.Cluster> clusters = createClusters(clusterNames, weights);
    Clustering.DistanceMap linkages = createLinkages(distances, clusters);

    Clustering.HierarchyBuilder builder = new Clustering.HierarchyBuilder(clusters, linkages);
        while (!builder.isTreeComplete())
                {
                builder.agglomerate(linkageStrategy);
                }

                return builder.getRootCluster();
                }
 */