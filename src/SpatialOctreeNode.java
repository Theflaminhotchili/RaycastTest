import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;


public class SpatialOctreeNode extends OctreeNode{
    //ArrayList<Vector3> extents;
    ArrayList<Triangle> myTris;
    Vector3 extents;
    Vector3 minCoords;
    Vector3 maxCoords;

    public SpatialOctreeNode(){
        super();
    }

    public SpatialOctreeNode(OctreeNode parent, Vector3 minCoords, Vector3 MaxCoords){
        super();
        setType(IS_BRANCH);
        setMyParent(parent);
        this.minCoords = minCoords;
        this.maxCoords = MaxCoords;
        extents = maxCoords.subtractVector(minCoords);

    }

    public SpatialOctreeNode(Vector3 minCoords, Vector3 MaxCoords, Mesh trisSource){
        super();
        setType(IS_TRUNK);
        myTris = trisSource.triangles;
        this.minCoords = minCoords;
        this.maxCoords = MaxCoords;
        extents = maxCoords.subtractVector(minCoords);
        navigateDown();


    }

    public void navigateDown(){
        if(myTris.size()<=1){
            setType(IS_LEAF);
            System.out.println("aoaoa");
            return;
        }

        Vector3 midpoint = minCoords.addVector(extents.multiplyScalar(0.5));
        ArrayList<OctreeNode> branches = new ArrayList<OctreeNode>();
        branches.add(new SpatialOctreeNode(this,minCoords,midpoint));
        branches.add(new SpatialOctreeNode(this,minCoords.addVector(new Vector3(extents.getX()/2.0,0,0)),
                        midpoint.addVector(new Vector3(extents.getX()/2.0,0,0))));
        branches.add(new SpatialOctreeNode(this,minCoords.addVector(new Vector3(0,extents.getY()/2.0,0)),
                        midpoint.addVector(new Vector3(0,extents.getY()/2.0,0))));
        branches.add(new SpatialOctreeNode(this,minCoords.addVector(new Vector3(extents.getX()/2.0,extents.getY()/2.0,0)),
                        midpoint.addVector(new Vector3(extents.getX()/2.0,extents.getY()/2.0,0))));
        branches.add(new SpatialOctreeNode(this,midpoint,maxCoords));
        branches.add(new SpatialOctreeNode(this,midpoint.addVector(new Vector3(extents.getX()/2.0,0,0)),
                        maxCoords.addVector(new Vector3(extents.getX()/2.0,0,0))));
        branches.add(new SpatialOctreeNode(this,midpoint.addVector(new Vector3(0,extents.getY()/2.0,0)),
                        maxCoords.addVector(new Vector3(0,extents.getY()/2.0,0))));
        branches.add(new SpatialOctreeNode(this,midpoint.addVector(new Vector3(extents.getX()/2.0,extents.getY()/2.0,0)),
                        maxCoords.addVector(new Vector3(extents.getX()/2.0,extents.getY()/2.0,0))));



        for (int i = 7; i >= 0; i--) {
            if(!((SpatialOctreeNode)branches.get(i)).setMyTris(myTris)){
                branches.remove(i);
            }else{
                ((SpatialOctreeNode) branches.get(i)).navigateDown();
            }
        }
        System.out.println(branches.size());
        setMyBranches(branches);

    }

    public boolean setMyTris(ArrayList<Triangle> tris){
        myTris = new ArrayList<Triangle>();
        boolean output = false;
        for (Triangle t: tris) {
            //System.out.println(t.getPosition()+""+(minCoords+" "+maxCoords));
            if(t.inBounds(minCoords,maxCoords)){
                myTris.add(t);
                output = true;
            }
        }
        return output;
    }

}
