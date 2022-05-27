import java.util.ArrayList;

public class BoundingVolume {
    private static double SQRT_3OVER3 = Math.sqrt(3)/3;
    public static Vector3[] planeSetNormals = new Vector3[]{
            new Vector3(1,0,0),
            new Vector3(0,1,0),
            new Vector3(0,0,1),
            new Vector3(SQRT_3OVER3,SQRT_3OVER3,SQRT_3OVER3),
            new Vector3(-SQRT_3OVER3,SQRT_3OVER3,SQRT_3OVER3),
            new Vector3(-SQRT_3OVER3,-SQRT_3OVER3,SQRT_3OVER3),
            new Vector3(SQRT_3OVER3,-SQRT_3OVER3,SQRT_3OVER3)};
    double[][] dValues;

    public BoundingVolume(){}

    public BoundingVolume(Vector3[] inputVerts){
        calculateDValues(inputVerts);
    }

    private void calculateDValues(Vector3[] inputVerts){
        dValues = new double[7][2];
        for (int i = 0; i < 7; i++) {
            dValues[i][0]=Double.POSITIVE_INFINITY;
            dValues[i][1]=Double.NEGATIVE_INFINITY;
        }
        double D;
        for (int i = 0; i < inputVerts.length; i++) {
            for (int j = 0; j < 7; j++) {
                D = planeSetNormals[j].dotProduct(inputVerts[i]);
                dValues[j][0] = Math.min(D,dValues[j][0]); //Near
                dValues[j][1] = Math.max(D,dValues[j][1]); //Far
            }
        }
    }

    public double[][] getdValues() {
        return dValues;
    }



}
