import java.util.Arrays;

public class Triangle {
    Vector3[] verts;
    Material material;
    double albedoVal = 0.17;
    double reflective = 0.5;
    Vector3 normal = null;
    Vector3 color;
    Mesh myMesh;

    public Triangle(){
        verts = new Vector3[3];
    }

    public  Triangle(Vector3[] setVerts){
        for (int i = 0; i < 3; i++) {
            verts[i] = setVerts[i];
        }
    }

    //======================================================================
    //Accessors
    public Vector3 getNormal(){
        if (normal == null){
            return (verts[1].subtractVector(verts[0])).crossProduct(verts[2].subtractVector(verts[0])).normalize();
        }
        else return normal;
    }


    public Vector3 getVert(int indx){
        return verts[indx];
    }

    public Vector3[] getVerts() {
        return verts;
    }

    public Material getMaterial() {
        return material;
    }

    public double getAlbedoVal() {
        return albedoVal;
    }

    public Vector3 getAlbedo(){return color.multiplyScalar(albedoVal);}

    public double getReflective() {
        return reflective;
    }

    public Vector3 getColorBalance(){return (color.multiplyScalar(1.0/255.0));}//rgbs from 0-1 instead of 255;

    //======================================================================
    //Mutators

    public void setVert(Vector3 vert, int indx){
        verts[indx] = vert;
    }

    public void setNormal(Vector3 normal) {
        this.normal = normal.normalize();
    }

    public void setMaterial(Material material) {
        this.material = material;
        this.color = material.getColor();
        this.reflective = material.getReflective();
    }

    public void setAlbedoVal(double albedoVal) {
        this.albedoVal = albedoVal;
    }

    //======================================================================
    //Function
    public void translate (Vector3 factor){
        for (int i = 0; i < 3; i++) {
            verts[i] = verts[i].addVector(factor);
        }
    }

    public Vector3 getPosition(){
        return new Vector3((verts[0].getX()+verts[1].getX()+verts[2].getX())/3.0,
            (verts[0].getY()+verts[1].getY()+verts[2].getY())/3.0,
                (verts[0].getX()+verts[1].getX()+verts[2].getX())/3.0);
    }

    public boolean inBounds(Vector3 minBounds, Vector3 maxBounds){
        Vector3 myPos = getPosition();
        if(myPos.getX()>=minBounds.getX()&&myPos.getX()<maxBounds.getX()){
            if (myPos.getY()>=minBounds.getY()&&myPos.getY()<maxBounds.getY())
                if (myPos.getZ()>=minBounds.getZ()&&myPos.getX()<maxBounds.getZ())
                    return true;
        }
        return false;
    }

    public String toString() {
        return "Triangle{" +
                "verts=" + Arrays.toString(verts) +
                ", material=" + material + "center = " + ((verts[0].getX()+verts[1].getX()+verts[2].getX()))/3 +
                ", "+ ((verts[0].getY()+verts[1].getY()+verts[2].getY()))/3 +
                ", "+ ((verts[0].getX()+verts[1].getX()+verts[2].getX()))/3 +
                '}';
    }
}
