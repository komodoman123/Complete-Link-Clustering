package Clustering;

import java.util.*;

/**
 *kelas distancemap untuk merepresentasikan linkage
 */
public class DistanceMap {

    private Map<String, Item> pairHash;
    private PriorityQueue<Item> data;

    private class Item implements Comparable<Item> {
        final ClusterPair pair;
        final String hash;
        boolean removed = false;

        Item(ClusterPair p) {
            pair = p;
            hash = hashCodePair(p);
        }

        @Override
        public int compareTo(Item o) {
            return pair.compareTo(o.pair);
        }

        @Override
        public String toString() {
            return hash;
        }
    }

    public DistanceMap() {
        data = new PriorityQueue<Item>();
        pairHash = new HashMap<String, Item>();
    }

    public List<ClusterPair> list() {
        List<ClusterPair> l = new ArrayList<ClusterPair>(data.size());
        for (Item clusterPair : data) {
            l.add(clusterPair.pair);
        }
        return l;
    }

    public ClusterPair findByCodePair(Cluster c1, Cluster c2) {
        String inCode = hashCodePair(c1, c2);
        return pairHash.get(inCode).pair;
    }

    public ClusterPair removeFirst() {
        Item poll = data.poll();
        while (poll != null && poll.removed) {
            poll = data.poll();
        }
        if (poll == null) {
            return null;
        }
        ClusterPair link = poll.pair;
        pairHash.remove(poll.hash);
        return link;
    }

    public boolean remove(ClusterPair link) {
        Item remove = pairHash.remove(hashCodePair(link));
        if (remove == null) {
            return false;
        }
        remove.removed = true;

        return true;
    }


    public boolean add(ClusterPair link) {
        Item e = new Item(link);
        Item existingItem = pairHash.get(e.hash);
        if (existingItem != null) {
            System.err.println("hashCode = " + existingItem.hash +
                    " adding redundant link:" + link + " (exist:" + existingItem + ")");
            return false;
        } else {
            pairHash.put(e.hash, e);
            data.add(e);

            return true;
        }
    }


    public Double minDist()
    {
        Item peek = data.peek();
        if (peek != null)
            return peek.pair.getLinkageDistance();
        else
            return null;
    }

    /**
     *
     * hitung ID untuk cluster pair
     */
    private String hashCodePair(ClusterPair link) {
        return hashCodePair(link.getLeft(), link.getRight());
    }

    private String hashCodePair(Cluster lCluster, Cluster rCluster) {
        return hashCodePairNames(lCluster.getName(), rCluster.getName());
    }

    private String hashCodePairNames(String lName, String rName) {
        if (lName.compareTo(rName) < 0) {
            return lName + "~~~" + rName;
        } else {
            return rName + "~~~" + lName;
        }
    }

    @Override
    public String toString() {
        return data.toString();
    }
}
