public class HitQueueElement {
    public SpatialOctreeNode node;
    public double t;

    public HitQueueElement(SpatialOctreeNode setNode, double setT){
        node = setNode;
        t = setT;
    }
}
