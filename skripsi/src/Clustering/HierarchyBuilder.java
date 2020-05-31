package Clustering;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HierarchyBuilder {

    private DistanceMap distances;
    private List<Cluster> clusters;
    private int globalClusterIndex = 0;

    public DistanceMap getDistances() {
        return distances;
    }

    public List<Cluster> getClusters() {
        return clusters;
    }

    public HierarchyBuilder(List<Cluster> clusters, DistanceMap distances) {
        this.clusters = clusters;
        this.distances = distances;
    }



    public void agglomerate() {
        ClusterPair minDistLink = distances.removeFirst();
        if (minDistLink != null) {
            clusters.remove(minDistLink.getRight());
            clusters.remove(minDistLink.getLeft());

            Cluster oldClusterL = minDistLink.getLeft();
            Cluster oldClusterR = minDistLink.getRight();
            Cluster newCluster = minDistLink.agglomerate(++globalClusterIndex);

            for (Cluster iClust : clusters) {
                ClusterPair link1 = findByClusters(iClust, oldClusterL);
                ClusterPair link2 = findByClusters(iClust, oldClusterR);
                ClusterPair newLinkage = new ClusterPair();
                newLinkage.setLeft(iClust);
                newLinkage.setRight(newCluster);
                Collection<Distance> distanceValues = new ArrayList<Distance>();

                if (link1 != null) {
                    Double distVal = link1.getLinkageDistance();

                    distanceValues.add(new Distance(distVal));
                    distances.remove(link1);
                }
                if (link2 != null) {
                    Double distVal = link2.getLinkageDistance();

                    distanceValues.add(new Distance(distVal));
                    distances.remove(link2);
                }

                Distance newDistance = calculateDistance(distanceValues);

                newLinkage.setLinkageDistance(newDistance.getDistance());
                distances.add(newLinkage);
            }
            clusters.add(newCluster);
        }
    }

    public Distance calculateDistance(Collection<Distance> distances) {
        double max = Double.NaN;

        for (Distance dist : distances) {
            if (Double.isNaN(max) || dist.getDistance() > max)
                max = dist.getDistance();
        }
        return new Distance(max);
    }

    private ClusterPair findByClusters(Cluster c1, Cluster c2) {
        return distances.findByCodePair(c1, c2);
    }

    public boolean isTreeComplete() {
        return clusters.size() == 1;
    }

    public Cluster getRootCluster() {
        if (!isTreeComplete()) {
            throw new RuntimeException("No root available");
        }
        return clusters.get(0);
    }

}


/**
 * Returns Flattened clusters, i.e. clusters that are at least apart by a given threshold
 * @param linkageStrategy
 * @param threshold
 * @return flat list of clusters

public List<Clustering.Cluster> flatAgg(LinkageStrategy linkageStrategy, Double threshold)
{
while((!isTreeComplete()) && (distances.minDist() != null) && (distances.minDist() <= threshold))
{
//System.out.println("Clustering.Cluster Distances: " + distances.toString());
//System.out.println("Clustering.Cluster Size: " + clusters.size());
agglomerate(linkageStrategy);
}

//System.out.println("Final MinDistance: " + distances.minDist());
//System.out.println("Tree complete: " + isTreeComplete());
return clusters;
}
 */
