public class Sphere {
    Vector3 position, color;
    double radius;
    double albedoVal = 0.18;
    double reflective = 0.2;
    double ior = 1.3;
    public Sphere(){
        position = new Vector3(0,0,0);
        radius = 1;
        color = new Vector3(00,2,255);
    }

    public  Sphere (Vector3 pos, double rad,Vector3 col) {
        position = pos;
        radius = rad;
        color = new Vector3(20, 0, 255);
        color = col;
    }

    public  Sphere (Vector3 pos, double rad, Material mat) {
        position = pos;
        radius = rad;
        color = mat.getColor();
        reflective = mat.getReflective();

        System.out.println(reflective);

    }


    //======================================================================
    //Accessors

    public Vector3 getPosition() {
        return position;
    }

    public double getRadius() {
        return radius;
    }

    public Vector3 getColor() {
        return color;
    }

    public Vector3 getColorBalance(){return (color.multiplyScalar(1.0/255.0));}//rgbs from 0-1 instead of 255;

    public double getReflective() {
        return reflective;
    }

    public double getIor() {
        return ior;
    }

    public int getColorInt(){return color.getRGB();}

    public Vector3 getAlbedo(){return color.multiplyScalar(albedoVal);}

    public double getAlbedoVal() {
        return albedoVal;
    }

    @Override
    public String toString() {
        return "Sphere{" +
                "position=" + position.getX() +
                ", color=" + color +
                ", radius=" + radius +
                ", albedoVal=" + albedoVal +
                '}';
    }

    //======================================================================
    //Other Functions
    public boolean isEquals (Sphere target){
        if(//(position.isEquals(target.getPosition())==true)&&
                (radius==target.getRadius())){
            return true;
        }return false;
    }

    public void setColor(Vector3 color) {
        this.color = color;
    }
}
