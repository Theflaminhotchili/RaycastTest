import java.util.ArrayList;

public class Raycast {
    Vector3 origin, direction;

    public Raycast(){
        origin=new Vector3(0,0,0);

        direction = new Vector3(0.5,0,0.5);
    }

    public Raycast(Vector3 originVec, Vector3 directionVec){
        origin = originVec;
        direction = directionVec.normalize();
    }

    //======================================================================
    //Accessors
    public Vector3 getDirection() {
        return direction;
    }

    public Vector3 getOrigin() {
        return origin;
    }

    //======================================================================
    //Mutators/Setters
    public void setDirection(Vector3 newDirect) {
        this.direction = newDirect.normalize();
    }
    public void setOrigin(Vector3 newOrigin) {
        this.origin = newOrigin;
    }

    //======================================================================
    //Raycast Hit Functions
    public double sphereHit(Sphere target){
        double d = -1*(direction.dotProduct(origin.subtractVector(target.getPosition()))) -
                Math.sqrt(
                    Math.pow((direction.dotProduct(origin.subtractVector(target.getPosition()))),2)
                    - (Math.pow(origin.subtractVector(target.getPosition()).getMag(),2)-(target.getRadius()*target.getRadius()))
                );
        /*double d2 = -1*(direction.dotProduct(origin.subtractVector(target.getPosition()))) -
                Math.sqrt(
                        Math.pow((direction.dotProduct(origin.subtractVector(target.getPosition()))),2)
                                - (Math.pow(origin.subtractVector(target.getPosition()).getMag(),2)-(target.getRadius()*target.getRadius()))
                );

         */
        if (0>d){
            return Double.NaN;
        }return d;
    }

    public Vector3 sphereHitCoords(Sphere target){
        return origin.addVector(direction.multiplyScalar(sphereHit(target)));
    }

    public boolean hitCheck (Sphere target){
        double hitDist = sphereHit(target);
        //System.out.println(hitDist);
        if((hitDist!=hitDist)||(hitDist<0)){
            return false;
        }
        return true;
    }

    public boolean hitCheck (Triangle target){
        double hitDist = waldIntersection(target);
        if((hitDist!=hitDist)){
            return false;
        }
        //System.out.println(hitDist);
        return true;
    }

    public boolean checkSphereHit(ArrayList<Sphere> list) {
        boolean output = false; //True if hit, false if not
        for (Sphere sph : list) {
            if (hitCheck(sph) == true) {
                output = true;
                break;
            }
        }
        return  output;
    }

    public boolean checkTriHit(Mesh mesh){
        Triangle[] triangles = mesh.getTris();
        for (int i = 0; i < triangles.length; i++) {
            if (hitCheck(triangles[i])){
                return true;
            }
        }
        return false;
    }

    public boolean checkMeshHit(ArrayList<Mesh> list){
        //boolean output = false;
        for (Mesh mesh: list){
            if(checkTriHit(mesh)){
                return true;
            }
        }

        return false;
    }

    public boolean checkObjectHit(ArrayList<Sphere> list, ArrayList<Mesh> meshlist, Mesh currentMesh) {
        boolean output = false; //True if hit, false if not
        for (Sphere sph : list) {
            if (hitCheck(sph) == true) {
                output = true;
                break;
            }
        }
        for (Mesh m : meshlist){
            if (m!=currentMesh) {
                Triangle[] T = m.getTris();
                for (Triangle t : T) {
                    if (hitCheck(t)==true) {
                        output = true;
                        break;
                    }
                }
            }
        }
        return  output;
    }

    public boolean checkObjectHit(ArrayList<Sphere> list, ArrayList<Mesh> meshlist) {
        boolean output = false; //True if hit, false if not
        for (Sphere sph : list) {
            if (hitCheck(sph) == true) {
                output = true;
                break;
            }
        }
        for (Mesh m : meshlist){
            Triangle[] T = m.getTris();
            for (Triangle t : T) {
                if (hitCheck(t)) {
                    output = true;
                    return output;
                }
            }
        }
        return  output;
    }


