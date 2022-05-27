import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;


public class SpatialOctreeNode extends OctreeNode{
    //ArrayList<Vector3> extents;
    ArrayList<Triangle> myTris;
    Vector3 extents;
    Vector3 minCoords;
    Vector3 maxCoords;
    Vector3[] vertices;
    BoundingVolume myBV;

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

    public SpatialOctreeNode(OctreeNode parent, Triangle triangle){
        super();
        setType(IS_LEAF);
        myTris = new ArrayList<Triangle>();
        myTris.add(triangle);
        vertices = triangle.getVerts();
        myBV = new BoundingVolume(vertices);
    }

    public SpatialOctreeNode(Vector3 minCoords, Vector3 MaxCoords, Mesh trisSource){
        super();
        setType(IS_TRUNK);
        myTris = new ArrayList<Triangle>(trisSource.triangles);
        this.minCoords = minCoords;
        this.maxCoords = MaxCoords;
        extents = maxCoords.subtractVector(minCoords);
        navigateDown();
        System.out.println(myTris.size());
        makeVertsArray();
        myBV = new BoundingVolume(vertices);


    }

    public ArrayList<Triangle> getMyTris() {
        return myTris;
    }

    private void makeVertsArray(){
        ArrayList<Vector3> verts = new ArrayList<Vector3>();
        for (int i = 0; i < myTris.size(); i++) {
            verts.addAll(myTris.get(i).getVertsArrayList());
        }
        vertices = verts.toArray(new Vector3[0]);
    }

    public ArrayList<Triangle> navigateDown(){
        if(myTris.size()<9){
            ArrayList<SpatialOctreeNode> branches = new ArrayList<SpatialOctreeNode>();
            for (int i = 0; i < myTris.size(); i++) {
                branches.add(new SpatialOctreeNode(this, myTris.get(i)));
            }
            setMyBranches(branches);
            System.gc();
            System.out.println("aooa");
            return myTris;
        }

        if(myTris.size()<=1){
            setType(IS_LEAF);
            System.out.println("aoaoa");
            return myTris;
        }else {

            //Vector3 midpoint = minCoords.addVector(extents.multiplyScalar(1/2.0));
            ArrayList<Triangle> tris = new ArrayList<Triangle>(myTris);
            ArrayList<SpatialOctreeNode> branches = new ArrayList<SpatialOctreeNode>();

            branches.add(new SpatialOctreeNode(this, minCoords, minCoords.addVector(extents.multiplyScalar(1/2.0))));
            branches.add(new SpatialOctreeNode(this, minCoords.addComponents(extents.getX() / 2.0, 0, 0),
                    minCoords.addVector(extents.multiplyScalar(1/2.0)).addComponents(extents.getX() / 2.0, 0, 0)));
            branches.add(new SpatialOctreeNode(this, minCoords.addComponents(0, extents.getY() / 2.0, 0),
                    minCoords.addVector(extents.multiplyScalar(1/2.0)).addComponents(0, extents.getY() / 2.0, 0)));
            branches.add(new SpatialOctreeNode(this, minCoords.addComponents(extents.getX() / 2.0, extents.getY() / 2.0, 0),
                    minCoords.addVector(extents.multiplyScalar(1/2.0)).addComponents(extents.getX() / 2.0, extents.getY() / 2.0, 0)));
            branches.add(new SpatialOctreeNode(this, minCoords.addVector(extents.multiplyScalar(1/2.0)), maxCoords));
            branches.add(new SpatialOctreeNode(this, minCoords.addComponents(0, 0, extents.getZ() / 2.0),
                    minCoords.addVector(extents.multiplyScalar(1/2.0)).addComponents(0, 0, extents.getZ() / 2.0)));
            branches.add(new SpatialOctreeNode(this, minCoords.addComponents(0, extents.getY() / 2.0, extents.getZ() / 2.0),
                    minCoords.addVector(extents.multiplyScalar(1/2.0)).addComponents(0, extents.getY() / 2.0, extents.getZ() / 2.0)));
            branches.add(new SpatialOctreeNode(this, minCoords.addComponents(extents.getX() / 2.0, 0, extents.getZ() / 2.0),
                    minCoords.addVector(extents.multiplyScalar(1/2.0)).addComponents(extents.getX() / 2.0, 0, extents.getZ() / 2.0)));

//        branches.add(new SpatialOctreeNode(this,minCoords,midpoint));
//        branches.add(new SpatialOctreeNode(this,minCoords.addVector(new Vector3(extents.getX()/2.0,0,0)),
//                        midpoint.addVector(new Vector3(extents.getX()/2.0,0,0))));
//        branches.add(new SpatialOctreeNode(this,minCoords.addVector(new Vector3(0,extents.getY()/2.0,0)),
//                        midpoint.addVector(new Vector3(0,extents.getY()/2.0,0))));
//        branches.add(new SpatialOctreeNode(this,minCoords.addVector(new Vector3(extents.getX()/2.0,extents.getY()/2.0,0)),
//                        midpoint.addVector(new Vector3(extents.getX()/2.0,extents.getY()/2.0,0))));
//        branches.add(new SpatialOctreeNode(this,midpoint,maxCoords));
//        branches.add(new SpatialOctreeNode(this,midpoint.addVector(new Vector3(extents.getX()/2.0,0,0)),
//                        maxCoords.addVector(new Vector3(extents.getX()/2.0,0,0))));
//        branches.add(new SpatialOctreeNode(this,midpoint.addVector(new Vector3(0,extents.getY()/2.0,0)),
//                        maxCoords.addVector(new Vector3(0,extents.getY()/2.0,0))));
//        branches.add(new SpatialOctreeNode(this,midpoint.addVector(new Vector3(extents.getX()/2.0,extents.getY()/2.0,0)),
//                        maxCoords.addVector(new Vector3(extents.getX()/2.0,extents.getY()/2.0,0))));


            for (SpatialOctreeNode s : branches) {
                if (s.setMyTris(tris)) {
                    for (Triangle t : s.getMyTris()) {
                        tris.remove(t);
                    }
                    //excludeList.addAll(s.getMyTris());
                    //s.navigateDown();
                }
            }
            branches.removeIf(spatialOctreeNode -> spatialOctreeNode.getMyTris().size() < 1);
            setMyBranches(branches);
            System.gc();
            for (OctreeNode o:myBranches) {
                ((SpatialOctreeNode)o).navigateDown();
            }
        }
        return myTris;
    }

    public boolean setMyTris(ArrayList<Triangle> tris){
        myTris = new ArrayList<Triangle>();
        ArrayList<Vector3> verts = new ArrayList<Vector3>();
        boolean output = false;
        for (int i = 0; i < tris.size(); i++) {
            //System.out.println(t.getPosition()+""+(minCoords+" "+maxCoords));
            if(tris.get(i).inBounds(minCoords,maxCoords)){
                myTris.add(tris.get(i));
                verts.addAll(tris.get(i).getVertsArrayList());
                output = true;
            }
        }
        vertices = verts.toArray(new Vector3[0]);

        myBV = new BoundingVolume(vertices);

        return output;
    }

    public BoundingVolume getMyBV() {
        return myBV;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpatialOctreeNode that = (SpatialOctreeNode) o;
        return Objects.equals(extents, that.extents) && minCoords.equals(that.minCoords) && maxCoords.equals(that.maxCoords);
    }

    @Override
    public int hashCode() {
        return Objects.hash(extents, minCoords, maxCoords);
    }
}
