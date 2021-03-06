import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class OctreeNode {
    OctreeNode[] myBranches;
    int type;
    OctreeNode myParent;

    static final int IS_TRUNK = 0;
    static final int IS_BRANCH = 1;
    static final int IS_LEAF = 2;

    public OctreeNode(){
        myBranches = new OctreeNode[8];
        type = IS_TRUNK;
    }

    public OctreeNode(OctreeNode parent){
        myParent = parent;
        type = myParent.IS_BRANCH;
        myBranches = new OctreeNode[8];
    }

    public void setMyBranches(OctreeNode[] myBranches) {
        if (myBranches.length == 8){
            this.myBranches = myBranches;
        }else if (myBranches.length<8){
            this.myBranches = new OctreeNode[myBranches.length];
            for (int i = 0; i < myBranches.length; i++) {
                this.myBranches[i] = myBranches[i];
            }
        }
    }

    public void setMyBranches(ArrayList<SpatialOctreeNode> myBranches) {
        this.myBranches = new OctreeNode[myBranches.size()];
        for (int i = 0; i < myBranches.size(); i++) {
            this.myBranches[i] = myBranches.get(i);
        }
    }

    public void setMyParent(OctreeNode myParent) {
        this.myParent = myParent;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void addChild(OctreeNode newChild){
        for (int i = 0; i < 8; i++) {
            if(Objects.isNull(myBranches[i])){
                myBranches[i] = newChild;
                break;
            }
        }
    }

    public void addChildAtIndex(OctreeNode newChild, int index){
        myBranches[index] = newChild;
    }

    public OctreeNode getMyParent() {
        return myParent;
    }

    public OctreeNode[] getMyBranches() {
        return myBranches;
    }

    public int getType() {
        return type;
    }
}