    public Sphere getClosestSphere(ArrayList<Sphere> list){
        Sphere output = null;
        double closestDist = 5000;
        for (Sphere sph : list){
            if ((sphereHit(sph)<=closestDist)&&(sphereHit(sph)>=0)){
                output = sph;
                closestDist = sphereHit(sph);
                //System.out.println(sphereHit(sph));
            }
        }return output;
    }

    public double triHitDist(Triangle tri){
        Vector3 n = tri.getNormal();
        double d = n.dotProduct(tri.getVert(0));//dist from origin
        double t = (n.dotProduct(origin)+d)/n.dotProduct(direction);
        return t;
    }

    public Vector3 triHitCoords(Triangle tri){
        //double t = triHitDist(tri);
        double t = waldIntersection(tri);
        return origin.addVector(direction.multiplyScalar(t));
    }

    public double triHit(Triangle tri){
        return waldIntersection(tri);
    }

    public double waldIntersection (Triangle tri){
        double[] rayDirec = direction.getCoords();

        //Backface culling
        //if (tri.getNormal().dotProduct(direction)>0){
          //  return Double.NaN;
        //}

        int kz = direction.getMaxDirec();
        int kx = kz + 1; if(kx==3){kx=0;}
        int ky = kx + 1; if(ky==3){ky=0;}
        if (rayDirec[kz] < 0.0f){
            int n = ky;
            ky = kx;
            kx = n;
        }

        //Shear constants

        double Sx = rayDirec[kx]/rayDirec[kz];
        double Sy = rayDirec[ky]/rayDirec[kz];
        double Sz = 1.0/rayDirec[kz];

        //get Verts relative to ray origin
        Vector3[] triVerts = tri.getVerts();
        //for (int i = 0; i < 3; i++) {
          //  triVerts[i] = triVerts[i].subtractVector(origin);
        //}

        double[] A = triVerts[0].subtractVector(origin).getCoords();
        double[] B = triVerts[1].subtractVector(origin).getCoords();
        double[] C = triVerts[2].subtractVector(origin).getCoords();

        //Shear and Scale Verts
        double Ax = A[kx] - Sx*A[kz];
        double Ay = A[ky] - Sy*A[kz];
        double Bx = B[kx] - Sx*B[kz];
        double By = B[ky] - Sy*B[kz];
        double Cx = C[kx] - Sx*C[kz];
        double Cy = C[ky] - Sy*C[kz];

        //calculate scaled barycentric coordinates
        double U = Cx*By - Cy*Bx;
        double V = Ax*Cy - Ay*Cx;
        double W = Bx*Ay - By*Ax;

        //Check if ray in bounds of tri


        if ((U<0.0f || V<0.0f || W<0.0f)) {return Double.NaN;}

        //If no Backface culling
        //if ((U<0.0f || V<0.0f || W<0.0f) &&
          //      (U>0.0f || V>0.0f || W>0.0f)) {return Double.NaN;}

        //calculate determinant
        double det = U + V + W;

        if (det == 0.0){
            return Double.NaN;
        }

        double Az = Sz*A[kz];
        double Bz = Sz*B[kz];
        double Cz = Sz*C[kz];
        double T = (U*Az + V*Bz + W*Cz);



        //if(T<0){
          //  return Double.NaN;
        //}

        /*int detSign = (int)Math.signum(det);
        if ((T>0)^(detSign<0)){
            //TODO: Finish This XOR Shit
        }*/

        //System.out.println(T);
        //if(T>2000){
        T = T/det;
        //}
        return T;

    }

    public double MollerTrumboreIntersection(Triangle tri){
        return 0;
    }






















    public Triangle getClosestTriInMesh(Mesh mesh){
        Triangle output = null;
        double closestDist = 100000;
        double hitDist;
        Triangle[] tList = mesh.getTris();
        for (int t = 0; t<tList.length ; t++){
            hitDist = triHit(tList[t]);
            if (hitDist<closestDist && hitDist>=0 && hitDist==hitDist){
                output = tList[t];
                closestDist = hitDist;
            }

        }
        return output;
    }

