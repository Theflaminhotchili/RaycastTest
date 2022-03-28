import java.awt.*;
import java.awt.image.*;
import java.awt.Graphics;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class TestRunner {

    public static ArrayList<Mesh> meshList = new ArrayList<Mesh>();

    public static Material red = new Material(new Vector3(255,0,0),0.1);
    public static Material green = new Material(new Vector3(0,255,0),0.1);
    public static Material blue = new Material(new Vector3(0,0,255),0.3);
    public static Material gray = new Material(new Vector3(100,100,100),0.8);
    public static Material black = new Material(new Vector3(0,0,0),0.8);

    public static ArrayList<Sphere> sphereList = new ArrayList<Sphere>();
    public static double focalLength, imgWidth,imgHeight;
    public static Vector3 origin = new Vector3(0,0,0);
    public static Vector3 background = new Vector3(45,210,247);


    public static void main(String[] args) {
        /*System.out.println(m1);
        m1.setMatrix(new double[][]{{1,2,3},{1,2,4}});
        System.out.println(m1);
        m2.setMatrix(new double[][]{{4,5},{5,4}});
        System.out.println(m1.multiplyMatrix(m2));
         */
        background = new Vector3(0,0,0);
        System.out.println(4/2*5);
        Light bigLight = new Light(50.0,new Vector3(0,-50,-50));
        imgWidth = 1920.0;
        imgHeight = 1080.0;
        BufferedImage img = new BufferedImage((int)imgWidth, (int)imgHeight, BufferedImage.TYPE_INT_RGB );
        //sphereList = new ArrayList<Sphere>();
        //sphereList.add(new Sphere(new Vector3(-12,0,40),5, red));
        //sphereList.add(new Sphere(new Vector3(4,0,20),1,green));
        //sphereList.add(new Sphere(new Vector3(-6,-12,50),20,red));
        //sphereList.add(new Sphere(new Vector3(2,-5,50),5,blue));
        sphereList.add(new Sphere(new Vector3(-2,1005,0),1000,gray));
        //meshList.add(new Mesh("src/rabbit.obj",blue,new Vector3(0,-0,70),
        //        new Vector3(90,180,0),new Vector3(0.1,0.1,0.1)));

        //meshList.add(new Mesh("src/cube.obj",blue,new Vector3(-10,-1,30),
        //new Vector3(45,45,45),new Vector3(1,1,1)));

        //meshList.add(new Mesh("src/cube.obj",gray,new Vector3(5,-1,20),
        //new Vector3(30,45,15),new Vector3(1,0.5,1)));

        meshList.add(new Mesh("src/rabbit.obj",blue,new Vector3(0,0,400),
                new Vector3(0,0,0),new Vector3(1,1,1)));

        System.out.println(new Vector3(22,18,79).createRotationMatrix());

        for (double rowIndx = 0; rowIndx < imgHeight; rowIndx++) {
            for (double colIndx = 0; colIndx < imgWidth; colIndx++) {
                double xPos = (2.0 / imgWidth) * (colIndx - imgWidth / 2.0);
                double yPos = (1.125 / imgHeight) * (rowIndx - imgHeight / 2.0);
                Raycast cast = new Raycast(origin, new Vector3(xPos, yPos, 1.0));

                if (cast.checkObjectHit(sphereList,meshList)==false) {
                    img.setRGB((int) colIndx, (int) rowIndx, background.getRGB());

                } else {
                    //Sphere sph = cast.getClosestSphere(sphereList);
                    //Mesh msh = meshList.get(0);


                    //int colorInt = cast.getLightHitColor(sph,bigLight,sphereList);
                    //Vector3 RGB = cast.getLightHitRGB(sph,bigLight,sphereList).addComponents(cast.getReflectionHitRGB(sph,sphereList,bigLight,1));
                    Vector3 RGB = cast.getLightHitRGB(bigLight,sphereList,meshList);
                    //RGB = cast.getLightHitMesh(cast.getClosestTri(meshList),bigLight, sphereList,meshList);
                    int colorInt = RGB.getRGB();
                    //if(sph.getPosition().getX()==-2){
                        //System.out.println(sph);
                    //System.out.println(colorInt);}
                    img.setRGB((int) colIndx, (int) rowIndx, colorInt);
                }
            }
            if(Math.round(100*rowIndx/imgHeight)>Math.round(100*(rowIndx-1)/imgHeight)) {
                System.out.println((int)(100*rowIndx/imgHeight)+"%");}
        }
        display(img);

        Raycast ray = new Raycast(new Vector3(0,0,0),new Vector3(0.1,0,1));
        for (int i = 0; i < sphereList.size(); i++) {
            System.out.println(ray.sphereHit(sphereList.get(i)));
        }

    }
    private static JFrame frame;
    private static JLabel label;
    public static void display(BufferedImage image){
        if(frame==null){
            frame=new JFrame();
            frame.setTitle("Rays Traced");
            frame.setSize(image.getWidth(), image.getHeight());
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            label=new JLabel();
            label.setIcon(new ImageIcon(image));
            frame.getContentPane().add(label,BorderLayout.CENTER);
            frame.setLocationRelativeTo(null);
            frame.pack();
            frame.setVisible(true);
        }else label.setIcon(new ImageIcon(image));
    }

}
