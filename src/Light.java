public class Light {
    double intensity;
    Vector3 position;

    public Light(){
        intensity=4;
        position = new Vector3(0,0,0);
    }

    public Light (double setIntensity, Vector3 setPos){
        intensity = setIntensity;
        position = setPos;
    }

    //======================================================================
    //Accessors
    public Vector3 getPosition() {
        return position;
    }

    public double getIntensity() {
        return intensity;
    }
}
