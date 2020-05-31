package Clustering;

public class ClusterPair implements Comparable<ClusterPair> {

    private Cluster leftCluster;
    private Cluster rightCluster;
    private Double linkageDistance;

    public ClusterPair(){

    }

    public ClusterPair(Cluster left, Cluster right, Double distance){
        rightCluster=right;
        leftCluster=left;
        linkageDistance=distance;
    }

    public Cluster getOtherCluster(Cluster c){

        return leftCluster==c? rightCluster : leftCluster;
    }

    public Cluster getLeft(){
        return leftCluster;
    }

    public Cluster getRight(){
        return rightCluster;
    }

    public void setRight(Cluster rightCluster){
        this.rightCluster=rightCluster;
    }

    public void setLeft(Cluster leftCluster){
        this.leftCluster=leftCluster;
    }

    public Double getLinkageDistance() {
        return linkageDistance;
    }

    public void setLinkageDistance(Double linkageDistance) {
        this.linkageDistance = linkageDistance;
    }

    public ClusterPair reverse() {
        return new ClusterPair(getRight(), getLeft(), getLinkageDistance());
    }

    @Override
    public int compareTo(ClusterPair o) {
        int result;
        if (o == null || o.getLinkageDistance() == null) {
            result = -1;
        } else if (getLinkageDistance() == null) {
            result = 1;
        } else {
            result = getLinkageDistance().compareTo(o.getLinkageDistance());
        }

        return result;
    }

    public Cluster agglomerate(int clusterIdx) {
        return agglomerate("clstr#" + clusterIdx);
    }

    public Cluster agglomerate(String name) {
        Cluster cluster = new Cluster(name);
        cluster.setDistance(new Distance(getLinkageDistance()));
        //cluster baru bisa track nama leaf children
        cluster.appendLeafNames(leftCluster.getLeafNames());
        cluster.appendLeafNames(rightCluster.getLeafNames());
        cluster.addChild(leftCluster);
        cluster.addChild(rightCluster);
        leftCluster.setParent(cluster);
        rightCluster.setParent(cluster);


        return cluster;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (leftCluster != null) {
            sb.append(leftCluster.getName());
        }
        if (rightCluster != null) {
            if (sb.length() > 0) {
                sb.append(" + ");
            }
            sb.append(rightCluster.getName());
        }
        sb.append(" : ").append(linkageDistance);
        return sb.toString();
    }
}
