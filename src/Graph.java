import java.util.ArrayList;
import java.util.Arrays;

public class Graph {
    private Matrix adjazenzMatrix;
    private Matrix distanzMatrix;
    private Matrix wegMatrix;
    private boolean connected;
    private ArrayList<Integer> exzentrizitäten;
    private int radius;
    private int diameter;
    private ArrayList<Integer> centre;
    private ArrayList<ArrayList<Integer>> components;
    private ArrayList<int[]> bridges;
    private ArrayList<Integer> articulations;


    public static void main(String[] args) {}

    public Graph(int rowLength, int columnLength) {


    }

    public Graph(String file) {
        adjazenzMatrix = new Matrix(file);
        calculateDistanzMatrix();
        calculateWegMatrix();
        calculateExzentrizitäten();
        calculateProperties();
        findComponents();
        findBridges(file);
        findArticulations(file);
    }

    public void calculateDistanzMatrix() {
        distanzMatrix = new Matrix(adjazenzMatrix.getRowLength(), adjazenzMatrix.getColumnLength(), false);
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
        wegMatrix = new Matrix(adjazenzMatrix.getRowLength(), adjazenzMatrix.getColumnLength(), false);
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
        exzentrizitäten = new ArrayList<>(1);

        for(int rowIndex = 0; rowIndex < distanzMatrix.getRowLength(); rowIndex++) {
            int exzentrizität = 0;

            for(int columnIndex = 0; columnIndex < distanzMatrix.getColumnLength(); columnIndex++) {
                if(distanzMatrix.getValueAt(columnIndex, rowIndex) > exzentrizität && rowIndex != columnIndex) {
                    exzentrizität = distanzMatrix.getValueAt(columnIndex, rowIndex);
                }
            }
            if(exzentrizität == 0) {
                connected = false;
                return;
            }
            exzentrizitäten.add(exzentrizität);
        }
    }

    public void calculateProperties() {
        if(!connected) {
            return;
        }
        
        radius = Integer.MAX_VALUE;
        diameter = -1;
        connected = true;
        centre = new ArrayList<>(1);

        for(int rowIndex = 0; rowIndex < exzentrizitäten.size(); rowIndex++) {
            if(exzentrizitäten.get(rowIndex) > diameter) {
                diameter = exzentrizitäten.get(rowIndex);
            }
            if(exzentrizitäten.get(rowIndex) == radius) {
                centre.add(rowIndex + 1);
            }
            if(exzentrizitäten.get(rowIndex) < radius) {
                radius = exzentrizitäten.get(rowIndex);
                centre.clear();
                centre.add(rowIndex + 1);
            }
        }
    }

    public void findComponents() {
        components = new ArrayList<>(1);
        ArrayList<Integer> component = new ArrayList<>(1);

        for(int rowIndex = 0; rowIndex < wegMatrix.getRowLength(); rowIndex++) {
            component = new ArrayList<>(1);

            for(int columnIndex = 0; columnIndex < wegMatrix.getColumnLength(); columnIndex++) {
                if(wegMatrix.getValueAt(rowIndex, columnIndex) == 1) {
                    component.add(columnIndex + 1);
                }
            }
            
            if(!components.contains(component)) {
                components.add(component);
            }
        }
    }

    public ArrayList<ArrayList<Integer>> findComponents(Matrix matrix) {
        ArrayList<ArrayList<Integer>> newComponents = new ArrayList<>(1);
        ArrayList<Integer> component = new ArrayList<>(1);

        for(int rowIndex = 0; rowIndex < matrix.getRowLength(); rowIndex++) {
            component = new ArrayList<>(1);

            for(int columnIndex = 0; columnIndex < wegMatrix.getColumnLength(); columnIndex++) {
                if(wegMatrix.getValueAt(rowIndex, columnIndex) == 1) {
                    component.add(columnIndex + 1);
                }
            }
            
            if(!newComponents.contains(component)) {
                newComponents.add(component);
            }
        }
        return  newComponents;
    }

    public void findBridges(String file) {
        bridges = new ArrayList<>(1);
        ArrayList<ArrayList<Integer>> newComponents;
        int[] bridge;
        boolean contains;

        for(int rowIndex = 0; rowIndex < adjazenzMatrix.getRowLength(); rowIndex++) {
            for(int columnIndex = 0; columnIndex < adjazenzMatrix.getColumnLength(); columnIndex++) {
                if(rowIndex == columnIndex) {
                    continue;
                }
                
                bridge = new int[2];
                if(columnIndex < rowIndex) {
                    bridge[0] = columnIndex + 1;
                    bridge[1] = rowIndex + 1;
                } else {
                    bridge[1] = columnIndex + 1;
                    bridge[0] = rowIndex + 1;
                }
                

                adjazenzMatrix.insert(rowIndex, columnIndex, 0);
                adjazenzMatrix.insert(columnIndex, rowIndex, 0);

                calculateWegMatrix();

                newComponents = findComponents(wegMatrix);

                contains = false;
                for (int[] array : bridges) {
                    if(Arrays.equals(array, bridge)) {
                        contains = true;
                        break;
                    }
                }

                if(newComponents.size() > components.size() && !contains) {
                    bridges.add(bridge);
                }

                adjazenzMatrix = new Matrix(file);
            }
        }
        calculateWegMatrix();
    }

    public void findArticulations(String file) {
        articulations = new ArrayList<>(1);
        ArrayList<ArrayList<Integer>> newComponents;

        for(int i = 0; i < adjazenzMatrix.getRowLength(); i++) {
            for(int rowIndex = 0; rowIndex < adjazenzMatrix.getRowLength(); rowIndex++) {
                for(int columnIndex = 0; columnIndex < adjazenzMatrix.getColumnLength(); columnIndex++) {
                    adjazenzMatrix.insert(rowIndex, i, 0);
                    adjazenzMatrix.insert(i, columnIndex, 0);
                }
            }

            calculateWegMatrix();

            newComponents = findComponents(wegMatrix);
    
            if(newComponents.size() > components.size() + 1) {
                articulations.add(i + 1);
            }

            adjazenzMatrix = new Matrix(file);
        }
        calculateWegMatrix();
    }

    public String toString() {
        String s = "";

        s += "Adjazenzmatrix:\n" + adjazenzMatrix + "\nDistanzmatrix:\n" + distanzMatrix + "\nWegmatrix:\n" + wegMatrix;
        
        if(!connected) {
            s += "\nExzentrizitäten/Radius/Durchmesser: Kein zusammenhängender Graph";
        } else {
            s += "\nExzentrizitäten: " + exzentrizitäten.toString() + "\nRadius: " + radius + "\nDurchmesser: " + diameter;
        }

        s += "\nZentrum: ";
        if(!connected) {
            s += "Kein zusammenhängender Graph";
        } else {
            s += centre;
        }
        
        s += "\nKomponente: {";
        for(int rowIndex = 0; rowIndex < components.size(); rowIndex++) {
            s += components.get(rowIndex).toString();
            if(rowIndex < components.size() - 1) {
                s += ",";
            }
        }
        s += "}";

        s += "\nBrücken: {";
        if(bridges != null) {
            for(int rowIndex = 0; rowIndex < bridges.size(); rowIndex++) {
                s += Arrays.toString(bridges.get(rowIndex));
                if(rowIndex < bridges.size() - 1) {
                    if(rowIndex < bridges.size() - 1) {
                        s += ",";
                    }
                }
            }
        }   
        s += "}";

        s += "\nArtikulationen: " + articulations.toString();
        return s;
    }
}