    public Triangle getClosestTri (ArrayList<Mesh> list){
        Triangle output = null;
        double closestDist = 100000;
        double hitDist;
        for (Mesh m: list){
            Triangle t = getClosestTriInMesh(m);
            if (t!=null && triHit(t)<closestDist){
                closestDist = triHit(t);
                //TODO: Solve wierd extremely high closestdist
                if(closestDist>2000){
                System.out.println((t));
                System.out.println(closestDist);}

                output = t;
            }
        }
        return output;
    }





    //======================================================================
    //Render Functions
    public Vector3 getReflectionVector(Sphere target, Vector3 hit){
        Vector3 normalVector = new Vector3(hit.getX()-target.getPosition().getX(),
                hit.getY()-target.getPosition().getY(),hit.getZ()-target.getPosition().getZ()).normalize();
        return (direction.subtractVector(normalVector.multiplyScalar(2*direction.dotProduct(normalVector)))).normalize();
    }

    public Vector3 getReflectionVector(Triangle target, Vector3 hit){
        Vector3 normalVector = target.getNormal();
        return (direction.subtractVector(normalVector.multiplyScalar(2*direction.dotProduct(normalVector)))).normalize();
    }

    public Raycast getReflectionRaycast(Sphere target){
        return new Raycast(sphereHitCoords(target),getReflectionVector(target, sphereHitCoords(target)));
    }

    public Raycast getReflectionRaycast(Triangle target){
        return new Raycast(triHitCoords(target),getReflectionVector(target, triHitCoords(target)));
    }

    public Vector3 getShadowVector(Sphere sph, Light light){
        return (light.getPosition()).subtractVector(sphereHitCoords(sph));
    }

    public Vector3 getShadowVector(Triangle tri, Light light){
        return (light.getPosition()).subtractVector(triHitCoords(tri));
    }




    public Raycast getShadowRay(Sphere sphe, Light light){
        return new Raycast(sphereHitCoords(sphe), getShadowVector(sphe,light)); //(sphereHitCoords(sphe).subtractVector(light.getPosition())).normalize());
    }
    public Raycast getShadowRay(Triangle tri, Light light){
        return new Raycast(triHitCoords(tri), getShadowVector(tri,light)); //(sphereHitCoords(sphe).subtractVector(light.getPosition())).normalize());
    }

    public Raycast getRefractionRay(Sphere refractSphere,double incomingIor, Light light, Vector3 incomingVector){
        Vector3 lightVec = getShadowVector(refractSphere,light).multiplyScalar(-1.0).normalize();
        Vector3 hitNormal = new Vector3(sphereHitCoords(refractSphere).getX()-refractSphere.getPosition().getX(),
                sphereHitCoords(refractSphere).getY()-refractSphere.getPosition().getY(),sphereHitCoords(refractSphere).getZ()-refractSphere.getPosition().getZ()).normalize();

        double c = hitNormal.multiplyScalar(-1.0).dotProduct(lightVec);
        hitNormal.multiplyScalar(Math.signum(c));
        c = hitNormal.multiplyScalar(-1.0).dotProduct(lightVec);
        double r = incomingIor/refractSphere.getIor();

        Vector3 refractDirec = lightVec.multiplyScalar(r).addVector(hitNormal.multiplyScalar(
                r*c - Math.sqrt(1-((r*r)*(1-c*c)))
        ));

        return new Raycast();
    }


    //======================================================================
    //Sphere Color Calculations

