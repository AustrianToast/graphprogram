import java.util.ArrayList;
import java.util.Arrays;

public class Graph {
    private Matrix adjazenzMatrix;
    private Matrix distanzMatrix;
    private Matrix wegMatrix;
    private boolean zusammenhaengend;
    private int[] exzentrizitäten;
    private int radius;
    private int durchmesser;
    private ArrayList<Integer> centre;
    private int[][] components;
    private int[][] bridges;
    private int[] articulations;


    public static void main(String[] args) {}

    public Graph(String file) {
        adjazenzMatrix = new Matrix(file);
        calculateDistanzMatrix();
        calculateWegMatrix();
        calculateExzentrizitäten();
        calculateProperties();
        findComponents();
        //findBridges();
        //findArticulations();
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
        zusammenhaengend = true;
        centre = new ArrayList<>(1);

        for(int rowIndex = 0; rowIndex < exzentrizitäten.length; rowIndex++) {
            if(exzentrizitäten[rowIndex] > durchmesser) {
                durchmesser = exzentrizitäten[rowIndex];
                centre.clear();
                centre.add(rowIndex + 1);
            }
            if(exzentrizitäten[rowIndex] < radius) {
                radius = exzentrizitäten[rowIndex];
            }
            if(exzentrizitäten[rowIndex] == durchmesser) {
                centre.add(rowIndex + 1);
            }
        }

        if(durchmesser == 0 && exzentrizitäten.length > 1) {
            zusammenhaengend = false;
            return;
        }

        centre.trimToSize();
    }

    public void findComponents() {
        ArrayList<int[]> tempComponents = new ArrayList<>(1);
        ArrayList<Integer> tempComponent = new ArrayList<>(1);
        int[] component;
        boolean contains;

        for(int rowIndex = 0; rowIndex < wegMatrix.getRowLength(); rowIndex++) {
            for(int columnIndex = 0; columnIndex < wegMatrix.getColumnLength(); columnIndex++) {
                if(wegMatrix.getValueAt(rowIndex, columnIndex) == 1) {
                    tempComponent.add(columnIndex + 1);
                }
            }
            component = new int[tempComponent.size()];

            for(int index = 0; index < component.length; index++) {
                component[index] = tempComponent.get(index);
            }

            contains = false;
            for(int index = 0; index < tempComponents.size(); index++) {
                if(Arrays.equals(tempComponents.get(index), component)) {
                    contains = true;
                }
            }

            if(!contains) {
                tempComponents.add(component);
            }

            component = null;
            tempComponent.clear();
        }
        
        components = new int[tempComponents.size()][wegMatrix.getColumnLength()];

        for(int rowIndex = 0; rowIndex < components.length; rowIndex++) {
            components[rowIndex] = tempComponents.get(rowIndex);
        }
    }

    public int[][] findComponents(Matrix matrix) {
        int[][] newComponents;
        ArrayList<int[]> tempComponents = new ArrayList<>(1);
        ArrayList<Integer> tempComponent = new ArrayList<>(1);
        int[] component;

        for(int rowIndex = 0; rowIndex < matrix.getRowLength(); rowIndex++) {
            for(int columnIndex = 0; columnIndex < matrix.getColumnLength(); columnIndex++) {
                if(matrix.getValueAt(rowIndex, columnIndex) == 1) {
                    tempComponent.add(columnIndex + 1);
                }
            }
            component = new int[tempComponent.size()];

            for(int index = 0; index < component.length; index++) {
                component[index] = tempComponent.get(index);
            }

            if(tempComponents.contains(component)) {
                continue;
            }

            tempComponents.add(component);

            component = null;
            tempComponent.clear();
        }
        newComponents = new int[tempComponents.size()][matrix.getColumnLength()];

        for(int rowIndex = 0; rowIndex < newComponents.length; rowIndex++) {
            newComponents[rowIndex] = tempComponents.get(rowIndex);
        }
        return  newComponents;
    }

    public void findBridges() {
        if(!zusammenhaengend) {
            return;
        }
        ArrayList<int[]> tempBridges = new ArrayList<>(10);
        int[][] newComponents;
        int[] bridge;

        for(int columnIndex = 0; columnIndex < wegMatrix.getColumnLength(); columnIndex++) {
            for(int rowIndex = 0; rowIndex < wegMatrix.getRowLength(); rowIndex++) {
                wegMatrix.insert(rowIndex, columnIndex, 0);
                wegMatrix.insert(columnIndex, rowIndex, 0);
                bridge = new int[2];
                bridge[0] = columnIndex + 1;
                bridge[1] = rowIndex + 1;

                newComponents = findComponents(wegMatrix);

                System.out.println(Arrays.toString(bridge));
                if(!components.equals(newComponents) && !tempBridges.contains(bridge)) {
                    System.out.println("bruh");
                    tempBridges.add(bridge);
                }
                bridge = null;
                wegMatrix.insert(rowIndex, columnIndex, 1);
                wegMatrix.insert(columnIndex, rowIndex, 1);
            }
        }
        bridges = new int[tempBridges.size()][2];
        
        for(int rowIndex = 0; rowIndex < bridges.length; rowIndex++) {
            bridges[rowIndex] = tempBridges.get(rowIndex);
        }
    }

    public void findArticulations() {
        if(!zusammenhaengend) {
            return;
        }
        ArrayList<Integer> tempArticulations = new ArrayList<>(10);
        int[][] newComponents;

        for(int i = 0; i < wegMatrix.getRowLength(); i++) {
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
                tempArticulations.add(i + 1);
            }

            for(int rowIndex = 0; rowIndex < wegMatrix.getRowLength(); rowIndex++) {
                for(int columnIndex = 0; columnIndex < wegMatrix.getColumnLength(); columnIndex++) {
                    wegMatrix.insert(rowIndex, i, 1);
                    wegMatrix.insert(i, columnIndex, 1);
                }
            }
        }
        articulations = new int[tempArticulations.size()];
        
        for(int rowIndex = 0; rowIndex < articulations.length; rowIndex++) {
            articulations[rowIndex] = tempArticulations.get(rowIndex);
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
            s += centre;
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

        s += "\nArtikulationen: " + Arrays.toString(articulations);
        return s;
    }
}