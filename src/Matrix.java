import java.util.Arrays;

public class Matrix {
    double[][] matrix;

    public Matrix(){}

    public Matrix(int rows, int cols){
        matrix = new double[cols][rows];
    }

    //======================================================================
    //Accessors
    public double[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(double[][] matrix) {
        this.matrix = matrix;
    }

    public double getIndex(int row, int col){
        return matrix[col][row];
    }

    public void setIndex(int row, int col, double value){
        matrix[col][row]=value;
    }

    @Override
    public String toString() {
        for (int i = 0; i < matrix[0].length; i++) {//col
            System.out.print("[ "); //new col
            for (int j = 0; j < matrix.length; j++) {
                System.out.print(matrix[j][i]+" ");
            }
            System.out.println("]");
            //System.out.println(matrix[1][0]);
            //col 1, row 0
        }
        return "Matrix{" +
                "matrix=" + Arrays.toString(matrix) +
                '}';
    }

    //======================================================================
    //Functions

    public double dotProduct(double[] l1,double[] l2){ //dot product of 2 lists
        if (l1.length!= l2.length){
            return Double.NaN;
        }
        double product = 0;
        for (int i = 0; i < l1.length; i++) {
            product += l1[i] * l2[i];
        }
        return product;
    }

    //matrix[col][row]
    //(double[rows])[cols]
    //list.length = num cols
    //list[i].length = num rows
    //second [] is cols

    //{{1,2},{1,2}}
    //[{1 {1]
    //[2} 2}]

    public Matrix multiplyMatrix(Matrix otherMatrix){
        double[][] matrix2 = otherMatrix.getMatrix();
        if(matrix.length != matrix2[0].length){return null;}
        double[][] output = new double[matrix2.length][matrix[0].length];
        //----------------------------[matrix1 rows]    [matrix2 cols]
        //System.out.println(output[0].length + "hgt");
        //System.out.println(output.length + "wid");
        for (int col = 0; col < output.length; col++) {
            for (int row = 0; row < output[0].length; row++) {
                //double[] thisRow = new double[output.length];
                double[] thisRow = new double[output[0].length];
                for (int i = 0; i < thisRow.length; i++) {
                    //System.out.println("I= " + i +" row" + row);
                    thisRow[i] = matrix[i][row];
                }
                //System.out.println(col);
                //System.out.println(row);
                output[col][row] = dotProduct(thisRow,matrix2[col]);
            }
        }
        Matrix returnM = new Matrix(matrix[0].length,matrix2.length);
        returnM.setMatrix(output);
        return returnM;
    }

    public int getRows(){
        return matrix[0].length;
    }

    public int getColumns(){
        return matrix.length;
    }

    public Vector3 toVector3(){
        if (getColumns()==3 && getRows()==1){
            return new Vector3(getIndex(0,0),getIndex(0,1),getIndex(0,2) );
        }else if (getColumns()==1&&getRows()==3){
            return new Vector3(matrix[0][0],matrix[0][1],matrix[0][2]);
        }
        return null;
    }

}
