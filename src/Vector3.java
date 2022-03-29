import java.lang.Math;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Vector3 {
    private double x, y, z,mag;
    Vector3 unitVec;
    public  Vector3(){

    }
    public Vector3(double xComp, double yComp, double zComp){
        x = (double)xComp;
        y = (double)yComp;
        z = (double)zComp;
        mag = Math.sqrt(x*x+y*y+z*z);
        //unitVec = new Vector3(x,y,z,mag);
    }

    /*//Called Exclusively for Unit Vectors
    public Vector3(double xComp, double yComp, double zComp,double magnitude){
        x = xComp/magnitude;
        y = yComp/magnitude;
        z = zComp/magnitude;
        mag = Math.sqrt(x*x+y*y+z*z);
        unitVec = null;
    }
     */

    //======================================================================
    //Accessors
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public double getMag() {
        return mag;
    }

    public Vector3 getUnitVec() {
        return new Vector3((x/mag),(y/mag),(z/mag));
        //return unitVec;
    }

    public int getRGB(){
        if (x>255){x=255;}
        if (y>255){y=255;}
        if (z>255){z=255;}

        return (int)Math.round(x)*256*256 + (int)Math.round(y)*256 + (int)Math.round(z);
    }

    public double[] getCoords(){
        return new double[]{x,y,z};
    }

    public int getMaxDirec(){
        double absX = Math.abs(x);
        double absY = Math.abs(y);
        double absZ = Math.abs(z);
        if (absX >= absY && absX>=absZ){
            return 0;
        }else if (absY>=absX && absY>=absZ){
            return 1;
        }else {
            return 2;
        }
    }

    public void setAsColor(){
        if (x>255){x=255;}
        if (y>255){y=255;}
        if (z>255){z=255;}
        if (x<0){x=0;}
        if (y<0){y=0;}
        if (z<0){z=0;}
    }


    //======================================================================
    //Mutators


    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    //======================================================================
    //Vector Math
    public double dotProduct(Vector3 otherVector){
        return (x*otherVector.getX()) + (y*otherVector.getY()) + (z*otherVector.getZ());
    }

    public Vector3 addVector(Vector3 otherVector){
        return new Vector3(x+otherVector.getX(),y+otherVector.getY(),z+otherVector.getZ());
    }

    public Vector3 subtractVector(Vector3 otherVector){
        return new Vector3(x-(otherVector.getX()),y-(otherVector.getY()),z-(otherVector.getZ()));
    }

    public boolean isEquals(Vector3 otherVector){
        if((x==otherVector.getX())&&(y==otherVector.getY())&&(z==otherVector.getZ())){
            return true;
        }else{return false;}
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector3 vector3 = (Vector3) o;
        return Double.compare(vector3.getX(), x) == 0 && Double.compare(vector3.getY(), y) == 0 && Double.compare(vector3.getZ(), z) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    public void setEqualTo(Vector3 otherVector){
        x = otherVector.getX();
        y = otherVector.getY();
        z = otherVector.getZ();
        mag = Math.sqrt(x*x+y*y+z*z);
        //unitVec = new Vector3((x/mag),(y/mag),(z/mag));
    }

    public Vector3 normalize(){
        //use this if you want to recalculate the unit vector
        if (mag == 0){
            new Vector3(1,1,1).normalize();
        }
        return new Vector3((x/mag),(y/mag),(z/mag));
    }

    public Vector3 multiplyScalar(double s){
        return new Vector3(s*x, s*y, s*z);
    }

    public Vector3 multiplyComponents(Vector3 otherVector){
        //use this to multiply each value in a vector by the corresponding value in another vector
        return new Vector3(x * otherVector.getX(), y * otherVector.getY(), z * otherVector.getZ());
    }

    public void getGreatestComponents(Vector3 otherVector){
        x = Math.max(x,otherVector.getX());
        y = Math.max(y, otherVector.getY());
        z = Math.max(z,otherVector.getZ());
    }

    public boolean isGreater(Vector3 otherVector){
        return (otherVector.getX()<=x&&otherVector.getY()<=y&&otherVector.getZ()<=z);
    }

    public boolean isLess(Vector3 otherVector){
        return (otherVector.getX()>=x&&otherVector.getY()>=y&&otherVector.getZ()>=z);
    }

    public void getLeastComponents(Vector3 otherVector){
        x = Math.min(x,otherVector.getX());
        y = Math.min(y, otherVector.getY());
        z = Math.min(z,otherVector.getZ());
    }

    public Vector3 addComponents(Vector3 otherVector){
        //use this to add the corresponding values in each vector
        return new Vector3(x + otherVector.getX(), y + otherVector.getY(), z + otherVector.getZ());
    }

    public Vector3 addComponents(double x, double y, double z){
        //use this to add the corresponding values in each vector
        return new Vector3(this.x + x, this.y + y, this.z +z);
    }

    public Vector3 crossProduct(Vector3 otherVector){
        return new Vector3(y*otherVector.getZ()-z*otherVector.getY(),
                z*otherVector.getX()-x*otherVector.getZ(),
                x*otherVector.getY()-y*otherVector.getX());
    }

    public Matrix createRotationMatrix(){
        Matrix output = new Matrix();
        x = x*Math.PI/180.0;
        y = y*Math.PI/180.0;
        z = z*Math.PI/180.0;
        double[][] rotMatrix= new double[][]{{Math.cos(y)*Math.cos(z),Math.sin(z)*Math.cos(y),-Math.sin(y)},
                {Math.cos(z)*Math.sin(y)*Math.sin(x)-Math.sin(z)*Math.cos(x),Math.sin(z)*Math.sin(y)*Math.sin(x)+Math.cos(z)*Math.cos(x),Math.cos(y)*Math.sin(x)},
                {Math.cos(z)*Math.sin(y)*Math.cos(x)+Math.sin(z)*Math.sin(x),Math.sin(z)*Math.sin(y)*Math.cos(x)-Math.cos(z)*Math.sin(x),Math.cos(y)*Math.cos(x)}
        };
        output.setMatrix(rotMatrix);
        return output;
    }

    public void scaleByMatrix(Vector3 scaleVector){
        Matrix posMatrix = new Matrix();
        posMatrix.setMatrix(new double[][]{{x,y,z}});
        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setMatrix(new double[][]{{scaleVector.getX(),0,0},{0,scaleVector.getY(),0},{0,0,scaleVector.getZ()}});
        //for (int i = 0; i < 3; i++) {
          //  scaleMatrix.setIndex(i,i,scaleVector.getCoords()[i]);
        //}

        double[] newCoords = scaleMatrix.multiplyMatrix(posMatrix).getMatrix()[0];
        x = newCoords[0];
        y = newCoords[1];
        z = newCoords[2];
    }

    public void rotate(Matrix rotMatrix){
        Matrix posMatrix = new Matrix();
        posMatrix.setMatrix(new double[][]{{x,y,z}});
        //System.out.println(posMatrix);

        Matrix newCoords1 = rotMatrix.multiplyMatrix(posMatrix);//.getMatrix()[0];

        double[] newCoords = newCoords1.getMatrix()[0];
        x = newCoords[0];
        y = newCoords[1];
        z = newCoords[2];
    }

    @Override
    public String toString() {
        return "Vector3{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", mag=" + mag +
                '}';
    }
}
