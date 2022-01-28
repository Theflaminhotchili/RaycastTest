import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.io.File;
import java.util.Scanner;
public class Mesh {
    ArrayList <Vector3> vertices;
    ArrayList <Triangle> triangles;
    ArrayList <Vector3> normals;
    Vector3 color;
    double albedoVal = 0.17;
    Triangle[] tris;
    Material material;
    public Mesh(String file, Material mat,Vector3 translation,Vector3 rotation){
        Matrix rotMatrix = rotation.createRotationMatrix();
        color = mat.getColor();
        material=mat;
        if (!file.contains(".obj")){return;}
        try {
            File myObj = new File(file);
            Scanner myReader = new Scanner(myObj);
            vertices = new ArrayList<>();
            triangles = new ArrayList<>();
            normals = new ArrayList<>();

            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if (!data.isEmpty()){
                    if (data.charAt(0)=='v' && data.charAt(1)!='n') {
                        Vector3 v = new Vector3();
                        int index2=0;
                        for (int i=0; i<3; i++) {
                            int index1 = data.indexOf(' ', index2);
                            index2 = data.indexOf(' ', index1 + 1);
                            String vert;
                            if (i==2){
                                vert = (String) data.subSequence(index1 + 1, data.length()-1);
                            } else {
                                vert = (String) data.subSequence(index1 + 1, index2);
                            }
                            if(i==0){
                                if (!vert.isEmpty()) {
                                    v.setX(Double.parseDouble(vert));
                                }
                            }
                            else if(i==1){
                                if (!vert.isEmpty()) {
                                    v.setY(Double.parseDouble(vert));
                                }
                            }
                            else if(i==2){
                                if (!vert.isEmpty()) {
                                    v.setZ(Double.parseDouble(vert));
                                    v.setZ(v.getZ());
                                }
                            }
                        }
                        v.rotate(rotMatrix);
                        vertices.add(v);

                        //System.out.println(vertex.size());
                    }else if (data.charAt(0)=='v' && data.charAt(1)=='n') {
                        double x = 0, y = 0, z = 0;
                        int index2=0;
                        for (int i=0; i<3; i++) {
                            int index1 = data.indexOf(' ', index2);
                            index2 = data.indexOf(' ', index1 + 1);
                            String vert;
                            if (i==2){
                                vert = (String) data.subSequence(index1 + 1, data.length());//-1);
                            } else {
                                vert = (String) data.subSequence(index1 + 1, index2);
                            }
                            if(i==0){
                                if (!vert.isEmpty()) {
                                    x = Double.parseDouble(vert);
                                }
                            }
                            else if(i==1){
                                if (!vert.isEmpty()) {
                                    y = Double.parseDouble(vert);
                                }
                            }
                            else if(i==2){
                                if (!vert.isEmpty()) {
                                    z = Double.parseDouble(vert);
                                }
                            }
                        }
                        Vector3 n = new Vector3(x,y,z);
                        n.rotate(rotMatrix);
                        normals.add(n);


                    }else if (data.charAt(0)=='f') {
                        Triangle tri = new Triangle();
                        int index2 = 0;
                        for (int i = 0; i < 3; i++) {
                            int index1 = data.indexOf(' ', index2);
                            index2 = data.indexOf(' ', index1 + 1);
                            String vert;
                            String norm;
                            if (i == 2) {
                                vert = (String) data.subSequence(index1 + 1, data.length()-1);
                            } else {
                                vert = (String) data.subSequence(index1 + 1, index2);
                            }
                            if (i==2){
                                norm = (String) data.subSequence(index1+1, data.length());
                                norm = norm.substring(norm.indexOf("/")+2,norm.length());
                                tri.setNormal(normals.get(Integer.parseInt(norm)-1));

                            }
                            vert = vert.substring(0,vert.indexOf('/'));
                            if (i == 0) {
                                if(!vert.isEmpty()) {
                                    tri.setVert(vertices.get(Integer.parseInt(vert)-1), 0);
                                }
                            } else if (i == 1) {
                                if(!vert.isEmpty()) {
                                    tri.setVert(vertices.get(Integer.parseInt(vert)-1),1);
                                }
                            } else if (i == 2) {
                                if(!vert.isEmpty()) {
                                    tri.setVert(vertices.get(Integer.parseInt(vert)-1),2);
                                }
                            }


                        }
                        tri.setMaterial(material);
                        tri.setAlbedoVal(0.17);
                        triangles.add(tri);
                        //System.out.println(triangles.size());
                        System.out.println(triangles.get(0));
                    }
                }
                tris = new Triangle[triangles.size()];
                for (int i=0; i< tris.length; i++) {
                    tris[i] = triangles.get(i);;
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        for (int i = 0; i < tris.length; i++) {
            tris[i].translate(translation);
            //System.out.println(tris[i]);
        }

    }

    public Triangle[] getTris() {
        return tris;
    }
    public Vector3 getAlbedo(){return color.multiplyScalar(albedoVal);}

    public ArrayList<Vector3> getVertices() {
        return vertices;
    }

    public boolean isTriInMesh(Triangle tri){
        for (int i = 0; i < tris.length; i++) {
            if (tri == tris[i]){
                return true;
            }
        }
        return false;
    }
}
