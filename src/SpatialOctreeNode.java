import java.util.ArrayList;


public class SpatialOctreeNode extends OctreeNode{
    ArrayList<Vector3> Extents;
    ArrayList<Triangle> myTris;

    public SpatialOctreeNode(){
        super();
    }

    public SpatialOctreeNode(OctreeNode parent, Vector3 minCoords, Vector3 MaxCoords){
        super();
        setMyParent(parent);

    }

    public SpatialOctreeNode(Vector3 minCoords, Vector3 MaxCoords, Mesh trisSource){
        super();
        setType(IS_TRUNK);
        myTris = trisSource.triangles;

    }

    public void navigateDown(){
        OctreeNode[] setBranches = new OctreeNode[8];
        ArrayList<Triangle> setTris = new ArrayList<Triangle>();
        for (int i = 0; i < 8; i++) {

        }
    }

}
