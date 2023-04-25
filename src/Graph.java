import java.util.Arrays;

public class Graph {
    private Matrix adjazenzMatrix;
    private Matrix distanzMatrix;
    private Matrix wegMatrix;
    private int[] exzentrizitäten;
    private int radius;
    private int durchmesser;
    private int[] zentrum;

    public static void main(String[] args) {}

    public Graph(String file) {
        adjazenzMatrix = new Matrix(file);
        calculateDistanzMatrix(adjazenzMatrix);
        calculateWegMatrix(adjazenzMatrix);
        calculateExzentrizitäten();
        calculateProperties();
    }

    public void calculateDistanzMatrix(Matrix matrix) {
        distanzMatrix = new Matrix(matrix.getRowLength(), matrix.getColumnLength());
        Matrix potenzMatrix = adjazenzMatrix;

        for(int columnIndex=0; columnIndex < distanzMatrix.getColumnLength(); columnIndex++) {
            for(int rowIndex=0; rowIndex < distanzMatrix.getRowLength(); rowIndex++) {
                if(matrix.getValueAt(rowIndex, columnIndex) == 1) {
                    distanzMatrix.insert(rowIndex, columnIndex, 1);
                } else if(columnIndex == rowIndex) {
                    distanzMatrix.insert(rowIndex, columnIndex, 0);
                } else {
                    distanzMatrix.insert(rowIndex, columnIndex, -1);
                }
            }
        }

        for(int k = 2; k < distanzMatrix.getRowLength(); k++) {
            potenzMatrix = potenzMatrix.multiply(adjazenzMatrix);

            for(int columnIndex=0; columnIndex < distanzMatrix.getColumnLength(); columnIndex++) {
                for(int rowIndex=0; rowIndex < distanzMatrix.getRowLength(); rowIndex++) {
                    if(potenzMatrix.getValueAt(rowIndex, columnIndex) != 0 && distanzMatrix.getValueAt(rowIndex, columnIndex) == -1) {
                        distanzMatrix.insert(rowIndex, columnIndex, k);
                    }
                }
            }
        }
    }

    public void calculateWegMatrix(Matrix matrix) {
        wegMatrix = new Matrix(matrix.getRowLength(), matrix.getColumnLength());
        Matrix potenzMatrix = matrix;

        for(int columnIndex=0; columnIndex < wegMatrix.getColumnLength(); columnIndex++) {
            for(int rowIndex=0; rowIndex < wegMatrix.getRowLength(); rowIndex++) {
                if(columnIndex == rowIndex) {
                    wegMatrix.insert(rowIndex, columnIndex, 1);
                } else if(matrix.getValueAt(rowIndex, columnIndex) > 0 && wegMatrix.getValueAt(rowIndex, columnIndex) == 0) {
                    wegMatrix.insert(rowIndex, columnIndex, 1);
                }
            }
        }

        for(int k = 2; k < wegMatrix.getRowLength(); k++) {
            potenzMatrix = potenzMatrix.multiply(matrix);

            for(int columnIndex=0; columnIndex < wegMatrix.getColumnLength(); columnIndex++) {
                for(int rowIndex=0; rowIndex < wegMatrix.getRowLength(); rowIndex++) {
                    if(potenzMatrix.getValueAt(rowIndex, columnIndex) != 0) {
                        wegMatrix.insert(rowIndex, columnIndex, 1);
                    }
                }
            }
        }
    }

    public void calculateExzentrizitäten() {
        exzentrizitäten = new int[distanzMatrix.getRowLength()];
        for(int rowIndex = 0; rowIndex < distanzMatrix.getRowLength(); rowIndex++) {
            int exzentrizität = -1;

            for(int columnIndex = 0; columnIndex < distanzMatrix.getColumnLength(); columnIndex++) {
                if(distanzMatrix.getValueAt(columnIndex, rowIndex) > exzentrizität) {
                    exzentrizität = distanzMatrix.getValueAt(columnIndex, rowIndex);
                }
            }
            exzentrizitäten[rowIndex] = exzentrizität;
        }
    }

    public void calculateProperties() {
        radius = Integer.MAX_VALUE;
        durchmesser = 0;
        int sum = 1;

        for(int rowIndex = 0; rowIndex < exzentrizitäten.length; rowIndex++) {
            if(exzentrizitäten[rowIndex] < radius) {
                radius = exzentrizitäten[rowIndex];
            }
            if(exzentrizitäten[rowIndex] == durchmesser) {
                sum++;
            }
            if(exzentrizitäten[rowIndex] > durchmesser) {
                durchmesser = exzentrizitäten[rowIndex];
                sum = 1;
            } 
        }
        zentrum = new int[sum];

        for(int rowIndex = 0, index = 0; rowIndex < exzentrizitäten.length; rowIndex++) {
            if(exzentrizitäten[rowIndex] == durchmesser) {
                zentrum[index] = rowIndex + 1;
                index++;
            }
        }
    }

    public String toString() {
        String s = "";

        s += "Adjazenzmatrix:\n" + adjazenzMatrix + "\nDistanzmatrix:\n" + distanzMatrix + "\nWegmatrix:\n" + wegMatrix;
        s += "\nExzentrizitäten: " + Arrays.toString(exzentrizitäten) + "\nRadius: " + radius + "\nDurchmesser: " + durchmesser + "\nZentrum: " + Arrays.toString(zentrum);

        return s;
    }
}