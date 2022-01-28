public class Material {
    double opacity, reflective, refraction;
    Vector3 color;
    public Material(){
        opacity = 0;
        reflective = 0.5;
        refraction = 0;
    }

    public Material(Vector3 setColor, double setReflective){
        color = setColor;
        reflective = setReflective;
    }

    public Vector3 getColor() {
        return color;
    }

    public double getReflective() {
        return reflective;
    }
}
