package AdventOfCode2022.Dec18;

import AdventOfCode2022.Common.Helpers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

// Works but kinda crappy solution (for part 2), needs extended memory to avoid stackoverflow. Run with:
//javac TT.java
//java -Xss4m TT
public class Dec18 {

    public static void main(String[] args) throws CloneNotSupportedException {
        ArrayList<String> input = Helpers.imp("Dec18/res/input.txt");
        Grid3D grid3d = new Grid3D();
        grid3d.parseStrArr(input);
        System.out.println("No. exposed sides: " + grid3d.exposedSides);
        grid3d.findAdjustedExp();
        System.out.println("No. exposed sides to the outer space: " + grid3d.adjustedExposed);
    }
}
 
class Grid3D {
    HashMap<Integer, HashMap<Integer, HashSet<Integer>>> map;
    HashMap<Integer, HashMap<Integer, HashSet<Integer>>> outerEmpty;
    Integer exposedSides, xmin, xmax, ymin, ymax, zmin, zmax, adjustedExposed;
    HashSet<String> visited;

    void parseStrArr(ArrayList<String> input) {
        for (String s : input) {
            parseAndInsert(s);
        }
    }

    void findAdjustedExp() {
        adjustedExposed = exposedSides;
        visited = new HashSet<>();
        buildOuterEmpty(xmin-1, ymin-1, zmin-1);
        for (int x = xmin; x <= xmax; x++) {
            for (int y = ymin; y <= ymax; y++) {
                for (int z = zmin; z <= zmax; z++) {
                    if (!inMapAt(map, x, y, z)) { continue;}
                    if (!(x == xmin)) { if (!inMapAt(map, x-1, y, z)) { if (!inMapAt(outerEmpty, x-1, y, z)) { adjustedExposed--;}}}
                    if (!(x == xmax)) { if (!inMapAt(map, x+1, y, z)) { if (!inMapAt(outerEmpty, x+1, y, z)) { adjustedExposed--;}}}
                    if (!(y == ymin)) { if (!inMapAt(map, x, y-1, z)) { if (!inMapAt(outerEmpty, x, y-1, z)) { adjustedExposed--;}}}
                    if (!(y == ymax)) { if (!inMapAt(map, x, y+1, z)) { if (!inMapAt(outerEmpty, x, y+1, z)) { adjustedExposed--;}}}
                    if (!(z == zmin)) { if (!inMapAt(map, x, y, z-1)) { if (!inMapAt(outerEmpty, x, y, z-1)) { adjustedExposed--;}}}
                    if (!(z == zmax)) { if (!inMapAt(map, x, y, z+1)) { if (!inMapAt(outerEmpty, x, y, z+1)) { adjustedExposed--;}}}
                }
            }
        }
    }

    private void buildOuterEmpty(Integer x, Integer y, Integer z) {
        if (x > xmax+1 || y > ymax+1 || z > zmax+1) {
            return;
        }
        if (x < xmin-1 || y < ymin-1 || z < zmin-1) {
            return;
        }
        String v = Integer.toString(x) + ";" + Integer.toString(y) + ";" + Integer.toString(z);
        if (visited.contains(v)) {
            return;
        }
        visited.add(v);
        if (inMapAt(map, x, y, z)) {
            return;
        }
        insert(outerEmpty, x, y, z);
        buildOuterEmpty(x+1,y,z);
        buildOuterEmpty(x-1,y,z);
        buildOuterEmpty(x,y+1,z);
        buildOuterEmpty(x,y-1,z);
        buildOuterEmpty(x,y,z+1);
        buildOuterEmpty(x,y,z-1);
    }

    void parseAndInsert(String s) {
        String[] sp = s.split(",");
        Integer x,y,z;
        x = Integer.parseInt(sp[0]);
        y = Integer.parseInt(sp[1]);
        z = Integer.parseInt(sp[2]);
        if (x < xmin || xmin == -1) {xmin = x;}
        if (x > xmax) {xmax = x;}
        if (y < ymin || ymin == -1) {ymin = y;}
        if (y > ymax) {ymax = y;}
        if (z < zmin || zmin == -1) {zmin = z;}
        if (z > zmax) {zmax = z;}
        insert(this.map, x, y, z);
        Integer curExpSides = 6;
        if (inMapAt(map, x-1, y, z)) { curExpSides--; exposedSides--; }
        if (inMapAt(map, x+1, y, z)) { curExpSides--; exposedSides--; }
        if (inMapAt(map, x, y-1, z)) { curExpSides--; exposedSides--; }
        if (inMapAt(map, x, y+1, z)) { curExpSides--; exposedSides--; }
        if (inMapAt(map, x, y, z-1)) { curExpSides--; exposedSides--; }
        if (inMapAt(map, x, y, z+1)) { curExpSides--; exposedSides--; }
        exposedSides += curExpSides;
    }
    private void insert(HashMap<Integer, HashMap<Integer, HashSet<Integer>>> curMap, Integer x, Integer y, Integer z) {

        HashMap<Integer, HashSet<Integer>> hmy;
        hmy = curMap.get(x);
        if (hmy == null) {
            hmy = new HashMap<>();
        }
        HashSet<Integer> hsz;
        hsz = hmy.get(y);
        if (hsz == null) {
            hsz = new HashSet<>();
        }
        hsz.add(z);
        hmy.put(y,hsz);
        curMap.put(x,hmy);
    }

    private boolean inMapAt(HashMap<Integer, HashMap<Integer, HashSet<Integer>>> curMap, Integer x, Integer y, Integer z) {
        if (curMap.get(x) != null && curMap.get(x).get(y) != null && curMap.get(x).get(y).contains(z)) {
            return true;
        }
        return false;
    }
    Grid3D() {
        map = new HashMap<>();
        outerEmpty = new HashMap<>();
        exposedSides = 0; xmin = -1; xmax = -1; ymin = -1; ymax = -1; zmin = -1; zmax = -1;
    }
}