    public int getLightHitColor(Sphere target, Light light,ArrayList<Sphere> list){

        Vector3 hitNormal = new Vector3(sphereHitCoords(target).getX()-target.getPosition().getX(),
                sphereHitCoords(target).getY()-target.getPosition().getY(),sphereHitCoords(target).getZ()-target.getPosition().getZ()).normalize();
        Vector3 shadowVec = getShadowVector(target, light);
        double normalFactor = hitNormal.dotProduct(shadowVec.normalize());
        if (0>=normalFactor){
            normalFactor = 0;
        }
        double vis = 1;
        Raycast shadowRay = getShadowRay(target,light);
        boolean hit = shadowRay.checkSphereHit(list);
        if (hit==true){
            vis=0.25;
        }
        Vector3 colorVec = (target.getAlbedo().multiplyScalar(1/Math.PI)).multiplyScalar(light.getIntensity()).multiplyScalar(normalFactor).multiplyScalar(vis);

        return colorVec.getRGB();
    }
    /*
    public Vector3 getLightHitRGB(Sphere target, Light light,ArrayList<Sphere> list){

        Vector3 hitNormal = new Vector3(sphereHitCoords(target).getX()-target.getPosition().getX(),
                sphereHitCoords(target).getY()-target.getPosition().getY(),sphereHitCoords(target).getZ()-target.getPosition().getZ()).normalize();
        Vector3 shadowVec = getShadowVector(target, light);
        double normalFactor = hitNormal.dotProduct(shadowVec.normalize());
        if (0>=normalFactor){
            normalFactor = 0;
        }
        double vis = 1;
        Raycast shadowRay = getShadowRay(target,light);
        boolean hit = shadowRay.checkSphereHit(list);
        if (hit==true){
            vis=0.25;
        }
        Vector3 colorVec = (target.getAlbedo().multiplyScalar(1/Math.PI)).multiplyScalar(light.getIntensity()).multiplyScalar(normalFactor).multiplyScalar(vis);
        colorVec.setAsColor();
        return colorVec;
    }

    public Vector3 getReflectionHitRGB(Sphere target, ArrayList<Sphere> list, Light light, int depth){
        Raycast reflectCast = getReflectionRaycast(target);
        Sphere reflectSphere = reflectCast.getClosestSphere(list);
        if (reflectSphere==null||depth>3){return new Vector3(0,0,0);}
        return reflectCast.getLightHitRGB(reflectSphere,light,list).multiplyScalar(target.getReflective()).addComponents(
                reflectCast.getReflectionHitRGB(reflectSphere,list,light,depth+1).multiplyComponents(target.getColorBalance()));
    }

    public Vector3 getRefractionHitRGB(Triangle target, ArrayList<Sphere> list, Light light, int depth){
        return null;
    }


    //======================================================================
    //Mesh Color Calculations

    public Vector3 getLightHitRGB(Mesh target, Light light,ArrayList<Sphere> list, ArrayList<Mesh> meshlist) {
        Triangle tri = getClosestTriInMesh(target);
        Vector3 shadowVec = getShadowVector(tri, light);
        double normalFactor = tri.getNormal().dotProduct(shadowVec.normalize());
        if (0 >= normalFactor) {
            normalFactor = 0;
        }
        double vis = 1;
        //System.out.println(triHit(tri));
        Raycast shadowRay = getShadowRay(tri,light);
        boolean hit = shadowRay.checkObjectHit(list,meshlist,target);
        if (hit == true) {
            vis = 0.25;
        }
        //vis=1.0;
        Vector3 colorVec = (target.getAlbedo().multiplyScalar(1 / Math.PI)).multiplyScalar(light.getIntensity()).multiplyScalar(normalFactor).multiplyScalar(vis);
        //colorVec = new Vector3(triHit(tri)*200,0,0);

        colorVec.setAsColor();
        return colorVec;
    }

*/

    //Universal Color Calculations

    public Vector3 getLightHitSphere(Sphere target, Light light,ArrayList<Sphere> list, ArrayList<Mesh> meshlist){
        Vector3 hitNormal = new Vector3(sphereHitCoords(target).getX()-target.getPosition().getX(),
                sphereHitCoords(target).getY()-target.getPosition().getY(),sphereHitCoords(target).getZ()-target.getPosition().getZ()).normalize();
        Vector3 shadowVec = getShadowVector(target, light);
        double normalFactor = hitNormal.dotProduct(shadowVec.normalize());
        if (0>=normalFactor){
            normalFactor = 0;
        }
        double vis = 1;
        Raycast shadowRay = getShadowRay(target,light);
        boolean hit = shadowRay.checkObjectHit(list, meshlist);
        if (hit==true){
            vis=0.25;
        }
        Vector3 colorVec = (target.getAlbedo().multiplyScalar(1/Math.PI)).multiplyScalar(light.getIntensity()).multiplyScalar(normalFactor).multiplyScalar(vis);
        colorVec.setAsColor();
        return colorVec;
    }

