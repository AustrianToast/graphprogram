import java.util.Arrays;

public class Graph {
    private Matrix adjazenzMatrix;
    private Matrix distanzMatrix;
    private Matrix wegMatrix;
    private boolean zusammenhaengend;
    private int[] exzentrizitäten;
    private int radius;
    private int durchmesser;
    private int[] zentrum;
    private int[][] components;
    private int[][] bridges;
    private int[] articulations;


    public static void main(String[] args) {}

    public Graph(String file) {
        zusammenhaengend = true;
        adjazenzMatrix = new Matrix(file);
        calculateDistanzMatrix();
        calculateWegMatrix();
        calculateExzentrizitäten();
        calculateProperties();
        findComponents();
        findBridges();
        findArticulations();
    }

    public void calculateDistanzMatrix() {
        distanzMatrix = new Matrix(adjazenzMatrix.getRowLength(), adjazenzMatrix.getColumnLength());
        Matrix potenzMatrix = adjazenzMatrix;

        for(int columnIndex=0; columnIndex < distanzMatrix.getColumnLength(); columnIndex++) {
            for(int rowIndex=0; rowIndex < distanzMatrix.getRowLength(); rowIndex++) {
                if(adjazenzMatrix.getValueAt(rowIndex, columnIndex) == 1) {
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

    public void calculateWegMatrix() {
        wegMatrix = new Matrix(adjazenzMatrix.getRowLength(), adjazenzMatrix.getColumnLength());
        Matrix potenzMatrix = adjazenzMatrix;

        for(int columnIndex=0; columnIndex < wegMatrix.getColumnLength(); columnIndex++) {
            for(int rowIndex=0; rowIndex < wegMatrix.getRowLength(); rowIndex++) {
                if(columnIndex == rowIndex) {
                    wegMatrix.insert(rowIndex, columnIndex, 1);
                } else if(adjazenzMatrix.getValueAt(rowIndex, columnIndex) > 0 && wegMatrix.getValueAt(rowIndex, columnIndex) == 0) {
                    wegMatrix.insert(rowIndex, columnIndex, 1);
                }
            }
        }

        for(int k = 2; k < wegMatrix.getRowLength(); k++) {
            potenzMatrix = potenzMatrix.multiply(adjazenzMatrix);

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
            int exzentrizität = 0;

            for(int columnIndex = 0; columnIndex < distanzMatrix.getColumnLength(); columnIndex++) {
                if(distanzMatrix.getValueAt(columnIndex, rowIndex) > exzentrizität && rowIndex != columnIndex) {
                    exzentrizität = distanzMatrix.getValueAt(columnIndex, rowIndex);
                }
            }
            exzentrizitäten[rowIndex] = exzentrizität;
        }
    }

    public void calculateProperties() {
        radius = Integer.MAX_VALUE;
        durchmesser = -1;
        int sum = 0;

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
        if(durchmesser == 0 && exzentrizitäten.length > 1) {
            zusammenhaengend = false;
            return;
        }

        zentrum = new int[sum];
        
        for(int rowIndex = 0, index = 0; rowIndex < exzentrizitäten.length; rowIndex++) {
            if(exzentrizitäten[rowIndex] == durchmesser) {
                zentrum[index] = rowIndex + 1;
                index++;
            }
        }
    }

    public void findComponents() {
        components = new int[wegMatrix.getRowLength()][wegMatrix.getColumnLength()];
        int[] component = new int[wegMatrix.getRowLength()];

        for(int rowIndex = 0; rowIndex < wegMatrix.getRowLength(); rowIndex++) {
            for(int columnIndex = 0, index = 0; columnIndex < wegMatrix.getColumnLength(); columnIndex++) {
                if(wegMatrix.getValueAt(rowIndex, columnIndex) == 1) {
                    component[index] = columnIndex + 1;
                    index++;
                }
            }
            components[rowIndex] = component;
        }
        
        for(int rowIndex = 0; rowIndex < components.length; rowIndex++) {
            for(int index = 1; index < components.length; index++) {
                if(components[index] != null && components[rowIndex].equals(components[index])) {
                    components[index] = null;
                }
            }
        }
    }

    public int[][] findComponents(Matrix matrix) {
        int[][] tempComponents = new int[matrix.getRowLength()][matrix.getColumnLength()];
        int[] component = new int[matrix.getRowLength()];

        for(int rowIndex = 0; rowIndex < matrix.getRowLength(); rowIndex++) {
            for(int columnIndex = 0, index = 0; columnIndex < matrix.getColumnLength(); columnIndex++) {
                if(matrix.getValueAt(rowIndex, columnIndex) == 1) {
                    component[index] = columnIndex + 1;
                    index++;
                }
            }
            tempComponents[rowIndex] = component;
        }
        if(!zusammenhaengend) {
            return null;
        }
        
        for(int rowIndex = 0; rowIndex < tempComponents.length; rowIndex++) {
            for(int index = 1; index < tempComponents.length; index++) {
                if(tempComponents[index] != null && tempComponents[rowIndex].equals(tempComponents[index])) {
                    tempComponents[index] = null;
                }
            }
        }
        return tempComponents;
    }

    public void findBridges() {
        if(!zusammenhaengend) {
            return;
        }
        bridges = new int[wegMatrix.getRowLength()][2];
        int[][] newComponents;
        int[] bridge = new int[2];

        for(int i = 0, columnIndex = 0; columnIndex < wegMatrix.getColumnLength(); columnIndex++) {
            for(int rowIndex = 0; rowIndex < wegMatrix.getRowLength(); rowIndex++) {
                if(rowIndex != 1) {
                    continue;
                }
                wegMatrix.insert(rowIndex, columnIndex, 0);
                wegMatrix.insert(columnIndex, rowIndex, 0);
                bridge[0] = columnIndex + 1;
                bridge[1] = rowIndex + 1;

                newComponents = findComponents(wegMatrix);

                if(!components.equals(newComponents)) {
                    bridges[i] = bridge;
                    i++;
                }

                wegMatrix.insert(rowIndex, columnIndex, 1);
                wegMatrix.insert(columnIndex, rowIndex, 1);
            }
        }

        for(int rowIndex = 0; rowIndex < bridges.length; rowIndex++) {
            for(int index = 1; index < bridges.length; index++) {
                if(bridges[index] != null && bridges[rowIndex].equals(bridges[index])) {
                    bridges[index] = null;
                }
            }
        }
    }

    public void findArticulations() {
        if(!zusammenhaengend) {
            return;
        }
        articulations = new int[wegMatrix.getRowLength()];
        int[][] newComponents;

        for(int i = 0, j = 0; i < wegMatrix.getRowLength(); i++) {
            for(int rowIndex = 0; rowIndex < wegMatrix.getRowLength(); rowIndex++) {
                for(int columnIndex = 0; columnIndex < wegMatrix.getColumnLength(); columnIndex++) {
                    wegMatrix.insert(rowIndex, i, 0);
                    wegMatrix.insert(i, columnIndex, 0);
                }
            }

            /*
             * need to figure out what removing a node means
             */
    
            newComponents = findComponents(wegMatrix);
    
            if(!components.equals(newComponents)) {
                articulations[j] = i + 1;
                j++;
            }

            for(int rowIndex = 0; rowIndex < wegMatrix.getRowLength(); rowIndex++) {
                for(int columnIndex = 0; columnIndex < wegMatrix.getColumnLength(); columnIndex++) {
                    wegMatrix.insert(rowIndex, i, 1);
                    wegMatrix.insert(i, columnIndex, 1);
                }
            }
        }
    }

    public String toString() {
        String s = "";

        s += "Adjazenzmatrix:\n" + adjazenzMatrix + "\nDistanzmatrix:\n" + distanzMatrix + "\nWegmatrix:\n" + wegMatrix;
        
        if(!zusammenhaengend) {
            s += "\nExzentrizitäten/Radius/Durchmesser: Kein zusammenhängender Graph";
        } else {
            s += "\nExzentrizitäten: " + Arrays.toString(exzentrizitäten) + "\nRadius: " + radius + "\nDurchmesser: " + durchmesser;
        }

        s += "\nZentrum: ";
        if(zusammenhaengend) {
            s += Arrays.toString(zentrum);
        } else {
            s += "Kein zusammenhängender Graph";
        }
        
        s += "\nKomponente: {";
        for(int rowIndex = 0; rowIndex < components.length; rowIndex++) {
            if(components[rowIndex] != null) {
                s += Arrays.toString(components[rowIndex]);
                if(rowIndex < components.length - 1) {
                    if(components[rowIndex + 1] != null) {
                        s += ",";
                    }
                }
            }
        }
        s += "}";

        s += "\nBrücken: {";
        if(bridges != null) {
            for(int rowIndex = 0; rowIndex < bridges.length; rowIndex++) {
                if(bridges[rowIndex] != null) {
                    s += Arrays.toString(bridges[rowIndex]);
                    if(rowIndex < bridges.length - 1) {
                        if(bridges[rowIndex + 1] != null) {
                            s += ",";
                        }
                    }
                }
            }
        }
        s += "}";

        s += "\nArtikulationen: {";
        if(articulations[0] != 0) {
            s += Arrays.toString(articulations);
        }
        s += "}";

        return s;
    }
}