    public Vector3 getLightHitMesh(Triangle tri, Light light,ArrayList<Sphere> list, ArrayList<Mesh> meshlist) {
        Vector3 shadowVec = getShadowVector(tri, light);
        double normalFactor = tri.getNormal().dotProduct(shadowVec.normalize());
        if (0 >= normalFactor) {
            normalFactor = 0;
        }
        double vis = 1;

        /*Mesh myMesh = null;
        for (Mesh m:meshlist) {
            if (m.isTriInMesh(tri)){
                myMesh = m;
                //System.out.println("yeah");
                break;
            }
        }*/
        Raycast shadowRay = getShadowRay(tri,light);
        boolean hit = shadowRay.checkObjectHit(list,meshlist);//,myMesh);
        if (hit == true) {
            vis = 0.25;
        }
        Vector3 colorVec = (tri.getAlbedo().multiplyScalar(1.0 / Math.PI)).multiplyScalar(light.getIntensity()).multiplyScalar(normalFactor).multiplyScalar(vis);
        colorVec.setAsColor();
        return colorVec;
    }




    public Vector3 getLightHitRGB(Light light, ArrayList<Sphere> spherelist, ArrayList<Mesh> meshlist){
        Sphere closestSphere = getClosestSphere(spherelist);
        Triangle closestTri = getClosestTri(meshlist);
        //System.out.println(closestTri);
        Vector3 output = new Vector3(0,0,0);

        if ((closestTri == null) || (closestSphere != null && sphereHit(closestSphere) < waldIntersection(closestTri)))
        {
            output = getLightHitSphere(closestSphere,light,spherelist,meshlist).addVector(
                    getReflectionHit(closestSphere,light,spherelist,meshlist,0));
        }
        else if (closestTri != null)
        {
            output = getLightHitMesh(closestTri,light,spherelist,meshlist).addVector(getReflectionHit(closestTri,light,spherelist,meshlist,0));
        }
        output.setAsColor();
        return output;
    }






    //Universal Reflections

    public Vector3 getReflectionHit(Sphere target, Light light, ArrayList<Sphere> sphereList, ArrayList<Mesh> meshlist, int depth){
        Raycast reflectCast = getReflectionRaycast(target);
        Triangle reflectTri = reflectCast.getClosestTri(meshlist);
        Sphere reflectSphere = reflectCast.getClosestSphere(sphereList);
        if ((reflectSphere==null&&reflectTri==null)||depth>3){return new Vector3(0,0,0);}

        if (reflectSphere==null || (reflectTri != null&&reflectCast.triHit(reflectTri)<reflectCast.sphereHit(reflectSphere))){
            return reflectCast.getLightHitMesh(reflectTri,light,sphereList,meshlist).addComponents(
                    reflectCast.getReflectionHit(reflectTri,light,sphereList,meshlist,depth+1));
        }else{
            return reflectCast.getLightHitSphere(reflectSphere,light,sphereList,meshlist).multiplyScalar(target.getReflective()).addComponents(
                    reflectCast.getReflectionHit(reflectSphere,light,sphereList,meshlist,depth+1).multiplyComponents(target.getColorBalance()));
        }
    }

    public Vector3 getReflectionHit(Triangle target, Light light, ArrayList<Sphere> sphereList, ArrayList<Mesh> meshlist, int depth){
        Raycast reflectCast = getReflectionRaycast(target);
        Triangle reflectTri = reflectCast.getClosestTri(meshlist);
        Sphere reflectSphere = reflectCast.getClosestSphere(sphereList);
        if ((reflectSphere==null&&reflectTri==null)||depth>3){return new Vector3(0,0,0);}
        if (reflectSphere == null || (reflectTri != null && reflectCast.triHit(reflectTri)<reflectCast.sphereHit(reflectSphere))){
            return reflectCast.getLightHitMesh(reflectTri,light,sphereList,meshlist).addComponents(
                    reflectCast.getReflectionHit(reflectTri,light,sphereList,meshlist,depth+1));
        }else{
            return reflectCast.getLightHitSphere(reflectSphere,light,sphereList,meshlist).multiplyScalar(target.getReflective()).addComponents(
                reflectCast.getReflectionHit(reflectSphere,light,sphereList,meshlist,depth+1).multiplyComponents(target.getColorBalance()));
        }
    }




}
